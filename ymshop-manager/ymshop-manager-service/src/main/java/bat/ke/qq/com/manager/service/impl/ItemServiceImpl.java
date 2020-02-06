package bat.ke.qq.com.manager.service.impl;

import bat.ke.qq.com.common.exception.YmshopException;
import bat.ke.qq.com.common.jedis.JedisClient;
import bat.ke.qq.com.common.pojo.DataTablesResult;
import bat.ke.qq.com.common.utils.IDUtil;
import bat.ke.qq.com.manager.dto.DtoUtil;
import bat.ke.qq.com.manager.dto.ItemDto;
import bat.ke.qq.com.manager.mapper.TbItemCatMapper;
import bat.ke.qq.com.manager.mapper.TbItemDescMapper;
import bat.ke.qq.com.manager.mapper.TbItemMapper;
import bat.ke.qq.com.manager.pojo.TbItem;
import bat.ke.qq.com.manager.pojo.TbItemCat;
import bat.ke.qq.com.manager.pojo.TbItemDesc;
import bat.ke.qq.com.manager.pojo.TbItemExample;
import bat.ke.qq.com.manager.service.ItemService;
import cn.hutool.core.util.ArrayUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sun.tools.javac.util.ArrayUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

/**
 * 源码学院
 * 只为培养BAT程序员而生
 * http://bat.ke.qq.com
 * 往期视频加群:516212256 暗号:6
 */
@Service
public class ItemServiceImpl implements ItemService {

    private final static Logger log = LoggerFactory.getLogger(ItemServiceImpl.class);

    @Autowired
    private TbItemMapper tbItemMapper;
    @Autowired
    private TbItemDescMapper tbItemDescMapper;
    @Autowired
    private TbItemCatMapper tbItemCatMapper;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Resource
    private Destination topicDestination;
    @Autowired
    private JedisClient jedisClient;

    @Value("${PRODUCT_ITEM}")
    private String PRODUCT_ITEM;

    @Override
    public ItemDto getItemById(Long id) {
        ItemDto itemDto = new ItemDto();

        TbItem tbItem = tbItemMapper.selectByPrimaryKey(id);
        itemDto = DtoUtil.TbItem2ItemDto(tbItem);

        TbItemCat tbItemCat = tbItemCatMapper.selectByPrimaryKey(itemDto.getCid());
        itemDto.setCname(tbItemCat.getName());

        TbItemDesc tbItemDesc = tbItemDescMapper.selectByPrimaryKey(id);
        itemDto.setDetail(tbItemDesc.getItemDesc());

        return itemDto;
    }

    @Override
    public TbItem getNormalItemById(Long id) {

        return tbItemMapper.selectByPrimaryKey(id);
    }

    @Override
    public DataTablesResult getItemList(int draw, int start, int length, int cid, String search,
                                        String orderCol, String orderDir) {

        DataTablesResult result = new DataTablesResult();

        //分页执行查询返回结果
        PageHelper.startPage(start / length + 1, length);
        List<TbItem> list = tbItemMapper.selectItemByCondition(cid, "%" + search + "%", orderCol, orderDir);
        PageInfo<TbItem> pageInfo = new PageInfo<>(list);
        result.setRecordsFiltered((int) pageInfo.getTotal());
        result.setRecordsTotal(getAllItemCount().getRecordsTotal());

        result.setDraw(draw);
        result.setData(list);

        return result;
    }

    @Override
    public DataTablesResult getItemSearchList(int draw, int start, int length, int cid, String search,
                                              String minDate, String maxDate, String orderCol, String orderDir) {

        DataTablesResult result = new DataTablesResult();

        //分页执行查询返回结果
        PageHelper.startPage(start / length + 1, length);
        List<TbItem> list = tbItemMapper.selectItemByMultiCondition(cid, "%" + search + "%", minDate, maxDate, orderCol, orderDir);
        PageInfo<TbItem> pageInfo = new PageInfo<>(list);
        result.setRecordsFiltered((int) pageInfo.getTotal());
        result.setRecordsTotal(getAllItemCount().getRecordsTotal());

        result.setDraw(draw);
        result.setData(list);

        return result;
    }

    @Override
    public DataTablesResult getAllItemCount() {
        TbItemExample example = new TbItemExample();
        Long count = tbItemMapper.countByExample(example);
        DataTablesResult result = new DataTablesResult();
        result.setRecordsTotal(Math.toIntExact(count));
        return result;
    }

    @Override
    public TbItem alertItemState(Long id, Integer state) {

        TbItem tbMember = getNormalItemById(id);
        tbMember.setStatus(state.byteValue());
        tbMember.setUpdated(new Date());

        if (tbItemMapper.updateByPrimaryKey(tbMember) != 1) {
            throw new YmshopException("修改商品状态失败");
        }
        return getNormalItemById(id);
    }

    @Override
    public int deleteItem(Long id) {

        if (tbItemMapper.deleteByPrimaryKey(id) != 1) {
            throw new YmshopException("删除商品失败");
        }
        if (tbItemDescMapper.deleteByPrimaryKey(id) != 1) {
            throw new YmshopException("删除商品详情失败");
        }
        //发送消息同步索引库
        try {
            sendRefreshESMessage("delete", id);
        } catch (Exception e) {
            log.error("同步索引出错");
        }
        return 0;
    }

