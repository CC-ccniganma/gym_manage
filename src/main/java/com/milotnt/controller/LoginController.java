package com.milotnt.controller;

import com.milotnt.pojo.Admin;
import com.milotnt.pojo.Member;
import com.milotnt.pojo.Coach;
import com.milotnt.service.AdminService;
import com.milotnt.service.CoachService;
import com.milotnt.service.EmployeeService;
import com.milotnt.service.EquipmentService;
import com.milotnt.service.MemberService;
import com.milotnt.util.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class LoginController {

    @Autowired
    private MemberService memberService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private CoachService coachService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private EquipmentService equipmentService;

    //主页、跳转管理员登录页面
    @RequestMapping("/")
    public String toAdminLogin() {
        return "adminLogin";
    }

    //跳转会员登录页面
    @RequestMapping("/toUserLogin")
    public String toUserLogin() {
        return "userLogin";
    }

    @RequestMapping("/toCoachLogin")
    public String toCoachLogin() {
        return "coachLogin";
    }

    //会员注册
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(Member member, Model model) {
        // 校验账号格式
        String accountStr = String.valueOf(member.getMemberAccount());
        if (!accountStr.matches("^\\d{1,9}$")) {
            model.addAttribute("msg", "账号必须为1-9位数字，且不能以0开头");
            return "userLogin";
        }
        try {
            int accountInt = Integer.parseInt(accountStr);
            if (accountInt < 1 || accountInt > 2147483647) {
                model.addAttribute("msg", "账号必须为1-9位数字，且不能超过2147483647");
                return "userLogin";
            }
        } catch (Exception e) {
            model.addAttribute("msg", "账号格式不正确");
            return "userLogin";
        }
        // 检查账号是否已存在
        if (memberService.selectByMemberAccount(member.getMemberAccount()).size() > 0) {
            model.addAttribute("msg", "该账号已被注册！");
            return "userLogin";
        }
        // 加密密码
        member.setMemberPassword(PasswordEncoder.encode(member.getMemberPassword()));
        
        // 设置默认值
        member.setIsSuper(false);
        member.setCardTime(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        member.setCardClass(0);
        member.setCardNextClass(0);
        
        // 保存会员信息
        if (memberService.insertMember(member)) {
            model.addAttribute("msg", "注册成功，请登录！");
        } else {
            model.addAttribute("msg", "注册失败，请重试！");
        }
        return "userLogin";
    }

    //管理员登录
    @RequestMapping("/adminLogin")
    public String adminLogin(Admin admin, Model model, HttpSession session) {
        Admin admin1 = adminService.adminLogin(admin);
        if (admin1 != null && PasswordEncoder.matches(admin.getAdminPassword(), admin1.getAdminPassword())) {
            //会员人数
            Integer memberTotal = memberService.selectTotalCount();
            model.addAttribute("memberTotal", memberTotal);
            session.setAttribute("memberTotal", memberTotal);

            //员工人数
            Integer employeeTotal = employeeService.selectTotalCount();
            model.addAttribute("employeeTotal", employeeTotal);
            session.setAttribute("employeeTotal", employeeTotal);

            //健身房总人数
            Integer humanTotal = memberTotal + employeeTotal;
            model.addAttribute("humanTotal", humanTotal);
            session.setAttribute("humanTotal", humanTotal);

            //器材数
            Integer equipmentTotal = equipmentService.selectTotalCount();
            model.addAttribute("equipmentTotal", equipmentTotal);
            session.setAttribute("equipmentTotal", equipmentTotal);

            // 设置用户类型为管理员
            session.setAttribute("userType", "admin");
            session.setAttribute("user", admin1);

            return "adminMain";
        }
        model.addAttribute("msg", "您输入的账号或密码有误，请重新输入！");
        return "adminLogin";
    }

    //会员登录
    @RequestMapping("/userLogin")
    public String userLogin(Member member, Model model, HttpSession session) {
        Member member1 = memberService.userLogin(member);
        if (member1 != null && PasswordEncoder.matches(member.getMemberPassword(), member1.getMemberPassword())) {
            model.addAttribute("member", member1);
            session.setAttribute("member", member1);
            session.setAttribute("userType", "member");
            return "userMain";
        }
        model.addAttribute("msg", "您输入的账号或密码有误，请重新输入！");
        return "userLogin";
    }

    //教练登录
    @RequestMapping("/coachLogin")
    public String coachLogin(Coach coach, Model model, HttpSession session) {
        Coach coach1 = coachService.coachLogin(coach);
        if (coach1 != null && PasswordEncoder.matches(coach.getCoachPassword(), coach1.getCoachPassword())) {
            model.addAttribute("coach", coach1);
            session.setAttribute("coach", coach1);
            session.setAttribute("userType", "coach");
            return "coachMain";
        }
        model.addAttribute("msg", "您输入的账号或密码有误，请重新输入！");
        return "coachLogin";
    }

    //跳转管理员主页
    @RequestMapping("/toAdminMain")
    public String toAdminMain(Model model, HttpSession session) {
        Integer memberTotal = (Integer) session.getAttribute("memberTotal");
        Integer employeeTotal = (Integer) session.getAttribute("employeeTotal");
        Integer humanTotal = (Integer) session.getAttribute("humanTotal");
        Integer equipmentTotal = (Integer) session.getAttribute("equipmentTotal");
        model.addAttribute("memberTotal", memberTotal);
        model.addAttribute("employeeTotal", employeeTotal);
        model.addAttribute("humanTotal", humanTotal);
        model.addAttribute("equipmentTotal", equipmentTotal);
        
        // 确保用户类型为管理员
        session.setAttribute("userType", "admin");
        
        return "adminMain";
    }

    //跳转会员主页
    @RequestMapping("/toUserMain")
    public String toUserMain(Model model, HttpSession session) {
        Member member = (Member) session.getAttribute("member");
        model.addAttribute("member", member);
        
        // 确保用户类型为会员
        session.setAttribute("userType", "member");
        
        return "userMain";
    }
    
    //跳转教练主页
    @RequestMapping("/toCoachMain")
    public String toCoachMain(Model model, HttpSession session) {
        Coach coach = (Coach) session.getAttribute("coach");
        model.addAttribute("coach", coach);
        
        // 确保用户类型为教练
        session.setAttribute("userType", "coach");
        
        return "coachMain";
    }
}
