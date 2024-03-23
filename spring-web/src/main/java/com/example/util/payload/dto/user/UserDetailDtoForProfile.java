package com.example.util.payload.dto.user;

import com.example.model.entity.Role_;
import com.example.model.entity.User;
import com.example.model.entity.User_;
import com.example.util.constant.Gender;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public final class UserDetailDtoForProfile {

    @NotBlank(message = "Name cannot be empty!")
    private String name;
    private String email;

    @NotBlank(message = "Phone Number cannot be empty!")
    @Positive(message = "Phone Number must be Number")
    private String phoneNo;
    private Gender gender;
    private String profileImagePath;

    @NotNull(message = "Dob cannot be empty!")
    @Past(message = "Date of Birth must be in the past!")
    @DateTimeFormat (pattern="yyyy-MM-dd")
    private LocalDate dob;
    private String role;

    // Just for upload image
    private MultipartFile profileImage;

    // it is used in db projection
    public UserDetailDtoForProfile(String name, String email, String phoneNo, Gender gender,
                                   String profileImagePath, LocalDate dob, String role) {
        this.name = name;
        this.email = email;
        this.phoneNo = phoneNo;
        this.gender = gender;
        this.profileImagePath = profileImagePath;
        this.dob = dob;
        this.role = role;
    }

    public static void select(CriteriaQuery<UserDetailDtoForProfile> cq, Root<User> root) {
        var role = root.join(User_.role);
        cq.multiselect(
                root.get(User_.name),
                root.get(User_.email),
                root.get(User_.phoneNo),
                root.get(User_.gender),
                root.get(User_.profileImagePath),
                root.get(User_.dob),
                role.get(Role_.name)
        );
    }

}
