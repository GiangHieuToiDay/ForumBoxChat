package com.forum.boxchat.controller;


import com.forum.boxchat.dto.request.CategoryDtoRequest;
import com.forum.boxchat.dto.respone.CategoryDtoResponse;
import com.forum.boxchat.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

    public final CategoryService categoryService;


    @GetMapping
    public ResponseEntity<List<CategoryDtoResponse>> getAllCategories() {
        return ResponseEntity.ok(categoryService.findAllCategories());
    }

    @PostMapping
    public ResponseEntity<CategoryDtoResponse> addCategory(@RequestBody CategoryDtoRequest categoryDtoRequest) {
        return ResponseEntity.ok(categoryService.createCategory(categoryDtoRequest));
    }




}
