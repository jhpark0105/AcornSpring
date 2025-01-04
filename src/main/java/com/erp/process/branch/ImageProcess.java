package com.erp.process.branch;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Map;

@Service
public class ImageProcess {
    @Autowired
    private Cloudinary cloudinary;

    public String uploadImage(MultipartFile file) throws IOException {
//        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
//        return (String) uploadResult.get("url");

        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String uploadedUrl = (String) uploadResult.get("url");
            System.out.println("Uploaded Image URL: " + uploadedUrl);
            return uploadedUrl;
        } catch (Exception e) {
            System.err.println("Image Upload Error: " + e.getMessage());
            throw e;
        }
    }

    public void deleteImage(String publicId) throws IOException {
        try {
            Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            System.out.println("Image Deletion Result: " + result);
        } catch (Exception e) {
            System.err.println("Image Deletion Error: " + e.getMessage());
            throw e;
        }
    }
}