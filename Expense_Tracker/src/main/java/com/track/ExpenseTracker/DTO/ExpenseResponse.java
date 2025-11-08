package com.track.ExpenseTracker.DTO;

import com.track.ExpenseTracker.entities.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseResponse {

    public Integer expenseId;
    public BigDecimal  amount;
    public String description;
    public Category category;
    public LocalDate expenseDate;
    public LocalDateTime createdDate;
    public LocalDateTime updatedDate;

}
