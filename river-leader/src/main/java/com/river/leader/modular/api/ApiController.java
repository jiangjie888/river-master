package com.river.leader.modular.api;

import com.river.leader.core.common.controller.BaseController;
import com.river.leader.core.jwt.utils.JwtTokenUtil;
import com.river.leader.core.model.reqres.response.ErrorResponseData;
import com.river.leader.core.model.reqres.response.ResponseData;
import com.river.leader.core.shiro.ShiroKit;
import com.river.leader.core.shiro.ShiroUser;
import com.river.leader.modular.system.model.SysUser;
import com.river.leader.modular.system.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * 接口控制器提供
 */
@Api(tags = "测试 restful api")
@RestController
@RequestMapping("/api/TestApi")
public class ApiController extends BaseController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private IUserService userService;

    /**
     * api登录接口，通过账号密码获取token
     */
    @ApiOperation(value = "测试接口是否走鉴权")
    @RequestMapping(value = "/auth", method = RequestMethod.POST, produces = "application/json")
    public Object auth(@RequestParam("username") String username, @RequestParam("password") String password) {

        //封装请求账号密码为shiro可验证的token
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password.toCharArray());

        //获取数据库中的账号密码，准备比对
        SysUser user = userService.getByAccount(username);

        String credentials = user.getPassword();
        String salt = "abcdefg";
        ByteSource credentialsSalt = new Md5Hash(salt);
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(
                new ShiroUser(), credentials, credentialsSalt, "");

        //校验用户账号密码
        HashedCredentialsMatcher md5CredentialsMatcher = new HashedCredentialsMatcher();
        md5CredentialsMatcher.setHashAlgorithmName(ShiroKit.hashAlgorithmName);
        md5CredentialsMatcher.setHashIterations(ShiroKit.hashIterations);
        boolean passwordTrueFlag = md5CredentialsMatcher.doCredentialsMatch(
                usernamePasswordToken, simpleAuthenticationInfo);

        if (passwordTrueFlag) {
            HashMap<String, Object> claims = new HashMap<>();
            claims.put("http://schemas.xmlsoap.org/ws/2005/05/identity/claims/sid",user.getUserAccout());
            claims.put("http://schemas.xmlsoap.org/ws/2005/05/identity/claims/nameidentifier",user.getId());
            claims.put("http://schemas.xmlsoap.org/ws/2005/05/identity/claims/name",user.getUsername());

            HashMap<String, Object> result = new HashMap<>();
            result.put("token", jwtTokenUtil.generateToken(String.valueOf(user.getId()),claims));
            return result;
        } else {
            return new ErrorResponseData(500, "账号密码错误！");
        }
    }

    /**
     * 测试接口是否走鉴权
     */
    @ApiOperation(value = "测试接口是否走鉴权")
    @RequestMapping(value = "/permission", method = RequestMethod.POST)
    public Object permission() {
        return SUCCESS_TIP;
    }

    @ApiOperation(value = "所有人都可以访问，但是用户与游客看到的内容不同")
    @GetMapping("/article")
    public ResponseData article() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return ResponseData.success(200, "You are already logged in", null);
        } else {
            return ResponseData.success(200, "You are guest", null);
        }
    }

    @ApiOperation(value = "登入的用户才可以进行访问")
    @GetMapping("/require_auth")
    @RequiresAuthentication
    public ResponseData requireAuth() {
        return ResponseData.success(200, "You are authenticated", null);
    }

    @ApiOperation(value = "admin的角色用户才可以登入")
    @GetMapping("/require_role")
    @RequiresRoles("admin")
    public ResponseData requireRole() {
        return ResponseData.success(200, "You are visiting require_role", null);
    }

    @ApiOperation(value = "拥有view和edit权限的用户才可以访问")
    @GetMapping("/require_permission")
    @RequiresPermissions(logical = Logical.AND, value = {"view", "edit"})
    public ResponseData requirePermission() {
        return ResponseData.success(200, "You are visiting permission require edit,view", null);
    }

    @RequestMapping(path = "/401")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseData unauthorized() {
        return ResponseData.success(401, "Unauthorized", null);
    }


}

