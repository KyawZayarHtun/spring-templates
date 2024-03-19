package com.example.model.entity;

import com.example.util.constant.RequestMethod;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "role_access")
public class RoleAccess extends BaseField implements Serializable {

    @Serial
    private static final long serialVersionUID = -3236067342532617314L;

    @Column(length = 45, unique = true, nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String url;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RequestMethod requestMethod;

    private String description;

    @ManyToMany(mappedBy = "roleAccesses")
    private List<Role> roles;

}
