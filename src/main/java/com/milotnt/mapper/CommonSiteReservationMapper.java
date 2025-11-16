package com.milotnt.mapper;

import com.milotnt.pojo.CommonSiteReservation;
import org.apache.ibatis.annotations.*;
import java.util.Date;

import java.util.List;


@Mapper
public interface CommonSiteReservationMapper {

    //查询所有器械
    List<CommonSiteReservation> findAll();

    //根据用户和日期删除预约记录
    Boolean deleteByMemberAccountAndReservationDate(Integer memberAccount, Date reservationDate);

    //添加预约记录
    Boolean insertCommonSiteReservation(CommonSiteReservation commonSiteReservation);

    //根据用户和日期修改预约记录
    List<CommonSiteReservation> selectByMemberAccountAndReservationDate(CommonSiteReservation commonSiteReservation);

    //统计特定日期和时段的预约人数
    Integer countByDateAndPeriod(Date reservationDate, Integer period);

    @Update("UPDATE common_site_reservation SET signed_in = 1 WHERE member_account = #{memberAccount} AND reservation_date = #{reservationDate} AND period = #{period} AND signed_in = 0")
    int signInReservation(@Param("memberAccount") Integer memberAccount, @Param("reservationDate") Date reservationDate, @Param("period") Integer period);

    List<CommonSiteReservation> selectSignInReservations(Integer memberAccount, Date today, Integer currentPeriod);

    void deleteExpiredUnCheckedReservations();

}
