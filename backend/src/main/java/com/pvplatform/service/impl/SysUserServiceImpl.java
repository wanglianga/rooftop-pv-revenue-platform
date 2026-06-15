package com.pvplatform.service.impl;

import com.pvplatform.common.Result;
import com.pvplatform.entity.SysUser;
import com.pvplatform.repository.SysUserRepository;
import com.pvplatform.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserRepository sysUserRepository;

    @Override
    public Result<List<SysUser>> listUsers() {
        return Result.success(sysUserRepository.findAll());
    }

    @Override
    public Result<SysUser> getUserById(Long id) {
        Optional<SysUser> user = sysUserRepository.findById(id);
        return user.map(Result::success).orElseGet(() -> Result.error("用户不存在"));
    }

    @Override
    public Result<SysUser> saveUser(SysUser sysUser) {
        return Result.success(sysUserRepository.save(sysUser));
    }

    @Override
    public Result<SysUser> updateUser(SysUser sysUser) {
        if (sysUser.getId() == null || !sysUserRepository.existsById(sysUser.getId())) {
            return Result.error("用户不存在");
        }
        return Result.success(sysUserRepository.save(sysUser));
    }

    @Override
    public Result<Void> deleteUser(Long id) {
        if (!sysUserRepository.existsById(id)) {
            return Result.error("用户不存在");
        }
        sysUserRepository.deleteById(id);
        return Result.success();
    }
}
