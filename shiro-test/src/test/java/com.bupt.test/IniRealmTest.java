package com.bupt.test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

public class IniRealmTest {

    @Test
    public void testAuthentication(){
        IniRealm iniRealm = new IniRealm("classpath:user.ini");
        //    1、构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(iniRealm);

        //主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject(); //获得主体

        UsernamePasswordToken token = new UsernamePasswordToken("tom", "1234");
        subject.login(token);
        System.out.println("isAuthenticated:"+ subject.isAuthenticated());
        subject.checkRoles("admin");  //角色验证
        subject.checkPermission("user:delete"); //权限验证
        subject.checkPermissions("user:delete","user:update");
//        subject.checkPermission("user:insert"); //报错Subject does not have permission [user:insert]

        subject.logout();
        System.out.println("isAuthenticated:"+ subject.isAuthenticated());

    }
}
