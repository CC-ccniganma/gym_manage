package com.milotnt.pojo;

import java.util.Date;

/**
 * 评价实体类
 */
public class Commentary {

    private Integer memberAccount;
    private String message;
    private Date commentDate;
    private String memberName;

    public Commentary() {
    }

    public Commentary(Integer memberAccount, String message, Date commentDate) {
        this.memberAccount = memberAccount;
        this.message = message;
        this.commentDate = commentDate;
    }

    public Integer getMemberAccount() {
        return memberAccount;
    }

    public void setMemberAccount(Integer memberAccount) {
        this.memberAccount = memberAccount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(Date commentDate) {
        this.commentDate = commentDate;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }
} 