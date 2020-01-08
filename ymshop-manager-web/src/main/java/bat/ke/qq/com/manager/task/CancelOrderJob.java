package bat.ke.qq.com.manager.task;

import bat.ke.qq.com.manager.service.OrderService;
import cn.hutool.core.date.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author 源码学院
 */
@Component
public class CancelOrderJob {

    final static Logger log= LoggerFactory.getLogger(CancelOrderJob.class);

    @Autowired
    private OrderService orderService;

    /**
     * 每1个小时判断订单是否失效
     */
    @Scheduled(cron = "0 0 */1 * * ?")
    public void run() {

        log.info("执行了自动取消订单定时任务 - " + DateUtil.now());
        orderService.cancelOrder();
    }
}
