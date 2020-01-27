package bat.ke.qq.com.manager.dto.front;

import java.io.Serializable;

/**
 * @author 源码学院
 */
public class MemberLoginRegist implements Serializable {

    private String userName;

    private String userPwd;

    private String captcha;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }
}
