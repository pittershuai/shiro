package com.bupt.filter;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

//web.xml文件中配置的roles["admin","admin1"]，是要求同时满足两个用户权限是才可以，若想只满足一个就可以需要自定义类。
public class RolesOrFilter extends AuthorizationFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        Subject subject = getSubject(servletRequest, servletResponse);
        String[] roles = (String[])o;
        if(roles == null || roles.length == 0){
            return true;
        }
        for (String role:roles){
            if(subject.hasRole(role)){
                return true;
            }
        }
        return false;
    }
}
