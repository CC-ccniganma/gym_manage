package com.milotnt.mapper;

import com.milotnt.pojo.SuperSiteReservation;
import org.apache.ibatis.annotations.*;
import java.util.Date;
import java.util.List;

@Mapper
public interface SuperSiteReservationMapper {
    //查询所有预约记录
    List<SuperSiteReservation> findAll();

    //根据用户和日期删除预约记录
    Boolean deleteByMemberAccountAndReservationDate(Integer memberAccount, Date reservationDate);

    //添加预约记录
    Boolean insertSuperSiteReservation(SuperSiteReservation superSiteReservation);

    //根据用户和日期查询预约记录
    List<SuperSiteReservation> selectByMemberAccountAndReservationDate(SuperSiteReservation superSiteReservation);

    //统计特定日期和时段的预约人数
    Integer countByDateAndPeriod(Date reservationDate, Integer period);
} 