package com.milotnt.controller;

import com.milotnt.pojo.Commentary;
import com.milotnt.service.CommentaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/commentary")
public class CommentaryController {

    @Autowired
    private CommentaryService commentaryService;

    /**
     * 查询所有评价 (所有用户可用)
     *
     * @return 所有评价列表
     */
    @GetMapping()
    public Map<String, Object> findAll() {
        Map<String, Object> map = new HashMap<>();
        
        List<Commentary> commentaryList = commentaryService.findAll();
        map.put("code", 0);
        map.put("msg", "查询成功");
        map.put("count", commentaryList.size());
        map.put("data", commentaryList);
        
        return map;
    }

    /**
     * 根据会员账号查询评价 (所有用户可用)
     *
     * @param memberAccount 会员账号
     * @return 评价列表
     */
    @GetMapping("/{memberAccount}")
    public Map<String, Object> findByMemberAccount(@PathVariable Integer memberAccount) {
        Map<String, Object> map = new HashMap<>();
        
        List<Commentary> commentaryList = commentaryService.findByMemberAccount(memberAccount);
        
        map.put("code", 0);
        map.put("msg", "查询成功");
        map.put("count", commentaryList.size());
        map.put("data", commentaryList);
        
        return map;
    }

    /**
     * 添加评价 (仅会员可用)
     *
     * @param commentary 评价对象
     * @return 添加结果
     */
    @PostMapping()
    public Map<String, Object> insertCommentary(@RequestBody Commentary commentary, HttpSession session) {
        Map<String, Object> map = new HashMap<>();
        
        String userType = (String) session.getAttribute("userType");
        Integer currentUserId = null;
        
        // 根据用户类型获取当前用户ID
        if ("member".equals(userType)) {
            currentUserId = (Integer) session.getAttribute("userId");
        }
        
        // 只有会员可以添加评价
        if (!"member".equals(userType)) {
            map.put("code", 1);
            map.put("msg", "权限不足，只有会员可以添加评价");
            return map;
        }
        
        // 确保会员只能以自己的身份添加评价
        if (!commentary.getMemberAccount().equals(currentUserId)) {
            map.put("code", 1);
            map.put("msg", "只能以自己的身份添加评价");
            return map;
        }
        
        // 如果评价日期为空，设置为当前日期
        if (commentary.getCommentDate() == null) {
            commentary.setCommentDate(new Date());
        }
        
        Boolean result = commentaryService.insertCommentary(commentary);
        if (result) {
            map.put("code", 0);
            map.put("msg", "添加成功");
        } else {
            map.put("code", 1);
            map.put("msg", "添加失败");
        }
        
        return map;
    }

    /**
     * 删除评价 (仅评价所属会员可用)
     *
     * @param memberAccount 会员账号
     * @param commentDate   评价日期
     * @return 删除结果
     */
    @DeleteMapping("/{memberAccount}/{commentDate}")
    public Map<String, Object> deleteCommentary(
            @PathVariable Integer memberAccount,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date commentDate,
            HttpSession session) {
        
        Map<String, Object> map = new HashMap<>();
        
        String userType = (String) session.getAttribute("userType");
        Integer currentUserId = null;
        
        // 只有会员可以删除评价
        if (!"member".equals(userType)) {
            map.put("code", 1);
            map.put("msg", "权限不足，只有会员可以删除评价");
            return map;
        }
        
        // 获取当前会员ID
        currentUserId = (Integer) session.getAttribute("userId");
        
        // 会员只能删除自己的评价
        if (!memberAccount.equals(currentUserId)) {
            map.put("code", 1);
            map.put("msg", "权限不足，只能删除自己的评价");
            return map;
        }
        
        Boolean result = commentaryService.deleteCommentary(memberAccount, commentDate);
        if (result) {
            map.put("code", 0);
            map.put("msg", "删除成功");
        } else {
            map.put("code", 1);
            map.put("msg", "删除失败");
        }
        
        return map;
    }
    
    /**
     * 会员查看自己的评价
     *
     * @return 评价列表
     */
    @GetMapping("/myCommentary")
    public Map<String, Object> getMyCommentary(HttpSession session) {
        Map<String, Object> map = new HashMap<>();
        
        String userType = (String) session.getAttribute("userType");
        
        // 只有会员可以使用此功能
        if (!"member".equals(userType)) {
            map.put("code", 1);
            map.put("msg", "权限不足，只有会员可以查看自己的评价");
            return map;
        }
        
        Integer memberAccount = (Integer) session.getAttribute("userId");
        List<Commentary> commentaryList = commentaryService.findByMemberAccount(memberAccount);
        
        map.put("code", 0);
        map.put("msg", "查询成功");
        map.put("count", commentaryList.size());
        map.put("data", commentaryList);
        
        return map;
    }
} 