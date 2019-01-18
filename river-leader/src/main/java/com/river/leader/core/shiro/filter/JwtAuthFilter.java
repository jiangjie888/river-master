package com.river.leader.core.shiro.filter;

import com.river.leader.core.common.constant.BizExceptionEnum;
import com.river.leader.core.jwt.utils.JwtTokenUtil;
import com.river.leader.core.model.reqres.response.ErrorResponseData;
import com.river.leader.core.shiro.token.JwtToken;
import com.river.leader.core.utils.HttpContext;
import com.river.leader.core.utils.RenderUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: river-master
 * @description:
 * @author: jason
 * @create: 2018-12-06 15:52
 **/
public class JwtAuthFilter extends AuthenticatingFilter {

    //private static JwtTokenUtil jwtTokenUtil = SpringContextHolder.getBean(JwtTokenUtil.class);

    //@Autowired
    private JwtTokenUtil jwtTokenUtil;

    private static final Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);

    public JwtAuthFilter(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    /**
     * 父类会在请求进入拦截器后调用该方法，返回true则继续，返回false则会调用onAccessDenied()。这里在不通过时，还调用了isPermissive()方法，我们后面解释。
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if(this.isLoginRequest(request, response))
            return true;
        boolean allowed = false;
        try {
            allowed = executeLogin(request, response);
        } catch(IllegalStateException e){ //not found any token
            log.error("Not found any token");
            //throw new AuthenticationException("token不允许为空");
        }catch (Exception e) {
            log.error("Error occurs when login", e);
        }
        return allowed || super.isPermissive(mappedValue);
    }
    /**
     * 这里重写了父类的方法，使用我们自己定义的Token类，提交给shiro。这个方法返回null的话会直接抛出异常，进入isAccessAllowed（）的异常处理逻辑。
     */
    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) {
        HttpServletRequest request = WebUtils.toHttp(servletRequest);

        final String requestHeader = request.getHeader("Authorization");
        String authToken;
        if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
            authToken = requestHeader.substring(7);

            if (StringUtils.isNotBlank(authToken) && !jwtTokenUtil.isTokenExpired(authToken)) {
                String userAccount = jwtTokenUtil.getPrivateClaimFromToken(authToken,"http://schemas.xmlsoap.org/ws/2005/05/identity/claims/sid");
                return new JwtToken(HttpContext.getIp(),"",authToken,userAccount);
            }
        }
        return null;
    }
    /**
     * 如果这个Filter在之前isAccessAllowed（）方法中返回false,则会进入这个方法。我们这里直接返回错误的response
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletResponse response = WebUtils.toHttp(servletResponse);
        RenderUtil.renderJson(response, new ErrorResponseData(BizExceptionEnum.TOKEN_ERROR.getCode(), BizExceptionEnum.TOKEN_ERROR.getMessage()));
        return false;
    }
    /**
     *  如果Shiro Login认证成功，会进入该方法，等同于用户名密码登录成功，我们这里还判断了是否要刷新Token
     */
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
       /*HttpServletResponse httpResponse = WebUtils.toHttp(response);
        String newToken = null;
        if(token instanceof JwtToken){
            JwtToken jwtToken = (JwtToken)token;
            ShiroUser user = (ShiroUser) subject.getPrincipal();
            boolean shouldRefresh = shouldTokenRefresh(JwtUtils.getIssuedAt(jwtToken.getToken()));
            if(shouldRefresh) {
                newToken = userService.generateJwtToken(user.getUsername());
            }
        }
        if(StringUtils.isNotBlank(newToken)) {
            httpResponse.setHeader("x-auth-token", newToken);
        }*/

        return true;
    }
    /**
     * 如果调用shiro的login认证失败，会回调这个方法，这里我们什么都不做，因为逻辑放到了onAccessDenied（）中。
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        log.error("Validate token fail, token:{}, error:{}", token.toString(), e.getMessage());
        return false;
    }


}