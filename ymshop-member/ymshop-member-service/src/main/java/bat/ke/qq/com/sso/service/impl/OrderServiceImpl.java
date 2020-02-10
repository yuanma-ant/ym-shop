package bat.ke.qq.com.sso.service.impl;

import bat.ke.qq.com.common.exception.YmshopException;
import bat.ke.qq.com.common.jedis.JedisClient;
import bat.ke.qq.com.common.utils.IDUtil;
import bat.ke.qq.com.intercepter.MemberUtils;
import bat.ke.qq.com.manager.dto.DtoUtil;
import bat.ke.qq.com.manager.dto.front.*;
import bat.ke.qq.com.manager.mapper.*;
import bat.ke.qq.com.manager.pojo.*;
import bat.ke.qq.com.sso.service.OrderService;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author 源码学院
 */
@Service
public class OrderServiceImpl implements OrderService {

    private final static Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private TbMemberMapper tbMemberMapper;    //用户
    @Autowired
    private TbItemMapper tbItemMapper;    //商品
    @Autowired
    private TbOrderMapper tbOrderMapper;    //订单
    @Autowired
    private TbOrderItemMapper tbOrderItemMapper;  //订单商品
    @Autowired
    private TbOrderShippingMapper tbOrderShippingMapper;  //订单物流
    @Autowired
    private TbThanksMapper tbThanksMapper;

    private static Redisson redisson;

    @Autowired
    private JedisClient jedisClient;

    @Value("${CART_PRE}")
    private String CART_PRE;
    @Value("${EMAIL_SENDER}")
    private String EMAIL_SENDER;
    @Value("${PAY_EXPIRE}")
    private int PAY_EXPIRE;

    @Autowired
    private EmailUtil emailUtil;

    static {
        //todo 暂时用代码方式初始化redisson对象
        Config config=new Config();
        config.useSingleServer().setAddress("ymshop.com:6379");
        config.useSingleServer().setPassword("batke666");
        config.useSingleServer().setConnectionMinimumIdleSize(1);

        redisson=(Redisson)Redisson.create(config);
    }

    @Override
    public PageOrder getOrderList(Long userId, int page, int size) {
        //分页
        if (page <= 0) {
            page = 1;
        }
        PageHelper.startPage(page, size);

        PageOrder pageOrder = new PageOrder();
        List<Order> list = new ArrayList<>();

        TbOrderExample example = new TbOrderExample();
        TbOrderExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        example.setOrderByClause("create_time DESC");
        List<TbOrder> listOrder = tbOrderMapper.selectByExample(example);
        for (TbOrder tbOrder : listOrder) {

            judgeOrder(tbOrder);

            Order order = new Order();
            //orderId
            order.setOrderId(Long.valueOf(tbOrder.getOrderId()));
            //orderStatus
            order.setOrderStatus(String.valueOf(tbOrder.getStatus()));
            //createDate
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String date = formatter.format(tbOrder.getCreateTime());
            order.setCreateDate(date);
            //address
            TbOrderShipping tbOrderShipping = tbOrderShippingMapper.selectByPrimaryKey(tbOrder.getOrderId());
            TbAddress address = new TbAddress();
            address.setUserName(tbOrderShipping.getReceiverName());
            address.setStreetName(tbOrderShipping.getReceiverAddress());
            address.setTel(tbOrderShipping.getReceiverPhone());
            order.setAddressInfo(address);
            //orderTotal
            if (tbOrder.getPayment() == null) {
                order.setOrderTotal(new BigDecimal(0));
            } else {
                order.setOrderTotal(tbOrder.getPayment());
            }
            //goodsList
            TbOrderItemExample exampleItem = new TbOrderItemExample();
            TbOrderItemExample.Criteria criteriaItem = exampleItem.createCriteria();
            criteriaItem.andOrderIdEqualTo(tbOrder.getOrderId());
            List<TbOrderItem> listItem = tbOrderItemMapper.selectByExample(exampleItem);
            List<CartProduct> listProduct = new ArrayList<>();
            for (TbOrderItem tbOrderItem : listItem) {

                CartProduct cartProduct = DtoUtil.TbOrderItem2CartProduct(tbOrderItem);

                listProduct.add(cartProduct);
            }
            order.setGoodsList(listProduct);
            list.add(order);
        }
        PageInfo<Order> pageInfo = new PageInfo<>(list);
        pageOrder.setTotal(getMemberOrderCount(userId));
        pageOrder.setData(list);
        return pageOrder;
    }

