package com.river.leader.core.shiro.realm;

import com.river.leader.core.common.constant.BizExceptionEnum;
import com.river.leader.core.jwt.utils.JwtTokenUtil;
import com.river.leader.core.shiro.ShiroUser;
import com.river.leader.core.shiro.matcher.JwtMatcher;
import com.river.leader.core.shiro.service.IUserAuthService;
import com.river.leader.core.shiro.service.impl.UserAuthServiceServiceImpl;
import com.river.leader.core.shiro.token.JwtToken;
import com.river.leader.modular.system.model.SysUser;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @program: river-master
 * @description:
 * @author: jason
 * @create: 2018-12-06 16:44
 **/
public class ShiroJwtRealm extends AuthorizingRealm {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    /*public ShiroJwtRealm(){
        //这里使用我们自定义的Matcher
        this.setCredentialsMatcher(new JwtMatcher(jwtTokenUtil));
    }*/

    @PostConstruct
    private void init0(){
        this.setCredentialsMatcher(new JwtMatcher(jwtTokenUtil));
    }


    /**
     * 限定这个Realm只支持我们自定义的JWT Token
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 根controller登录一样，也是获取用户的salt值，给到shiro，由shiro来调用matcher来做认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {

       if (!(authcToken instanceof JwtToken)) {
            return null;
        }
        /*JwtToken jwtToken = (JwtToken)authcToken;
        String jwt = (String)jwtToken.getCredentials();
        Claims payload = null;
        try{
            // 预先解析Payload
            // 没有做任何的签名校验
            payload = jwtTokenUtil.getClaimFromToken(jwt);
        } catch(MalformedJwtException e){
            throw new AuthenticationException("errJwt");     //令牌格式错误
        } catch(Exception e){
            throw new AuthenticationException("errsJwt");    //令牌无效
        }
        if(null == payload){
            throw new AuthenticationException("errJwt");    //令牌无效
        }
        return new SimpleAuthenticationInfo("jwt:"+payload,jwt,this.getName());*/

        IUserAuthService shiroFactory = UserAuthServiceServiceImpl.me();
        JwtToken jwtToken = (JwtToken)authcToken;
        SysUser user = shiroFactory.user(jwtToken.getAppId());
        if (user == null) {
            throw new AuthenticationException(BizExceptionEnum.USER_NOT_EXISTED.getMessage());
        }

        ShiroUser shiroUser = shiroFactory.shiroUser(user);
        return new SimpleAuthenticationInfo(shiroUser, user.getPassword(),this.getName());
        //return shiroFactory.info(shiroUser, user, this.getName());


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
    /*@Override
    public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
        HashedCredentialsMatcher md5CredentialsMatcher = new HashedCredentialsMatcher();
        md5CredentialsMatcher.setHashAlgorithmName(ShiroKit.hashAlgorithmName);
        md5CredentialsMatcher.setHashIterations(ShiroKit.hashIterations);
        super.setCredentialsMatcher(md5CredentialsMatcher);
    }*/
}