package com.ibm.anz.wmla.sdk.ibmwmla.service;

import com.ibm.anz.wmla.sdk.ibmwmla.interfaces.Uploader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
public class FileSystemUploader implements Uploader {

    private final Logger LOG = LoggerFactory.getLogger(FileSystemUploader.class);

    @Value("${volume.path}")
    private String volumePath;

    @Async("taskExecutor")
    @Override
    public void uploadFile(MultipartFile multipartFile) {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        Path path = Paths.get(volumePath.concat(fileName));
        try {
            Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioException) {
            LOG.error("Error when uploading", ioException);
        }
    }
}
