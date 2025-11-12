package com.track.ExpenseTracker.service;

import com.track.ExpenseTracker.DTO.CategoryRequest;
import com.track.ExpenseTracker.DTO.CategoryResponse;
import com.track.ExpenseTracker.Exception.ResourceNotFoundException;
import com.track.ExpenseTracker.Exception.UnauthorizeException;
import com.track.ExpenseTracker.entities.Category;
import com.track.ExpenseTracker.entities.User;
import com.track.ExpenseTracker.repository.CategoryRepo;
import com.track.ExpenseTracker.repository.ExpenseRepo;
import com.track.ExpenseTracker.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepo categoryRepo;
    private final UserRepo userRepo;
    private final ExpenseRepo expenseRepo;
    public CategoryResponse createCategory(CategoryRequest categoryRequest, User us) {
        User user =
                userRepo.findById(us.getId()).orElseThrow(()->new ResourceNotFoundException("User not found.. id = "+us.getId()));
        Category category = new Category();
        category.setCategoryName(categoryRequest.getCategoryName());
        category.setIcon(categoryRequest.getIcon());
        category.setUser(user);
        Category savedCategory = categoryRepo.save(category);
        return modelToCategoryResponse(savedCategory);
    }

    private CategoryResponse modelToCategoryResponse(Category category) {
        return new CategoryResponse(
                           category.getId(),
                           category.getCategoryName(),
                           category.getIcon());
    }

    public CategoryResponse updateCategory(Integer categoryId, CategoryRequest categoryRequest) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("Category Not Found ...id = "+categoryId));
        category.setCategoryName(categoryRequest.getCategoryName());
        category.setIcon(categoryRequest.getIcon());
        categoryRepo.save(category);
        return modelToCategoryResponse(category);
    }

    public String deleteCategory(Integer categoryId) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("Category Not Found...id = "+categoryId));

        Integer count = expenseRepo.countByCategoryId(categoryId);

        if(count > 0) {
            throw new UnauthorizeException("Category Already Exists..id "+categoryId);
        }
            categoryRepo.deleteById(categoryId);
            return "Category Deleted Successfully";

    }

    public Page<CategoryResponse> getAllCategories(Integer userId, Pageable pageable) {

        Page<Category> categoryResponse = categoryRepo.findByUserId(userId,pageable);

        return categoryResponse.map(this::modelToCategoryResponse);
    }
}
