package com.pvplatform.controller;

import com.pvplatform.common.Result;
import com.pvplatform.entity.SysUser;
import com.pvplatform.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @GetMapping
    public Result<List<SysUser>> list() {
        return sysUserService.listUsers();
    }

    @GetMapping("/{id}")
    public Result<SysUser> getById(@PathVariable Long id) {
        return sysUserService.getUserById(id);
    }

    @PostMapping
    public Result<SysUser> save(@RequestBody SysUser sysUser) {
        return sysUserService.saveUser(sysUser);
    }

    @PutMapping
    public Result<SysUser> update(@RequestBody SysUser sysUser) {
        return sysUserService.updateUser(sysUser);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        return sysUserService.deleteUser(id);
    }
}
