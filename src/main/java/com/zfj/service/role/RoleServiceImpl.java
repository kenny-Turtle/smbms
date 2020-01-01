package com.zfj.service.role;

import com.zfj.dao.BaseDao;
import com.zfj.dao.role.RoleDao;
import com.zfj.dao.role.RoleDaoImpl;
import com.zfj.pojo.Role;
import org.junit.Test;

import javax.management.relation.RoleList;
import java.sql.Connection;
import java.util.List;

/**
 * @Author zfj
 * @create 2019/12/29 11:34
 */
public class RoleServiceImpl implements RoleService {

    private RoleDao roleDao;

    public RoleServiceImpl(){
        roleDao=new RoleDaoImpl();
    }

    public List<Role> getRoleList() {

        Connection connection=null;
        List<Role> roleList=null;
        try {
            connection=BaseDao.getConnection();
            roleList = roleDao.getRoleList(connection);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(connection,null,null);
        }

        return roleList;

    }

    @Test
    public void test(){
        RoleServiceImpl roleService = new RoleServiceImpl();
        List<Role> roleList = roleService.getRoleList();
        for (Role role : roleList) {
            System.out.println(role.getRoleName());

        }
    }
}
