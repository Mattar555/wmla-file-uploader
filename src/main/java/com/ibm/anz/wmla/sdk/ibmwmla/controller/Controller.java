package com.ibm.anz.wmla.sdk.ibmwmla.controller;


import com.ibm.anz.wmla.sdk.ibmwmla.service.RedisService;
import com.ibm.anz.wmla.sdk.ibmwmla.service.FileSystemUploader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
public class Controller {

    private final Logger LOG = LoggerFactory.getLogger(Controller.class);

    @Autowired
    private FileSystemUploader fileSystemUploader;

    @Autowired
    private RedisService redisService;

    @PostMapping("/api/upload")
    public ResponseEntity<Object> upload(@RequestParam("file") final MultipartFile multipartFile) {
        if (multipartFile != null && !multipartFile.isEmpty()) {
            UUID uuid = UUID.randomUUID();
            fileSystemUploader.uploadFile(multipartFile);
            String identifier = redisService.populateEntry(
                    uuid,
                    multipartFile.getOriginalFilename(),
                    multipartFile.getSize()
            );
            return new ResponseEntity<>(identifier, HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/api/uploadStatus")
    public ResponseEntity<Object> fetchStatus(@RequestParam(name = "identifier") String identifier) {
        String status = redisService.fetchStatus(identifier);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }
}