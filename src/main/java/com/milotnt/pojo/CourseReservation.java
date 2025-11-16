package com.milotnt.pojo;

import java.util.Date;

public class CourseReservation {
    private Integer memberAccount;
    private String memberName;
    private Integer coachAccount;
    private String coachName;
    private Date reservationDate;
    private Integer period;

    public CourseReservation() {
    }

    public CourseReservation(Integer memberAccount, Integer coachAccount, Date reservationDate, Integer period) {
        this.memberAccount = memberAccount;
        this.coachAccount = coachAccount;
        this.reservationDate = reservationDate;
        this.period = period;
    }

    public Integer getMemberAccount() {
        return memberAccount;
    }

    public void setMemberAccount(Integer memberAccount) {
        this.memberAccount = memberAccount;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public Integer getCoachAccount() {
        return coachAccount;
    }

    public void setCoachAccount(Integer coachAccount) {
        this.coachAccount = coachAccount;
    }

    public String getCoachName() {
        return coachName;
    }

    public void setCoachName(String coachName) {
        this.coachName = coachName;
    }

    public Date getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(Date reservationDate) {
        this.reservationDate = reservationDate;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }
}