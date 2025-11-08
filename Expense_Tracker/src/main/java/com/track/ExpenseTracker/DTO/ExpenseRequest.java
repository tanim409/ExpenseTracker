package com.track.ExpenseTracker.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseRequest {

    public BigDecimal amount;
    public Integer categoryId;
    public String description;
    public LocalDate expenseDate;



}
