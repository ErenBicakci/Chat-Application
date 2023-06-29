package com.erenbicakci.chatapplication.dto;

import com.erenbicakci.chatapplication.entity.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class UserDto {
    private Long id;
    private String nameSurname;
    private String username;
    private String password;

    private List<Role> roles;

}
