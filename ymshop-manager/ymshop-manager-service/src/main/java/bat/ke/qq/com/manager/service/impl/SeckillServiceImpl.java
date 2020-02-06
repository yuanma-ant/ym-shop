package bat.ke.qq.com.manager.service.impl;

import bat.ke.qq.com.common.jedis.JedisClient;
import bat.ke.qq.com.common.pojo.DataTablesResult;
import bat.ke.qq.com.manager.dto.DtoUtil;
import bat.ke.qq.com.manager.dto.SeckillDto;
import bat.ke.qq.com.manager.mapper.TbSeckillExample;
import bat.ke.qq.com.manager.mapper.TbSeckillMapper;
import bat.ke.qq.com.manager.pojo.TbSeckill;
import bat.ke.qq.com.manager.service.SeckillService;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 源码学院-ANT
 * 只为培养BAT程序员而生
 * http://bat.ke.qq.com
 * 往期视频加群:516212256 暗号:6
 */
@Service
public class SeckillServiceImpl implements SeckillService {
    @Autowired
    private JedisClient jedisClient;
    @Autowired
    private TbSeckillMapper tbSeckillMapper;

    private static String SECKILL_REDISKEY = "seckill_info:";

    @Override
    public DataTablesResult querySeckillList() {
        DataTablesResult dataTablesResult = new DataTablesResult();
        TbSeckillExample tbSeckillExample = new TbSeckillExample();
        List<TbSeckill> tbSeckills = tbSeckillMapper.selectByExample(tbSeckillExample);
        dataTablesResult.setData(tbSeckills);
        return dataTablesResult;
    }

    @Override
    public int addSeckill(SeckillDto dto) {
        //todo  实际应用中应当校验当前秒杀信息是否可以保存
        TbSeckill tbSeckill = DtoUtil.seckillDto2TbSeckill(dto);
        int insert = tbSeckillMapper.insert(tbSeckill);
        if (insert > 0) {
            //秒杀信息插入成功，讲活动信息存入redis，商品详情页根据商品查询是否
            //有匹配的秒杀信息.
            String seckill_key = SECKILL_REDISKEY + dto.getProductId();
            Integer expireSeconds = Integer.valueOf(
                    String.valueOf(
                            (tbSeckill.getEnddate().getTime() - System.currentTimeMillis()) / 1000));
            jedisClient.set(seckill_key, JSONUtil.toJsonStr(tbSeckill));
            jedisClient.expire(seckill_key,expireSeconds );
        }
        return insert;
    }

    @Override
    public int delSeckill(Long id) {
        TbSeckill tbSeckill =tbSeckillMapper.selectByPrimaryKey(id);
        if(tbSeckill!=null){
            String seckill_key = SECKILL_REDISKEY + tbSeckill.getItemId();
            if(jedisClient.exists(seckill_key)){
                jedisClient.del(seckill_key);
            }
        }
        return tbSeckillMapper.deleteByPrimaryKey(id);
    }
}
