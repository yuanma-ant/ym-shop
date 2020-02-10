package bat.ke.qq.com.front.controller;

import bat.ke.qq.com.common.pojo.Result;
import bat.ke.qq.com.common.utils.ResultUtil;
import bat.ke.qq.com.intercepter.MemberUtils;
import bat.ke.qq.com.manager.dto.front.Member;
import bat.ke.qq.com.manager.dto.front.Order;
import bat.ke.qq.com.manager.dto.front.OrderInfo;
import bat.ke.qq.com.manager.dto.front.PageOrder;
import bat.ke.qq.com.manager.pojo.TbThanks;
import bat.ke.qq.com.sso.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author 源码学院
 */
@RestController
@Api(description = "订单")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/member/orderList",method = RequestMethod.GET)
    @ApiOperation(value = "获得用户所有订单")
    public Result<PageOrder> getOrderList(String userId,
                                          @RequestParam(defaultValue = "1") int page,
                                          @RequestParam(defaultValue = "5") int size){

        PageOrder pageOrder=orderService.getOrderList(Long.valueOf(MemberUtils.getUserId()), page, size);
        return new ResultUtil<PageOrder>().setData(pageOrder);
    }

    @RequestMapping(value = "/member/orderDetail",method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取订单")
    public Result<Order> getOrder(String orderId){
        //todo ant 订单查询权限漏洞    需要根据当前用户校验查询对应订单号是否有权限
//        MemberUtils.getUserId()
        Order order=orderService.getOrder(Long.valueOf(orderId));
        return new ResultUtil<Order>().setData(order);
    }

    @RequestMapping(value = "/member/addOrder",method = RequestMethod.POST)
    @ApiOperation(value = "创建订单")
    public Result<Object> addOrder(@RequestBody OrderInfo orderInfo){
        Member member=MemberUtils.getMemberThreadLocal();
        orderInfo.setUserId(member.getId().toString());
        Long orderId=orderService.createOrder(orderInfo);
        return new ResultUtil<Object>().setData(orderId.toString());
    }

    @RequestMapping(value = "/member/cancelOrder",method = RequestMethod.POST)
    @ApiOperation(value = "取消订单")
    public Result<Object> cancelOrder(@RequestBody Order order){

        int result=orderService.cancelOrder(order.getOrderId());
        return new ResultUtil<Object>().setData(result);
    }

    @RequestMapping(value = "/member/delOrder",method = RequestMethod.GET)
    @ApiOperation(value = "删除订单")
    public Result<Object> delOrder(String orderId){

        int result=orderService.delOrder(Long.valueOf(orderId));
        return new ResultUtil<Object>().setData(result);
    }

    @RequestMapping(value = "/member/payOrder",method = RequestMethod.POST)
    @ApiOperation(value = "支付订单")
    public Result<Object> payOrder(@RequestBody TbThanks tbThanks){

        int result=orderService.payOrder(tbThanks);
        return new ResultUtil<Object>().setData(result);
    }
}
