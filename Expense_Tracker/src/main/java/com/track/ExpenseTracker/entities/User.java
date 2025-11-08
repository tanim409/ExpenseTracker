package com.track.ExpenseTracker.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.track.ExpenseTracker.DTO.ExpenseSummary;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.HashSet;
import java.util.Set;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;
    @NotBlank(message = "username is required")
    @Column(nullable = false)
    private String username;
    @NotBlank(message = "password is required")
    @Size(min = 6, max = 18)
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String email;


    @OneToMany(mappedBy = "user")
    @JsonIgnore
    Set<Expense> expenses = new HashSet<Expense>();
    @OneToMany(mappedBy = "user")
    @JsonIgnore
    Set<Category> categories = new HashSet<Category>();


}
