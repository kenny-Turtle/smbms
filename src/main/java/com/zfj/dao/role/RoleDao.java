package com.zfj.dao.role;

import com.zfj.pojo.Role;

import java.sql.Connection;
import java.util.List;

/**
 * @Author zfj
 * @create 2019/12/29 11:30
 */
public interface RoleDao {

    //获取角色列表
    public List<Role> getRoleList(Connection connection) throws Exception;
}
