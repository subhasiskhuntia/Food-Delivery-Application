package com.food.userservice.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserChangePasswordDto {
    @NotBlank(message = "Email cannot be Blank")
    private String email;
    @NotBlank(message = "oldPassword cannot be Blank")
    private String oldPassword;
    @NotBlank(message = "newPassword cannot be Blank")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}", message = "Password must contain one capital letter,one small letter , one number and one special character and of at-least 8 character long")
    private String newPassword;

}
