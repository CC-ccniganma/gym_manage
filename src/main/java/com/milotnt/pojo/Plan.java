package com.milotnt.pojo;

/**
 * 健身计划实体类
 */
public class Plan {

    private Integer memberAccount;
    private Integer coachAccount;
    private String message;
    private String memberName;
    private String coachName;

    public Plan() {
    }

    public Plan(Integer memberAccount, Integer coachAccount, String message) {
        this.memberAccount = memberAccount;
        this.coachAccount = coachAccount;
        this.message = message;
    }

    public Integer getMemberAccount() {
        return memberAccount;
    }

    public void setMemberAccount(Integer memberAccount) {
        this.memberAccount = memberAccount;
    }

    public Integer getCoachAccount() {
        return coachAccount;
    }

    public void setCoachAccount(Integer coachAccount) {
        this.coachAccount = coachAccount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getCoachName() {
        return coachName;
    }

    public void setCoachName(String coachName) {
        this.coachName = coachName;
    }
} 