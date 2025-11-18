package com.milotnt.controller;

import com.milotnt.pojo.CourseReservation;
import com.milotnt.pojo.Member;
import com.milotnt.pojo.Coach;
import com.milotnt.pojo.Plan;
import com.milotnt.service.CourseReservationService;
import com.milotnt.service.CommonSiteReservationService;
import com.milotnt.service.SuperSiteReservationService;
import com.milotnt.service.PlanService;
import com.milotnt.pojo.CommonSiteReservation;
import com.milotnt.pojo.SuperSiteReservation;
import com.milotnt.service.MemberService;
import com.milotnt.service.CoachService;
import com.milotnt.util.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import java.text.SimpleDateFormat;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Date;
import com.milotnt.pojo.Commentary;
import com.milotnt.service.CommentaryService;
import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private CoachService coachService;

    @Autowired
    private CourseReservationService courseReservationService;

    @Autowired
    private CommonSiteReservationService commonSiteReservationService;

    @Autowired
    private SuperSiteReservationService superSiteReservationService;

    @Autowired
    private PlanService planService;

    @Autowired
    private CommentaryService commentaryService;

    //跳转个人信息页面
    @RequestMapping("/toUserInfo")
    public String toUserInformation(Model model, HttpSession session) {
        Member member = (Member) session.getAttribute("member");
        model.addAttribute("member", member);
        return "userInformation";
    }


    private int getCurrentPeriod() {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        int hour = calendar.get(java.util.Calendar.HOUR_OF_DAY);
        int period = 0; // 默认值
        if (hour < 11 && hour >= 9) {
            period = 1; // 上午
            System.out.println("当前小时: " + hour + "，计算得到的period: " + period);
            return 1; // 上午
        } else if (hour <17  && hour >= 15) {
            period = 2; // 下午
            System.out.println("当前小时: " + hour + "，计算得到的period: " + period);
            return 2; // 下午
        } else if(hour<21 && hour>19) {
            period = 3; // 晚上
            System.out.println("当前小时: " + hour + "，计算得到的period: " + period);
            return 3; // 晚上
        }
        return period;
    }

    @RequestMapping("/toSiteReservation")
    public String toSiteReservation(Model model, HttpSession session,
                                    @RequestParam(value = "success", required = false) String success,
                                    @RequestParam(value = "error", required = false) String error) {
        LocalDate today = LocalDate.now();
        LocalDate maxDay = today.plusDays(3);
        //设置明天的日期
        LocalDate tomorrow = today.plusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        model.addAttribute("minDate", today.toString());
        model.addAttribute("maxDate", maxDay.toString());
        Member member = (Member) session.getAttribute("member");
        if (member == null) {
            return "redirect:/login?error=请先登录";
        }
        model.addAttribute("member", member);

        // 查询当前会员的普通预约记录
        CommonSiteReservation commonQuery = new CommonSiteReservation();
        commonQuery.setMemberAccount(member.getMemberAccount());
        List<CommonSiteReservation> commonReservationList = commonSiteReservationService.selectByMemberAccountAndReservationDate(commonQuery);
        model.addAttribute("commonReservationList", commonReservationList);



        // 获取当前日期和时段
        Date now = java.sql.Date.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        System.out.println(now);
        int currentPeriod = getCurrentPeriod(); // 你需要实现这个方法，返回1/2/3
        System.out.println("当前时段: " + currentPeriod);
        System.out.println(member.getMemberAccount());

        List<CommonSiteReservation> signInReservationList =
                commonSiteReservationService.findSignInReservations(member.getMemberAccount(), now, currentPeriod);
        System.out.println("当前会员的签到预约记录：");
        for (CommonSiteReservation reservation : signInReservationList) {
            System.out.println(reservation);
        }
        model.addAttribute("signInReservationList", signInReservationList);


        // 查询当前会员的特权预约记录
        SuperSiteReservation superQuery = new SuperSiteReservation();
        superQuery.setMemberAccount(member.getMemberAccount());
        List<SuperSiteReservation> superReservationList = superSiteReservationService.selectByMemberAccountAndReservationDate(superQuery);
        model.addAttribute("superReservationList", superReservationList);

        if (success != null) model.addAttribute("success", success);
        if (error != null) model.addAttribute("error", error);
        return "userSiteReservation";
    }




    @RequestMapping("/toCoachInfo")
    public String toCoachInformation(Model model, HttpSession session) {
        Coach coach = (Coach) session.getAttribute("coach");
        model.addAttribute("coach", coach);
        return "coachInformation";
    }

    //跳转修改个人信息页面
    @RequestMapping("/toUpdateInfo")
    public String toUpdateUserInformation(HttpSession session, Model model) {
        Member member = (Member) session.getAttribute("member");
        model.addAttribute("member", member);
        return "updateUserInformation";
    }

    //修改个人信息
    @RequestMapping("/updateInfo")
    public String updateUserInformation(HttpSession session, Member member) {
        Member member1 = (Member) session.getAttribute("member");

        member.setMemberAccount(member1.getMemberAccount());
        member.setCardClass(member1.getCardClass());
        member.setCardTime(member1.getCardTime());
        member.setCardNextClass(member1.getCardNextClass());
        member.setIsSuper(member1.getIsSuper());

        // 处理密码更新逻辑
        if (member.getMemberPassword() == null || member.getMemberPassword().trim().isEmpty()) {
            // 如果密码为空，保持原密码不变（使用session中的加密密码）
            member.setMemberPassword(member1.getMemberPassword());
        } else {
            // 如果密码不为空，加密新密码
            if (!PasswordEncoder.isEncoded(member.getMemberPassword())) {
                member.setMemberPassword(PasswordEncoder.encode(member.getMemberPassword()));
            }
        }

        try {
            if (memberService.updateMemberByMemberAccount(member)) {
                // 从数据库重新获取更新后的会员信息，确保数据同步
                List<Member> updatedMemberList = memberService.selectByMemberAccount(member.getMemberAccount());
                if (updatedMemberList != null && !updatedMemberList.isEmpty()) {
                    Member updatedMember = updatedMemberList.get(0);
                    // 更新session中的会员信息
                    session.setAttribute("member", updatedMember);
                }
                return "redirect:/user/toUserInfo?success=" + URLEncoder.encode("修改成功", "UTF-8");
            } else {
                return "redirect:/user/toUserInfo?error=" + URLEncoder.encode("修改失败", "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            return "redirect:/user/toUserInfo?error=error";
        }
    }


    @RequestMapping("/deleteSiteReservation")
    public String deleteSiteReservation(@RequestParam("memberAccount") Integer memberAccount,
                                        @RequestParam("reservationDate") String reservationDateStr,
                                        @RequestParam("reservationType") String reservationType) {
        Date reservationDate;
        try {
            reservationDate = new SimpleDateFormat("yyyy-MM-dd").parse(reservationDateStr);
        } catch (Exception e) {
            try {
                return "redirect:/user/toSiteReservation?error=" + URLEncoder.encode("日期格式错误", "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                return "redirect:/user/toSiteReservation?error=error";
            }
        }

        boolean result;
        if ("common".equals(reservationType)) {
            result = commonSiteReservationService.deleteByMemberAccountAndReservationDate(memberAccount, reservationDate);
        } else {
            result = superSiteReservationService.deleteByMemberAccountAndReservationDate(memberAccount, reservationDate);
        }

        try {
            if (result) {
                return "redirect:/user/toSiteReservation?success=" + URLEncoder.encode("取消预约成功", "UTF-8");
            } else {
                return "redirect:/user/toSiteReservation?error=" + URLEncoder.encode("取消预约失败", "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            return "redirect:/user/toSiteReservation?error=error";
        }
    }

    // src/main/java/com/milotnt/controller/UserController.java

    @RequestMapping("/addSiteReservation")
    public String addSiteReservation(HttpSession session,
                                     @RequestParam("reservationDate") String reservationDateStr,
                                     @RequestParam("period") Integer period,
                                     @RequestParam("memberAccount") Integer memberAccount,
                                     @RequestParam("reservationType") String reservationType) {
        Member member = (Member) session.getAttribute("member");
        Date reservationDate;
        try {
            reservationDate = new SimpleDateFormat("yyyy-MM-dd").parse(reservationDateStr);
        } catch (Exception e) {
            try {
                return "redirect:/user/toSiteReservation?error=" + URLEncoder.encode("日期格式错误", "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                return "redirect:/user/toSiteReservation?error=error";
            }
        }

        // 如果是特权预约，检查用户是否为超级会员
        if ("super".equals(reservationType)) {
            if (!Boolean.TRUE.equals(member.getIsSuper())) {
                try {
                    return "redirect:/user/toSiteReservation?error=" + URLEncoder.encode("您不是超级会员，无法进行特权预约", "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    return "redirect:/user/toSiteReservation?error=error";
                }
            }
        }

        // 检查是否已存在同一天的预约
        boolean hasReservationOnSameDay = false;
        if ("common".equals(reservationType)) {
            CommonSiteReservation query = new CommonSiteReservation();
            query.setReservationDate(reservationDate);
            query.setMemberAccount(memberAccount);
            List<CommonSiteReservation> existList = commonSiteReservationService.selectByMemberAccountAndReservationDate(query);
            hasReservationOnSameDay = existList.stream()
                    .anyMatch(r -> r.getReservationDate().equals(reservationDate));
        } else {
            SuperSiteReservation query = new SuperSiteReservation();
            query.setReservationDate(reservationDate);
            query.setMemberAccount(memberAccount);
            List<SuperSiteReservation> existList = superSiteReservationService.selectByMemberAccountAndReservationDate(query);
            hasReservationOnSameDay = existList.stream()
                    .anyMatch(r -> r.getReservationDate().equals(reservationDate));
        }

        if (hasReservationOnSameDay) {
            try {
                return "redirect:/user/toSiteReservation?error=" + URLEncoder.encode("您已经预约了同一天的场地", "UTF-8");
            } catch (UnsupportedEncodingException e) {
                return "redirect:/user/toSiteReservation?error=error";
            }
        }

        // 检查该时段预约人数是否达到限制
        Integer currentCount;
        if ("common".equals(reservationType)) {
            currentCount = commonSiteReservationService.countByDateAndPeriod(reservationDate, period);
        } else {
            currentCount = superSiteReservationService.countByDateAndPeriod(reservationDate, period);
        }

        if (currentCount == null) {
            currentCount = 0;
        }
        System.out.println("当前预约人数: " + currentCount);

        if (currentCount >= 2) {
            try {
                return "redirect:/user/toSiteReservation?error=" + URLEncoder.encode("该时段预约人数已满，当前已预约：" + currentCount + "/2", "UTF-8");
            } catch (UnsupportedEncodingException e) {
                return "redirect:/user/toSiteReservation?error=error";
            }
        }

        boolean result;
        if ("common".equals(reservationType)) {
            CommonSiteReservation reservation = new CommonSiteReservation(reservationDate, period, memberAccount);
            result = commonSiteReservationService.insertCommonSiteReservation(reservation);
        } else {
            SuperSiteReservation reservation = new SuperSiteReservation(reservationDate, period, memberAccount);
            result = superSiteReservationService.insertSuperSiteReservation(reservation);
        }

        try {
            if (result) {
                return "redirect:/user/toSiteReservation?success=" + URLEncoder.encode("预约成功", "UTF-8");
            } else {
                return "redirect:/user/toSiteReservation?error=" + URLEncoder.encode("预约失败", "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            return "redirect:/user/toSiteReservation?error=error";
        }
    }

    //跳转到会员课程管理页面
    @RequestMapping("/toUserClass")
    public String toUserClass(Model model, HttpSession session) {
        Member member = (Member) session.getAttribute("member");
        if (member == null) {
            return "redirect:/login?error=请先登录";
        }
        List<CourseReservation> reservationList = courseReservationService.findByMemberAccount(member.getMemberAccount());
        model.addAttribute("reservationList", reservationList);
        return "userClass";
    }
    //跳转到课程预约页面
    @RequestMapping("/toApplyClass")
    public String toUserApplyClass(Model model, HttpSession session) {
        Member member = (Member) session.getAttribute("member");
        if (member == null) {
            return "redirect:/login?error=请先登录";
        }
        
        // 计算日期范围
        LocalDate today = LocalDate.now();
        LocalDate maxDate = today.plusDays(7);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        model.addAttribute("minDate", today.format(formatter));
        model.addAttribute("maxDate", maxDate.format(formatter));
        
        List<Coach> coachList = coachService.findAll();
        model.addAttribute("coachList", coachList);
        return "userApplyClass";
    }

    //处理课程预约
    @RequestMapping(value = "/applyClass", method = RequestMethod.POST)
    public String userApplyClass(@RequestParam Integer coachAccount,
                               @RequestParam String reservationDateStr,
                               @RequestParam Integer period,
                               HttpSession session,
                               Model model) {
        Member member = (Member) session.getAttribute("member");
        if (member == null) {
            try {
                return "redirect:/login?error=" + URLEncoder.encode("请先登录", "UTF-8");
            } catch (UnsupportedEncodingException e) {
                return "redirect:/login?error=error";
            }
        }

        try {
            Date reservationDate = new SimpleDateFormat("yyyy-MM-dd").parse(reservationDateStr);
            
            // 检查是否已存在同一天的预约
            List<CourseReservation> existingReservations = courseReservationService.findByMemberAccount(member.getMemberAccount());
            boolean hasReservationOnSameDay = existingReservations.stream()
                    .anyMatch(r -> r.getReservationDate().equals(reservationDate) && r.getPeriod().equals(period));
            
            if (hasReservationOnSameDay) {
                try {
                    return "redirect:/user/toApplyClass?error=" + URLEncoder.encode("您已经预约了同一时段的课程", "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    return "redirect:/user/toApplyClass?error=error";
                }
            }

            // 检查该时段是否已被其他会员预约
            boolean isTimeSlotAvailable = !courseReservationService.isReservationExists(
                    member.getMemberAccount(),
                    coachAccount,
                    reservationDate,
                    period);

            if (!isTimeSlotAvailable) {
                try {
                    return "redirect:/user/toApplyClass?error=" + URLEncoder.encode("该时段已被预约", "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    return "redirect:/user/toApplyClass?error=error";
                }
            }

            // 创建预约
            CourseReservation reservation = new CourseReservation(
                    member.getMemberAccount(),
                    coachAccount,
                    reservationDate,
                    period
            );

            // 保存预约
            if (courseReservationService.addReservation(reservation)) {
                try {
                    return "redirect:/user/toUserClass?success=" + URLEncoder.encode("预约成功", "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    return "redirect:/user/toUserClass?error=error";
                }
            } else {
                try {
                    return "redirect:/user/toApplyClass?error=" + URLEncoder.encode("预约失败", "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    return "redirect:/user/toApplyClass?error=error";
                }
            }
        } catch (Exception e) {
            try {
                return "redirect:/user/toApplyClass?error=" + URLEncoder.encode("日期格式错误", "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                return "redirect:/user/toApplyClass?error=error";
            }
        }
    }

    @PostMapping("/signInReservation")
    public String signInReservation(@RequestParam Integer memberAccount,
                                    @RequestParam String reservationDate,
                                    @RequestParam Integer period,
                                    Model model) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(reservationDate);
            boolean result = commonSiteReservationService.signInReservation(memberAccount, date, period);
            if (result) {
                model.addAttribute("success", "签到成功！");
            } else {
                model.addAttribute("error", "签到失败或已签到！");
            }
        } catch (Exception e) {
            model.addAttribute("error", "签到失败，日期格式错误！");
        }
        return "redirect:/user/toSiteReservation";
    }

    //取消课程预约
    @RequestMapping("/delUserClass")
    public String deleteUserClass(@RequestParam Integer memberAccount,
                                @RequestParam Integer coachAccount,
                                @RequestParam String reservationDateStr,
                                @RequestParam Integer period) {
        try {
            Date reservationDate = new SimpleDateFormat("yyyy-MM-dd").parse(reservationDateStr);
            CourseReservation reservation = new CourseReservation(
                    memberAccount,
                    coachAccount,
                    reservationDate,
                    period
            );
            
            if (courseReservationService.deleteReservation(reservation)) {
                try {
                    return "redirect:/user/toUserClass?success=" + URLEncoder.encode("取消预约成功", "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    return "redirect:/user/toUserClass?error=error";
                }
            } else {
                try {
                    return "redirect:/user/toUserClass?error=" + URLEncoder.encode("取消预约失败", "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    return "redirect:/user/toUserClass?error=error";
                }
            }
        } catch (Exception e) {
            try {
                return "redirect:/user/toUserClass?error=" + URLEncoder.encode("日期格式错误", "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                return "redirect:/user/toUserClass?error=error";
            }
        }
    }

    /**
     * 跳转到会员健身计划页面
     */
    @RequestMapping("/toPlan")
    public String toMemberPlan(Model model, HttpSession session) {
        Member member = (Member) session.getAttribute("member");
        if (member == null) {
            return "redirect:/login?error=请先登录";
        }
        
        // 查询当前会员的健身计划
        Plan plan = planService.findByMemberAccount(member.getMemberAccount());
        model.addAttribute("plan", plan);
        
        return "memberPlan";
    }

    //跳转到评价页面
    @RequestMapping("/toCommentary")
    public String toCommentary(Model model, HttpSession session) {
        Member member = (Member) session.getAttribute("member");
        if (member == null) {
            return "redirect:/login?error=请先登录";
        }
        
        // 获取当前会员的所有评价
        List<Commentary> commentaryList = commentaryService.findByMemberAccount(member.getMemberAccount());
        model.addAttribute("commentaryList", commentaryList);
        
        // 获取所有会员的评价
        List<Commentary> allCommentaryList = commentaryService.findAll();
        model.addAttribute("allCommentaryList", allCommentaryList);
        
        return "userCommentary";
    }

    //提交评价
    @RequestMapping("/submitCommentary")
    public String submitCommentary(@RequestParam String message, HttpSession session) {
        Member member = (Member) session.getAttribute("member");
        if (member == null) {
            try {
                return "redirect:/login?error=" + URLEncoder.encode("请先登录", "UTF-8");
            } catch (UnsupportedEncodingException e) {
                return "redirect:/login?error=error";
            }
        }

        // 检查是否已经提交过今天的评价
        Date today = new Date();
        List<Commentary> todayCommentaries = commentaryService.findByMemberAccount(member.getMemberAccount());
        boolean hasTodayCommentary = todayCommentaries.stream()
                .anyMatch(c -> {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    return sdf.format(c.getCommentDate()).equals(sdf.format(today));
                });

        if (hasTodayCommentary) {
            try {
                return "redirect:/user/toCommentary?error=" + URLEncoder.encode("您今天已经提交过评价了", "UTF-8");
            } catch (UnsupportedEncodingException e) {
                return "redirect:/user/toCommentary?error=error";
            }
        }

        Commentary commentary = new Commentary();
        commentary.setMemberAccount(member.getMemberAccount());
        commentary.setMessage(message);
        commentary.setCommentDate(today);

        try {
            if (commentaryService.insertCommentary(commentary)) {
                return "redirect:/user/toCommentary?success=" + URLEncoder.encode("评价提交成功", "UTF-8");
            } else {
                return "redirect:/user/toCommentary?error=" + URLEncoder.encode("评价提交失败", "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            return "redirect:/user/toCommentary?error=error";
        }
    }

}
