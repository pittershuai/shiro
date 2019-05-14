package com.bupt.dao.impl;

import com.bupt.dao.IUserDao;
import com.bupt.vo.User;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Repository("iUserDao")
public class UserDaoImpl implements IUserDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public User getUserByUserName(final String username) {
        String sql = "select username,password from users where username=?";
        List<User> list = jdbcTemplate.query(sql, new String[]{username}, new RowMapper<User>() {
            //将结果集放到User对象中
            @Override
            public User mapRow(ResultSet resultSet, int i) throws SQLException {
                User user = new User();
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                return user;
            }
        });
        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<String> getRolesByUserName(String username) {
        String sql = "select role_name from user_roles where username = ?";
        List<String> list = jdbcTemplate.query(sql, new String[]{username}, new RowMapper<String>() {
            //将结果集放到User对象中
            @Override
            public String mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("role_name");
            }
        });
        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        return list;
    }
}
