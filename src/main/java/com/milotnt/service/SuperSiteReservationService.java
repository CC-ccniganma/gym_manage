package com.milotnt.service;

import com.milotnt.pojo.SuperSiteReservation;
import java.util.Date;
import java.util.List;

public interface SuperSiteReservationService {
    // 查询所有预约记录
    List<SuperSiteReservation> findAll();

    // 添加预约记录
    Boolean insertSuperSiteReservation(SuperSiteReservation reservation);

    // 根据会员账号和日期删除预约
    Boolean deleteByMemberAccountAndReservationDate(Integer memberAccount, Date reservationDate);

    // 根据会员账号和日期查询预约
    List<SuperSiteReservation> selectByMemberAccountAndReservationDate(SuperSiteReservation reservation);

    // 统计特定日期和时段的预约人数
    Integer countByDateAndPeriod(Date reservationDate, Integer period);
} 