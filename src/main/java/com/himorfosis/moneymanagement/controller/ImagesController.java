package com.himorfosis.moneymanagement.controller;

import com.himorfosis.moneymanagement.exception.DataNotAvailableException;
import com.himorfosis.moneymanagement.exception.FileNotFoundException;
import com.himorfosis.moneymanagement.service.ImageStorageService;
import com.himorfosis.moneymanagement.utilities.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ImagesController {

    @Autowired
    ImageStorageService service;
    @Autowired
    ImageStorageService imageStorageService;

    @GetMapping("/images/assets")
    @ResponseBody
    public ResponseEntity<byte[]> getImagesCategory(@RequestParam String name) throws IOException {

        ClassPathResource imgFile = new ClassPathResource("images/assets/" + name);
        byte[] dataImage = StreamUtils.copyToByteArray(imgFile.getInputStream());
        String typeFile = name.substring(name.length() -3, name.length());

        if (typeFile.equals("jpg")) {
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(dataImage);
        } else if (typeFile.equals("png")) {
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(dataImage);
        } else {
            new DataNotAvailableException();
        }

        return null;
    }

    @GetMapping("/images")
    @ResponseBody
    public ResponseEntity<byte[]> getImage(@RequestParam String name) throws IOException {

        try {
            ClassPathResource imgFile = new ClassPathResource("images/" + name);
            byte[] dataImage = StreamUtils.copyToByteArray(imgFile.getInputStream());
            String typeFile = name.substring(name.length() -3, name.length());
            if (typeFile.equals("jpg")) {
                return ResponseEntity
                        .ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(dataImage);
            } else if (typeFile.equals("png")) {
                return ResponseEntity
                        .ok()
                        .contentType(MediaType.IMAGE_PNG)
                        .body(dataImage);
            } else {
                new DataNotAvailableException();
            }

        } catch (IOException e) {
            throw new FileNotFoundException();
        }
        return null;
    }

    private void isLog(String message) {
        Util.log("File Controller", message);
    }
    private void isBadRequest() {

    }

}
