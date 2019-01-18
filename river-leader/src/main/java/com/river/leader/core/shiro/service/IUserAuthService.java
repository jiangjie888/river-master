package com.river.leader.core.shiro.service;

import com.river.leader.core.shiro.ShiroUser;
import com.river.leader.modular.system.model.SysUser;
import org.apache.shiro.authc.SimpleAuthenticationInfo;

/**
 * 定义shirorealm所需数据的接口
 */
public interface IUserAuthService {

    /**
     * 根据账号获取登录用户
     *
     * @param account 账号
     */
    SysUser user(String account);

    /**
     * 根据系统用户获取Shiro的用户
     *
     * @param user 系统用户
     */
    ShiroUser shiroUser(SysUser user);

    /**
     * 获取权限列表通过角色id
     *
     * @param roleId 角色id
     */
    //List<String> findPermissionsByRoleId(Integer roleId);

    /**
     * 根据角色id获取角色名称
     *
     * @param roleId 角色id
     */
    String findRoleCodeByRoleId(String roleId);

    /**
     * 获取shiro的认证信息
     */
    SimpleAuthenticationInfo info(ShiroUser shiroUser, SysUser user, String realmName);

}
