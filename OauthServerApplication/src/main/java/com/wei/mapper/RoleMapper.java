package com.wei.mapper;

import com.wei.entity.SysRole;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface RoleMapper {
    @Select("select r.id,r.role_name roleName ,r.role_desc roleDesc " +
            "FROM sys_role r,sys_user_role ur " +
            "WHERE r.id=ur.rid AND ur.uid=#{uid}")
    public List<SysRole> findByUid(Integer uid);
}