package com.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;  // javax → jakarta in Boot 3

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequestDto {
    @NotBlank private String username;
    @NotBlank private String password;
}
