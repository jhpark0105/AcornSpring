package com.erp.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dundp7uag",
                "api_key", "756375656318959",
                "api_secret", "_n_eKnIkAt27mrbltsCFASIO4BY"
        ));
    }
}