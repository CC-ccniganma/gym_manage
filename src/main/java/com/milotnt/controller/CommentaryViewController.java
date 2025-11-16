package com.milotnt.controller;

import com.milotnt.pojo.Commentary;
import com.milotnt.pojo.Member;
import com.milotnt.pojo.Coach;
import com.milotnt.service.CommentaryService;
import com.milotnt.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/commentary-view")
public class CommentaryViewController {

    @Autowired
    private CommentaryService commentaryService;
    
    @Autowired
    private MemberService memberService;

    /**
     * 所有用户查看评价列表
     */
    @GetMapping("/list")
    public String viewCommentaryList(Model model, HttpSession session) {
        // 获取用户类型
        String userType = (String) session.getAttribute("userType");
        model.addAttribute("userType", userType);
        
        // 获取所有评价
        List<Commentary> commentaryList = commentaryService.findAll();
        
        // 确保commentaryList不为null，避免模板解析错误
        if (commentaryList != null && !commentaryList.isEmpty()) {
            model.addAttribute("commentaryList", commentaryList);
        } else {
            model.addAttribute("commentaryList", java.util.Collections.emptyList());
        }
        
        // 检查当前用户是否为会员，用于控制UI显示添加评价按钮
        boolean isMember = false;
        Integer memberAccount = null;
        
        Object user = session.getAttribute("user");
        if (user instanceof Member) {
            isMember = true;
            Member member = (Member) user;
            memberAccount = member.getMemberAccount();
            model.addAttribute("memberName", member.getMemberName());
        }
        
        model.addAttribute("isMember", isMember);
        model.addAttribute("memberAccount", memberAccount);
        
        return "commentaryList";
    }
    
    /**
     * 跳转到添加评价页面（仅会员可用）
     */
    @GetMapping("/toAdd")
    public String toAddCommentary(HttpSession session, Model model) {
        // 检查是否是会员
        Object user = session.getAttribute("user");
        if (!(user instanceof Member)) {
            model.addAttribute("error", "只有会员可以添加评价");
            return "redirect:/commentary-view/list";
        }
        
        Member member = (Member) user;
        model.addAttribute("memberAccount", member.getMemberAccount());
        model.addAttribute("memberName", member.getMemberName());
        
        return "addCommentary";
    }
    
    /**
     * 处理添加评价请求（仅会员可用）
     */
    @PostMapping("/add")
    public String addCommentary(
            @RequestParam String message,
            HttpSession session,
            Model model) {
        
        // 检查是否是会员
        Object user = session.getAttribute("user");
        if (!(user instanceof Member)) {
            model.addAttribute("error", "只有会员可以添加评价");
            return "redirect:/commentary-view/list";
        }
        
        Member member = (Member) user;
        Commentary commentary = new Commentary(member.getMemberAccount(), message, new Date());
        
        boolean result = commentaryService.insertCommentary(commentary);
        if (result) {
            model.addAttribute("success", "评价提交成功！");
        } else {
            model.addAttribute("error", "评价提交失败！");
        }
        
        return "redirect:/commentary-view/list";
    }
    
    /**
     * 处理删除评价请求（仅会员可用，删除自己的评价）
     */
    @GetMapping("/delete/{memberAccount}/{commentDate}")
    public String deleteCommentary(
            @PathVariable Integer memberAccount,
            @PathVariable String commentDate,
            HttpSession session,
            Model model) {
        
        // 检查当前用户类型和权限
        Object user = session.getAttribute("user");
        
        boolean canDelete = false;
        
        // 只有会员才能删除自己的评价
        if (user instanceof Member) {
            Member member = (Member) user;
            canDelete = member.getMemberAccount().equals(memberAccount);
        }
        
        if (!canDelete) {
            model.addAttribute("error", "您没有权限删除此评价");
            return "redirect:/commentary-view/list";
        }
        
        try {
            // 解析日期字符串
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(commentDate);
            
            boolean result = commentaryService.deleteCommentary(memberAccount, date);
            if (result) {
                model.addAttribute("success", "评价删除成功！");
            } else {
                model.addAttribute("error", "评价删除失败！");
            }
        } catch (Exception e) {
            model.addAttribute("error", "日期格式错误！");
        }
        
        return "redirect:/commentary-view/list";
    }
} 