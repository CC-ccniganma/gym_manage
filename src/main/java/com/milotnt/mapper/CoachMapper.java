package com.milotnt.mapper;

import com.milotnt.pojo.Coach;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CoachMapper {
    //查询教练信息
    List<Coach> findAll();

    //新增教练信息
    Boolean insertCoach(Coach coach);

    //根据教练账号修改教练信息
    Boolean updateCoachByCoachAccount(Coach coach);

    //查询教练账号密码
    Coach selectByAccountAndPassword(Coach coach);

    //根据教练账号删除教练信息
    Boolean deleteByCoachAccount(Integer coachAccount);

    //根据教练账号查询教练
    List<Coach> selectByCoachAccount(Integer coachAccount);

}
