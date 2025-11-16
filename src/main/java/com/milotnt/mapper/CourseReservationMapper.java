package com.milotnt.mapper;

import com.milotnt.pojo.CourseReservation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import com.milotnt.pojo.CommonSiteReservation;
@Mapper
public interface CourseReservationMapper {
    
    List<CourseReservation> findAll();
    
    List<CourseReservation> selectByMemberAccount(Integer memberAccount);
    
    List<CourseReservation> selectByCoachAccount(Integer coachAccount);
    
    boolean isReservationExists(@Param("memberAccount") Integer memberAccount,
                              @Param("coachAccount") Integer coachAccount,
                              @Param("reservationDate") Date reservationDate,
                              @Param("period") Integer period);
    
    boolean insertReservation(CourseReservation reservation);
    
    boolean deleteReservation(CourseReservation reservation);
    
    List<CourseReservation> selectByCoachAndDate(@Param("coachAccount") Integer coachAccount,
                                                @Param("reservationDate") Date reservationDate);

    // 查询可签到的普通预约
    List<CommonSiteReservation> selectSignInReservations(
            @Param("memberAccount") Integer memberAccount,
            @Param("today") Date today,
            @Param("currentPeriod") Integer currentPeriod
    );

    Integer checkReservationExists(@Param("memberAccount") Integer memberAccount,
                                 @Param("coachAccount") Integer coachAccount,
                                 @Param("reservationDate") Date reservationDate,
                                 @Param("period") Integer period);
    
    List<CourseReservation> findByMemberAccount(Integer memberAccount);
    
    boolean addReservation(CourseReservation reservation);
} 