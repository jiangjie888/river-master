package com.river.leader.modular.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.river.leader.core.common.constant.DatasourceEnum;
import com.river.leader.core.mutidatasource.annotion.DataSource;
import com.river.leader.modular.system.dao.UserMapper;
import com.river.leader.modular.system.model.SysUser;
import com.river.leader.modular.system.service.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 管理员表 服务实现类
 * </p>
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, SysUser> implements IUserService {

    @Override
    @DataSource(name = DatasourceEnum.DATA_SOURCE_Infra)
    @Transactional
    public int setStatus(Integer userId, int status) {
        return this.baseMapper.setStatus(userId, status);
    }

    @Override
    public int changePwd(Integer userId, String pwd) {
        return this.baseMapper.changePwd(userId, pwd);
    }

    @Override
    public List<Map<String, Object>> selectUsers(String name, String beginTime, String endTime) {
        return this.baseMapper.selectUsers(name, beginTime, endTime);
    }

    @Override
    public int setRoles(Integer userId, String roleIds) {
        return this.baseMapper.setRoles(userId, roleIds);
    }

    @Override
    public SysUser getByAccount(String account) {
        return this.baseMapper.getByAccount(account);
    }
}
