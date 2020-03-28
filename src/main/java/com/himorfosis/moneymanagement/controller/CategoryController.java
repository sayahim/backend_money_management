package com.himorfosis.moneymanagement.controller;

import com.himorfosis.moneymanagement.entity.CategoryEntity;
import com.himorfosis.moneymanagement.exception.ResourceNotFoundException;
import com.himorfosis.moneymanagement.model.CategoryModel;
import com.himorfosis.moneymanagement.model.ResponseStatus;
import com.himorfosis.moneymanagement.model.StatusResponse;
import com.himorfosis.moneymanagement.repository.CategoryRepository;
import com.himorfosis.moneymanagement.service.ImageStorageService;
import com.himorfosis.moneymanagement.utilities.DateSetting;
import com.himorfosis.moneymanagement.utilities.encryption.Encryption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    private String TAG = "CategoryController";

    private String TIME_START = " 00:00:00";
    private String TIME_FINISH = " 23:59:59";

    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ImageStorageService imageStorageService;

    @GetMapping("")
    public List<CategoryModel> categoryAll() {

        List<CategoryModel> category = new ArrayList<>();

        List<CategoryEntity> listData = categoryRepository.findAll();
        for (CategoryEntity item : listData) {

            category.add(new CategoryModel(
                    Encryption.setEncrypt(String.valueOf(item.getId())),
                    item.getTitle(),
                    item.getDescription(),
                    item.getType_category(),
                    item.getImage_category(),
                    item.getImage_category_url(),
                    item.getCreated_at(),
                    item.getUpdated_at()
            ));
        }

        return category;
    }

    @PostMapping(value = "/sortByDate", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<CategoryModel> categorySortByDate(
            @RequestPart(value = "date_start", required = true) @Valid String getStart,
            @RequestPart(value = "date_finish", required = true) @Valid String getFinish
    ) {

        List<CategoryModel> category = new ArrayList<>();

        List<CategoryEntity> listData = categoryRepository.sortDateBy(getStart + TIME_START , getFinish + TIME_FINISH);
        for (CategoryEntity item : listData) {

            category.add(new CategoryModel(
                    Encryption.setEncrypt(String.valueOf(item.getId())),
                    item.getTitle(),
                    item.getDescription(),
                    item.getType_category(),
                    item.getImage_category(),
                    item.getImage_category_url(),
                    item.getCreated_at(),
                    item.getUpdated_at()
            ));
        }

        return category;
    }

    @PostMapping(value = "/details", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CategoryModel> categoryDetails(
            @RequestPart(value = "id", required = true) @Valid String getId) {

        String getDescrypt = Encryption.getDecrypt(getId);

        CategoryEntity item = categoryRepository.findById(Long.valueOf(getDescrypt))
                .orElseThrow(() -> new ResourceNotFoundException(getId));

        CategoryModel data = new CategoryModel(
                Encryption.setEncrypt(String.valueOf(item.getId())),
                item.getTitle(),
                item.getDescription(),
                item.getType_category(),
                item.getImage_category(),
                item.getImage_category_url(),
                item.getCreated_at(),
                item.getUpdated_at());

        return new ResponseEntity<CategoryModel>(data, HttpStatus.OK);
    }

    @PostMapping(value = "/encryption", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseStatus dataEncryption(
            @RequestPart(value = "data", required = true) @Valid String getData) {

        String dataEncryption = Encryption.setEncrypt(getData);
        return new ResponseStatus(200, dataEncryption);
    }

    @PostMapping(value = "/decryption", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseStatus dataDecryption(
            @RequestPart(value = "data", required = true) @Valid String getData) {

        String dataDescryption = Encryption.getDecrypt(getData);
        return new ResponseStatus(200, dataDescryption);
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public StatusResponse categoryCreate(
            @RequestPart(value = "image", required = false) MultipartFile getImage,
            @RequestPart(value = "title", required = true) @Valid String getTitle,
            @RequestPart(value = "description", required = true) @Valid String getDescription,
            @RequestPart(value = "user_id", required = true) @Valid String getUserId,
            @RequestPart(value = "type_category", required = true) @Valid String getTypeCategory
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
                    item.setType_category(getTypeCategory);
                    item.setId_user_category(Integer.valueOf(getUserId));
                    item.setImage_category_url(imageStorageService.getLinkimage(fileImageName));
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
                item.setType_category(getTypeCategory);
                item.setId_user_category(Integer.valueOf(getUserId));
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
            @RequestPart(value = "image", required = false) MultipartFile getImage,
            @RequestPart(value = "id", required = true) @Valid String getId,
            @RequestPart(value = "title", required = true) @Valid String getTitle,
            @RequestPart(value = "description", required = true) @Valid String getDescription) {

        StatusResponse status = new StatusResponse();

        if (getTitle.isEmpty() || getDescription.isEmpty()) {

            status.setStatus(500);
            status.setMessage("Please complete the data");

        } else {

            Long idUser = Long.valueOf(getId);

            CategoryEntity checkCategory = categoryRepository.findById(idUser)
                    .orElseThrow(() -> new ResourceNotFoundException(idUser));

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
                        update.setImage_category_url(imageStorageService.getLinkimage(fileImageName));
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
                    update.setImage_category_url(checkCategory.getImage_category_url());
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

    @DeleteMapping(value = "delete/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public StatusResponse delete(@RequestPart(value = "id", required = true) @Valid String getId) {

        System.out.println("category id : " + getId);
        Long id = Long.parseLong(Encryption.getDecrypt(getId));

        CategoryEntity category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        StatusResponse status = new StatusResponse();

        if (getId != null) {

            if (category.getImage_category() != null) {

                // delete file image form directory
                imageStorageService.deleteImage(category.getImage_category());
            }

            categoryRepository.delete(category);

            status.setStatus(200);
            status.setMessage("Success Deteled Data");

        } else {

            // set response callback
            status.setStatus(404);
            status.setMessage("Data not available");
        }

        return status;
    }

    @PostMapping(value = "/generateInputData")
    public StatusResponse generateInputData() {

        StatusResponse status = new StatusResponse();

        for (int i = 0; i < 10; i++) {

            CategoryEntity item = new CategoryEntity();

            // set data
            item.setTitle("title data ke : " + i);
            item.setDescription("deskripsi data ke " + i);
            item.setCreated_at(DateSetting.timestamp());
            item.setUpdated_at(DateSetting.timestamp());

            categoryRepository.save(item);
        }

        status.setStatus(200);
        status.setMessage("Success Create Data");

        return status;
    }

}
