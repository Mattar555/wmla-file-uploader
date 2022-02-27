package com.ibm.anz.wmla.sdk.ibmwmla.interfaces;

import org.springframework.web.multipart.MultipartFile;

public interface Uploader {

    void uploadFile(MultipartFile multipartFile);

}
