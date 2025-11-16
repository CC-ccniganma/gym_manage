package com.milotnt.pojo;
public class Coach {

    private Integer coachAccount;
    private String coachPassword;
    private String coachName;
    private String coachGender;
    private Integer coachAge;

    public Integer getCoachAccount() {
        return coachAccount;
    }

    public void setCoachAccount(Integer coachAccount) {
        this.coachAccount = coachAccount;
    }

    public String getCoachPassword() {
        return coachPassword;
    }

    public void setCoachPassword(String coachPassword) {
        this.coachPassword = coachPassword;
    }

    public String getCoachName() {
        return coachName;
    }

    public void setCoachName(String coachName) {
        this.coachName = coachName;
    }

    public String getCoachGender() {
        return coachGender;
    }

    public void setCoachGender(String coachGender) {
        this.coachGender = coachGender;
    }

    public Integer getCoachAge() {
        return coachAge;
    }

    public void setCoachAge(Integer coachAge) {
        this.coachAge = coachAge;
    }

    @Override
    public String toString() {
        return "Coach{" +
                ", coachAccount=" + coachAccount +
                ", coachPassword='" + coachPassword + '\'' +
                ", coachName='" + coachName + '\'' +
                ", coachGender='" + coachGender + '\'' +
                ", coachAge=" + coachAge +
                '}';
    }
}
