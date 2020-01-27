package bat.ke.qq.com.front.controller;

import bat.ke.qq.com.annotation.IgnoreAuth;
import bat.ke.qq.com.common.exception.YmshopException;
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
import bat.ke.qq.com.sso.dto.CaptchaCodeRequest;
import bat.ke.qq.com.sso.dto.CaptchaCodeResponse;
import bat.ke.qq.com.sso.service.CaptchaService;
import bat.ke.qq.com.sso.service.LoginService;
import bat.ke.qq.com.sso.service.MemberService;
import bat.ke.qq.com.sso.service.RegisterService;
import com.google.gson.Gson;
import com.sun.scenario.effect.impl.prism.PrImage;
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
    @Autowired
    private CaptchaService captchaService;

    @RequestMapping(value = "/member/geetestInit",method = RequestMethod.GET)
    @ApiOperation(value = "极验初始化")
    @IgnoreAuth
    public Result<String> geetesrInit(HttpServletRequest request,HttpServletResponse response){
        CaptchaCodeRequest captchaCodeRequest=new CaptchaCodeRequest();
        CaptchaCodeResponse kaptchaCodeResponse=captchaService.getCaptchaCode(captchaCodeRequest);
        if(kaptchaCodeResponse!=null){
            Cookie cookie=CookieUtil.genCookie("kaptcha_uuid",kaptchaCodeResponse.getUuid(),"/",60);
            response.addCookie(cookie);
            return new ResultUtil<String>().setData(kaptchaCodeResponse.getImageCode());
        }
        return new ResultUtil<String>().setErrorMsg("获取验证码失败");
    }

    @RequestMapping(value = "/member/login",method = RequestMethod.POST)
    @ApiOperation(value = "用户登录")
    @IgnoreAuth
    public Result<Member> login(@RequestBody MemberLoginRegist memberLoginRegist,
                                HttpServletRequest request, HttpServletResponse response){

        CaptchaCodeRequest captchaCodeRequest = new CaptchaCodeRequest();
        String uuid = CookieUtil.getCookieValue(request, "kaptcha_uuid");
        captchaCodeRequest.setCode(memberLoginRegist.getCaptcha());
        captchaCodeRequest.setUuid(uuid);
        boolean validate = captchaService.validateCaptchaCode(captchaCodeRequest);
        Member member=null;
        if(validate){
            member=loginService.userLogin(memberLoginRegist.getUserName(), memberLoginRegist.getUserPwd());
            // 登录成功写入cookies
            Cookie cookie= CookieUtil.genCookie(TokenIntercepter.ACCESS_TOKEN,member.getToken(),"/",24*60*60);
            response.addCookie(cookie);
        }else {
           throw new YmshopException("请输入正确的验证码");
        }

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
        CaptchaCodeRequest captchaCodeRequest = new CaptchaCodeRequest();
        String uuid = CookieUtil.getCookieValue(request, "kaptcha_uuid");
        captchaCodeRequest.setCode(memberLoginRegist.getCaptcha());
        captchaCodeRequest.setUuid(uuid);
        boolean validate = captchaService.validateCaptchaCode(captchaCodeRequest);

        int result=0;
        if(validate){
            result=registerService.register(memberLoginRegist.getUserName(), memberLoginRegist.getUserPwd());
            if(result==0){
                return new ResultUtil<Object>().setErrorMsg("该用户名已被注册");
            }else if(result==-1){
                return new ResultUtil<Object>().setErrorMsg("用户名密码不能为空");
            }
        }else{
            return new ResultUtil<Object>().setErrorMsg("验证码验证失败");
        }
        return new ResultUtil<Object>().setData(result);
    }

    @RequestMapping(value = "/member/imgaeUpload",method = RequestMethod.POST)
    @ApiOperation(value = "用户头像上传")
    public Result<Object> imgaeUpload(@RequestBody CommonDto common){

        String imgPath = memberService.imageUpload(common.getUserId(),common.getToken(),common.getImgData());
        return new ResultUtil<Object>().setData(imgPath);
    }
}
