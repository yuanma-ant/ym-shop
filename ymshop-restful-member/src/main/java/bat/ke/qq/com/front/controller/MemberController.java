package bat.ke.qq.com.front.controller;

import bat.ke.qq.com.annotation.IgnoreAuth;
import bat.ke.qq.com.common.jedis.JedisClient;
import bat.ke.qq.com.common.pojo.GeetInit;
import bat.ke.qq.com.common.utils.CookieUtil;
import bat.ke.qq.com.common.utils.GeetestLib;
import bat.ke.qq.com.intercepter.TokenIntercepter;
import bat.ke.qq.com.manager.dto.front.CommonDto;
import bat.ke.qq.com.manager.dto.front.MemberLoginRegist;
import bat.ke.qq.com.common.pojo.Result;
import bat.ke.qq.com.common.utils.ResultUtil;
import bat.ke.qq.com.manager.dto.front.Member;
import bat.ke.qq.com.sso.service.LoginService;
import bat.ke.qq.com.sso.service.MemberService;
import bat.ke.qq.com.sso.service.RegisterService;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.UUID;

/**
 * @author 源码学院
 */
@RestController
@Api(description = "会员注册登录")
public class MemberController {

    private final static Logger log= LoggerFactory.getLogger(MemberController.class);

    @Autowired
    private LoginService loginService;
    @Autowired
    private RegisterService registerService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private JedisClient jedisClient;

    @RequestMapping(value = "/member/geetestInit",method = RequestMethod.GET)
    @ApiOperation(value = "极验初始化")
    @IgnoreAuth
    public String geetesrInit(HttpServletRequest request){

        GeetestLib gtSdk = new GeetestLib(GeetestLib.id, GeetestLib.key,GeetestLib.newfailback);

        String resStr = "{}";

        //自定义参数,可选择添加
        HashMap<String, String> param = new HashMap<String, String>();

        //进行验证预处理
        int gtServerStatus = gtSdk.preProcess(param);

        //将服务器状态设置到redis中
        //request.getSession().setAttribute(gtSdk.gtServerStatusSessionKey, gtServerStatus);
        String key = UUID.randomUUID().toString();
        jedisClient.set(key,gtServerStatus+"");
        jedisClient.expire(key,360);

        resStr = gtSdk.getResponseStr();
        GeetInit geetInit = new Gson().fromJson(resStr,GeetInit.class);
        geetInit.setStatusKey(key);
        return new Gson().toJson(geetInit);
    }

    @RequestMapping(value = "/member/login",method = RequestMethod.POST)
    @ApiOperation(value = "用户登录")
    @IgnoreAuth
    public Result<Member> login(@RequestBody MemberLoginRegist memberLoginRegist,
                                HttpServletRequest request, HttpServletResponse response){

        //极验验证
        GeetestLib gtSdk = new GeetestLib(GeetestLib.id, GeetestLib.key,GeetestLib.newfailback);

        String challenge=memberLoginRegist.getChallenge();
        String validate=memberLoginRegist.getValidate();
        String seccode=memberLoginRegist.getSeccode();

        //从redis中获取gt-server状态
        //int gt_server_status_code = (Integer) request.getSession().getAttribute(gtSdk.gtServerStatusSessionKey);
        int gt_server_status_code = Integer.parseInt(jedisClient.get(memberLoginRegist.getStatusKey()));

        //自定义参数,可选择添加
        HashMap<String, String> param = new HashMap<String, String>();

        int gtResult = 0;

        if (gt_server_status_code == 1) {
            //gt-server正常，向gt-server进行二次验证
            gtResult = gtSdk.enhencedValidateRequest(challenge, validate, seccode, param);
            System.out.println(gtResult);
        } else {
            // gt-server非正常情况下，进行failback模式验证
            System.out.println("failback:use your own server captcha validate");
            gtResult = gtSdk.failbackValidateRequest(challenge, validate, seccode);
            System.out.println(gtResult);
        }

        Member member=loginService.userLogin(memberLoginRegist.getUserName(), memberLoginRegist.getUserPwd());
        //极验验证码需要收费，暂时移除：
//        if (gtResult == 1) {
            // 验证成功
//            member=loginService.userLogin(memberLoginRegist.getUserName(), memberLoginRegist.getUserPwd());
//        }
//        else {
//            // 验证失败
//            member.setState(0);
//            member.setMessage("验证失败");
//        }
//        登录成功写入cookies
        Cookie cookie= CookieUtil.genCookie(TokenIntercepter.ACCESS_TOKEN,member.getToken(),"/",24*60*60);
        response.addCookie(cookie);
        return new ResultUtil<Member>().setData(member);
    }

    @RequestMapping(value = "/member/checkLogin",method = RequestMethod.GET)
    @ApiOperation(value = "判断用户是否登录")
    @IgnoreAuth
    public Result<Member> checkLogin(@RequestParam(defaultValue = "") String token){
        Member member=loginService.getUserByToken(token);
        return new ResultUtil<Member>().setData(member);
    }

    @RequestMapping(value = "/member/loginOut",method = RequestMethod.GET)
    @ApiOperation(value = "退出登录")
    public Result<Object> logout(@RequestParam(defaultValue = "") String token){

        loginService.logout(token);
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/member/register",method = RequestMethod.POST)
    @ApiOperation(value = "用户注册")
    @IgnoreAuth
    public Result<Object> register(@RequestBody MemberLoginRegist memberLoginRegist,
                                   HttpServletRequest request){

        //极验验证
        GeetestLib gtSdk = new GeetestLib(GeetestLib.id, GeetestLib.key,GeetestLib.newfailback);

        String challenge=memberLoginRegist.getChallenge();
        String validate=memberLoginRegist.getValidate();
        String seccode=memberLoginRegist.getSeccode();

        //从redis中获取gt-server状态
        //int gt_server_status_code = (Integer) request.getSession().getAttribute(gtSdk.gtServerStatusSessionKey);
        int gt_server_status_code = Integer.parseInt(jedisClient.get(memberLoginRegist.getStatusKey()));

        //自定义参数,可选择添加
        HashMap<String, String> param = new HashMap<String, String>();

        int gtResult = 0;

        if (gt_server_status_code == 1) {
            //gt-server正常，向gt-server进行二次验证
            gtResult = gtSdk.enhencedValidateRequest(challenge, validate, seccode, param);
            System.out.println(gtResult);
        } else {
            // gt-server非正常情况下，进行failback模式验证
            System.out.println("failback:use your own server captcha validate");
            gtResult = gtSdk.failbackValidateRequest(challenge, validate, seccode);
            System.out.println(gtResult);
        }

//        if (gtResult == 1) {
            // 验证成功
            int result=registerService.register(memberLoginRegist.getUserName(), memberLoginRegist.getUserPwd());
            if(result==0){
                return new ResultUtil<Object>().setErrorMsg("该用户名已被注册");
            }else if(result==-1){
                return new ResultUtil<Object>().setErrorMsg("用户名密码不能为空");
            }
            return new ResultUtil<Object>().setData(result);
//        }
//        else {
//            // 验证失败
//            return new ResultUtil<Object>().setErrorMsg("验证失败");
//        }
    }

    @RequestMapping(value = "/member/imgaeUpload",method = RequestMethod.POST)
    @ApiOperation(value = "用户头像上传")
    public Result<Object> imgaeUpload(@RequestBody CommonDto common){

        String imgPath = memberService.imageUpload(common.getUserId(),common.getToken(),common.getImgData());
        return new ResultUtil<Object>().setData(imgPath);
    }
}
