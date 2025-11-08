package com.track.ExpenseTracker.service;

import com.track.ExpenseTracker.DTO.ExpenseRequest;
import com.track.ExpenseTracker.DTO.ExpenseResponse;
import com.track.ExpenseTracker.entities.Category;
import com.track.ExpenseTracker.entities.Expense;

import com.track.ExpenseTracker.entities.User;
import com.track.ExpenseTracker.repository.CategoryRepo;
import com.track.ExpenseTracker.repository.ExpenseRepo;

import com.track.ExpenseTracker.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ExpenseService {

    public final ExpenseRepo expenseRepo;
    public final UserRepo userRepo;
    public final CategoryRepo categoryRepo;


    public ExpenseResponse createExpense(ExpenseRequest expenseRequest, User user) {

        Expense expense = new Expense();
        expense.setUser(user);
        expense.setAmount(expenseRequest.getAmount());
        expense.setDescription(expenseRequest.getDescription());
        expense.setExpenseDate(expenseRequest.getExpenseDate());
        if (expenseRequest.getCategoryId() != null) {
            Category category = categoryRepo.findById(expenseRequest.getCategoryId()).orElseThrow(() -> new RuntimeException("Category not found"));
            if (category.getUser().getId().equals(user.getId())) {
                expense.setCategory(category);
            }
        }
        Expense exp = expenseRepo.save(expense);
        return modToResponse(exp);
    }

    private ExpenseResponse modToResponse(Expense exp) {

        ExpenseResponse expenseResponse = new ExpenseResponse();
        expenseResponse.setExpenseDate(exp.getExpenseDate());
        expenseResponse.setAmount(exp.getAmount());
        expenseResponse.setDescription(exp.getDescription());
        expenseResponse.setCategory(exp.getCategory());
        expenseResponse.setExpenseId(exp.getId());
        expenseResponse.setCreatedDate(exp.getCreatedAt());
        expenseResponse.setUpdatedDate(exp.getUpdatedAt());

        return expenseResponse;
    }

    public Page<ExpenseResponse> getAllExpenses(User user, Pageable pageable) {
        Page<Expense> expenses = expenseRepo.findByUserId(user.getId(), pageable);
        return expenses.map(this::modToResponse);
    }

    public Page<ExpenseResponse> getExpenseByCategory(CustomUserDetails customUserDetails, int categoryId, Pageable pageable) {
        Integer userId = customUserDetails.getUser().getId();
        Page<Expense> expensesByCategory = expenseRepo.findByUserIdAndCategoryId(userId, categoryId, pageable);
        return expensesByCategory.map(this::modToResponse);
    }

    public Page<ExpenseResponse> expenseDateRange(User user, LocalDate fromDate, LocalDate toDate, Pageable pageable) {
        Page<Expense> expenses = expenseRepo.findByUserIdAndDateBetween(user.getId(), fromDate, toDate, pageable);
        return expenses.map(this::modToResponse);
    }

    public ExpenseResponse updateExpense(Integer expenseId, ExpenseRequest expenseRequest) {

        Expense expense = expenseRepo.findById(expenseId).orElseThrow(() -> new RuntimeException("Expense not found"));
        expense.setAmount(expenseRequest.getAmount());
        expense.setDescription(expenseRequest.getDescription());
        expense.setExpenseDate(expenseRequest.getExpenseDate());
        if (expenseRequest.getCategoryId() != null) {
            Category category = categoryRepo.findById(expenseRequest.getCategoryId()).orElseThrow(() -> new RuntimeException("Category not found"));
            expense.setCategory(category);
        }
        expenseRepo.save(expense);
        return modToResponse(expense);
    }

    public String deleteExpense(Integer expenseId) {
        if (expenseRepo.findById(expenseId).isPresent()) {
            expenseRepo.deleteById(expenseId);
            return "Expense has been deleted";
        } else {
            return "ExpenseId is not found";
        }

    }

    public BigDecimal sumOfExpense(User user) {
        return expenseRepo.sumExpenseByUser(user);
    }
}
