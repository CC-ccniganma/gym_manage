package com.milotnt.service;

import com.milotnt.pojo.Coach;

import java.util.List;

public interface CoachService {

    //查询教练信息
    List<Coach> findAll();

    //新增教练信息
    Boolean insertCoach(Coach coach);

    //根据教练账号修改教练信息
    Boolean updateCoachByCoachAccount(Coach coach);

    //查询教练账号密码（登录）
    Coach coachLogin(Coach coach);
    //Member selectByAccountAndPassword(Member member);

    //根据教练账号删除教练信息
    Boolean deleteByCoachAccount(Integer coachAccount);


    //根据教练账号查询教练
    List<Coach> selectByCoachAccount(Integer coachAccount);

}
