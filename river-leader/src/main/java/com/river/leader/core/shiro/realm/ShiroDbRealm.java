package com.river.leader.core.shiro;

import com.river.leader.core.common.constant.BizExceptionEnum;
import com.river.leader.core.shiro.service.IUserAuthService;
import com.river.leader.core.shiro.service.impl.UserAuthServiceServiceImpl;
import com.river.leader.modular.system.model.SysUser;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ShiroDbRealm extends AuthorizingRealm {

    /**
     * 登录认证,主要是用来进行身份认证的，也就是说验证用户输入的账号和密码是否正确
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken)
            throws AuthenticationException {
        IUserAuthService shiroFactory = UserAuthServiceServiceImpl.me();
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;

        /*if (JwtTokenUtil.checkToken()) {
            throw new AuthenticationException("token invalid");
        }*/

        //实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
        SysUser user = shiroFactory.user(token.getUsername());
        if (user == null) {
            throw new AuthenticationException(BizExceptionEnum.USER_NOT_EXISTED.getMessage());
        }
        //自己写的基于密码的Sceret
        /*if (!JWTUtil.verify(token, username, managerInfo.getPassword())) {
            throw new AuthenticationException("Username or password error");
        }*/
        ShiroUser shiroUser = shiroFactory.shiroUser(user);
        return shiroFactory.info(shiroUser, user, super.getName());
    }

    /**
     * 此方法调用hasRole,hasPermission的时候才会进行回调.
     * <p>
     * 权限信息.(授权):
     * 1、如果用户正常退出，缓存自动清空；
     * 2、如果用户非正常退出，缓存自动清空；
     * 3、如果我们修改了用户的权限，而用户不退出系统，修改的权限无法立即生效。
     * （需要手动编程进行实现；放在service进行调用）
     * 在权限修改后调用realm中的方法，realm已经由spring管理，所以从spring中获取realm实例，调用clearCached方法；
     * :Authorization 是授权访问控制，用于对用户进行的操作授权，证明该用户是否允许进行当前操作，如访问某个链接，某个资源文件等。
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        /*
         * 当没有使用缓存的时候，不断刷新页面的话，这个代码会不断执行，
         * 当其实没有必要每次都重新设置权限信息，所以我们需要放到缓存中进行管理；
         * 当放到缓存中时，这样的话，doGetAuthorizationInfo就只会执行一次了，
         * 缓存过期之后会再次执行。
         */
        //_logger.info("权限配置-->MyShiroRealm.doGetAuthorizationInfo()");

        IUserAuthService shiroFactory = UserAuthServiceServiceImpl.me();
        ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();

        List<String> roleList = shiroUser.getRoleList();
        Set<String> permissionSet = new HashSet<>();
        Set<String> roleCodeSet = new HashSet<>();

        for (String roleId : roleList) {
            /*List<String> permissions = shiroFactory.findPermissionsByRoleId(roleId);
            if (permissions != null) {
                for (String permission : permissions) {
                    if (ToolUtil.isNotEmpty(permission)) {
                        permissionSet.add(permission);
                    }
                }
            }*/
            String roleCode = shiroFactory.findRoleCodeByRoleId(roleId);
            roleCodeSet.add(roleCode);
        }

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addStringPermissions(permissionSet);
        info.addRoles(roleCodeSet);
        return info;
    }

    /**
     * 设置认证加密方式
     */
    @Override
    public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
        HashedCredentialsMatcher md5CredentialsMatcher = new HashedCredentialsMatcher();
        md5CredentialsMatcher.setHashAlgorithmName(ShiroKit.hashAlgorithmName);
        md5CredentialsMatcher.setHashIterations(ShiroKit.hashIterations);
        super.setCredentialsMatcher(md5CredentialsMatcher);
    }
}
