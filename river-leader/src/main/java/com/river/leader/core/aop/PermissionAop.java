package com.river.leader.core.aop;

import com.river.leader.core.common.annotion.Permission;
import com.river.leader.core.shiro.service.IPermissionCheckService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.naming.NoPermissionException;
import java.lang.reflect.Method;

/**
 * 权限检查的aop
 */
@Aspect
@Component
@Order(200)
public class PermissionAop {

    @Autowired
    private IPermissionCheckService check;

    @Pointcut(value = "@annotation(com.river.leader.core.common.annotion.Permission)")
    private void cutPermission() {

    }

    @Around("cutPermission()")
    public Object doPermission(ProceedingJoinPoint point) throws Throwable {
        MethodSignature ms = (MethodSignature) point.getSignature();
        Method method = ms.getMethod();
        Permission permission = method.getAnnotation(Permission.class);
        Object[] permissions = permission.value();
        if (permissions.length == 0) {

            //检查全体角色
            boolean result = check.checkAll();
            if (result) {
                return point.proceed();
            } else {
                throw new NoPermissionException();
            }

        } else {

            //检查指定角色
            boolean result = check.check(permissions);
            if (result) {
                return point.proceed();
            } else {
                throw new NoPermissionException();
            }
        }
    }

}
