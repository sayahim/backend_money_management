package com.himorfosis.moneymanagement.service;

import com.himorfosis.moneymanagement.exception.FileNotFoundException;
import com.himorfosis.moneymanagement.exception.FileStorageException;
import com.himorfosis.moneymanagement.property.ImageStorageProperties;
import com.himorfosis.moneymanagement.utilities.DateSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
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
public class ImageStorageService {

    private final Path imageStorageLocation;

    public static String IMAGE_DIR = "/resources/images/";
    public static String URL_ASSETS = "http://192.190.0.101:8080/api/images/assets/?name=";

    @Autowired
    public ImageStorageService(ImageStorageProperties imageStorageProperties) {
        this.imageStorageLocation = Paths.get(imageStorageProperties.getUploadImageAssets())
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.imageStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

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


    public String uploadFile(MultipartFile file) {

        // set image name
        String originalNameImage = StringUtils.cleanPath(file.getOriginalFilename());
        String imageTypeFile = getFileExtension(originalNameImage);

        String fileName = "IMG_" + DateSetting.generateNameByDateTime() + "." + imageTypeFile;

        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.imageStorageLocation.resolve(fileName);

            // save file in directory
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;

        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }

    }

    public byte[] loadImage(String directoryFileName) {
        try {
            Path filePath = this.imageStorageLocation.resolve(directoryFileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                ClassPathResource imgFile = new ClassPathResource(directoryFileName);
                byte[] bytes = StreamUtils.copyToByteArray(imgFile.getInputStream());
                return bytes;
            } else {
                isFileNotFound();
            }
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException("Image not found " + directoryFileName, ex);
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileNotFoundException("Image not found " + directoryFileName, e);
        }
        return null;
    }

    public String deleteImage(String imageName) {

        String status = "";

        File file = new File(getLinkimage(imageName));

        if (file.exists()) {
            if (file.delete()) {
                status = "success";
                System.out.println("Image deleted successfully");
            } else {
                status = "failed";
                System.out.println("Image to delete file");
            }
        } else {
            status = "failed";
            System.out.println("Image not found");
        }
        return status;
    }

    public String storeFile(MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.imageStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.imageStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return resource;
            } else {
                isFileNotFound();
            }
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException("File not found " + fileName, ex);
        }
        return null;
    }

    public Path getDirectoryImage(String fileName) {
        try {
            Path filePath = this.imageStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return filePath;
            } else {
                isFileNotFound();
            }
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException("File not found " + fileName, ex);
        }
        return null;
    }

    public String getLinkimage(String imageName) {

        try {
            Path filePath = this.imageStorageLocation.resolve(imageName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return filePath.toString();
            } else {
//                throw new FileNotFoundException("File not found " + imageName);
                return "image not found";
            }
        } catch (MalformedURLException ex) {
//            throw new FileNotFoundException("File not found " + fileName, ex);
            return "image not found";
        }

    }



    private void isFileNotFound() {
        throw new FileNotFoundException();
    }

}
