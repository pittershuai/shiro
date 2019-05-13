package com.bupt.test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;

public class AuthenticationTest {
    //使用简单的realm
    SimpleAccountRealm simpleAccountRealm = new SimpleAccountRealm();

    @Before
    public void addUser(){
        simpleAccountRealm.addAccount("tom","123","admin");
        simpleAccountRealm.addAccount("tony","123","admin","user");
    }

    @Test
    public void testAuthentication(){
        //    1、构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(simpleAccountRealm);

        //主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject(); //获得主体

        UsernamePasswordToken token = new UsernamePasswordToken("tom", "123");
        subject.login(token);
        System.out.println("isAuthenticated:"+ subject.isAuthenticated());

        subject.checkRole("admin");//角色验证
//        subject.checkRole("admin1"); //会报错Subject does not have role [admin1]

        subject.logout();
        System.out.println("isAuthenticated:"+ subject.isAuthenticated());

        //--------------------------
        UsernamePasswordToken tony = new UsernamePasswordToken("tony", "123");
        subject.login(tony);
        subject.checkRoles("admin","user"); //多个角色验证
    }
}
