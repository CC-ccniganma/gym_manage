package com.milotnt.service.impl;

import com.milotnt.mapper.CoachMapper;
import com.milotnt.mapper.CourseReservationMapper;
import com.milotnt.pojo.Coach;
import com.milotnt.pojo.CourseReservation;
import com.milotnt.service.CourseStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CourseStatisticsServiceImpl implements CourseStatisticsService {

    @Autowired
    private CourseReservationMapper courseReservationMapper;
    
    @Autowired
    private CoachMapper coachMapper;

    @Override
    public Map<String, Integer> getCoachCourseCountByDateRange(Date startDate, Date endDate) {
        List<CourseReservation> reservations = courseReservationMapper.findAll();
        Map<Integer, String> coachNames = getAllCoaches();
        Map<String, Integer> coachCounts = new LinkedHashMap<>();
        
        // 初始化所有教练的计数为0
        for (Map.Entry<Integer, String> entry : coachNames.entrySet()) {
            String coachInfo = entry.getKey() + " - " + entry.getValue();
            coachCounts.put(coachInfo, 0);
        }
        
        // 统计日期范围内各教练的课程数
        for (CourseReservation reservation : reservations) {
            Date reservationDate = reservation.getReservationDate();
            Integer coachAccount = reservation.getCoachAccount();
            
            if (reservationDate != null && coachAccount != null && 
                !reservationDate.before(startDate) && !reservationDate.after(endDate)) {
                String coachName = coachNames.get(coachAccount);
                if (coachName != null) {
                    String coachInfo = coachAccount + " - " + coachName;
                    coachCounts.put(coachInfo, coachCounts.getOrDefault(coachInfo, 0) + 1);
                }
            }
        }
        
        return coachCounts;
    }

    @Override
    public Map<String, Integer> getCoachPeriodStatsByDateRange(Integer coachAccount, Date startDate, Date endDate) {
        List<CourseReservation> reservations = courseReservationMapper.findAll();
        
        Map<String, Integer> periodStats = new LinkedHashMap<>();
        periodStats.put("上午 (9:00-12:00)", 0);
        periodStats.put("下午 (15:00-17:00)", 0);
        periodStats.put("晚上 (18:00-21:00)", 0);
        
        for (CourseReservation reservation : reservations) {
            Date reservationDate = reservation.getReservationDate();
            Integer reservationCoach = reservation.getCoachAccount();
            
            if (reservationDate != null && reservationCoach != null && 
                reservationCoach.equals(coachAccount) && 
                !reservationDate.before(startDate) && !reservationDate.after(endDate)) {
                Integer period = reservation.getPeriod();
                if (period != null) {
                    String periodStr = getPeriodString(period);
                    periodStats.put(periodStr, periodStats.get(periodStr) + 1);
                }
            }
        }
        
        return periodStats;
    }

    @Override
    public Map<Integer, String> getAllCoaches() {
        List<Coach> coaches = coachMapper.findAll();
        Map<Integer, String> coachMap = new HashMap<>();
        
        for (Coach coach : coaches) {
            coachMap.put(coach.getCoachAccount(), coach.getCoachName());
        }
        
        return coachMap;
    }
    
    @Override
    public Map<String, Integer> getAllCoachesPeriodStatsByDateRange(Date startDate, Date endDate) {
        List<CourseReservation> reservations = courseReservationMapper.findAll();
        
        Map<String, Integer> periodStats = new LinkedHashMap<>();
        periodStats.put("上午 (9:00-12:00)", 0);
        periodStats.put("下午 (15:00-17:00)", 0);
        periodStats.put("晚上 (18:00-21:00)", 0);
        
        for (CourseReservation reservation : reservations) {
            Date reservationDate = reservation.getReservationDate();
            
            if (reservationDate != null && 
                !reservationDate.before(startDate) && !reservationDate.after(endDate)) {
                Integer period = reservation.getPeriod();
                if (period != null) {
                    String periodStr = getPeriodString(period);
                    periodStats.put(periodStr, periodStats.get(periodStr) + 1);
                }
            }
        }
        
        return periodStats;
    }
    
    /**
     * 将时段编号转换为可读的时段字符串
     */
    private String getPeriodString(Integer period) {
        switch (period) {
            case 1:
                return "上午 (9:00-12:00)";
            case 2:
                return "下午 (15:00-17:00)";
            case 3:
                return "晚上 (18:00-21:00)";
            default:
                return "未知时段";
        }
    }
} 