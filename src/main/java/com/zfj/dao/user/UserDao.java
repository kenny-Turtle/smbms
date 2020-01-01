package com.zfj.dao.user;

import com.zfj.pojo.Role;
import com.zfj.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @Author zfj
 * @create 2019/12/22 12:57
 */
public interface UserDao {

    //得到要登录的用户
    public User getLoginUser(Connection connection,String userCode) throws  Exception;

    //修改密码
    public int updatePwd(Connection connection,int id,String password) throws Exception;

    //查询用户总数
    public int getUserCount(Connection connection,String username,int userRole) throws Exception;

    //通过条件获取用户列表-userList
    public List<User> getUserList(Connection connection,String userName,int userRole,int currentPageNo,int pageSize) throws Exception;

    //添加用户
    public int add(Connection connection,User user) throws  Exception;

    //删除用户 通过id
    public int deleteUserById(Connection connection,Integer delId) throws Exception;
}
