package com.cointrendnotifier.android.api.dtos;

public class UserDto extends UserUpdateDto {
    private Integer notifiedAt;

    public UserDto(String email, String username, int alertLimit, String phoneNumber, Integer notifiedAt) {
        super(email, username, alertLimit, phoneNumber);
        this.notifiedAt = notifiedAt;
    }

    public UserDto(String email, String username, int alertLimit, Integer notifiedAt) {
        super(email, username, alertLimit);
        this.notifiedAt = notifiedAt;
    }

    public UserDto(String email, String username, int alertLimit) {
        super(email, username, alertLimit);
    }

    public Integer getNotifiedAt() {
        return notifiedAt;
    }

    public void setNotifiedAt(Integer notifiedAt) {
        this.notifiedAt = notifiedAt;
    }
}
