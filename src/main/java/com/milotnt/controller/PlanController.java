package com.milotnt.controller;

import com.milotnt.pojo.Plan;
import com.milotnt.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/plan")
public class PlanController {

    @Autowired
    private PlanService planService;

    /**
     * 查询所有健身计划 (仅管理员和教练可用)
     *
     * @return 所有健身计划列表
     */
    @GetMapping()
    public Map<String, Object> findAll(HttpSession session) {
        Map<String, Object> map = new HashMap<>();
        
        // 获取当前用户类型
        String userType = (String) session.getAttribute("userType");
        
        // 只有管理员和教练可以查看所有计划
        if ("admin".equals(userType) || "coach".equals(userType)) {
            List<Plan> planList = planService.findAll();
            map.put("code", 0);
            map.put("msg", "查询成功");
            map.put("count", planList.size());
            map.put("data", planList);
        } else {
            map.put("code", 1);
            map.put("msg", "权限不足");
        }
        
        return map;
    }

    /**
     * 根据会员账号查询健身计划 (管理员、教练和对应会员可用)
     *
     * @param memberAccount 会员账号
     * @return 健身计划
     */
    @GetMapping("/memberAccount/{memberAccount}")
    public Map<String, Object> findByMemberAccount(@PathVariable Integer memberAccount, HttpSession session) {
        Map<String, Object> map = new HashMap<>();
        
        String userType = (String) session.getAttribute("userType");
        Integer currentUserId = null;
        
        // 根据用户类型获取当前用户ID
        if ("member".equals(userType)) {
            currentUserId = (Integer) session.getAttribute("userId");
        }
        
        // 如果是会员，只能查看自己的计划
        if ("member".equals(userType) && !memberAccount.equals(currentUserId)) {
            map.put("code", 1);
            map.put("msg", "权限不足，只能查看自己的计划");
            return map;
        }
        
        Plan plan = planService.findByMemberAccount(memberAccount);
        if (plan != null) {
            map.put("code", 0);
            map.put("msg", "查询成功");
            map.put("data", plan);
        } else {
            map.put("code", 0);
            map.put("msg", "暂无健身计划");
        }
        
        return map;
    }

    /**
     * 根据教练账号查询健身计划 (仅管理员和对应教练可用)
     *
     * @param coachAccount 教练账号
     * @return 健身计划列表
     */
    @GetMapping("/coachAccount/{coachAccount}")
    public Map<String, Object> findByCoachAccount(@PathVariable Integer coachAccount, HttpSession session) {
        Map<String, Object> map = new HashMap<>();
        
        String userType = (String) session.getAttribute("userType");
        Integer currentUserId = null;
        
        // 根据用户类型获取当前用户ID
        if ("coach".equals(userType)) {
            currentUserId = (Integer) session.getAttribute("userId");
        }
        
        // 如果是教练，只能查看自己的学员计划
        if ("coach".equals(userType) && !coachAccount.equals(currentUserId)) {
            map.put("code", 1);
            map.put("msg", "权限不足，只能查看自己的学员计划");
            return map;
        }
        
        // 如果是会员，不能使用此功能
        if ("member".equals(userType)) {
            map.put("code", 1);
            map.put("msg", "权限不足");
            return map;
        }
        
        List<Plan> planList = planService.findByCoachAccount(coachAccount);
        map.put("code", 0);
        map.put("msg", "查询成功");
        map.put("count", planList.size());
        map.put("data", planList);
        
        return map;
    }

