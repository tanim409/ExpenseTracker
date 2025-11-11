

package com.track.ExpenseTracker;

import com.track.ExpenseTracker.entities.Category;
import com.track.ExpenseTracker.entities.Expense;
import com.track.ExpenseTracker.entities.User;
import com.track.ExpenseTracker.repository.ExpenseRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;


import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RepoTest{

    @Autowired
    TestEntityManager testEntityManager;
    User testUser;
    Category testCategory;
    @Autowired
    ExpenseRepo expenseRepo;

    @BeforeEach
    void setUp() {

        testUser = new User();
        testUser.setUsername("siddik");
        testUser.setPassword("111111");
        testUser.setEmail("sid@123");

        testCategory = new Category();
        testCategory.setCategoryName("furniture");
        testEntityManager.persist(testUser);
        testEntityManager.persist(testCategory);
        testEntityManager.flush();
    }

    @Test
    void saveExpense() {
        Expense expenses = new Expense();
        expenses.setAmount(new BigDecimal(123));
        expenses.setExpenseDate(LocalDate.now());
        expenses.setDescription("Groceries");
        expenses.setCategory(testCategory);
        expenses.setUser(testUser);
        Expense expense = expenseRepo.save(expenses);

        assertThat(expense.getId()).isNotNull();
        assertThat(expense.getAmount()).isEqualTo(new BigDecimal("123"));
        assertThat(expense.getExpenseDate()).isEqualTo(LocalDate.now());
        assertThat(expense.getDescription()).isEqualTo("Groceries");


    }

    @Test
    void getExpenseByUserId() {
        Expense expenses = createExpense(BigDecimal.valueOf(1453), "chair", LocalDate.now(), testCategory, testUser);
        testEntityManager.persist(expenses);
        testEntityManager.flush();
        Page<Expense> expense = expenseRepo.findByUserId(testUser.getId(), PageRequest.of(0, 10));

        assertThat(expense.getContent()).hasSize(1);
        assertThat(expense.getContent().get(0).getAmount()).isEqualTo(new BigDecimal("1453"));
        assertThat(expense.getContent().get(0).getExpenseDate()).isEqualTo(LocalDate.now());
        assertThat(expense.getContent().get(0).getDescription()).isEqualTo("chair");
        assertThat(expense.getContent().get(0).getCategory()).isEqualTo(testCategory);
        assertThat(expense.getContent().get(0).getUser()).isEqualTo(testUser);

    }

    @Test
    void getExpenseByUserIdAndCategoryId() {
        Expense expense = createExpense(BigDecimal.valueOf(1453), "chair", LocalDate.now(), testCategory, testUser);
        testEntityManager.persist(expense);
        testEntityManager.flush();

        Page<Expense> expenses = expenseRepo.findByUserIdAndCategoryId(testUser.getId(), testCategory.getId(), PageRequest.of(0, 10));
        assertThat(expenses).hasSize(1);
        assertThat(expenses.getContent().get(0).getAmount()).isEqualTo(new BigDecimal("1453"));
        assertThat(expenses.getContent().get(0).getExpenseDate()).isEqualTo(LocalDate.now());
        assertThat(expenses.getContent().get(0).getDescription()).isEqualTo("chair");
        assertThat(expenses.getContent().get(0).getCategory().getCategoryName()).isEqualTo("furniture");
        assertThat(expenses.getContent().get(0).getUser()).isEqualTo(testUser);

    }

    @Test
    void getExpenseByUserIdAndDateBetween() {

        Expense expense1 = new Expense();
        expense1.setAmount(new BigDecimal(123));
        expense1.setExpenseDate(LocalDate.now());
        expense1.setUser(testUser);
        Expense expense2 = new Expense();
        expense2.setAmount(new BigDecimal(123));
        expense2.setExpenseDate(LocalDate.now());
        expense2.setUser(testUser);
        testEntityManager.persist(expense1);
        testEntityManager.persist(expense2);
        testEntityManager.flush();
        LocalDate startDate = LocalDate.of(2018, 1, 1);
        LocalDate endDate = LocalDate.now();
        Page<Expense> expenses = expenseRepo.findByUserIdAndDateBetween(testUser.getId(), startDate, endDate, PageRequest.of(0, 10));
        assertThat(expenses.getContent()).hasSize(2);
        assertThat(expenses).containsExactlyInAnyOrder(expense1, expense2);

    }

    Expense createExpense(BigDecimal amount, String description, LocalDate expenseDate, Category category, User user) {
        Expense expenses = new Expense();
        expenses.setAmount(amount);
        expenses.setExpenseDate(expenseDate);
        expenses.setDescription(description);
        expenses.setCategory(category);
        expenses.setUser(user);
        expenseRepo.save(expenses);
        return expenses;
    }
}


