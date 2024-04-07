package com.example.util.payload.dto.user;

import com.example.util.constant.Gender;
import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public record UserUpdateForm(
        @NotNull(message = "Id can't be empty or null!")
        @Positive(message = "Id must be positive number")
        Long id,
        @NotEmpty(message = "Name can't be empty or null!")
        String name,
        @NotEmpty(message = "Phone Number can't be empty or null!")
        String phoneNo,
        @NotNull(message = "Gender can't be null!")
        Gender gender,
        @NotNull(message = "Date of Birth can't be null!")
        @Past(message = "Date of Birth must be past!")
        LocalDate dob,
        MultipartFile profileImage
) {
}
