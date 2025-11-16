package com.milotnt.service.impl;

import com.milotnt.mapper.SuperSiteReservationMapper;
import com.milotnt.pojo.SuperSiteReservation;
import com.milotnt.service.SuperSiteReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SuperSiteReservationServiceImpl implements SuperSiteReservationService {

    @Autowired
    private SuperSiteReservationMapper superSiteReservationMapper;

    @Override
    public List<SuperSiteReservation> findAll() {
        return superSiteReservationMapper.findAll();
    }

    @Override
    public Boolean insertSuperSiteReservation(SuperSiteReservation reservation) {
        return superSiteReservationMapper.insertSuperSiteReservation(reservation);
    }

    @Override
    public Boolean deleteByMemberAccountAndReservationDate(Integer memberAccount, Date reservationDate) {
        return superSiteReservationMapper.deleteByMemberAccountAndReservationDate(memberAccount, reservationDate);
    }

    @Override
    public List<SuperSiteReservation> selectByMemberAccountAndReservationDate(SuperSiteReservation reservation) {
        return superSiteReservationMapper.selectByMemberAccountAndReservationDate(reservation);
    }

    @Override
    public Integer countByDateAndPeriod(Date reservationDate, Integer period) {
        return superSiteReservationMapper.countByDateAndPeriod(reservationDate, period);
    }
} 