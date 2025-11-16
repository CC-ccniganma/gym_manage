package com.milotnt.service.impl;

import com.milotnt.mapper.CommentaryMapper;
import com.milotnt.pojo.Commentary;
import com.milotnt.service.CommentaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CommentaryServiceImpl implements CommentaryService {

    @Autowired
    private CommentaryMapper commentaryMapper;

    @Override
    public List<Commentary> findAll() {
        return commentaryMapper.selectAll();
    }

    @Override
    public List<Commentary> findByMemberAccount(Integer memberAccount) {
        return commentaryMapper.selectByMemberAccount(memberAccount);
    }

    @Override
    public boolean insertCommentary(Commentary commentary) {
        return commentaryMapper.insertCommentary(commentary) > 0;
    }

    @Override
    public Boolean deleteCommentary(Integer memberAccount, Date commentDate) {
        return commentaryMapper.deleteCommentary(memberAccount, commentDate);
    }
} 