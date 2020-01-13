package bat.ke.qq.com.intercepter;

import bat.ke.qq.com.manager.dto.front.Member;

/**
 * 会员认证工具类，在需要认证的接口执行完拦截器后
 * 通过该类可以在当前线程任何位置获取到当前用户信息
 * 源码学院-ANT
 * 只为培养BAT程序员而生
 * http://bat.ke.qq.com
 * 往期视频加群:516212256 暗号:6
 */
public class MemberUtils {
    private static ThreadLocal<Member> memberThreadLocal=new ThreadLocal<>();

    public static Member getMemberThreadLocal() {
        return memberThreadLocal.get();
    }

    public static void setMemberThreadLocal(Member member) {
        memberThreadLocal.remove();
        memberThreadLocal.set(member);
    }

    public static Long getUserId(){
        Member member = memberThreadLocal.get();
        if(member!=null){
            return member.getId();
        }
        return null;
    }
}