    @Override
    public Order getOrder(Long orderId) {

        Order order = new Order();

        TbOrder tbOrder = tbOrderMapper.selectByPrimaryKey(String.valueOf(orderId));
        if (tbOrder == null) {
            throw new YmshopException("通过id获取订单失败");
        }

        String validTime = judgeOrder(tbOrder);
        if (validTime != null) {
            order.setFinishDate(validTime);
        }

        //orderId
        order.setOrderId(Long.valueOf(tbOrder.getOrderId()));
        //orderStatus
        order.setOrderStatus(String.valueOf(tbOrder.getStatus()));
        //createDate
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String createDate = formatter.format(tbOrder.getCreateTime());
        order.setCreateDate(createDate);
        //payDate
        if (tbOrder.getPaymentTime() != null) {
            String payDate = formatter.format(tbOrder.getPaymentTime());
            order.setPayDate(payDate);
        }
        //closeDate
        if (tbOrder.getCloseTime() != null) {
            String closeDate = formatter.format(tbOrder.getCloseTime());
            order.setCloseDate(closeDate);
        }
        //finishDate
        if (tbOrder.getEndTime() != null && tbOrder.getStatus() == 4) {
            String finishDate = formatter.format(tbOrder.getEndTime());
            order.setFinishDate(finishDate);
        }
        //address
        TbOrderShipping tbOrderShipping = tbOrderShippingMapper.selectByPrimaryKey(tbOrder.getOrderId());
        TbAddress address = new TbAddress();
        address.setUserName(tbOrderShipping.getReceiverName());
        address.setStreetName(tbOrderShipping.getReceiverAddress());
        address.setTel(tbOrderShipping.getReceiverPhone());
        order.setAddressInfo(address);
        //orderTotal
        if (tbOrder.getPayment() == null) {
            order.setOrderTotal(new BigDecimal(0));
        } else {
            order.setOrderTotal(tbOrder.getPayment());
        }
        //goodsList
        TbOrderItemExample exampleItem = new TbOrderItemExample();
        TbOrderItemExample.Criteria criteriaItem = exampleItem.createCriteria();
        criteriaItem.andOrderIdEqualTo(tbOrder.getOrderId());
        List<TbOrderItem> listItem = tbOrderItemMapper.selectByExample(exampleItem);
        List<CartProduct> listProduct = new ArrayList<>();
        for (TbOrderItem tbOrderItem : listItem) {

            CartProduct cartProduct = DtoUtil.TbOrderItem2CartProduct(tbOrderItem);

            listProduct.add(cartProduct);
        }
        order.setGoodsList(listProduct);
        return order;
    }

    @Override
    public int cancelOrder(Long orderId) {

        TbOrder tbOrder = tbOrderMapper.selectByPrimaryKey(String.valueOf(orderId));
        if (tbOrder == null) {
            throw new YmshopException("通过id获取订单失败");
        }
        tbOrder.setStatus(5);
        tbOrder.setCloseTime(new Date());
        if (tbOrderMapper.updateByPrimaryKey(tbOrder) != 1) {
            throw new YmshopException("取消订单失败");
        }
        return 1;
    }

