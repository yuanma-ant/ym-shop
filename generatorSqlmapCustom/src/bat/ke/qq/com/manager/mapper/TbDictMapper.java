package bat.ke.qq.com.manager.mapper;

import bat.ke.qq.com.manager.pojo.TbDict;
import bat.ke.qq.com.manager.pojo.TbDictExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbDictMapper {
    long countByExample(TbDictExample example);

    int deleteByExample(TbDictExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TbDict record);

    int insertSelective(TbDict record);

    List<TbDict> selectByExample(TbDictExample example);

    TbDict selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TbDict record, @Param("example") TbDictExample example);

    int updateByExample(@Param("record") TbDict record, @Param("example") TbDictExample example);

    int updateByPrimaryKeySelective(TbDict record);

    int updateByPrimaryKey(TbDict record);
}
