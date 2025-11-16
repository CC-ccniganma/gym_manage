package com.milotnt.service.impl;

import com.milotnt.mapper.AdminMapper;
import com.milotnt.pojo.Admin;
import com.milotnt.service.AdminService;
import com.milotnt.util.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author MiloTnT [milotntspace@gmail.com]
 * @date 2021/8/11
 */

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public Admin adminLogin(Admin admin) {
        Admin dbAdmin = adminMapper.selectByAccountAndPassword(admin);
        if (dbAdmin != null && PasswordEncoder.matches(admin.getAdminPassword(), dbAdmin.getAdminPassword())) {
            return dbAdmin;
        }
        return null;
    }
}
