package com.cointrendnotifier.android.api.dtos;

public class RegisteredUserDto extends UserDto {
    private String _id;

    public RegisteredUserDto(String email, String username, int alertLimit, String _id, String phoneNumber, Integer notifiedAt) {
        super(email, username, alertLimit, phoneNumber, notifiedAt);
        this._id = _id;
    }

    public RegisteredUserDto(String email, String username, int alertLimit, String _id, Integer notifiedAt) {
        super(email, username, alertLimit, notifiedAt);
        this._id = _id;
    }

    public RegisteredUserDto(String email, String username, int alertLimit, String _id) {
        super(email, username, alertLimit);
        this._id = _id;
    }

    public String getId() {
        return _id;
    }
}
