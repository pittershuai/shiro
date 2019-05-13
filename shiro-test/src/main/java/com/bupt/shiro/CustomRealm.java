package com.bupt.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class CustomRealm extends AuthorizingRealm {
    HashMap<String,String> map = new HashMap<String, String>(16);
    {
        map.put("tom","123");
        super.setName("customRealm");
    }
    /**
     * 做授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //从主体传过来的认证信息中，获得用户名
        String username = (String) principalCollection.getPrimaryPrincipal();
        //从数据库或缓存中获取角色
        Set<String> roles = getRolesByUserName(username);
        //从数据库或缓存中获取权限
        Set<String> permissions = getPermissionsByRoles(username);
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setRoles(roles);
        simpleAuthorizationInfo.setStringPermissions(permissions);
        return simpleAuthorizationInfo;
    }

    private Set<String> getPermissionsByRoles(String username) {
        Set set = new HashSet();
        set.add("user:delete");
        set.add("user:add");
        return set;
    }

    private Set<String> getRolesByUserName(String username) {
        Set set = new HashSet();
        set.add("admin");
        set.add("user");
        return set;
    }

    /**
     * 做认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //1.从主体传过来的认证信息中，获得用户名
        String username = (String) authenticationToken.getPrincipal();

        //2.通过用户名得到数据库中密码
        String password = getPasswordByUserName(username);
        if(password == null){
            return null;
        }
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo("tom", password, "customRealm");
        return simpleAuthenticationInfo;
    }

    /**
     * 模拟数据库
     * @param username
     * @return
     */
    private String getPasswordByUserName(String username) {
        return map.get(username);
    }


}
