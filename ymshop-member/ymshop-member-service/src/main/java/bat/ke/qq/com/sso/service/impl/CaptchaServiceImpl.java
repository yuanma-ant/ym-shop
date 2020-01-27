package bat.ke.qq.com.sso.service.impl;

import bat.ke.qq.com.common.exception.YmshopException;
import bat.ke.qq.com.common.jedis.JedisClient;
import bat.ke.qq.com.common.pojo.ImageResult;
import bat.ke.qq.com.common.utils.VerifyCodeUtils;
import bat.ke.qq.com.sso.dto.CaptchaCodeRequest;
import bat.ke.qq.com.sso.dto.CaptchaCodeResponse;
import bat.ke.qq.com.sso.service.CaptchaService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CaptchaServiceImpl implements CaptchaService {
    private final static Logger logger = LoggerFactory.getLogger(CaptchaServiceImpl.class);
    @Autowired
    private JedisClient jedisClient;

    private final String CAPTCHA_UUID="kaptcha_uuid";


    @Override
    public CaptchaCodeResponse getCaptchaCode(CaptchaCodeRequest request) {
        CaptchaCodeResponse response=new CaptchaCodeResponse();
        try {
            ImageResult capText = VerifyCodeUtils.VerifyCode(140, 43, 4);
            response.setImageCode(capText.getImg());
            String uuid= UUID.randomUUID().toString();
            String captchaKey=CAPTCHA_UUID+uuid;
            jedisClient.set(captchaKey,capText.getCode());
            jedisClient.expire(captchaKey,60);
            response.setImageCode(capText.getImg());
            response.setUuid(uuid);
        }catch (Exception e){
            throw new YmshopException("生成验证码失败");
        }
        return response;
    }

    @Override
    public boolean validateCaptchaCode(CaptchaCodeRequest request) {
        try{
            if(request==null || StringUtils.isBlank(request.getCode())
                    || StringUtils.isBlank(request.getUuid())){
                throw new YmshopException("验证码参数校验失败");
            }
            String redisKey = CAPTCHA_UUID+request.getUuid();
            String code=jedisClient.get(redisKey);
            if (logger.isInfoEnabled()){
                logger.info("请求的redisKey={},请求的code={},从redis获得的code={}",new Object[]{redisKey,request.getCode(),code});
            }

            if(StringUtils.isNotBlank(code)&&request.getCode().equalsIgnoreCase(code)){
                return true;
            }
        }catch (Exception e){
            if (logger.isInfoEnabled()){
                logger.error("KaptchaServiceImpl.validateKaptchaCode occur Exception :"+e);
            }
            throw new YmshopException("验证码验证出现异常");
        }
        return false;
    }
}
