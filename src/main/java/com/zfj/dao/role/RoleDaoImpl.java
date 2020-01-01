package com.zfj.dao.role;

import com.zfj.dao.BaseDao;
import com.zfj.pojo.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author zfj
 * @create 2019/12/29 11:31
 */
public class RoleDaoImpl implements RoleDao {
    public List<Role> getRoleList(Connection connection) throws Exception {

        PreparedStatement pstm=null;
        ResultSet rs=null;
        ArrayList<Role> roles = new ArrayList<Role>();
        if(connection!=null){
            String sql="select * from smbms_role";
            Object[] params={};
            rs= BaseDao.execute(connection,pstm,rs,sql,params);

            while(rs.next()){
                Role role = new Role();
                role.setId(rs.getInt("id"));
                role.setRoleCode(rs.getString("roleCode"));
                role.setRoleName(rs.getString("roleName"));
                roles.add(role);
            }
            BaseDao.closeResource(null,pstm,rs);
        }
        return roles;
    }
}
