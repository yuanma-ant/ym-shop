package bat.ke.qq.com.manager.service;

import bat.ke.qq.com.common.pojo.DataTablesResult;
import bat.ke.qq.com.manager.pojo.TbThanks;

/**
 * @author 源码学院
 */
public interface ThanksService {

    /**
     * 获得支付列表
     * @return
     */
    DataTablesResult getThanksList();

    /**
     * 分页获取支付列表
     * @param page
     * @param size
     * @return
     */
    DataTablesResult getThanksListByPage(int page, int size);

    /**
     * 统计支付表数目
     * @return
     */
    Long countThanks();

    /**
     * 添加支付
     * @param tbThanks
     * @return
     */
    int addThanks(TbThanks tbThanks);

    /**
     * 更新支付
     * @param tbThanks
     * @return
     */
    int updateThanks(TbThanks tbThanks);

    /**
     * 删除支付
     * @param id
     * @return
     */
    int deleteThanks(int id);

    /**
     * 通过id获取
     * @param id
     * @return
     */
    TbThanks getThankById(int id);
}
