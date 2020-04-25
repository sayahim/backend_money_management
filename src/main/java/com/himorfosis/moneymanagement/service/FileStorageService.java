package com.himorfosis.moneymanagement.service;



import com.himorfosis.moneymanagement.exception.FileNotFoundException;
import com.himorfosis.moneymanagement.exception.FileStorageException;
import com.himorfosis.moneymanagement.property.ImageStorageProperties;
import com.himorfosis.moneymanagement.utilities.DateSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    public String checkTypeFileImage(MultipartFile file) {

        String fileOriginalName = StringUtils.cleanPath(file.getOriginalFilename());
        return getFileExtension(fileOriginalName);

    }

    public String getFileExtension(String fullName) {

        int dotIndex;
        String fileName;

        if (fullName != null) {
            fileName = new File(fullName).getName();
            dotIndex = fileName.lastIndexOf('.');

        } else {
            return null;
        }

        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }

    @Autowired
    public FileStorageService(ImageStorageProperties imageStorageProperties) {
        this.fileStorageLocation = Paths.get(imageStorageProperties.getUploadImage())
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String storeImage(MultipartFile file) {

        // Normalize file name
        String fileType = StringUtils.cleanPath(file.getOriginalFilename());
        String fileName = "IMG_" + DateSetting.generateNameByDateTime() + "." + getFileExtension(fileType);

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;

        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }


    public String storeFile(MultipartFile file) {
        // Normalize file name
//        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileName = "IMG_" + DateSetting.generateNameByDateTime();

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                isFileNotFound();
            }
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException("File not found " + fileName, ex);
        }

        return null;
    }

    public Resource deleteFileAsResoruce(String fileName) {

        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {

//                File file = new File(resource.get.toString());
//
//                if(filePath.delete()){
//                    System.out.println("File deleted successfully");
//                }else{
//                    System.out.println("Fail to delete file");
//                }

                return resource;
            } else {
                isFileNotFound();
            }
        } catch (MalformedURLException ex) {
            isFileNotFound();
        }

        return null;
    }

    private void isFileNotFound() {
        throw new FileNotFoundException();
    }

}
