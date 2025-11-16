package com.milotnt.controller;

import com.milotnt.pojo.Member;
import com.milotnt.pojo.SignIn;
import com.milotnt.service.SignInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
public class SignInController {

    @Autowired
    private SignInService signInService;

    @PostMapping("/sign-in")
    public String signIn(Integer period, Model model, HttpSession session) {
        Object user = session.getAttribute("member");
        if (!(user instanceof Member)) {
            model.addAttribute("error", "请先登录会员账号");
            return "redirect:/toUserLogin";
        }
        Member member = (Member) user;
        Date today = new Date();
        SignIn signIn = new SignIn(member.getMemberAccount(), today, period, new Date());
        boolean result = signInService.signIn(signIn);
        if (result) {
            model.addAttribute("success", "签到成功！");
        } else {
            model.addAttribute("error", "您已签到或签到失败！");
        }
        return "redirect:/userMain";
    }
}