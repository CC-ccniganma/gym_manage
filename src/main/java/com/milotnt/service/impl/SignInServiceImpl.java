package com.milotnt.service.impl;

import com.milotnt.mapper.SignInMapper;
import com.milotnt.pojo.SignIn;
import com.milotnt.service.SignInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SignInServiceImpl implements SignInService {

    @Autowired
    private SignInMapper signInMapper;

    @Override
    public boolean signIn(SignIn signIn) {
        return signInMapper.insertSignIn(signIn) > 0;
    }

    @Override
    public boolean hasSignedIn(Integer memberAccount, java.util.Date signInDate, Integer period) {
        return signInMapper.countSignIn(memberAccount, signInDate, period) > 0;
    }
}