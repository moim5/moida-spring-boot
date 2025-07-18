package com.kh.moida.mapper;

import com.kh.moida.model.Category;
import com.kh.moida.model.File;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {
    List<Category> findAllCategoriesWithFile();

    void insertCategory(Category category);
}
