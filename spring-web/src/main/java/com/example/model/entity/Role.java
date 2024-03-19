package com.example.model.entity;

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
@Table(name = "role")
public class Role extends BaseField implements Serializable {

    @Serial
    private static final long serialVersionUID = 4191562114313976166L;

    @Column(length = 45, unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "role")
    private List<User> users;

    @ManyToMany
    @JoinTable(name = "role_role_access",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "role_access_id"))
    private List<RoleAccess> roleAccesses;

}
