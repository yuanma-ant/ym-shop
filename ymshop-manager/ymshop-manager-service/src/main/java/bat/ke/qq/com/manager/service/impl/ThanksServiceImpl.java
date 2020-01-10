package bat.ke.qq.com.manager.service.impl;

import bat.ke.qq.com.common.exception.YmshopException;
import bat.ke.qq.com.common.pojo.DataTablesResult;
import bat.ke.qq.com.manager.mapper.TbThanksMapper;
import bat.ke.qq.com.manager.pojo.TbThanks;
import bat.ke.qq.com.manager.pojo.TbThanksExample;
import bat.ke.qq.com.manager.service.ThanksService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author Exrickx
 */
@Service
public class ThanksServiceImpl implements ThanksService {

    @Autowired
    private TbThanksMapper tbThanksMapper;

    @Override
    public DataTablesResult getThanksList() {

        DataTablesResult result=new DataTablesResult();
        TbThanksExample example=new TbThanksExample();
        List<TbThanks> list=tbThanksMapper.selectByExample(example);
        if(list==null){
            throw new YmshopException("获取捐赠列表失败");
        }
        result.setSuccess(true);
        result.setData(list);
        return result;
    }

    @Override
    public DataTablesResult getThanksListByPage(int page, int size) {

        DataTablesResult result=new DataTablesResult();
        TbThanksExample example=new TbThanksExample();
        if(page<=0) {
            page = 1;
        }
        PageHelper.startPage(page,size);
        List<TbThanks> list=tbThanksMapper.selectByExample(example);
        if(list==null){
            throw new YmshopException("获取捐赠列表失败");
        }
        PageInfo<TbThanks> pageInfo=new PageInfo<>(list);

        for(TbThanks tbThanks:list){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = null;
            try {
                date = dateFormat.format(tbThanks.getDate());
            } catch (Exception e) {
                e.printStackTrace();
            }
            tbThanks.setTime(date);
            tbThanks.setEmail(null);
        }

        result.setSuccess(true);
        result.setRecordsTotal((int) pageInfo.getTotal());
        result.setData(list);
        return result;
    }

    @Override
    public Long countThanks() {

        TbThanksExample example=new TbThanksExample();
        Long result=tbThanksMapper.countByExample(example);
        if(result==null){
            throw new YmshopException("统计捐赠数目失败");
        }
        return result;
    }

    @Override
    public int addThanks(TbThanks tbThanks) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(tbThanks.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tbThanks.setDate(date);
        if(tbThanksMapper.insert(tbThanks)!=1){
            throw new YmshopException("添加捐赠失败");
        }
        return 1;
    }

    @Override
    public int updateThanks(TbThanks tbThanks) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(tbThanks.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tbThanks.setDate(date);
        if(tbThanksMapper.updateByPrimaryKey(tbThanks)!=1){
            throw new YmshopException("更新捐赠失败");
        }
        return 1;
    }

    @Override
    public int deleteThanks(int id) {

        if(tbThanksMapper.deleteByPrimaryKey(id)!=1){
            throw new YmshopException("删除捐赠失败");
        }
        return 1;
    }

    @Override
    public TbThanks getThankById(int id) {

        TbThanks tbThanks=tbThanksMapper.selectByPrimaryKey(id);
        if(tbThanks==null){
            throw new YmshopException("获取捐赠数据失败");
        }
        return tbThanks;
    }
}
