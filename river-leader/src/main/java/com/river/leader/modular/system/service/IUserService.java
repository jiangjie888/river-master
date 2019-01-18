package com.river.leader.modular.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.river.leader.modular.system.model.SysUser;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 管理员表 服务类
 * </p>
 */
public interface IUserService extends IService<SysUser> {


    /**
     * 修改用户状态
     */
    int setStatus(Integer userId, int status);

    /**
     * 修改密码
     */
    int changePwd(Integer userId, String pwd);

    /**
     * 根据条件查询用户列表
     */
    List<Map<String, Object>> selectUsers(String name, String beginTime, String endTime);

    /**
     * 设置用户的角色
     */
    int setRoles(Integer userId, String roleIds);

    /**
     * 通过账号获取用户
     */
    SysUser getByAccount(String account);

}
