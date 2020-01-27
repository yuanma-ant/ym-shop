package bat.ke.qq.com.sso.dto;

import java.io.Serializable;

public class CaptchaCodeResponse implements Serializable {
    private String imageCode;
    private String uuid;

    public String getImageCode() {
        return imageCode;
    }

    public void setImageCode(String imageCode) {
        this.imageCode = imageCode;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
