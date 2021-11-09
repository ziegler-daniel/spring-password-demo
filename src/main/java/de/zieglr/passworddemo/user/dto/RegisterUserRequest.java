package de.zieglr.passworddemo.user.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RegisterUserRequest {

    private String username;
    private CharSequence password;

}
