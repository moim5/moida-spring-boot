package com.kh.moida.service;

import com.kh.moida.common.file.FileUploadService;
import com.kh.moida.mapper.CategoryMapper;
import com.kh.moida.mapper.FileMapper;
import com.kh.moida.mapper.MoimMapper;
import com.kh.moida.model.Category;
import com.kh.moida.model.File;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final FileUploadService fileUploadService;
    private final CategoryMapper categoryMapper;
    private final FileMapper fileMapper;
    private final MoimMapper moimMapper;

    public List<Category> getCategoryList() {
        return categoryMapper.findAllCategoriesWithFile();
    }

    public Category getCategory(Long categoryId) {
        return categoryMapper.findCategoryWithFile(categoryId);
    }

    public void insertCategory(Category category, MultipartFile categoryImage) throws IOException {
        String originalName = categoryImage.getOriginalFilename();
        String ext = Objects.requireNonNull(originalName).substring(originalName.lastIndexOf(".") + 1);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        String newName = "category/" + timestamp + "." + ext;

        fileUploadService.uploadFile(categoryImage, newName);

        File file = new File();
        file.setFileOrigin(originalName);
        file.setFileConvert(newName);
        fileMapper.insertFile(file);

        category.setFileId(file.getFileId());
        categoryMapper.insertCategory(category);
    }

    public void updateCategory(Category updated, MultipartFile categoryImage, Category existCategory) throws IOException {
        if (categoryImage == null || categoryImage.isEmpty()) {
            categoryMapper.updateCategoryNameOnly(updated);
            return;
        }

        if (existCategory.getFileConvert() != null) {
            fileUploadService.deleteFile(existCategory.getFileConvert());
        }

        String originalName = categoryImage.getOriginalFilename();
        String ext = Objects.requireNonNull(originalName).substring(originalName.lastIndexOf(".") + 1);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        String newName = "category/" + timestamp + "." + ext;

        fileUploadService.uploadFile(categoryImage, newName);

        File file = new File();
        file.setFileOrigin(originalName);
        file.setFileConvert(newName);
        fileMapper.insertFile(file);

        updated.setFileOrigin(originalName);
        updated.setFileConvert(newName);
        updated.setFileId(file.getFileId());

        categoryMapper.updateCategoryWithFile(updated);
        // 1. 새로운 파일이 생겼나? -> 없으면, 디비만 쓰고 끝
        // 2. 그럼 원래 있던 파일을 지우자
        // 3. 그리고 새로운 파일을 올리자
        // 4. 디비 정보를 업데이트 하자
    }

    public void deleteCategory(Long categoryId) {
        Category category = categoryMapper.findCategoryWithFile(categoryId);
        if (category == null) return;

        String s3Key = category.getFileConvert();
        if (s3Key != null && !s3Key.isEmpty()) {
            fileUploadService.deleteFile(s3Key);
        }

        moimMapper.updateMoimCategoryToDefault(categoryId);
        categoryMapper.deleteCategory(categoryId);
        fileMapper.deleteFile(category.getFileId());
    }
}
