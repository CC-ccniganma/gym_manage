package com.milotnt.service;

import com.milotnt.pojo.SignIn;

public interface SignInService {
    boolean signIn(SignIn signIn);
    boolean hasSignedIn(Integer memberAccount, java.util.Date signInDate, Integer period);
}