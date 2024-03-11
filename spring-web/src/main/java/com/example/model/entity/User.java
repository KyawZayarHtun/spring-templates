package com.example.model.entity;

import com.example.util.constant.Gender;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.type.TrueFalseConverter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "user")
public class User extends BaseField implements Serializable{

    @Serial
    private static final long serialVersionUID = 5207483492648808468L;

    @Column(name = "name", length = 45, nullable = false)
    private String name;

    @Column(name = "email", length = 45, nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "phone_number", length = 11)
    private String phoneNo;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "profile_image_path")
    private String profileImagePath;

    @Column(name = "dob")
    private LocalDate dob;

    @Column(name = "locked", nullable = false)
    @Convert(converter = TrueFalseConverter.class)
    private Boolean locked;

    @Column(name = "activated", nullable = false)
    @Convert(converter = TrueFalseConverter.class)
    private Boolean activated;

}
