package org.zerock.allivery.dto.user;

import lombok.Data;

import java.util.List;

@Data
public class SignupUserDTO {
    private String email;
    private String password;
    private String userName;
    private List<String> roles;
}
