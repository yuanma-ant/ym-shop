package bat.ke.qq.com.manager.service;

import bat.ke.qq.com.common.pojo.DataTablesResult;
import bat.ke.qq.com.manager.dto.SeckillDto;

public interface SeckillService {
    DataTablesResult querySeckillList();

    int addSeckill(SeckillDto dto);

    int delSeckill(Long id);
}
