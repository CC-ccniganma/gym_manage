package com.milotnt.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 课程统计服务接口
 */
public interface CourseStatisticsService {
    
    /**
     * 获取指定日期范围内每位教练的课程数量
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 教练（账号+姓名）和课程数量的映射
     */
    Map<String, Integer> getCoachCourseCountByDateRange(Date startDate, Date endDate);
    
    /**
     * 获取指定日期范围内特定教练的时段分布
     * @param coachAccount 教练账号
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 时段和数量的映射
     */
    Map<String, Integer> getCoachPeriodStatsByDateRange(Integer coachAccount, Date startDate, Date endDate);
    
    /**
     * 获取所有教练列表（账号和姓名）
     * @return 教练账号和姓名的映射
     */
    Map<Integer, String> getAllCoaches();
    
    /**
     * 获取指定日期范围内所有教练的课程按时段统计
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 时段和数量的映射
     */
    Map<String, Integer> getAllCoachesPeriodStatsByDateRange(Date startDate, Date endDate);
} 