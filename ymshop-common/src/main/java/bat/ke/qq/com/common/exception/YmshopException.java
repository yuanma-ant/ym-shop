package bat.ke.qq.com.common.exception;

/**
 * @author Exrick
 * @date 2017/8/24
 */
public class YmshopException extends RuntimeException {

    private String msg;

    public YmshopException(String msg){
        super(msg);
        this.msg=msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
