package com.forum.boxchat.service;


import com.forum.boxchat.dto.request.CategoryDtoRequest;
import com.forum.boxchat.dto.respone.CategoryDtoResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {

    List<CategoryDtoResponse> findAllCategories();

    CategoryDtoResponse findCategoryById(int id);

    CategoryDtoResponse findCategoryByName(String name);

    CategoryDtoResponse createCategory(CategoryDtoRequest categoryDtoRequest);

    CategoryDtoResponse updateCategory(CategoryDtoRequest categoryDtoRequest);

    void deleteCategory(int id);



}
