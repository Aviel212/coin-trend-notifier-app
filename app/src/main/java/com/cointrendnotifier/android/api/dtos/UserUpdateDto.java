package com.cointrendnotifier.android.api.dtos;

public class UserUpdateDto {
    private String email;
    private String username;
    private int alertLimit;
    private String phoneNumber;

    public UserUpdateDto(String email, String username, int alertLimit, String phoneNumber) {
        this.email = email;
        this.username = username;
        this.alertLimit = alertLimit;
        this.phoneNumber = phoneNumber;
    }

    public UserUpdateDto(String email, String username, int alertLimit) {
        this(email, username, alertLimit, null);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAlertLimit() {
        return alertLimit;
    }

    public void setAlertLimit(int alertLimit) {
        this.alertLimit = alertLimit;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
