package com.milotnt.mapper;

import com.milotnt.pojo.Commentary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.Date;
import java.util.List;

@Mapper
public interface CommentaryMapper {
    
    /**
     * 添加评价
     * @param commentary 评价信息
     * @return 影响的行数
     */
    int insertCommentary(Commentary commentary);
    
    /**
     * 根据会员账号查询评价
     * @param memberAccount 会员账号
     * @return 评价列表
     */
    List<Commentary> selectByMemberAccount(@Param("memberAccount") Integer memberAccount);
    
    /**
     * 查询所有评价
     * @return 评价列表
     */
    List<Commentary> selectAll();

    boolean deleteCommentary(Integer memberAccount, Date commentDate);
} 