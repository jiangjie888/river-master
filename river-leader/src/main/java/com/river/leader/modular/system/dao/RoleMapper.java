package com.river.leader.modular.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.river.leader.modular.system.model.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 */
@Mapper
public interface RoleMapper extends BaseMapper<SysRole> {

    /**
     * 根据条件查询角色列表
     */
    List<Map<String, Object>> selectRoles(@Param("condition") String condition);


    /**
     * 根据用户Id查询角色列表
     */
    List<Map<String, Object>> selectRolesByUserid(@Param("userId") Long userId);

    /**
     * 删除某个角色的所有权限
     */
    int delRoleById(@Param("roleId") String roleId);

}