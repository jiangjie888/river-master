package com.river.leader.core.shiro.service.impl;

import com.river.leader.core.common.constant.factory.ConstantFactory;
import com.river.leader.core.common.constant.state.ManagerStatus;
import com.river.leader.core.shiro.ShiroUser;
import com.river.leader.core.shiro.service.IUserAuthService;
import com.river.leader.core.utils.SpringContextHolder;
import com.river.leader.modular.system.dao.RoleMapper;
import com.river.leader.modular.system.dao.UserMapper;
import com.river.leader.modular.system.model.SysRole;
import com.river.leader.modular.system.model.SysUser;
import org.apache.shiro.authc.CredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.util.ByteSource;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@DependsOn("springContextHolder")
@Transactional(readOnly = true)
public class UserAuthServiceServiceImpl implements IUserAuthService {

    //@Autowired
    //private UserMapper userMapper;
    private UserMapper userMapper = SpringContextHolder.getBean(UserMapper.class);
    private RoleMapper roleMapper = SpringContextHolder.getBean(RoleMapper.class);

    //@Autowired
    //private MenuMapper menuMapper;

    public static IUserAuthService me() {
        return SpringContextHolder.getBean(IUserAuthService.class);
    }

    @Override
    public SysUser user(String account) {

        SysUser user = userMapper.getByAccount(account);
        List<Map<String, Object>> rolesMap = roleMapper.selectRolesByUserid(user.getId());
        List<SysRole> roles = new ArrayList<SysRole>();
        for (Map<String, Object> m : rolesMap) {
            SysRole role = new SysRole();
            role.setId(m.get("id").toString());
            role.setRoleCode(m.get("roleCode").toString());
            role.setRoleName(m.get("roleName").toString());
            role.setIsDefaultRole((boolean)m.get("isDefaultRole"));
            role.setRoleDescription(m.get("roleDescription").toString());
            roles.add(role);
        }
        user.setRoles(roles);
        // 账号不存在
        if (null == user) {
            throw new CredentialsException();
        }
        // 账号被冻结
        if (user.getUserStatus() != ManagerStatus.OK.getCode()) {
            throw new LockedAccountException();
        }
        return user;
    }

    @Override
    public ShiroUser shiroUser(SysUser user) {
        ShiroUser shiroUser = new ShiroUser();

        shiroUser.setId(user.getId().intValue());
        shiroUser.setAccount(user.getUserAccout());
        //shiroUser.setDeptId(user.getDeptid());
        //shiroUser.setDeptName(ConstantFactory.me().getDeptName(user.getDeptid()));
        shiroUser.setName(user.getUsername());

        //String[] roleArray = Convert.toStrArray(user.getRoles());
        List<SysRole> roles = user.getRoles();
        List<String> roleList = new ArrayList<String>();
        List<String> roleNameList = new ArrayList<String>();
        for (SysRole role : roles) {
            roleList.add(role.getId());
            roleNameList.add(role.getRoleName());
        }
        shiroUser.setRoleList(roleList);
        shiroUser.setRoleNames(roleNameList);

        return shiroUser;
    }

    /*@Override
    public List<String> findPermissionsByRoleId(Integer roleId) {
        return menuMapper.getResUrlsByRoleId(roleId);
    }*/

    @Override
    public String findRoleCodeByRoleId(String roleId) {
        return ConstantFactory.me().getSingleRoleTip(roleId);
    }

    @Override
    public SimpleAuthenticationInfo info(ShiroUser shiroUser, SysUser user, String realmName) {
        String credentials = user.getPassword();

        // 密码加盐处理
        String source = "abcdefg";
        ByteSource credentialsSalt = new Md5Hash(source);
        return new SimpleAuthenticationInfo(shiroUser, credentials, credentialsSalt, realmName);
    }

}