    @Override
    @Transactional
    //todo ant 事务   分布式锁  扣库存
    public Long createOrder(OrderInfo orderInfo) {
        //进入生成订单阶段，触发用户级别锁，
        // 当前用户在提交订单过程中不运行并行对当前用户订单数据进行操作
        //购物车流程中的数据修改都需要有用户锁
        TbMember member=tbMemberMapper.selectByPrimaryKey(Long.valueOf(orderInfo.getUserId()));
        if(member==null){
            throw new YmshopException("获取下单用户失败");
        }

        RLock userLock=redisson.getLock("userCart:"+member.getId());
        userLock.lock();
        //统一库存、商品状态校验
        List<CartProduct> cartProducts = orderInfo.getGoodsList();
        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        List<Long> ids = new ArrayList<>();
        for (CartProduct cartProduct : cartProducts) {
            ids.add(cartProduct.getProductId());
        }
        criteria.andIdIn(ids);
        List<TbItem> tbItems = tbItemMapper.selectByExample(example);
        if (tbItems == null || tbItems.size() != ids.size()) {
            throw new YmshopException("商品信息查询异常");
        }
        List<String> outOfStock = new ArrayList<>();
        for (TbItem tbItem : tbItems) {
            if (tbItem.getNum() < 0 || tbItem.getStatus() != 1) {
                outOfStock.add(tbItem.getTitle());
            }
        }
        if (outOfStock.size() > 0) {
            throw new YmshopException("商品" + JSONUtil.toJsonStr(outOfStock) + "缺货！请取消勾选后提交订单。");
        }

        //扣减库存，使用商品级别锁,防止超卖
        for (TbItem item : tbItems) {
            RLock itemLock=redisson.getLock(item.getCid().toString());
            itemLock.lock();
            TbItem tbItem = tbItemMapper.selectByPrimaryKey(item.getId());
            if (tbItem.getNum() < 0 || tbItem.getStatus() != 1) {
                throw new YmshopException("下单出现异常，请刷新购物车重新提交");
            }
            tbItem.setNum(tbItem.getNum()-1);
            tbItemMapper.updateByPrimaryKey(tbItem);
            itemLock.unlock();
        }

        TbOrder order = new TbOrder();
        //生成订单ID
        Long orderId = IDUtil.getRandomId();
        order.setOrderId(String.valueOf(orderId));
        order.setUserId(Long.valueOf(orderInfo.getUserId()));
        order.setBuyerNick(member.getUsername());
        order.setPayment(orderInfo.getOrderTotal());
        order.setCreateTime(new Date());
        order.setUpdateTime(new Date());
        //0、未付款，1、已付款，2、未发货，3、已发货，4、交易成功，5、交易关闭，6、交易失败
        order.setStatus(0);

        if (tbOrderMapper.insert(order) != 1) {
            throw new YmshopException("生成订单失败");
        }

        List<CartProduct> list = orderInfo.getGoodsList();
        for (CartProduct cartProduct : list) {
            TbOrderItem orderItem = new TbOrderItem();
            //生成订单商品ID
            Long orderItemId = IDUtil.getRandomId();
            orderItem.setId(String.valueOf(orderItemId));
            orderItem.setItemId(String.valueOf(cartProduct.getProductId()));
            orderItem.setOrderId(String.valueOf(orderId));
            orderItem.setNum(Math.toIntExact(cartProduct.getProductNum()));
            orderItem.setPrice(cartProduct.getSalePrice());
            orderItem.setTitle(cartProduct.getProductName());
            orderItem.setPicPath(cartProduct.getProductImg());
            orderItem.setTotalFee(cartProduct.getSalePrice().multiply(BigDecimal.valueOf(cartProduct.getProductNum())));

            if (tbOrderItemMapper.insert(orderItem) != 1) {
                throw new YmshopException("生成订单商品失败");
            }

            //删除购物车中含该订单的商品
            try {
                List<String> jsonList = jedisClient.hvals(CART_PRE + ":" + orderInfo.getUserId());
                for (String json : jsonList) {
                    CartProduct cart = new Gson().fromJson(json, CartProduct.class);
                    if (cart.getProductId().equals(cartProduct.getProductId())) {
                        jedisClient.hdel(CART_PRE + ":" + orderInfo.getUserId(), cart.getProductId() + "");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //物流表
        TbOrderShipping orderShipping = new TbOrderShipping();
        orderShipping.setOrderId(String.valueOf(orderId));
        orderShipping.setReceiverName(orderInfo.getUserName());
        orderShipping.setReceiverAddress(orderInfo.getStreetName());
        orderShipping.setReceiverPhone(orderInfo.getTel());
        orderShipping.setCreated(new Date());
        orderShipping.setUpdated(new Date());

        if (tbOrderShippingMapper.insert(orderShipping) != 1) {
            throw new YmshopException("生成物流信息失败");
        }

        userLock.unlock();
        return orderId;
    }

    @Override
    //todo ant 事务
    public int delOrder(Long orderId) {

        if (tbOrderMapper.deleteByPrimaryKey(String.valueOf(orderId)) != 1) {
            throw new YmshopException("删除订单失败");
        }

        TbOrderItemExample example = new TbOrderItemExample();
        TbOrderItemExample.Criteria criteria = example.createCriteria();
        criteria.andOrderIdEqualTo(String.valueOf(orderId));
        List<TbOrderItem> list = tbOrderItemMapper.selectByExample(example);
        for (TbOrderItem tbOrderItem : list) {
            if (tbOrderItemMapper.deleteByPrimaryKey(tbOrderItem.getId()) != 1) {
                throw new YmshopException("删除订单商品失败");
            }
        }

        if (tbOrderShippingMapper.deleteByPrimaryKey(String.valueOf(orderId)) != 1) {
            throw new YmshopException("删除物流失败");
        }
        return 1;
    }

    @Override
    public int payOrder(TbThanks tbThanks) {
        //在实际应用场景中在支付平台支付后，支付结果通知由支付平台异步通知，接收到通知后修改订单状态
        //设置订单为已付款
        TbOrder tbOrder = tbOrderMapper.selectByPrimaryKey(tbThanks.getOrderId());
        tbOrder.setStatus(1);
        tbOrder.setUpdateTime(new Date());
        tbOrder.setPaymentTime(new Date());
        if (tbOrderMapper.updateByPrimaryKey(tbOrder) != 1) {
            throw new YmshopException("更新订单失败");
        }
        //todo  通知物流系统 ...
        return 1;
    }

    /**
     * 判断订单是否超时未支付
     */
    public String judgeOrder(TbOrder tbOrder) {

        String result = null;
        if (tbOrder.getStatus() == 0) {
            //判断是否已超1天
            long diff = System.currentTimeMillis() - tbOrder.getCreateTime().getTime();
            long days = diff / (1000 * 60 * 60 * 24);
            if (days >= 1) {
                //设置失效
                tbOrder.setStatus(5);
                tbOrder.setCloseTime(new Date());
                if (tbOrderMapper.updateByPrimaryKey(tbOrder) != 1) {
                    throw new YmshopException("更新订单失效失败");
                }
            } else {
                //返回到期时间
                long time = tbOrder.getCreateTime().getTime() + 1000 * 60 * 60 * 24;
                result = String.valueOf(time);
            }
        }
        return result;
    }

    public int getMemberOrderCount(Long userId) {

        TbOrderExample example = new TbOrderExample();
        TbOrderExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        List<TbOrder> listOrder = tbOrderMapper.selectByExample(example);
        if (listOrder != null) {
            return listOrder.size();
        }
        return 0;
    }
}
