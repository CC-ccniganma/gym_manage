package com.milotnt.service.impl;

import com.milotnt.mapper.CommonSiteReservationMapper;
import com.milotnt.mapper.SuperSiteReservationMapper;
import com.milotnt.pojo.CommonSiteReservation;
import com.milotnt.pojo.SuperSiteReservation;
import com.milotnt.service.SiteStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class SiteStatisticsServiceImpl implements SiteStatisticsService {

    @Autowired
    private CommonSiteReservationMapper commonSiteReservationMapper;

    @Autowired
    private SuperSiteReservationMapper superSiteReservationMapper;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public Map<String, Integer> getCommonSiteStatsByDateRange(Date startDate, Date endDate) {
        List<CommonSiteReservation> reservations = commonSiteReservationMapper.findAll();
        Map<String, Integer> stats = new TreeMap<>();
        
        for (CommonSiteReservation reservation : reservations) {
            Date reservationDate = reservation.getReservationDate();
            
            if (reservationDate != null && !reservationDate.before(startDate) && !reservationDate.after(endDate)) {
                String dateStr = dateFormat.format(reservationDate);
                stats.put(dateStr, stats.getOrDefault(dateStr, 0) + 1);
            }
        }
        
        return stats;
    }

    @Override
    public Map<String, Integer> getSuperSiteStatsByDateRange(Date startDate, Date endDate) {
        List<SuperSiteReservation> reservations = superSiteReservationMapper.findAll();
        Map<String, Integer> stats = new TreeMap<>();
        
        for (SuperSiteReservation reservation : reservations) {
            Date reservationDate = reservation.getReservationDate();
            
            if (reservationDate != null && !reservationDate.before(startDate) && !reservationDate.after(endDate)) {
                String dateStr = dateFormat.format(reservationDate);
                stats.put(dateStr, stats.getOrDefault(dateStr, 0) + 1);
            }
        }
        
        return stats;
    }

    @Override
    public Map<String, Integer> getAllSiteStatsByDateRange(Date startDate, Date endDate) {
        Map<String, Integer> commonStats = getCommonSiteStatsByDateRange(startDate, endDate);
        Map<String, Integer> superStats = getSuperSiteStatsByDateRange(startDate, endDate);
        Map<String, Integer> allStats = new TreeMap<>(commonStats);
        
        // 合并两种场地的统计数据
        for (Map.Entry<String, Integer> entry : superStats.entrySet()) {
            String date = entry.getKey();
            Integer count = entry.getValue();
            allStats.put(date, allStats.getOrDefault(date, 0) + count);
        }
        
        return allStats;
    }

    @Override
    public Map<String, Integer> getSiteStatsByPeriod(Date startDate, Date endDate) {
        List<CommonSiteReservation> commonReservations = commonSiteReservationMapper.findAll();
        List<SuperSiteReservation> superReservations = superSiteReservationMapper.findAll();
        
        Map<String, Integer> periodStats = new LinkedHashMap<>();
        periodStats.put("上午 (9:00-12:00)", 0);
        periodStats.put("下午 (15:00-17:00)", 0);
        periodStats.put("晚上 (18:00-21:00)", 0);
        
        // 统计普通场地预约
        for (CommonSiteReservation reservation : commonReservations) {
            Date reservationDate = reservation.getReservationDate();
            
            if (reservationDate != null && !reservationDate.before(startDate) && !reservationDate.after(endDate)) {
                Integer period = reservation.getPeriod();
                if (period != null) {
                    String periodStr = getPeriodString(period);
                    periodStats.put(periodStr, periodStats.get(periodStr) + 1);
                }
            }
        }
        
        // 统计高级场地预约
        for (SuperSiteReservation reservation : superReservations) {
            Date reservationDate = reservation.getReservationDate();
            
            if (reservationDate != null && !reservationDate.before(startDate) && !reservationDate.after(endDate)) {
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