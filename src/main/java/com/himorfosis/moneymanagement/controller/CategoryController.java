package com.himorfosis.moneymanagement.controller;

import com.himorfosis.moneymanagement.entity.CategoryEntity;
import com.himorfosis.moneymanagement.exception.ResourceNotFoundException;
import com.himorfosis.moneymanagement.model.response.StatusResponse;
import com.himorfosis.moneymanagement.repository.CategoryRepository;
import com.himorfosis.moneymanagement.repository.UsersRepository;
import com.himorfosis.moneymanagement.service.ImageStorageService;
import com.himorfosis.moneymanagement.utilities.DateSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ImageStorageService imageStorageService;

    @GetMapping("/")
    public List<CategoryEntity> categoryAll() {
         return categoryRepository.findAll();
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public StatusResponse categoryCreate(
            @RequestPart(value = "title", required = true) @Valid String getTitle,
            @RequestPart(value = "description", required = true) @Valid String getDescription,
            @RequestPart(value = "image", required = false) MultipartFile getImage
    ) {

        StatusResponse status = new StatusResponse();
        CategoryEntity item = new CategoryEntity();

        if (getTitle.isEmpty() || getDescription.isEmpty()) {

            status.setStatus(500);
            status.setMessage("Please complete the data");

        } else {

            if (getImage != null) {

                // update dengan gambar
                String typeFile = imageStorageService.checkTypeFileImage(getImage);

                if (typeFile.equals("jpg") || typeFile.equals("png")) {

                    // save image
                    String fileImageName = imageStorageService.uploadFile(getImage);

                    item.setTitle(getTitle);
                    item.setDescription(getDescription);
                    item.setImage_category(fileImageName);
                    item.setCreated_at(DateSetting.timestamp());
                    item.setUpdated_at(DateSetting.timestamp());

                } else {
                    status.setStatus(415);
                    status.setMessage("Data image must jpg or png");
                }

            } else {

                // set data
                item.setTitle(getTitle);
                item.setDescription(getDescription);
                item.setCreated_at(DateSetting.timestamp());
                item.setUpdated_at(DateSetting.timestamp());

            }

            categoryRepository.save(item);

            status.setStatus(200);
            status.setMessage("Success Create Data");
        }

        return status;
    }

    @PutMapping(value = "update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public StatusResponse categoryUpdate(
            @RequestPart(value = "id", required = true) @Valid String getId,
            @RequestPart(value = "title", required = true) @Valid String getTitle,
            @RequestPart(value = "description", required = true) @Valid String getDescription,
            @RequestPart(value = "image", required = false) MultipartFile getImage) {

        StatusResponse status = new StatusResponse();

        if (getTitle.isEmpty() || getDescription.isEmpty()) {

            status.setStatus(500);
            status.setMessage("Please complete the data");

        } else {

            Long idUser = Long.valueOf(getId);

            CategoryEntity checkCategory = categoryRepository.findById(idUser)
                    .orElseThrow(() -> new ResourceNotFoundException("Users", "id", idUser));

            if (checkCategory != null) {

                CategoryEntity update = new CategoryEntity();

                if (!getImage.isEmpty()) {

                    // update dengan gambar
                    String typeFile = imageStorageService.checkTypeFileImage(getImage);

                    if (typeFile.equals("jpg") || typeFile.equals("png")) {

                        // delete image form directory
                        imageStorageService.deleteImage(checkCategory.getImage_category());
                        // save image
                        String fileImageName = imageStorageService.uploadFile(getImage);

                        update.setId(idUser);
                        update.setTitle(getTitle);
                        update.setDescription(getDescription);

                        update.setImage_category(fileImageName);
                        update.setCreated_at(checkCategory.getCreated_at());
                        update.setUpdated_at(DateSetting.timestamp());

                    } else {

                        status.setStatus(415);
                        status.setMessage("Data image must jpg or png");
                    }

                } else {

                    // update data tanpa gambar
                    update.setId(idUser);
                    update.setTitle(getTitle);
                    update.setDescription(getDescription);

                    update.setImage_category(checkCategory.getImage_category());
                    update.setCreated_at(checkCategory.getCreated_at());
                    update.setUpdated_at(DateSetting.timestamp());

                }

                categoryRepository.save(update);

                status.setStatus(200);
                status.setMessage("Success Update Data");

            } else {

                status.setStatus(500);
                status.setMessage("ID Category not found");
            }
        }

        return status;
    }

}
