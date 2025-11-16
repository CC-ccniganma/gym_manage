package com.milotnt.service.impl;

import com.milotnt.mapper.CommonSiteReservationMapper;
import com.milotnt.pojo.CommonSiteReservation;
import com.milotnt.service.CommonSiteReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CommonSiteReservationServiceImpl implements CommonSiteReservationService {

    @Autowired
    private CommonSiteReservationMapper commonSiteReservationMapper;

    @Override
    public List<CommonSiteReservation> findAll() {
        return commonSiteReservationMapper.findAll();
    }

    @Override
    public Boolean insertCommonSiteReservation(CommonSiteReservation reservation) {
        Integer count = commonSiteReservationMapper.countByDateAndPeriod(reservation.getReservationDate(), reservation.getPeriod());
        if (count != null && count >= 2) {
            return false; // 已满10人，不能预约
        }
        return commonSiteReservationMapper.insertCommonSiteReservation(reservation);
    }

    @Override
    public List<CommonSiteReservation> findSignInReservations(Integer memberAccount, Date today, Integer currentPeriod) {
        return commonSiteReservationMapper.selectSignInReservations(memberAccount, today, currentPeriod);
    }

    @Override
    public Boolean deleteByMemberAccountAndReservationDate(Integer memberAccount, Date reservationDate) {
        return commonSiteReservationMapper.deleteByMemberAccountAndReservationDate(memberAccount, reservationDate);
    }

    @Override
    public Boolean signInReservation(Integer memberAccount, Date reservationDate, Integer period) {
        return commonSiteReservationMapper.signInReservation(memberAccount, reservationDate, period) > 0;
    }

    @Override
    public List<CommonSiteReservation> selectByMemberAccountAndReservationDate(CommonSiteReservation reservation) {
        return commonSiteReservationMapper.selectByMemberAccountAndReservationDate(reservation);
    }

    @Override
    public Integer countByDateAndPeriod(Date reservationDate, Integer period) {
        return commonSiteReservationMapper.countByDateAndPeriod(reservationDate, period);
    }

    @Override
    public void deleteExpiredUnCheckedReservations() {
        commonSiteReservationMapper.deleteExpiredUnCheckedReservations();
    }

}