package com.zfj.dao.user;

import com.mysql.cj.protocol.Resultset;
import com.mysql.cj.util.StringUtils;
import com.zfj.dao.BaseDao;
import com.zfj.pojo.Role;
import com.zfj.pojo.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author zfj
 * @create 2019/12/22 12:58
 */
public class UserDaoImpl implements UserDao {

    //得到要登陆的用户
    public User getLoginUser(Connection connection, String userCode)
            throws Exception {
        // TODO Auto-generated method stub
        PreparedStatement pstm = null;
        ResultSet rs = null;
        User user = null;
        if(null != connection){
            String sql = "select * from smbms_user where userCode=?";
            Object[] params = {userCode};
            rs = BaseDao.execute(connection, pstm, rs, sql, params);
            if(rs.next()){
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUserCode(rs.getString("userCode"));
                user.setUserName(rs.getString("userName"));
                user.setUserPassword(rs.getString("userPassword"));
                user.setGender(rs.getInt("gender"));
                user.setBirthday(rs.getDate("birthday"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                user.setUserRole(rs.getInt("userRole"));
                user.setCreatedBy(rs.getInt("createdBy"));
                user.setCreationDate(rs.getTimestamp("creationDate"));
                user.setModifyBy(rs.getInt("modifyBy"));
                user.setModifyDate(rs.getTimestamp("modifyDate"));
            }
            BaseDao.closeResource(null, pstm, rs);
        }
        return user;
    }

    //修改密码
    public int updatePwd(Connection connection, int id, String password) throws Exception {

        PreparedStatement pstm=null;
        int execute=0;
        if(connection!=null){

            String sql="update smbms.smbms_user set userPassword = ? where id = ?";
            Object params[]={password,id};
            execute = BaseDao.execute(connection, pstm, sql, params);
            BaseDao.closeResource(null,pstm,null);

        }
        return execute;


    }

    //根据用户名或者角色查询
    public int getUserCount(Connection connection, String username, int userRole) throws Exception {

        PreparedStatement pstm=null;
        ResultSet rs=null;
        int count=0;
        if(connection!=null){
            StringBuffer sql=new StringBuffer();
            sql.append("select count(*) as count from smbms.smbms_user u,smbms.smbms_role r where u.userRole=r.id");

            ArrayList<Object> list = new ArrayList<Object>();//用来存放我们的参数

            if(!StringUtils.isNullOrEmpty(username)){
                sql.append(" and u.userName like ?");
                list.add("%"+username+"%");
            }

            if(userRole>0){
                sql.append(" and u.userRole=?");
                list.add(userRole);
            }

            //list转数组
            Object[] params = list.toArray();

            System.out.println("UserDaoImpl->getUserCount:"+sql.toString());//输出最后完整的sql语句

            rs=BaseDao.execute(connection,pstm,rs,sql.toString(),params);

            if(rs.next()){
                count=rs.getInt("count"); //从结果集中获取最终的数量
            }
            BaseDao.closeResource(null,pstm,rs);

        }

        return count;

    }

    public List<User> getUserList(Connection connection, String userName, int userRole, int currentPageNo, int pageSize) throws Exception {

        PreparedStatement pstm=null;
        ResultSet rs=null;
        ArrayList<User> users = new ArrayList<User>();
        if(connection!=null){
            StringBuffer sql = new StringBuffer();
            sql.append("select u.*,r.roleName as userRoleName from smbms_user u,smbms_role r where u.userRole=r.id");
            ArrayList<Object> list = new ArrayList<Object>();
            if(!StringUtils.isNullOrEmpty(userName)){
                sql.append(" and u.userName like ?");
                list.add("%"+userName+"%");
            }
            if(userRole>0){
                sql.append(" and u.userRole=?");
                list.add(userRole);
            }
            sql.append(" order by creationDate DESC limit ?,?");
            currentPageNo=(currentPageNo-1)*pageSize;
            list.add(currentPageNo);
            list.add(pageSize);

            Object[] params = list.toArray();
            System.out.println("sql---->"+sql.toString());
            rs = BaseDao.execute(connection, pstm, rs, sql.toString(), params);
            while(rs.next()){
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUserCode(rs.getString("userCode"));
                user.setUserName(rs.getString("userName"));
                user.setGender(rs.getInt("gender"));
                user.setBirthday(rs.getDate("birthday"));
                user.setPhone(rs.getString("phone"));
                user.setUserRole(rs.getInt("userRole"));
                user.setUserRoleName(rs.getString("userRoleName"));
                users.add(user);
            }
            BaseDao.closeResource(connection,pstm,rs);

        }
        return users;


    }

    //添加用户
    public int add(Connection connection, User user) throws Exception {
        PreparedStatement pstm=null;
        int updateRows=0;
        if(connection!=null){
            String sql= "insert into smbms_user (userCode,userName,userPassword," +
                    "userRole,gender,birthday,phone,address,creationDate,createdBy) " +
                    "values(?,?,?,?,?,?,?,?,?,?)";
            Object[] params={user.getUserCode(),user.getUserName(),user.getUserPassword(),
            user.getUserRole(),user.getGender(),user.getBirthday(),user.getPhone(),user.getAddress(),
            user.getCreationDate(),user.getCreatedBy()};
            updateRows=BaseDao.execute(connection,pstm,sql,params);
            BaseDao.closeResource(null,pstm,null);
        }
        return updateRows;
    }

    //通过id删除用户
    public int deleteUserById(Connection connection, Integer delId) throws Exception {
        PreparedStatement pstm=null;
        int flag=0;
        if(connection!=null){
            String sql="delete from smbms_user where id=?";
            Object[] params={delId};
            flag=BaseDao.execute(connection,pstm,sql,params);
            BaseDao.closeResource(null,pstm,null);
        }
        return flag;
    }

}
