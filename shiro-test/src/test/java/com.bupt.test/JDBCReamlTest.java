package com.bupt.test;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

public class JDBCReamlTest {
    @Test
    public void testAuthentication(){
        //创建数据源,Druid是一个数据库连接池。
        DruidDataSource druidDataSource = new DruidDataSource();
        {
            druidDataSource.setUrl("jdbc:mysql://localhost:3306/shiro");
            druidDataSource.setUsername("root");
            druidDataSource.setPassword("root");
        }

        //创建JdbcRealm
        JdbcRealm jdbcRealm = new JdbcRealm();
        jdbcRealm.setDataSource(druidDataSource);
        jdbcRealm.setPermissionsLookupEnabled(true);//权限开关，默认情况下是关闭的，这样就不去查roles_permissions数据库了。

        //    1、构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(jdbcRealm);

        //主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject(); //获得主体

        UsernamePasswordToken token = new UsernamePasswordToken("tom", "1234");
        subject.login(token);
        System.out.println("isAuthenticated:"+ subject.isAuthenticated());
        subject.checkRoles("admin");  //角色验证
        subject.checkPermission("user:select"); //权限验证

        subject.logout();
        System.out.println("isAuthenticated:"+ subject.isAuthenticated());
    }
}
