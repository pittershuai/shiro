package com.bupt.shiro;

import com.bupt.dao.IUserDao;
import com.bupt.vo.User;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CustomRealm extends AuthorizingRealm {

    @Resource(name = "iUserDao")
    private IUserDao iuserDao;

    HashMap<String,String> map = new HashMap<String, String>(16);
    {
        map.put("tom","6d295738eb6579053ac46a9ca1902583");
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
        List<String> roles = iuserDao.getRolesByUserName(username);
        Set set = new HashSet(roles);
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
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(username, password, "customRealm");
        simpleAuthenticationInfo.setCredentialsSalt(ByteSource.Util.bytes(username));//或者用随机数加盐
        return simpleAuthenticationInfo;
    }

    /**
     * 模拟数据库
     * @param username
     * @return
     */
    private String getPasswordByUserName(String username) {
        User user = iuserDao.getUserByUserName(username);
        if(user==null){
            return null;
        }
        return user.getPassword();
    }

    public static void main(String[] args) {
        Md5Hash md5Hash = new Md5Hash("1234","tom"); //密码加盐
        System.out.println(md5Hash);
    }

}
