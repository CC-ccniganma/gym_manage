package com.milotnt.pojo;
import java.util.Date;

public class SuperSiteReservation {
    private Date reservationDate;
    private Integer period;
    private Integer memberAccount;

    public SuperSiteReservation() {
    }

    public SuperSiteReservation(Date reservationDate, Integer period, Integer memberAccount) {
        this.reservationDate = reservationDate;
        this.period = period;
        this.memberAccount = memberAccount;
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

    public Integer getMemberAccount() {
        return memberAccount;
    }

    public void setMemberAccount(Integer memberAccount) {
        this.memberAccount = memberAccount;
    }

    @Override
    public String toString() {
        return "SuperSiteReservation{" +
                "reservationDate=" + reservationDate +
                ", period='" + period + '\'' +
                ", memberAccount='" + memberAccount + '\'' +
                '}';
    }
} 