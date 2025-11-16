package com.milotnt.service;

import java.util.Date;
import java.util.Map;
import java.util.List;

/**
 * 场地预约统计服务接口
 */
public interface SiteStatisticsService {
    
    /**
     * 获取指定日期范围内的普通场地预约统计（按日期统计）
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 日期和预约人次的映射
     */
    Map<String, Integer> getCommonSiteStatsByDateRange(Date startDate, Date endDate);
    
    /**
     * 获取指定日期范围内的高级场地预约统计（按日期统计）
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 日期和预约人次的映射
     */
    Map<String, Integer> getSuperSiteStatsByDateRange(Date startDate, Date endDate);
    
    /**
     * 获取指定日期范围内的所有场地预约统计（按日期统计）
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 日期和预约人次的映射
     */
    Map<String, Integer> getAllSiteStatsByDateRange(Date startDate, Date endDate);
    
    /**
     * 获取指定日期范围内的场地预约按时段统计
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 时段和预约人次的映射
     */
    Map<String, Integer> getSiteStatsByPeriod(Date startDate, Date endDate);
} 