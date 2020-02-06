package bat.ke.qq.com.manager.mapper;

import bat.ke.qq.com.manager.pojo.TbSeckill;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbSeckillMapper {
    long countByExample(TbSeckillExample example);

    int deleteByExample(TbSeckillExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TbSeckill record);

    int insertSelective(TbSeckill record);

    List<TbSeckill> selectByExample(TbSeckillExample example);

    TbSeckill selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TbSeckill record, @Param("example") TbSeckillExample example);

    int updateByExample(@Param("record") TbSeckill record, @Param("example") TbSeckillExample example);

    int updateByPrimaryKeySelective(TbSeckill record);

    int updateByPrimaryKey(TbSeckill record);
}
