package com.pvplatform.service.impl;

import com.pvplatform.common.Result;
import com.pvplatform.entity.SysUser;
import com.pvplatform.repository.SysUserRepository;
import com.pvplatform.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class LoginServiceImpl implements LoginService {

    private SysUserRepository sysUserRepository;

    @Autowired
    public void setSysUserRepository(SysUserRepository sysUserRepository) {
        this.sysUserRepository = sysUserRepository;
    }

    public SysUserRepository getSysUserRepository() {
        return sysUserRepository;
    }

    @Override
    public Result<SysUser> login(String username, String password) {
        SysUser user = sysUserRepository.findByUsername(username);
        if (user == null) {
            return Result.error("用户不存在");
        }
        if (!password.equals(user.getPassword())) {
            return Result.error("密码错误");
        }
        if (user.getStatus() != null && user.getStatus() == 0) {
            return Result.error("用户已被禁用");
        }
        return Result.success("登录成功", user);
    }

    @Override
    public Result<Void> logout() {
        return Result.success("退出成功", null);
    }
}
