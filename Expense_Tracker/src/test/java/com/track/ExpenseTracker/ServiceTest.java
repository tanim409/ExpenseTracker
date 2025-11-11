package com.track.ExpenseTracker;

import com.track.ExpenseTracker.DTO.ExpenseRequest;
import com.track.ExpenseTracker.DTO.ExpenseResponse;
import com.track.ExpenseTracker.entities.Category;
import com.track.ExpenseTracker.entities.Expense;
import com.track.ExpenseTracker.entities.User;
import com.track.ExpenseTracker.repository.CategoryRepo;
import com.track.ExpenseTracker.repository.ExpenseRepo;
import com.track.ExpenseTracker.repository.UserRepo;
import com.track.ExpenseTracker.service.ExpenseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServiceTest {

    @Mock
    private ExpenseRepo expenseRepo;
    @Mock
    private UserRepo userRepo;
    @Mock
    private CategoryRepo categoryRepo;
    @InjectMocks
    private ExpenseService expenseService;

    User testUser;
    Category testCategory;
    Expense testExpense;

    @BeforeEach
    public void setUp() {
        testUser = new User();
        testUser.setId(1);
        testUser.setUsername("siddik");
        testUser.setPassword("111111");
        testUser.setEmail("sid@123");

        testCategory = new Category();
        testCategory.setId(1);
        testCategory.setCategoryName("test");
        testCategory.setUser(testUser);

        testExpense = new Expense();
        testExpense.setId(1);
        testExpense.setDescription("food");
        testExpense.setAmount(new BigDecimal(123));
        testExpense.setExpenseDate(LocalDate.now());
        testExpense.setCategory(testCategory);
        testExpense.setUser(testUser);
    }

    @Test
    void createExpense() {

        ExpenseRequest expenseRequest = new ExpenseRequest();
        expenseRequest.setDescription(testExpense.getDescription());
        expenseRequest.setAmount(testExpense.getAmount());
        expenseRequest.setExpenseDate(testExpense.getExpenseDate());
        expenseRequest.setCategoryId(1);

        when(categoryRepo.findById(1)).thenReturn(Optional.of(testCategory));
        when(expenseRepo.save(any(Expense.class))).thenReturn(testExpense);

        ExpenseResponse expenseResponse = expenseService.createExpense(expenseRequest, testUser);

        assertThat(expenseResponse).isNotNull();
        verify(expenseRepo, times(1)).save(any(Expense.class));
    }

    @Test
    void getAllExpenses() {
        List<Expense> expenses = Arrays.asList(testExpense);
        Page<Expense> expensesPage = new PageImpl<>(expenses);
        when(expenseRepo.findByUserId(testUser.getId(), PageRequest.of(0, 10,
                Sort.by("expenseDate").descending()))).thenReturn(expensesPage);
        Page<ExpenseResponse> expense = expenseService.getAllExpenses(testUser, PageRequest.of(0,10, Sort.by("expenseDate").descending()));


        assertThat(expense).isNotNull();
        verify(expenseRepo, times(1)).findByUserId(testUser.getId(),
                PageRequest.of(0, 10,Sort.by("expenseDate").descending()));

    }

    @Test
    void getExpenseByCategory() {
        List<Expense> expenses = Arrays.asList(testExpense);
        Page<Expense> expensesPage = new PageImpl<>(expenses);
        when(expenseRepo.findByUserIdAndCategoryId(testExpense.getUser().getId(),testExpense.getCategory().getId(),
                PageRequest.of(0,10,Sort.by("expenseDate").descending())))
                .thenReturn(expensesPage);

        Page<ExpenseResponse> exp = expenseService.getExpenseByCategory(testUser,testExpense.getCategory().getId(),
                PageRequest.of(0,10,Sort.by("expenseDate").descending()));

        assertThat(exp).isNotNull();
        verify(expenseRepo,times(1)).findByUserIdAndCategoryId(testUser.getId(),testCategory.getId(),
                PageRequest.of(0,10,Sort.by("expenseDate").descending()));

    }

    @Test
    void expenseDateRange(){
        List<Expense> expenses = Arrays.asList(testExpense);
        Page<Expense> expensesPage = new PageImpl<>(expenses);
        LocalDate fromDate = LocalDate.of(2025,8,23);
        LocalDate toDate = LocalDate.of(2025,9,23);
        when(expenseRepo.findByUserIdAndDateBetween(testUser.getId(),fromDate,toDate,PageRequest.of(0,10))).thenReturn(expensesPage);
        Page<ExpenseResponse> exp = expenseService.expenseDateRange(testUser,fromDate,toDate,PageRequest.of(0,10));
        assertThat(exp).isNotNull();
        verify(expenseRepo,times(1)).findByUserIdAndDateBetween(testUser.getId(),fromDate,toDate,PageRequest.of(0,10));
    }

    @Test
    void sumOfExpenses(){
        ExpenseRequest expenseRequest = new ExpenseRequest();
        expenseRequest.setAmount(new BigDecimal(123));
        ExpenseRequest expenseRequest1 = new ExpenseRequest();
        expenseRequest1.setAmount(new BigDecimal(8888888));
        ExpenseRequest expenseRequest2 = new ExpenseRequest();
        expenseRequest2.setAmount(new BigDecimal(99999999));

        when(expenseRepo.sumExpenseByUser(testUser)).thenReturn(expenseRequest.getAmount().add(expenseRequest1.getAmount().add(expenseRequest2.getAmount())));
        BigDecimal expenseSum = expenseRepo.sumExpenseByUser(testUser);
        assertThat(expenseSum).isNotNull();
        assertThat(expenseSum).isEqualByComparingTo("108889010");
        verify(expenseRepo,times(1)).sumExpenseByUser(testUser);
    }

    @Test
    void deleteExpense(){
        when(expenseRepo.findById(testExpense.getId())).thenReturn(Optional.of(testExpense));
        expenseService.deleteExpense(testExpense.getId());
        verify(expenseRepo,times(1)).deleteById(testExpense.getId());

    }
    @Test
    void ShowUpdateExpense(){
        ExpenseRequest expenseRequest = new ExpenseRequest();
        expenseRequest.setAmount(new BigDecimal(123));
        expenseRequest.setDescription("healthCare");
        expenseRequest.setExpenseDate(LocalDate.now());
        expenseRequest.setCategoryId(testCategory.getId());

        when(expenseRepo.findById(testExpense.getId())).thenReturn(Optional.of(testExpense));
        when(categoryRepo.findById(1)).thenReturn(Optional.of(testCategory));
        when(expenseRepo.save(any(Expense.class))).thenReturn(testExpense);
        ExpenseResponse response =  expenseService.updateExpense(testExpense.getId(), expenseRequest);
        assertThat(response).isNotNull();
        verify(expenseRepo,times(1)).save(any(Expense.class));
    }

}
