package com.king.im.user.service;

import com.king.im.user.domain.FileVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

    FileVO upload(MultipartFile file) throws IOException;
}
