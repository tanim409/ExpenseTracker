package com.track.ExpenseTracker.controller;

import com.track.ExpenseTracker.DTO.ApiResponse;
import com.track.ExpenseTracker.DTO.CategoryRequest;
import com.track.ExpenseTracker.DTO.CategoryResponse;
import com.track.ExpenseTracker.entities.User;
import com.track.ExpenseTracker.service.CategoryService;
import com.track.ExpenseTracker.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/category")
public class CategoryController {
    private final CategoryService categoryService;
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(@RequestBody CategoryRequest categoryRequest,
                                                                        @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        User user = customUserDetails.getUser();
        CategoryResponse category = categoryService.createCategory(categoryRequest,user);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true,"Category Created",category));
    }
    @PutMapping("/update/{categoryId}")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(@PathVariable Integer categoryId,
                                                                        @RequestBody CategoryRequest categoryRequest) {

        CategoryResponse categoryResponse = categoryService.updateCategory(categoryId,categoryRequest);
        return ResponseEntity.ok(new ApiResponse<>(true,"Category Updated",categoryResponse));
    }
    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Integer categoryId) {
        String message = categoryService.deleteCategory(categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    @GetMapping("/get")
    public ResponseEntity<ApiResponse<Page<CategoryResponse>>> getAllCategories(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                                @RequestParam (defaultValue = "0") Integer page,
                                                                                @RequestParam (defaultValue = "10") Integer size){
        Pageable pageable = PageRequest.of(page, size);
        Page<CategoryResponse> categoryResponses = categoryService.getAllCategories(customUserDetails.getUser().getId(),pageable);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true,"Categories Found",categoryResponses));
    }



}
