package com.forum.boxchat.mapper;

import com.forum.boxchat.dto.request.CategoryDtoRequest;
import com.forum.boxchat.dto.respone.CategoryDtoResponse;
import com.forum.boxchat.model.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDtoResponse toResponse(Category category);
    Category toEntity(CategoryDtoRequest categoryDtoRequest);
}
