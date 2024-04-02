package com.example.model.service.image;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class ImageServiceImpl implements ImageService{

    @Value("${img-server.folder-path}")
    private String imageFolderPath;

    @Value("${img-server.path}")
    private String imageServerPath;

    @Override
    public String saveImageToDesireLocation(String folderName, MultipartFile image) throws IOException {
        String fileName = image.getOriginalFilename();

        if (fileName == null || fileName.isBlank())
            return "";

        String filePath = "%s%s/%s".formatted(imageFolderPath, folderName, fileName);
        image.transferTo(new File(filePath));
        return "%s%s/%s".formatted(imageServerPath, folderName, fileName);
    }

}
