package com.bupt.controller;

import com.bupt.vo.User;
import jdk.nashorn.internal.ir.RuntimeNode;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {
    //produces = "application/json;charset=utf-8"处理页面中文乱码
    @RequestMapping(value = "/subLogin",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String subLogin(User user){
        //获得主体
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
        try{
            subject.login(token);
        }catch (Exception e){
            return e.getMessage();
        }
        if (subject.hasRole("admin")){
            return "是admin角色";
        }
        return "是admin角色";
    }

    //只有admin角色才能访问，这里应该用到Bean作用域为session的概念。
    @RequiresRoles("admin")
    @RequestMapping(value = "/testRole.do",method = RequestMethod.GET)
    @ResponseBody
    public String testRole(){
        return "testRole success";
    }

    @RequiresRoles("admin1")
    @RequestMapping(value = "/testRole1.do",method = RequestMethod.GET)
    @ResponseBody
    public String testRole1(){
        return "testRole1 success";
    }

    //由于没有编写从数据库读取权限数据的代码，所以此处不再演示。（实现与角色完全相同）
    @RequiresPermissions("xx")
    @RequestMapping(value = "/testPermission.do",method = RequestMethod.GET)
    @ResponseBody
    public String testPermission(){
        return "testPermission success";
    }
}
