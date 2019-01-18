package com.river.leader.core.shiro.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.river.leader.core.listener.ConfigListener;
import com.river.leader.core.shiro.ShiroKit;
import com.river.leader.core.shiro.ShiroUser;
import com.river.leader.core.shiro.service.IPermissionCheckService;
import com.river.leader.core.utils.HttpContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

/**
 * 权限自定义检查
 */
@Service
@Transactional(readOnly = true)
public class PermissionCheckServiceServiceImpl implements IPermissionCheckService {

    /**
     * 检查当前登录用户是否拥有指定的角色访问当
     */
    @Override
    public boolean check(Object[] permissions) {
        ShiroUser user = ShiroKit.getUser();
        if (null == user) {
            return false;
        }
        ArrayList<Object> objects = CollectionUtil.newArrayList(permissions);
        String join = CollectionUtil.join(objects, ",");
        if (ShiroKit.hasAnyRoles(join)) {
            return true;
        }
        return false;
    }

    /**
     * 检查当前登录用户是否拥有当前请求的servlet的权限
     */
    @Override
    public boolean checkAll() {
        HttpServletRequest request = HttpContext.getRequest();
        ShiroUser user = ShiroKit.getUser();
        if (null == user) {
            return false;
        }
        String requestURI = request.getRequestURI().replaceFirst(ConfigListener.getConf().get("contextPath"), "");
        String[] str = requestURI.split("/");
        if (str.length > 3) {
            requestURI = "/" + str[1] + "/" + str[2];
        }
        if (ShiroKit.hasPermission(requestURI)) {
            return true;
        }
        return false;
    }

}
