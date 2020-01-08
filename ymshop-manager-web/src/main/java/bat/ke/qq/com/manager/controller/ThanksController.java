package bat.ke.qq.com.manager.controller;

import bat.ke.qq.com.common.pojo.DataTablesResult;
import bat.ke.qq.com.common.pojo.Result;
import bat.ke.qq.com.common.utils.ResultUtil;
import bat.ke.qq.com.manager.pojo.TbThanks;
import bat.ke.qq.com.manager.service.ThanksService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 源码学院
 */
@RestController
@Api(description= "支付管理")
public class ThanksController {

    @Autowired
    private ThanksService thanksService;

    @RequestMapping(value = "/thanks/list",method = RequestMethod.GET)
    @ApiOperation(value = "获取支付列表")
    public DataTablesResult getThanksList(){

        DataTablesResult result=thanksService.getThanksList();
        return result;
    }

    @RequestMapping(value = "/thanks/count",method = RequestMethod.GET)
    @ApiOperation(value = "获取支付列表总数")
    public Result<Object> getThanksCount(){

        Long result=thanksService.countThanks();
        return new ResultUtil<Object>().setData(result);
    }

    @RequestMapping(value = "/thanks/add",method = RequestMethod.POST)
    @ApiOperation(value = "添加支付")
    public Result<Object> addThanks(@ModelAttribute TbThanks tbThanks){

        thanksService.addThanks(tbThanks);
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/thanks/update",method = RequestMethod.POST)
    @ApiOperation(value = "编辑支付")
    public Result<Object> updateThanks(@ModelAttribute TbThanks tbThanks){

        thanksService.updateThanks(tbThanks);
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/thanks/del/{ids}",method = RequestMethod.DELETE)
    @ApiOperation(value = "删除支付")
    public Result<Object> delThanks(@PathVariable int[] ids){

        for(int id:ids){
            thanksService.deleteThanks(id);
        }
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/thanks/{id}",method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取支付")
    public Result<TbThanks> getThanks(@PathVariable int id){

        TbThanks tbThanks=thanksService.getThankById(id);
        return new ResultUtil<TbThanks>().setData(tbThanks);
    }
}
