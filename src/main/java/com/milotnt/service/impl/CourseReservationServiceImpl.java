package com.milotnt.service.impl;

import com.milotnt.mapper.CourseReservationMapper;
import com.milotnt.pojo.CourseReservation;
import com.milotnt.service.CourseReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CourseReservationServiceImpl implements CourseReservationService {

    @Autowired
    private CourseReservationMapper courseReservationMapper;

    @Override
    public List<CourseReservation> findAll() {
        return courseReservationMapper.findAll();
    }

    @Override
    public List<CourseReservation> findByMemberAccount(Integer memberAccount) {
        return courseReservationMapper.findByMemberAccount(memberAccount);
    }

    @Override
    public List<CourseReservation> selectByCoachAccount(Integer coachAccount) {
        return courseReservationMapper.selectByCoachAccount(coachAccount);
    }

    @Override
    public boolean isReservationExists(Integer memberAccount,
                                     Integer coachAccount,
                                     Date reservationDate,
                                     Integer period) {
        return courseReservationMapper.isReservationExists(memberAccount, coachAccount, reservationDate, period);
    }

    @Override
    public boolean addReservation(CourseReservation reservation) {
        return courseReservationMapper.addReservation(reservation);
    }

    @Override
    public boolean deleteReservation(CourseReservation reservation) {
        return courseReservationMapper.deleteReservation(reservation);
    }

    @Override
    public List<CourseReservation> findByCoachAndDate(Integer coachAccount, Date reservationDate) {
        return courseReservationMapper.selectByCoachAndDate(coachAccount, reservationDate);
    }
}