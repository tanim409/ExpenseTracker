package com.track.ExpenseTracker.repository;

import com.track.ExpenseTracker.entities.Expense;
import com.track.ExpenseTracker.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Repository
public interface ExpenseRepo extends JpaRepository<Expense, Integer> {
    Page<Expense> findByUserId(Integer id, Pageable pageable);

    @Query("select ex from Expense ex where ex.user.id = :userId and ex.category.id = :categoryId")
    Page<Expense> findByUserIdAndCategoryId(@Param("userId") Integer userId,
                                            @Param("categoryId") int categoryId,
                                            Pageable pageable);

    @Query("select ex from Expense ex where ex.user.id = :userId and ex.expenseDate between :fromDate and :toDate")
    Page<Expense> findByUserIdAndDateBetween(@Param("userId") Integer userId,
                                             @Param("fromDate") LocalDate fromDate,
                                             @Param("toDate") LocalDate toDate,
                                             Pageable pageable);

    Integer countByCategoryId(Integer categoryId);


    @Query("select coalesce(sum(e.amount),0) from Expense e where e.user =:user")
    BigDecimal sumExpenseByUser(@Param("user") User user);
}
