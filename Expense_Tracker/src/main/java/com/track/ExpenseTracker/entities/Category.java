package com.track.ExpenseTracker.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String categoryName;
    private String icon;
    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private Set<Expense> expense;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;



}
