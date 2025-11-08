package com.track.ExpenseTracker.controller;

import com.track.ExpenseTracker.DTO.*;
import com.track.ExpenseTracker.entities.User;
import com.track.ExpenseTracker.service.CustomUserDetails;
import com.track.ExpenseTracker.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/expense")
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<ExpenseResponse>> createExpense(@RequestBody ExpenseRequest expenseRequest,
                                                                      @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        User user = customUserDetails.getUser();
        ExpenseResponse expense = expenseService.createExpense(expenseRequest, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, "create successfully", expense));
    }

    @PutMapping("/update/{expenseId}")
    public ResponseEntity<ApiResponse<ExpenseResponse>> updateExpense(@PathVariable Integer expenseId,
                                                                      @RequestBody ExpenseRequest expenseRequest) {
        ExpenseResponse expenseResponse = expenseService.updateExpense(expenseId, expenseRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "Update Successfully", expenseResponse));
    }


    @GetMapping
    public ResponseEntity<ApiResponse<Page<ExpenseResponse>>> getAllExpenses(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                             @RequestParam(defaultValue = "0") int page,
                                                                             @RequestParam(defaultValue = "10") int size,
                                                                             @RequestParam(defaultValue = "expenseDate") String sortBy) {
        User user = customUserDetails.getUser();
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<ExpenseResponse> expense = expenseService.getAllExpenses(user, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, "get all successfully", expense));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<Page<ExpenseResponse>>> getExpenseByCategory(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                                   @PathVariable int categoryId,
                                                                                   @RequestParam(defaultValue = "0") int page,
                                                                                   @RequestParam(defaultValue = "10") int size,
                                                                                   @RequestParam(defaultValue = "expenseDate") String sortBy) {
        User user = customUserDetails.getUser();
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<ExpenseResponse> expense = expenseService.getExpenseByCategory(customUserDetails, categoryId, pageable);
        return ResponseEntity.ok(new ApiResponse<>(true, "get all successfully", expense));
    }

    @GetMapping("/Date-Range")
    public ResponseEntity<ApiResponse<Page<ExpenseResponse>>> getExpenseByDateRange(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                                                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate
    ) {
        User user = customUserDetails.getUser();
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "expenseDate"));
        Page<ExpenseResponse> expense = expenseService.expenseDateRange(user, fromDate, toDate, pageable);
        return ResponseEntity.ok().body(new ApiResponse<>(true, "get all successfully", expense));

    }

    @DeleteMapping("/delete/{expenseId}")
    ResponseEntity<String> deleteExpense(@PathVariable Integer expenseId) {
        String message = expenseService.deleteExpense(expenseId);
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    @GetMapping("/totalAmount")
    public ResponseEntity<BigDecimal> sumOfExpense(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        BigDecimal sum = expenseService.sumOfExpense(customUserDetails.getUser());
        return ResponseEntity.status(HttpStatus.OK).body(sum);
    }


}
