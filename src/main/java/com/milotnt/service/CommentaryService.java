package com.milotnt.service;

import com.milotnt.pojo.Commentary;

import java.util.Date;
import java.util.List;

public interface CommentaryService {

    /**
     * 添加评价
     * @param commentary 评价信息
     * @return 是否添加成功
     */
    boolean insertCommentary(Commentary commentary);

    /**
     * 根据会员账号查询评价
     * @param memberAccount 会员账号
     * @return 评价列表
     */
    List<Commentary> findByMemberAccount(Integer memberAccount);

    /**
     * 查询所有评价
     * @return 评价列表
     */
    List<Commentary> findAll();

    // 删除评价
    Boolean deleteCommentary(Integer memberAccount, Date commentDate);
} 