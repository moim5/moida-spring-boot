package com.kh.moida.service;

import com.kh.moida.common.file.FileUploadService;
import com.kh.moida.mapper.CategoryMapper;
import com.kh.moida.mapper.FileMapper;
import com.kh.moida.model.Category;
import com.kh.moida.model.File;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
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
    private final CategoryMapper categoryMapper;
    private final FileMapper fileMapper;
    private final FileUploadService fileUploadService;

    public List<Category> getCategoryList() {
        return categoryMapper.findAllCategoriesWithFile();
    }

    public void insertCategory(Category category, MultipartFile categoryImage) throws IOException {
        String originalName = categoryImage.getOriginalFilename();
        String ext = Objects.requireNonNull(originalName).substring(originalName.lastIndexOf(".") + 1);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        String newName = "category/" + timestamp + "." + ext;

        String url = fileUploadService.uploadFile(categoryImage, newName);

        File file = new File();
        file.setFileOrigin(originalName);
        file.setFileConvert(newName);
        fileMapper.insertFile(file);

        category.setFileId(file.getFileId());
        categoryMapper.insertCategory(category);
    }
}
