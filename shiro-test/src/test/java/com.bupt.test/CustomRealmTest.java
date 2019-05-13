package com.bupt.test;

import com.bupt.shiro.CustomRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

public class CustomRealmTest {
    @Test
    public void testAuthentication(){
        CustomRealm customRealm = new CustomRealm();
        //    1、构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(customRealm);

        //shiro加密
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        matcher.setHashAlgorithmName("md5");//设置加密算法
        matcher.setHashIterations(1); //设置加密次数
        customRealm.setCredentialsMatcher(matcher);

        //主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject(); //获得主体

        UsernamePasswordToken token = new UsernamePasswordToken("tom", "123");
        subject.login(token);
        System.out.println("isAuthenticated:"+ subject.isAuthenticated());
//        subject.checkRoles("admin");  //角色验证
//        subject.checkPermission("user:delete"); //权限验证
//        subject.checkPermissions("user:delete","user:add");

        subject.logout();
        System.out.println("isAuthenticated:"+ subject.isAuthenticated());

    }
}
