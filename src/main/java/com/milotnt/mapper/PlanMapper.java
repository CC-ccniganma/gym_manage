package com.milotnt.mapper;

import com.milotnt.pojo.Plan;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PlanMapper {

    // 查询所有健身计划
    List<Plan> findAll();

    // 根据会员账号查询健身计划
    Plan findByMemberAccount(Integer memberAccount);

    // 根据教练账号查询健身计划
    List<Plan> findByCoachAccount(Integer coachAccount);

    // 添加健身计划
    Boolean insertPlan(Plan plan);

    // 修改健身计划
    Boolean updatePlan(Plan plan);

    // 删除健身计划
    Boolean deletePlan(Integer memberAccount);
} 