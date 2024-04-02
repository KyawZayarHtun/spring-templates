package com.example.model.service.image;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {

    String saveImageToDesireLocation(String folderName, MultipartFile image) throws IOException;

}
