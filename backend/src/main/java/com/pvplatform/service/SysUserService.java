package com.pvplatform.service;

import com.pvplatform.common.Result;
import com.pvplatform.entity.SysUser;

import java.util.List;

public interface SysUserService {
    Result<List<SysUser>> listUsers();
    Result<SysUser> getUserById(Long id);
    Result<SysUser> saveUser(SysUser sysUser);
    Result<SysUser> updateUser(SysUser sysUser);
    Result<Void> deleteUser(Long id);
}
