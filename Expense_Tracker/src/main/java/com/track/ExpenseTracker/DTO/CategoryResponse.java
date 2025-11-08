package com.track.ExpenseTracker.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {

    public Integer categoryId;
    public String categoryName;
    public String icon;


}
