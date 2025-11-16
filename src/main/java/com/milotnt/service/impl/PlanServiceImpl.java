package com.milotnt.service.impl;

import com.milotnt.mapper.PlanMapper;
import com.milotnt.pojo.Plan;
import com.milotnt.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanServiceImpl implements PlanService {

    @Autowired
    private PlanMapper planMapper;

    @Override
    public List<Plan> findAll() {
        return planMapper.findAll();
    }

    @Override
    public Plan findByMemberAccount(Integer memberAccount) {
        return planMapper.findByMemberAccount(memberAccount);
    }

    @Override
    public List<Plan> findByCoachAccount(Integer coachAccount) {
        return planMapper.findByCoachAccount(coachAccount);
    }

    @Override
    public Boolean insertPlan(Plan plan) {
        return planMapper.insertPlan(plan);
    }

    @Override
    public Boolean updatePlan(Plan plan) {
        return planMapper.updatePlan(plan);
    }

    @Override
    public Boolean deletePlan(Integer memberAccount) {
        return planMapper.deletePlan(memberAccount);
    }
} 