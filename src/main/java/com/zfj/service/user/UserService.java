package com.zfj.service.user;

import com.zfj.pojo.User;

import javax.swing.text.StyledEditorKit;
import java.util.List;

/**
 * @Author zfj
 * @create 2019/12/22 13:15
 */
public interface UserService {

    //用户登录
    public User login(String userCode,String password);

    //根据用户id修改密码
    public boolean updatePwd(int id,String password);

    //查询记录数
    public int getUserCount(String username,int userRole);

    //根据条件查询用户列表
    public List<User> getUserList(String queryUserName,int queryUserRole,int currentPageNo,int pageSize);

    //增加用户
    public boolean add(User user);

    //删除用户
    public boolean deleteUserById(Integer delId);
}
