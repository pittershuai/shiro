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

    @RequestMapping(value = "/testRole.do",method = RequestMethod.GET)
    @ResponseBody
    public String testRole(){
        System.out.println("testRole success");
        return "testRole success";
    }

    @RequestMapping(value = "/testRole1.do",method = RequestMethod.GET)
    @ResponseBody
    public String testRole1(){
        return "testRole1 success";
    }

    @RequestMapping(value = "/testRole2.do",method = RequestMethod.GET)
    @ResponseBody
    public String testRole2(){
        return "testRole2 success";
    }

    @RequestMapping(value = "/testPerms.do",method = RequestMethod.GET)
    @ResponseBody
    public String testPerms(){
        return "testPerms success";
    }

    @RequestMapping(value = "/testPerms1.do",method = RequestMethod.GET)
    @ResponseBody
    public String testPerms1(){
        return "testPerms1 success";
    }


}
