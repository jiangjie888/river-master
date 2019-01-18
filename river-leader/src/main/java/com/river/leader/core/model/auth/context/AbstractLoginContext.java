package com.river.leader.core.model.auth.context;


import com.river.leader.core.model.auth.AbstractLoginUser;

/**
 * 快速获取登录信息上下文
 */
public interface AbstractLoginContext {

    /**
     * 获取当前用户的token
     */
    String getCurrentUserToken();

    /**
     * 获取当前用户
     */
    <T extends AbstractLoginUser> T getLoginUser();

}
