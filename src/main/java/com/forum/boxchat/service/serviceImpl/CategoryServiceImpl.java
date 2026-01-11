package com.forum.boxchat.service.serviceImpl;


import com.forum.boxchat.dto.request.CategoryDtoRequest;
import com.forum.boxchat.dto.respone.CategoryDtoResponse;
import com.forum.boxchat.exception.AppException;
import com.forum.boxchat.exception.ErrorCode;
import com.forum.boxchat.mapper.CategoryMapper;
import com.forum.boxchat.model.entity.Category;
import com.forum.boxchat.repository.CategoryRepository;
import com.forum.boxchat.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    public final CategoryRepository categoryRepository;
    public final CategoryMapper  categoryMapper;

    @Override
    public List<CategoryDtoResponse> findAllCategories() {
        List<Category> categories = categoryRepository.findAll();

        if(categories.isEmpty()){
            throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
        }

        return categories.stream()
                .map(categoryMapper :: toResponse)
                .toList();
    }

    @Override
    public CategoryDtoResponse findCategoryById(int id) {
        return null;
    }

    @Override
    public CategoryDtoResponse findCategoryByName(String name) {
        return null;
    }

    @Override
    public CategoryDtoResponse createCategory(CategoryDtoRequest categoryDtoRequest) {
        Category category = categoryMapper.toEntity(categoryDtoRequest);
        if( checkUnique(categoryDtoRequest.getName()) == 0){
            throw new AppException(ErrorCode.CATEGORY_NAME_IS_EXIST);
        }
        categoryRepository.save(category);
        return categoryMapper.toResponse(category);
    }

    public int checkUnique(String categoryName){
        int x = 1;
        for(Category category : categoryRepository.findAll()){
            if(category.getName().equals(categoryName)){
                x= 0;
            }
        }
        return x;
    }

    @Override
    public CategoryDtoResponse updateCategory(CategoryDtoRequest categoryDtoRequest) {
        Category category = categoryRepository.findByName(categoryDtoRequest.getName());
        if(category == null){
            throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
        }

        if( checkUnique(categoryDtoRequest.getName()) == 0 && !category.getName().equals(categoryDtoRequest.getName())){
            throw new AppException(ErrorCode.CATEGORY_NAME_IS_EXIST);
        }

        category.setName(categoryDtoRequest.getName());
        categoryRepository.save(category);
        return categoryMapper.toResponse(category);
    }

    @Override
    public void deleteCategory(int id) {
        categoryRepository.deleteById(id);
    }
}
