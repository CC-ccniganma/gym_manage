package com.milotnt.service.impl;

import com.milotnt.mapper.CoachMapper;
import com.milotnt.pojo.Coach;
import com.milotnt.service.CoachService;
import com.milotnt.util.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoachServiceImpl implements CoachService {

    @Autowired
    private CoachMapper coachMapper;

    @Override
    public List<Coach> findAll() {
        return coachMapper.findAll();
    }

    @Override
    public Boolean insertCoach(Coach coach) {
        return coachMapper.insertCoach(coach);
    }

    @Override
    public Boolean updateCoachByCoachAccount(Coach coach) {
        return coachMapper.updateCoachByCoachAccount(coach);
    }

    @Override
    public Coach coachLogin(Coach coach) {
        Coach dbCoach = coachMapper.selectByAccountAndPassword(coach);
        if (dbCoach != null && PasswordEncoder.matches(coach.getCoachPassword(), dbCoach.getCoachPassword())) {
            return dbCoach;
        }
        return null;
    }

    @Override
    public Boolean deleteByCoachAccount(Integer coachAccount) {
        return coachMapper.deleteByCoachAccount(coachAccount);
    }

    @Override
    public List<Coach> selectByCoachAccount(Integer coachAccount) {
        return coachMapper.selectByCoachAccount(coachAccount);
    }
}
