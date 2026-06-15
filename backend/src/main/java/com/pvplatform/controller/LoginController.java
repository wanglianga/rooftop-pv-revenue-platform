package com.pvplatform.controller;

import com.pvplatform.common.Result;
import com.pvplatform.entity.SysUser;
import com.pvplatform.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public Result<SysUser> login(@RequestParam String username, @RequestParam String password) {
        return loginService.login(username, password);
    }

    @PostMapping("/logout")
    public Result<Void> logout() {
        return loginService.logout();
    }
}
