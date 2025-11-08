package com.track.ExpenseTracker.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    @NotBlank(message = "username is required")
    private String username;
    @NotBlank(message = "email is required")
    @Email(message = "Email should be valid")
    private String email;
    @NotBlank(message = "password is required")
    @Size(min = 6, max = 18)
    private String password;

}
