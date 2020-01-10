package bat.ke.qq.com.manager.service;


import bat.ke.qq.com.manager.dto.OrderChartData;

import java.util.Date;
import java.util.List;

/**
 * @author 源码学院
 */
public interface CountService {

    /**
     * 统计订单销量
     * @param type
     * @param startTime
     * @param endTime
     * @param year
     * @return
     */
    List<OrderChartData> getOrderCountData(int type, Date startTime, Date endTime, int year);
}
