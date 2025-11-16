package com.milotnt.service;

import com.milotnt.pojo.CommonSiteReservation;
import java.util.Date;
import java.util.List;

public interface CommonSiteReservationService {

    // 查询所有预约记录
    List<CommonSiteReservation> findAll();

    // 添加预约记录
    Boolean insertCommonSiteReservation(CommonSiteReservation reservation);

    // 根据会员账号和日期删除预约
    Boolean deleteByMemberAccountAndReservationDate(Integer memberAccount, Date reservationDate);

    // 根据会员账号和日期查询预约
    List<CommonSiteReservation> selectByMemberAccountAndReservationDate(CommonSiteReservation reservation);

    List<CommonSiteReservation> findSignInReservations(Integer memberAccount, Date today, Integer currentPeriod);

    // 统计特定日期和时段的预约人数
    Integer countByDateAndPeriod(Date reservationDate, Integer period);

    Boolean signInReservation(Integer memberAccount, Date reservationDate, Integer period);

    // 定时删除未签到且超时的预约
    void deleteExpiredUnCheckedReservations();

}