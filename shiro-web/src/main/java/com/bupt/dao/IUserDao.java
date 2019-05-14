package com.bupt.dao;

import com.bupt.vo.User;

import java.util.List;
import java.util.Set;

public interface IUserDao {
    User getUserByUserName(String username);
    List<String> getRolesByUserName(String username);

}
