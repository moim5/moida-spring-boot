package com.kh.moida.mapper;

import com.kh.moida.model.File;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileMapper {
    void insertFile(File file);

    void deleteFile(Long fileId);
}
