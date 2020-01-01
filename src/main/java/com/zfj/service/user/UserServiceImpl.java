package com.zfj.service.user;

import com.sun.media.sound.UlawCodec;
import com.zfj.dao.BaseDao;
import com.zfj.dao.user.UserDao;
import com.zfj.dao.user.UserDaoImpl;
import com.zfj.pojo.User;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @Author zfj
 * @create 2019/12/22 13:16
 */
public class UserServiceImpl implements UserService {

    private UserDao userDao;

    public UserServiceImpl(){
        userDao=new UserDaoImpl();
    }

    //用户登录
    public User login(String userCode, String userPassword) {
        // TODO Auto-generated method stub
        Connection connection = null;
        User user = null;
        try {
            connection = BaseDao.getConnection();
            user = userDao.getLoginUser(connection, userCode);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            BaseDao.closeResource(connection, null, null);
        }

        //匹配密码
        if(null != user){
            if(!user.getUserPassword().equals(userPassword))
                user = null;
        }

        return user;
    }

    public boolean updatePwd(int id, String password) {

        Connection connection=null;
        boolean flag=false;
        try {
            connection = BaseDao.getConnection();
            //修改密码
            if(userDao.updatePwd(connection,id,password)>0){
                flag=true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        return flag;

    }

    public int getUserCount(String username, int userRole) {

        Connection connection=null;
        int count=0;
        try {
            connection = BaseDao.getConnection();
            count=userDao.getUserCount(connection,username,userRole);

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection,null,null);
        }

        return count;

    }

    public List<User> getUserList(String queryUserName, int queryUserRole, int currentPageNo, int pageSize) {
        Connection connection=null;
        List<User> UserList=null;
        System.out.println("queryUserName----->"+queryUserName);
        System.out.println("queryUserRole----->"+queryUserRole);
        System.out.println("currentPageNo----->"+currentPageNo);
        System.out.println("pageSize----->"+pageSize);

        try {
            connection=BaseDao.getConnection();
            UserList=userDao.getUserList(connection,queryUserName,queryUserRole,currentPageNo,pageSize);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(connection,null,null);
        }
        return UserList;

    }

    public boolean add(User user) {
        boolean flag=false;
        Connection connection=null;

        try {
            connection=BaseDao.getConnection();
            connection.setAutoCommit(false);//开启jdbc事务管理
            int updateRows = userDao.add(connection, user);
            connection.commit();
            if(updateRows>0){
                flag=true;
                System.out.println("add success!");
            }else
                System.out.println("add failed!");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(connection,null,null);
        }
        return flag;

    }

    public boolean deleteUserById(Integer delId) {
        Connection connection=null;
        boolean flag=false;

        try {
            connection=BaseDao.getConnection();
            if(userDao.deleteUserById(connection,delId)>0){
                flag=true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(connection,null,null);
        }
        return  flag;
    }

    @Test
    public void test(){

        UserServiceImpl userService = new UserServiceImpl();
        int userCount = userService.getUserCount(null,2);
        System.out.println(userCount);


    }

}