    @Override
    public TbItem addItem(ItemDto itemDto) {
        long id = IDUtil.getRandomId();
        TbItem tbItem = DtoUtil.ItemDto2TbItem(itemDto);
        tbItem.setId(id);
        tbItem.setStatus((byte) 1);
        tbItem.setCreated(new Date());
        tbItem.setUpdated(new Date());
        if (tbItem.getImage().isEmpty()) {
            tbItem.setImage("http://ow2h3ee9w.bkt.clouddn.com/nopic.jpg");
        }
        if (tbItemMapper.insert(tbItem) != 1) {
            throw new YmshopException("添加商品失败");
        }

        TbItemDesc tbItemDesc = new TbItemDesc();
        tbItemDesc.setItemId(id);
        tbItemDesc.setItemDesc(itemDto.getDetail());
        tbItemDesc.setCreated(new Date());
        tbItemDesc.setUpdated(new Date());

        if (tbItemDescMapper.insert(tbItemDesc) != 1) {
            throw new YmshopException("添加商品详情失败");
        }
        //发送消息同步索引库
        try {
            sendRefreshESMessage("add", id);
        } catch (Exception e) {
            log.error("同步索引出错");
        }
        return getNormalItemById(id);
    }

    @Override
    public TbItem updateItem(Long id, ItemDto itemDto) {

        TbItem oldTbItem = getNormalItemById(id);

        TbItem tbItem = DtoUtil.ItemDto2TbItem(itemDto);

        if (tbItem.getImage().isEmpty()) {
            tbItem.setImage(oldTbItem.getImage());
        }
        tbItem.setId(id);
        tbItem.setStatus(oldTbItem.getStatus());
        tbItem.setCreated(oldTbItem.getCreated());
        tbItem.setUpdated(new Date());
        if (tbItemMapper.updateByPrimaryKey(tbItem) != 1) {
            throw new YmshopException("更新商品失败");
        }

        TbItemDesc tbItemDesc = new TbItemDesc();

        tbItemDesc.setItemId(id);
        tbItemDesc.setItemDesc(itemDto.getDetail());
        tbItemDesc.setUpdated(new Date());
        tbItemDesc.setCreated(oldTbItem.getCreated());

        if (tbItemDescMapper.updateByPrimaryKey(tbItemDesc) != 1) {
            throw new YmshopException("更新商品详情失败");
        }
        //同步缓存
        deleteProductDetRedis(id);
        //发送消息同步索引库
        try {
            sendRefreshESMessage("add", id);
        } catch (Exception e) {
            log.error("同步索引出错");
        }
        return getNormalItemById(id);
    }

    /**
     * 同步商品详情缓存
     *
     * @param id
     */
    public void deleteProductDetRedis(Long id) {
        try {
            jedisClient.del(PRODUCT_ITEM + ":" + id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送消息同步索引库
     *
     * @param type
     * @param id
     */
    public void sendRefreshESMessage(String type, Long id) {
        jmsTemplate.send(topicDestination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage textMessage = session.createTextMessage(type + "," + String.valueOf(id));
                return textMessage;
            }
        });
    }

    /**
     * 静态化商品详情页
     *
     * @param id
     * @return
     */

    public String toStatic(Long id) {
        //查询商品信息
        TbItem tbItem = tbItemMapper.selectByPrimaryKey(id);
        if (tbItem==null){
            return null;
        }
        String outPath="";
        try {
            String userHome = System.getProperty("user.home");
            // 第一步：创建一个Configuration对象，直接new一个对象。构造方法的参数就是freemarker对于的版本号。
            Configuration configuration = new Configuration(Configuration.getVersion());

            // 第二步：设置模板文件所在的路径。
            configuration.setDirectoryForTemplateLoading(new File(userHome+"/template/ftl"));

            // 第三步：设置模板文件使用的字符集。一般就是utf-8.
            configuration.setDefaultEncoding("utf-8");

            // 第四步：加载一个模板，创建一个模板对象。
            Template template = null;

            template = configuration.getTemplate("report.ftl");
            // 第五步：创建一个模板使用的数据集，可以是pojo也可以是map。一般是Map。
            Map dataModel = new HashMap();
            // 向数据集中添加数据
            dataModel.put("item", tbItem);

            String images= tbItem.getImage();
            if(StringUtils.isNotEmpty(images)){
                String[] split = images.split(",");
                List<String> imageList=Arrays.asList(split);
                dataModel.put("imageList", imageList);
            }

            // 第六步：创建一个Writer对象，一般创建一FileWriter对象，指定生成的文件名。
            outPath=userHome+"/template/report/itemTemplate"+tbItem.getId()+".html";
            Writer out = new FileWriter(new File(outPath));
            // 第七步：调用模板对象的process方法输出文件。
            template.process(dataModel, out);
            // 第八步：关闭流。
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException te) {
            te.printStackTrace();
        }
        return outPath;
    }
}
