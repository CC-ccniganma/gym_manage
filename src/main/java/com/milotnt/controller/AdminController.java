package com.milotnt.controller;

import com.milotnt.pojo.CommonSiteReservation;
import com.milotnt.pojo.CourseReservation;
import com.milotnt.pojo.SuperSiteReservation;
import com.milotnt.pojo.Commentary;
import com.milotnt.service.CommonSiteReservationService;
import com.milotnt.service.CourseReservationService;
import com.milotnt.service.CourseStatisticsService;
import com.milotnt.service.SiteStatisticsService;
import com.milotnt.service.SuperSiteReservationService;
import com.milotnt.service.CommentaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CourseReservationService courseReservationService;
    
    @Autowired
    private SiteStatisticsService siteStatisticsService;
    
    @Autowired
    private CourseStatisticsService courseStatisticsService;

    @Autowired
    private CommonSiteReservationService commonSiteReservationService;
    
    @Autowired
    private SuperSiteReservationService superSiteReservationService;

    @Autowired
    private CommentaryService commentaryService;

    /**
     * 跳转到课程预约管理页面
     */
    @GetMapping("/toCourseReservation")
    public String toCourseReservation(Model model) {
        List<CourseReservation> reservationList = courseReservationService.findAll();
        model.addAttribute("reservationList", reservationList);
        return "adminCourseReservation";
    }

    /**
     * 删除课程预约
     */
    @GetMapping("/deleteCourseReservation")
    public String deleteCourseReservation(@RequestParam Integer memberAccount,
                                        @RequestParam Integer coachAccount,
                                        @RequestParam String reservationDateStr,
                                        @RequestParam Integer period,
                                        Model model) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date reservationDate = sdf.parse(reservationDateStr);
            
            CourseReservation reservation = new CourseReservation();
            reservation.setMemberAccount(memberAccount);
            reservation.setCoachAccount(coachAccount);
            reservation.setReservationDate(reservationDate);
            reservation.setPeriod(period);
            
            if (courseReservationService.deleteReservation(reservation)) {
                model.addAttribute("success", "取消预约成功！");
            } else {
                model.addAttribute("error", "取消预约失败！");
            }
        } catch (Exception e) {
            model.addAttribute("error", "取消预约失败：" + e.getMessage());
        }
        
        return "redirect:/admin/toCourseReservation";
    }
    
    /**
     * 跳转到场地预约管理页面
     */
    @GetMapping("/toSiteReservation")
    public String toSiteReservation(Model model) {
        // 获取所有普通场地预约
        List<CommonSiteReservation> commonSiteList = commonSiteReservationService.findAll();
        
        // 获取所有高级场地预约
        List<SuperSiteReservation> superSiteList = superSiteReservationService.findAll();
        
        model.addAttribute("commonSiteList", commonSiteList);
        model.addAttribute("superSiteList", superSiteList);
        
        // 统计预约数量
        model.addAttribute("commonSiteCount", commonSiteList.size());
        model.addAttribute("superSiteCount", superSiteList.size());
        model.addAttribute("totalSiteCount", commonSiteList.size() + superSiteList.size());
        
        return "adminSiteReservation";
    }
    
    /**
     * 删除普通场地预约
     */
    @GetMapping("/deleteCommonSiteReservation")
    public String deleteCommonSiteReservation(@RequestParam Integer memberAccount,
                                             @RequestParam String reservationDateStr,
                                             Model model) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date reservationDate = sdf.parse(reservationDateStr);
            
            if (commonSiteReservationService.deleteByMemberAccountAndReservationDate(memberAccount, reservationDate)) {
                model.addAttribute("success", "取消普通场地预约成功！");
            } else {
                model.addAttribute("error", "取消普通场地预约失败！");
            }
        } catch (Exception e) {
            model.addAttribute("error", "取消普通场地预约失败：" + e.getMessage());
        }
        
        return "redirect:/admin/toSiteReservation";
    }
    
    /**
     * 删除高级场地预约
     */
    @GetMapping("/deleteSuperSiteReservation")
    public String deleteSuperSiteReservation(@RequestParam Integer memberAccount,
                                            @RequestParam String reservationDateStr,
                                            Model model) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date reservationDate = sdf.parse(reservationDateStr);
            
            if (superSiteReservationService.deleteByMemberAccountAndReservationDate(memberAccount, reservationDate)) {
                model.addAttribute("success", "取消高级场地预约成功！");
            } else {
                model.addAttribute("error", "取消高级场地预约失败！");
            }
        } catch (Exception e) {
            model.addAttribute("error", "取消高级场地预约失败：" + e.getMessage());
        }
        
        return "redirect:/admin/toSiteReservation";
    }
    
    /**
     * 跳转到场地预约统计页面（简化版）
     */
    @GetMapping("/toSiteStatistics")
    public String toSiteStatistics(
            @RequestParam(required = false) String startDateStr,
            @RequestParam(required = false) String endDateStr,
            Model model) {
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate, endDate;
        
        try {
            // 如果未指定日期范围，默认使用最近一周的日期
            if (startDateStr == null || endDateStr == null) {
                Calendar cal = Calendar.getInstance();
                endDate = cal.getTime(); // 当前日期
                
                cal.add(Calendar.DATE, -6); // 默认一周（7天）
                startDate = cal.getTime();
                
                // 更新字符串形式的日期，用于前端显示
                startDateStr = sdf.format(startDate);
                endDateStr = sdf.format(endDate);
            } else {
                startDate = sdf.parse(startDateStr);
                endDate = sdf.parse(endDateStr);
            }
            
            // 获取普通场地统计
            Map<String, Integer> commonSiteStats = siteStatisticsService.getCommonSiteStatsByDateRange(startDate, endDate);
            
            // 获取高级场地统计
            Map<String, Integer> superSiteStats = siteStatisticsService.getSuperSiteStatsByDateRange(startDate, endDate);
            
            // 获取所有场地合计统计
            Map<String, Integer> allSiteStats = siteStatisticsService.getAllSiteStatsByDateRange(startDate, endDate);
            
            // 获取时段统计
            Map<String, Integer> periodStats = siteStatisticsService.getSiteStatsByPeriod(startDate, endDate);
            
            // 添加到模型
            model.addAttribute("commonSiteStats", commonSiteStats);
            model.addAttribute("superSiteStats", superSiteStats);
            model.addAttribute("allSiteStats", allSiteStats);
            model.addAttribute("periodStats", periodStats);
            model.addAttribute("startDate", startDateStr);
            model.addAttribute("endDate", endDateStr);

            // 统计总预约数
            int commonTotal = commonSiteStats.values().stream().mapToInt(Integer::intValue).sum();
            int superTotal = superSiteStats.values().stream().mapToInt(Integer::intValue).sum();
            int allTotal = allSiteStats.values().stream().mapToInt(Integer::intValue).sum();
            
            model.addAttribute("commonTotal", commonTotal);
            model.addAttribute("superTotal", superTotal);
            model.addAttribute("allTotal", allTotal);
            
        } catch (ParseException e) {
            model.addAttribute("error", "日期格式错误：" + e.getMessage());
        }
        
        return "adminSiteStatistics";
    }
    
    /**
     * 在课程管理页面添加教练课程统计（简化版）
     */
    @GetMapping("/toCoachStatistics")
    public String toCoachStatistics(
            @RequestParam(required = false) String startDateStr,
            @RequestParam(required = false) String endDateStr,
            @RequestParam(required = false) Integer coachAccount,
            Model model) {
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate, endDate;
        
        try {
            // 如果未指定日期范围，默认使用一周范围
            if (startDateStr == null || endDateStr == null) {
                Calendar cal = Calendar.getInstance();
                endDate = cal.getTime(); // 当前日期
                
                cal.add(Calendar.DATE, -6); // 一周前
                startDate = cal.getTime();
                
                // 更新字符串形式的日期，用于前端显示
                startDateStr = sdf.format(startDate);
                endDateStr = sdf.format(endDate);
            } else {
                startDate = sdf.parse(startDateStr);
                endDate = sdf.parse(endDateStr);
            }
            
            // 获取所有教练列表
            Map<Integer, String> coachList = courseStatisticsService.getAllCoaches();
            
            // 获取教练课程数量统计
            Map<String, Integer> coachCourseStats = courseStatisticsService.getCoachCourseCountByDateRange(startDate, endDate);
            
            // 获取所有教练课程按时段统计
            Map<String, Integer> allCoachesPeriodStats = courseStatisticsService.getAllCoachesPeriodStatsByDateRange(startDate, endDate);
            
            // 如果选择了特定教练，获取该教练的时段统计
            Map<String, Integer> coachPeriodStats = null;
            if (coachAccount != null) {
                coachPeriodStats = courseStatisticsService.getCoachPeriodStatsByDateRange(coachAccount, startDate, endDate);
                model.addAttribute("selectedCoach", coachList.get(coachAccount));
                model.addAttribute("selectedCoachAccount", coachAccount);
            }
            
            // 添加到模型
            model.addAttribute("coachList", coachList);
            model.addAttribute("coachCourseStats", coachCourseStats);
            model.addAttribute("coachPeriodStats", coachPeriodStats);
            model.addAttribute("allCoachesPeriodStats", allCoachesPeriodStats);
            model.addAttribute("startDate", startDateStr);
            model.addAttribute("endDate", endDateStr);
            
            // 统计总课程数
            int totalCourses = coachCourseStats.values().stream().mapToInt(Integer::intValue).sum();
            model.addAttribute("totalCourses", totalCourses);
            
        } catch (ParseException e) {
            model.addAttribute("error", "日期格式错误：" + e.getMessage());
        }
        
        return "adminCoachStatistics";
    }

    /**
     * 跳转到评价管理页面
     */
    @GetMapping("/toCommentary")
    public String toCommentary(Model model) {
        List<Commentary> commentaryList = commentaryService.findAll();
        model.addAttribute("commentaryList", commentaryList);
        return "adminCommentary";
    }

    /**
     * 删除评价
     */
    @GetMapping("/deleteCommentary")
    public String deleteCommentary(@RequestParam Integer memberAccount,
                                 @RequestParam String commentDateStr,
                                 Model model) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date commentDate = sdf.parse(commentDateStr);
            
            if (commentaryService.deleteCommentary(memberAccount, commentDate)) {
                return "redirect:/admin/toCommentary?success=" + URLEncoder.encode("删除评价成功", "UTF-8");
            } else {
                return "redirect:/admin/toCommentary?error=" + URLEncoder.encode("删除评价失败", "UTF-8");
            }
        } catch (Exception e) {
            try {
                return "redirect:/admin/toCommentary?error=" + URLEncoder.encode("删除评价失败：" + e.getMessage(), "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                return "redirect:/admin/toCommentary?error=error";
            }
        }
    }
} 