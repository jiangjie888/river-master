package com.river.leader.core.model.auth.context;


import com.river.leader.core.model.auth.AbstractLoginUser;

/**
 * <pre>
 * 当前登录用户的临时保存容器
 *
 *  说明：
 *     当OPEN_UP_FLAG标识在ThreadLocal里为true
 * </pre>
 */
public class LoginUserHolder {

    private static final ThreadLocal<Boolean> OPEN_UP_FLAG = new ThreadLocal<>();
    private static final ThreadLocal<AbstractLoginUser> LONGIN_USER_HOLDER = new ThreadLocal<>();

    /**
     * 初始化
     */
    public static void init() {
        OPEN_UP_FLAG.set(true);
    }

    /**
     * 这个方法如果OPEN_UP_FLAG标识没开启，则会set失效
     */
    public static void set(AbstractLoginUser abstractLoginUser) {
        Boolean openUpFlag = OPEN_UP_FLAG.get();
        if (openUpFlag == null || openUpFlag.equals(false)) {
            return;
        } else {
            LONGIN_USER_HOLDER.set(abstractLoginUser);
        }
    }

    /**
     * 这个方法如果OPEN_UP_FLAG标识没开启，则会get值为null
     */
    public static AbstractLoginUser get() {
        Boolean openUpFlag = OPEN_UP_FLAG.get();
        if (openUpFlag == null || openUpFlag.equals(false)) {
            return null;
        } else {
            return LONGIN_USER_HOLDER.get();
        }
    }

    /**
     * 删除保存的用户
     */
    public static void remove() {
        OPEN_UP_FLAG.remove();
        LONGIN_USER_HOLDER.remove();
    }
}
