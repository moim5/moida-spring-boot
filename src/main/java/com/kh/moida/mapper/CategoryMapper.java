package com.kh.moida.mapper;

import com.kh.moida.model.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {
    List<Category> findAllCategoriesWithFile();

    void insertCategory(Category category);

    Category findCategoryWithFile(Long categoryId);

    void updateCategoryNameOnly(Category updated);

    void updateCategoryWithFile(Category updated);

    void deleteCategory(Long categoryId);
}
