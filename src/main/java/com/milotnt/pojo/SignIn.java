package com.milotnt.pojo;

import java.util.Date;

public class SignIn {
    private Integer memberAccount;
    private Date signInDate;
    private Integer period;
    private Date signInTime;

    // 构造方法
    public SignIn() {}

    public SignIn(Integer memberAccount, Date signInDate, Integer period, Date signInTime) {
        this.memberAccount = memberAccount;
        this.signInDate = signInDate;
        this.period = period;
        this.signInTime = signInTime;
    }

    // getter和setter
    public Integer getMemberAccount() { return memberAccount; }
    public void setMemberAccount(Integer memberAccount) { this.memberAccount = memberAccount; }

    public Date getSignInDate() { return signInDate; }
    public void setSignInDate(Date signInDate) { this.signInDate = signInDate; }

    public Integer getPeriod() { return period; }
    public void setPeriod(Integer period) { this.period = period; }

    public Date getSignInTime() { return signInTime; }
    public void setSignInTime(Date signInTime) { this.signInTime = signInTime; }
}