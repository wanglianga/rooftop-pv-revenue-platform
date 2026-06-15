package com.pvplatform.service;

import com.pvplatform.common.Result;
import com.pvplatform.entity.SysUser;

public interface LoginService {
    Result<SysUser> login(String username, String password);
    Result<Void> logout();
}
