package bat.ke.qq.com.common.exception;

/**
 * @author 源码学院
 */
public class YmshopUploadException extends RuntimeException {

    private String msg;

    public YmshopUploadException(String msg){
        super(msg);
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
