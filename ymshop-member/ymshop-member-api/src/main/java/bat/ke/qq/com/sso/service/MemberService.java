package bat.ke.qq.com.sso.service;

/**
 * @author 源码学院
 */
public interface MemberService {

    /**
     * 头像上传
     * @param userId
     * @param token
     * @param imgData
     * @return
     */
    String imageUpload(Long userId,String token,String imgData);
}
