package bat.ke.qq.com.manager.controller;

import bat.ke.qq.com.common.pojo.DataTablesResult;
import bat.ke.qq.com.common.pojo.Result;
import bat.ke.qq.com.common.utils.ResultUtil;
import bat.ke.qq.com.manager.dto.SeckillDto;
import bat.ke.qq.com.manager.pojo.TbItem;
import bat.ke.qq.com.manager.service.SeckillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 源码学院-ANT
 * 只为培养BAT程序员而生
 * http://bat.ke.qq.com
 * 往期视频加群:516212256 暗号:6
 */
@RestController
@Api(description = "秒杀活动管理")
public class SeckillController {
    @Autowired
    private SeckillService seckillService;

    @RequestMapping(value = "/seckill/list",method = RequestMethod.GET)
    @ApiOperation(value = "查询秒杀活动列表")
    public DataTablesResult seckillList(){
        DataTablesResult dataTablesResult = seckillService.querySeckillList();
        return dataTablesResult;
    }

    @RequestMapping(value = "/seckill/add",method = RequestMethod.POST)
    @ApiOperation(value = "添加秒杀活动")
    public Result<Object> addsSckill(SeckillDto dto){
        int result = seckillService.addSeckill(dto);
        return new ResultUtil<Object>().setData(result);
    }

    @RequestMapping(value = "/seckill/del/{id}",method = RequestMethod.DELETE)
    @ApiOperation(value = "删除秒杀活动")
    public Result<Object> delSeckill(@PathVariable Long id){
        int result = seckillService.delSeckill(id);
        return new ResultUtil<Object>().setData(result);
    }
}
