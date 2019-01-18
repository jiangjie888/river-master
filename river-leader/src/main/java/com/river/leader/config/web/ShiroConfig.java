package com.river.leader.config.web;


import com.river.leader.core.jwt.utils.JwtTokenUtil;
import com.river.leader.core.shiro.filter.JwtAuthFilter;
import com.river.leader.core.shiro.realm.ShiroJwtRealm;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * shiro权限管理的配置
 */
@Configuration
public class ShiroConfig {

    //@Autowired
    //private JwtMatcher jwtMatcher;

    /**
     * 安全管理器
     */
    @Bean
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(this.shiroJwtRealm());

        // 关闭shiro自带的session
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);

        //securityManager.setCacheManager(cacheShiroManager);
        //securityManager.setRememberMeManager(rememberMeManager);
        //securityManager.setSessionManager(sessionManager);
        return securityManager;
    }

    /**
     * spring session管理器（多机环境）
     */
    /*@Bean
    @ConditionalOnProperty(prefix = "smartcloud", name = "spring-session-open", havingValue = "true")
    public ServletContainerSessionManager servletContainerSessionManager() {
        return new ServletContainerSessionManager();
    }*/

    /**
     * session管理器(单机环境)
     */
    /*@Bean
    @ConditionalOnProperty(prefix = "smartcloud", name = "spring-session-open", havingValue = "false")
    public DefaultWebSessionManager defaultWebSessionManager(CacheManager cacheShiroManager, AppProperties appProperties) {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setCacheManager(cacheShiroManager);
        sessionManager.setSessionValidationInterval(appProperties.getSessionValidationInterval() * 1000);
        sessionManager.setGlobalSessionTimeout(appProperties.getSessionInvalidateTime() * 1000);
        sessionManager.setDeleteInvalidSessions(true);
        sessionManager.setSessionValidationSchedulerEnabled(true);
        Cookie cookie = new SimpleCookie(ShiroHttpSession.DEFAULT_SESSION_ID_NAME);
        cookie.setName("shiroCookie");
        cookie.setHttpOnly(true);
        sessionManager.setSessionIdCookie(cookie);
        return sessionManager;
    }*/

    /**
     * 缓存管理器 使用Ehcache实现
     */
    /*@Bean
    public CacheManager getCacheShiroManager(EhCacheManagerFactoryBean ehcache) {
        EhCacheManager ehCacheManager = new EhCacheManager();
        ehCacheManager.setCacheManager(ehcache.getObject());
        return ehCacheManager;
    }*/

    /**
     * 项目自定义的Realm
     */
    /*@Bean
    public ShiroDbRealm shiroDbRealm() {
        return new ShiroDbRealm();
    }*/

    @Bean
    public ShiroJwtRealm shiroJwtRealm() {
        ShiroJwtRealm jwtRealm = new ShiroJwtRealm();
        //jwtRealm.setCredentialsMatcher(new JwtMatcher());
        //jwtRealm.setAuthenticationTokenClass(JwtToken.class);
        return jwtRealm;
    }

    /**
     * rememberMe管理器, cipherKey生成见{@code Base64Test.java}
     */
    @Bean
    public CookieRememberMeManager rememberMeManager(SimpleCookie rememberMeCookie) {
        CookieRememberMeManager manager = new CookieRememberMeManager();
        manager.setCipherKey(Base64.decode("Z3VucwAAAAAAAAAAAAAAAA=="));
        manager.setCookie(rememberMeCookie);
        return manager;
    }

    /**
     * 记住密码Cookie
     */
    @Bean
    public SimpleCookie rememberMeCookie() {
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        simpleCookie.setHttpOnly(true);
        simpleCookie.setMaxAge(7 * 24 * 60 * 60);//7天
        return simpleCookie;
    }

    /**
     * 增加对rest api鉴权的spring mvc过滤器
     */
    /*@Bean(name="jwtAuthFilter")
    public JwtAuthFilter jwtAuthFilter(){
        return new JwtAuthFilter();
    }*/

    /*@Bean(name="appUserFilter")
    public AppUserFilter appUserFilter(){
        return new AppUserFilter();
    }*/

    /*@Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        //filterRegistrationBean.setFilter(new JwtAuthFilter());
        filterRegistrationBean.setFilter(new DelegatingFilterProxy("shiroFilter"));
        //  该值缺省为false,表示生命周期由SpringApplicationContext管理,设置为true则表示由ServletContainer管理
        filterRegistrationBean.addInitParameter("targetFilterLifecycle","true");  //false:webcontext  true:servlet
        filterRegistrationBean.setAsyncSupported(true);
        filterRegistrationBean.setEnabled(true);
        filterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST);
        //List<String> urlPatterns = new ArrayList<String>();
        //urlPatterns.add("/*");
        *//*urlPatterns.add("/druid/**");
        urlPatterns.add("/swagger-ui.html");
        urlPatterns.add("/swagger-resources");
        urlPatterns.add("/swagger-resources/**");
        urlPatterns.add("/v2/api-docs");
        urlPatterns.add("/webjars/springfox-swagger-ui/**");
        urlPatterns.add("/webjars/**");*//*
        //filterRegistrationBean.setUrlPatterns(urlPatterns);
        //filterRegistrationBean.addUrlPatterns("/swagger-ui.html");
        //filterRegistrationBean.addUrlPatterns("/swagger-resources/**");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/reg,/druid/*,/swagger-ui.html");
        //filterRegistrationBean.setOrder(100);//order的数值越小 则优先级越高

        return filterRegistrationBean;
    }*/

    /**
     * Shiro的过滤器链
     */
    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(DefaultWebSecurityManager securityManager,JwtTokenUtil jwtTokenUtil) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);
        /**
         * 默认的登陆访问url
         */
        //shiroFilter.setLoginUrl("/login");
        /**
         * 登陆成功后跳转的url
         */
        //shiroFilter.setSuccessUrl("/");
        /**
         * 没有权限跳转的url
         */
        shiroFilter.setUnauthorizedUrl("/api/TestApi/401");

        HashMap<String, Filter> myFilters = new HashMap<>();
        myFilters.put("jwt", new JwtAuthFilter(jwtTokenUtil));
        //myFilters.put("api", new RestApiInteceptor());
        shiroFilter.setFilters(myFilters);

        /**
         * 配置shiro拦截器链
         *
         * anon  不需要认证
         * authc 需要认证
         * user  验证通过或RememberMe登录的都可以
         *
         * 当应用开启了rememberMe时,用户下次访问时可以是一个user,但不会是authc,因为authc是需要重新认证的
         *
         * 顺序从上到下,优先级依次降低
         *
         * api开头的接口，走rest api鉴权，不走shiro鉴权
         *
         */
        Map<String, String> hashMap = new LinkedHashMap<>();
        hashMap.put("/druid/**", "anon");
        hashMap.put("/swagger-ui.html", "anon");
        hashMap.put("/swagger-resources", "anon");
        hashMap.put("/swagger-resources/**", "anon");
        hashMap.put("/v2/api-docs", "anon");
        hashMap.put("/webjars/springfox-swagger-ui/**", "anon");
        hashMap.put("/webjars/**", "anon");

        hashMap.put("/static/**", "anon");
        hashMap.put("/login", "anon");
        hashMap.put("/api/TestApi/**", "anon");
        hashMap.put("/global/sessionError", "anon");
        hashMap.put("/kaptcha", "anon");

        hashMap.put("/**", "jwt");
        shiroFilter.setFilterChainDefinitionMap(hashMap);
        return shiroFilter;
    }


    /**
     * 在方法中 注入 securityManager,进行代理控制
     */
    @Bean
    public MethodInvokingFactoryBean methodInvokingFactoryBean(DefaultWebSecurityManager securityManager) {
        MethodInvokingFactoryBean bean = new MethodInvokingFactoryBean();
        bean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
        bean.setArguments(new Object[]{securityManager});
        return bean;
    }

    /**
     * Shiro生命周期处理器:
     * 用于在实现了Initializable接口的Shiro bean初始化时调用Initializable接口回调(例如:UserRealm)
     * 在实现了Destroyable接口的Shiro bean销毁时调用 Destroyable接口回调(例如:DefaultSecurityManager)
     */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * 启用shrio授权注解拦截方式，AOP式方法级权限检查
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor =
                new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

}
