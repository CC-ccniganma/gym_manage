package com.milotnt.controller;

import com.milotnt.pojo.CourseReservation;
import com.milotnt.pojo.Plan;
import com.milotnt.pojo.Coach;
import com.milotnt.service.CourseReservationService;
import com.milotnt.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import com.milotnt.service.CoachService;

@Controller
@RequestMapping("/coach")
public class CoachController {

    @Autowired
    private CourseReservationService courseReservationService;
    
    @Autowired
    private PlanService planService;

    @Autowired
    private CoachService coachService;

@GetMapping("/toUpdateCoachInfo")
public String toUpdateCoachInfo(HttpSession session, Model model) {
    Coach coach = (Coach) session.getAttribute("coach");
    if (coach == null) {
        return "redirect:/toLogin";
    }
    model.addAttribute("coach", coach);
    return "updateCoachInfo";
}

@PostMapping("/updateCoachInfo")
public String updateCoachInfo(@ModelAttribute Coach coachForm, HttpSession session, Model model) {
    Coach coach = (Coach) session.getAttribute("coach");
    if (coach == null) {
        return "redirect:/toLogin";
    }
    // 假设有 coachService 并实现了 updateCoach 方法
    coachForm.setCoachAccount(coach.getCoachAccount());
    boolean updated = coachService.updateCoachByCoachAccount(coachForm);
    if (updated) {
        session.setAttribute("coach", coachForm);
        model.addAttribute("success", "信息更新成功！");
    } else {
        model.addAttribute("error", "信息更新失败！");
    }
    return "updateCoachInfo";
}
    @GetMapping("/toCourseReservation")
    public String toCourseReservation(HttpSession session, Model model) {
        Coach coach = (Coach) session.getAttribute("coach");
        if (coach == null) {
            return "redirect:/toLogin";
        }
        
        List<CourseReservation> reservationList = courseReservationService.selectByCoachAccount(coach.getCoachAccount());
        model.addAttribute("reservationList", reservationList);
        return "coachCourseReservation";
    }

    @GetMapping("/deleteCourseReservation")
    public String deleteCourseReservation(@RequestParam Integer memberAccount,
                                        @RequestParam Integer coachAccount,
                                        @RequestParam String reservationDateStr,
                                        @RequestParam Integer period,
                                        HttpSession session,
                                        Model model) {
        Coach coach = (Coach) session.getAttribute("coach");
        if (coach == null || !coach.getCoachAccount().equals(coachAccount)) {
            model.addAttribute("error", "您没有权限取消此预约！");
            return "redirect:/coach/toCourseReservation";
        }

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
        
        return "redirect:/coach/toCourseReservation";
    }
    
    /**
     * 跳转到教练健身计划页面
     */
    @GetMapping("/toPlan")
    public String toPlan(HttpSession session, Model model) {
        Coach coach = (Coach) session.getAttribute("coach");
        if (coach == null) {
            return "redirect:/toLogin";
        }
        
        // 获取当前教练创建的所有健身计划
        List<Plan> planList = planService.findByCoachAccount(coach.getCoachAccount());
        model.addAttribute("planList", planList);
        
        return "coachPlan";
    }
    
    /**
     * 跳转到添加健身计划页面
     */
    @GetMapping("/toAddPlan")
    public String toAddPlan(HttpSession session) {
        Coach coach = (Coach) session.getAttribute("coach");
        if (coach == null) {
            return "redirect:/toLogin";
        }
        return "coachAddPlan";
    }
    
    /**
     * 添加健身计划
     */
    @PostMapping("/addPlan")
    public String addPlan(@RequestParam Integer memberAccount,
                          @RequestParam String message,
                          HttpSession session,
                          Model model) {
        Coach coach = (Coach) session.getAttribute("coach");
        if (coach == null) {
            return "redirect:/toLogin";
        }
        
        Plan plan = new Plan(memberAccount, coach.getCoachAccount(), message);
        
        if (planService.insertPlan(plan)) {
            model.addAttribute("success", "添加健身计划成功！");
        } else {
            model.addAttribute("error", "添加健身计划失败！");
        }
        
        return "redirect:/coach/toPlan";
    }
    
    /**
     * 删除健身计划
     */
    @GetMapping("/deletePlan/{memberAccount}")
    public String deletePlan(@PathVariable Integer memberAccount,
                             HttpSession session,
                             Model model) {
        Coach coach = (Coach) session.getAttribute("coach");
        if (coach == null) {
            return "redirect:/toLogin";
        }
        
        // 确认该计划是否由当前教练创建
        Plan existingPlan = planService.findByMemberAccount(memberAccount);
        if (existingPlan == null || !existingPlan.getCoachAccount().equals(coach.getCoachAccount())) {
            model.addAttribute("error", "您没有权限删除此健身计划！");
            return "redirect:/coach/toPlan";
        }
        
        if (planService.deletePlan(memberAccount)) {
            model.addAttribute("success", "删除健身计划成功！");
        } else {
            model.addAttribute("error", "删除健身计划失败！");
        }
        
        return "redirect:/coach/toPlan";
    }
    
    /**
     * 跳转到编辑健身计划页面
     */
    @GetMapping("/toEditPlan/{memberAccount}")
    public String toEditPlan(@PathVariable Integer memberAccount,
                            HttpSession session,
                            Model model) {
        Coach coach = (Coach) session.getAttribute("coach");
        if (coach == null) {
            return "redirect:/toLogin";
        }
        
        // 确认该计划是否由当前教练创建
        Plan existingPlan = planService.findByMemberAccount(memberAccount);
        if (existingPlan == null || !existingPlan.getCoachAccount().equals(coach.getCoachAccount())) {
            model.addAttribute("error", "您没有权限编辑此健身计划！");
            return "redirect:/coach/toPlan";
        }
        
        model.addAttribute("plan", existingPlan);
        return "coachEditPlan";
    }
    
    /**
     * 更新健身计划
     */
    @PostMapping("/updatePlan")
    public String updatePlan(@RequestParam Integer memberAccount,
                            @RequestParam String message,
                            HttpSession session,
                            Model model) {
        Coach coach = (Coach) session.getAttribute("coach");
        if (coach == null) {
            return "redirect:/toLogin";
        }
        
        // 确认该计划是否由当前教练创建
        Plan existingPlan = planService.findByMemberAccount(memberAccount);
        if (existingPlan == null || !existingPlan.getCoachAccount().equals(coach.getCoachAccount())) {
            model.addAttribute("error", "您没有权限编辑此健身计划！");
            return "redirect:/coach/toPlan";
        }
        
        Plan plan = new Plan(memberAccount, coach.getCoachAccount(), message);
        
        if (planService.updatePlan(plan)) {
            model.addAttribute("success", "更新健身计划成功！");
        } else {
            model.addAttribute("error", "更新健身计划失败！");
        }
        
        return "redirect:/coach/toPlan";
    }
} 