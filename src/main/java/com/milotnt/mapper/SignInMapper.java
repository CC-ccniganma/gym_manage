package com.milotnt.mapper;

import com.milotnt.pojo.SignIn;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SignInMapper {
    int insertSignIn(SignIn signIn);
    int countSignIn(@Param("memberAccount") Integer memberAccount,
                    @Param("signInDate") java.util.Date signInDate,
                    @Param("period") Integer period);
}