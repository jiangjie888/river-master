package com.river.leader.modular.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.river.leader.modular.system.dao.RoleMapper;
import com.river.leader.modular.system.model.SysRole;
import com.river.leader.modular.system.service.IRoleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 角色服务
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, SysRole> implements IRoleService {

    /*@Override
    @Transactional
    public void setAuthority(Integer roleId, String ids) {

        // 删除该角色所有的权限
        this.roleMapper.deleteRolesById(roleId);

        // 添加新的权限
        for (Long id : Convert.toLongArray(ids.split(","))) {
            Relation relation = new Relation();
            relation.setRoleid(roleId);
            relation.setMenuid(id);
            this.relationMapper.insert(relation);
        }
    }*/

    @Override
    public void delRoleById(String roleId) {
        this.baseMapper.delRoleById(roleId);
    }

    @Override
    public List<Map<String, Object>> selectRoles(String condition) {
        return this.baseMapper.selectRoles(condition);
    }

    /**
     * 根据用户Id查询角色列表
     */
    @Override
    public List<Map<String, Object>> selectRolesByUserid(Long userId) {
        return this.baseMapper.selectRolesByUserid(userId);
    }


}
