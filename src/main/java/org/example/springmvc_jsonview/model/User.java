package org.example.springmvc_jsonview.model;


import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.example.springmvc_jsonview.dto.Views;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    @JsonView(Views.UserSummary.class)
    private long id;

    @Column(name = "name")
    @JsonView(Views.UserSummary.class)
    @NotBlank(message = "Name is mandatory")
    private String name;

    @Column(name = "email")
    @JsonView(Views.UserSummary.class)
    @NotBlank(message = "Email is mandatory")
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonView(Views.UserDetails.class)
    private List<UserOrder> orders = new ArrayList<>();

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    void setId(long id) {
        this.id = id;
    }
}
