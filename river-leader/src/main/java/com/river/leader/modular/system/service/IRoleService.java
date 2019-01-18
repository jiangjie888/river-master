package com.river.leader.modular.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.river.leader.modular.system.model.SysRole;

import java.util.List;
import java.util.Map;

/**
 * 角色相关业务
 */
public interface IRoleService extends IService<SysRole> {

    /**
     * 设置某个角色的权限
     */
    //void setAuthority(Integer roleId, String ids);

    /**
     * 删除角色
     */
    void delRoleById(String roleId);

    /**
     * 根据条件查询角色列表
     */
    List<Map<String, Object>> selectRoles(String condition);

    /**
     * 根据用户Id查询角色列表
     */
    List<Map<String, Object>> selectRolesByUserid(Long userId);

    /**
     * 删除某个角色的所有权限
     */
    //int deleteRolesById(String roleId);

}
