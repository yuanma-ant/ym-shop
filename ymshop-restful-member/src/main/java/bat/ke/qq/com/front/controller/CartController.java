package bat.ke.qq.com.front.controller;

import bat.ke.qq.com.common.pojo.Result;
import bat.ke.qq.com.common.utils.ResultUtil;
import bat.ke.qq.com.intercepter.MemberUtils;
import bat.ke.qq.com.manager.dto.front.Cart;
import bat.ke.qq.com.manager.dto.front.CartProduct;
import bat.ke.qq.com.sso.service.CartService;
import com.github.pagehelper.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 源码学院
 */
@RestController
@Api(description = "购物车")
public class CartController {

    @Autowired
    private CartService cartService;

    @RequestMapping(value = "/member/addCart",method = RequestMethod.POST)
    @ApiOperation(value = "添加购物车商品")
    public Result<Object> addCart(@RequestBody Cart cart){
//        表单传userId的方式不安全，改成通过cookies的sn token认证后获取当前用户信息
        int result=cartService.addCart(MemberUtils.getUserId(),cart.getProductId(),cart.getProductNum());
        return new ResultUtil<Object>().setData(result);
    }

    @RequestMapping(value = "/member/cartList",method = RequestMethod.POST)
    @ApiOperation(value = "获取购物车商品列表")
    public Result<List<CartProduct>> getCartList(@RequestBody Cart cart){

        List<CartProduct> list=cartService.getCartList(MemberUtils.getUserId());
        return new ResultUtil<List<CartProduct>>().setData(list);
    }

    @RequestMapping(value = "/member/cartEdit",method = RequestMethod.POST)
    @ApiOperation(value = "编辑购物车商品")
    public Result<Object> updateCartNum(@RequestBody Cart cart){
        int result=cartService.updateCartNum(MemberUtils.getUserId(),cart.getProductId(),cart.getProductNum(),cart.getChecked());
        return new ResultUtil<Object>().setData(result);
    }

    @RequestMapping(value = "/member/editCheckAll",method = RequestMethod.POST)
    @ApiOperation(value = "是否全选购物车商品")
    public Result<Object> editCheckAll(@RequestBody Cart cart){
        int result=cartService.checkAll(MemberUtils.getUserId(),cart.getChecked());
        return new ResultUtil<Object>().setData(result);
    }

    @RequestMapping(value = "/member/cartDel",method = RequestMethod.POST)
    @ApiOperation(value = "删除购物车商品")
    public Result<Object> deleteCartItem(@RequestBody Cart cart){

        int result=cartService.deleteCartItem(MemberUtils.getUserId(),cart.getProductId());
        return new ResultUtil<Object>().setData(result);
    }

    @RequestMapping(value = "/member/delCartChecked",method = RequestMethod.POST)
    @ApiOperation(value = "删除购物车选中商品")
    public Result<Object> delChecked(@RequestBody Cart cart){

        cartService.delChecked(MemberUtils.getUserId());
        return new ResultUtil<Object>().setData(null);
    }
}