    /**
     * 添加健身计划 (仅教练可用)
     *
     * @param plan 健身计划对象
     * @return 添加结果
     */
    @PostMapping()
    public Map<String, Object> insertPlan(@RequestBody Plan plan, HttpSession session) {
        Map<String, Object> map = new HashMap<>();
        
        String userType = (String) session.getAttribute("userType");
        Integer currentUserId = null;
        
        // 根据用户类型获取当前用户ID
        if ("coach".equals(userType)) {
            currentUserId = (Integer) session.getAttribute("userId");
        }
        
        // 只有教练可以添加计划，且只能用自己的账号添加
        if (!"coach".equals(userType)) {
            map.put("code", 1);
            map.put("msg", "权限不足，只有教练可以添加计划");
            return map;
        }
        
        // 确保教练只能以自己的身份添加计划
        if (!plan.getCoachAccount().equals(currentUserId)) {
            map.put("code", 1);
            map.put("msg", "只能以自己的身份创建计划");
            return map;
        }
        
        Boolean result = planService.insertPlan(plan);
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
     * 修改健身计划 (仅教练可用)
     *
     * @param plan 健身计划对象
     * @return 修改结果
     */
    @PutMapping()
    public Map<String, Object> updatePlan(@RequestBody Plan plan, HttpSession session) {
        Map<String, Object> map = new HashMap<>();
        
        String userType = (String) session.getAttribute("userType");
        Integer currentUserId = null;
        
        // 根据用户类型获取当前用户ID
        if ("coach".equals(userType)) {
            currentUserId = (Integer) session.getAttribute("userId");
        }
        
        // 只有教练可以修改计划
        if (!"coach".equals(userType)) {
            map.put("code", 1);
            map.put("msg", "权限不足，只有教练可以修改计划");
            return map;
        }
        
        // 确保教练只能修改自己负责的计划
        Plan existingPlan = planService.findByMemberAccount(plan.getMemberAccount());
        if (existingPlan != null && !existingPlan.getCoachAccount().equals(currentUserId)) {
            map.put("code", 1);
            map.put("msg", "只能修改自己负责的学员计划");
            return map;
        }
        
        Boolean result = planService.updatePlan(plan);
        if (result) {
            map.put("code", 0);
            map.put("msg", "修改成功");
        } else {
            map.put("code", 1);
            map.put("msg", "修改失败");
        }
        
        return map;
    }

    /**
     * 删除健身计划 (仅教练可用)
     *
     * @param memberAccount 会员账号
     * @return 删除结果
     */
    @DeleteMapping("/{memberAccount}")
    public Map<String, Object> deletePlan(@PathVariable Integer memberAccount, HttpSession session) {
        Map<String, Object> map = new HashMap<>();
        
        String userType = (String) session.getAttribute("userType");
        Integer currentUserId = null;
        
        // 根据用户类型获取当前用户ID
        if ("coach".equals(userType)) {
            currentUserId = (Integer) session.getAttribute("userId");
        }
        
        // 只有教练可以删除计划
        if (!"coach".equals(userType)) {
            map.put("code", 1);
            map.put("msg", "权限不足，只有教练可以删除计划");
            return map;
        }
        
        // 确保教练只能删除自己负责的计划
        Plan existingPlan = planService.findByMemberAccount(memberAccount);
        if (existingPlan != null && !existingPlan.getCoachAccount().equals(currentUserId)) {
            map.put("code", 1);
            map.put("msg", "只能删除自己负责的学员计划");
            return map;
        }
        
        Boolean result = planService.deletePlan(memberAccount);
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
     * 会员查看自己的健身计划
     *
     * @return 健身计划
     */
    @GetMapping("/myPlan")
    public Map<String, Object> getMyPlan(HttpSession session) {
        Map<String, Object> map = new HashMap<>();
        
        String userType = (String) session.getAttribute("userType");
        
        // 只有会员可以查看自己的计划
        if (!"member".equals(userType)) {
            map.put("code", 1);
            map.put("msg", "权限不足，只有会员可以查看自己的计划");
            return map;
        }
        
        Integer memberAccount = (Integer) session.getAttribute("userId");
        Plan plan = planService.findByMemberAccount(memberAccount);
        
        if (plan != null) {
            map.put("code", 0);
            map.put("msg", "查询成功");
            map.put("data", plan);
        } else {
            map.put("code", 0);
            map.put("msg", "暂无健身计划");
        }
        
        return map;
    }

    /**
     * 教练查看自己创建的所有健身计划
     *
     * @return 健身计划列表
     */
    @GetMapping("/coach")
    public Map<String, Object> getCoachPlans(HttpSession session) {
        Map<String, Object> map = new HashMap<>();
        
        String userType = (String) session.getAttribute("userType");
        
        // 只有教练可以查看此页面
        if (!"coach".equals(userType)) {
            map.put("code", 1);
            map.put("msg", "权限不足，只有教练可以查看此页面");
            return map;
        }
        
        Integer coachAccount = (Integer) session.getAttribute("userId");
        List<Plan> planList = planService.findByCoachAccount(coachAccount);
        
        map.put("code", 0);
        map.put("msg", "查询成功");
        map.put("count", planList.size());
        map.put("data", planList);
        
        return map;
    }
} 