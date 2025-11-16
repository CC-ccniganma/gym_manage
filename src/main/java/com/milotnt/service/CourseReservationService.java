package com.milotnt.service;

import com.milotnt.pojo.CourseReservation;

import java.util.Date;
import java.util.List;
import com.milotnt.pojo.CommonSiteReservation;

public interface CourseReservationService {
    
    List<CourseReservation> findAll();
    
    List<CourseReservation> findByMemberAccount(Integer memberAccount);
    
    List<CourseReservation> selectByCoachAccount(Integer coachAccount);
    
    boolean isReservationExists(Integer memberAccount,
                              Integer coachAccount,
                              Date reservationDate,
                              Integer period);
    
    boolean addReservation(CourseReservation reservation);
    
    boolean deleteReservation(CourseReservation reservation);
    
    List<CourseReservation> findByCoachAndDate(Integer coachAccount, Date reservationDate);
} 