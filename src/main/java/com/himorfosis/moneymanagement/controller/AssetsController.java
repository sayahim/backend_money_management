package com.himorfosis.moneymanagement.controller;

import com.himorfosis.moneymanagement.entity.AssetsCategoryEntity;
import com.himorfosis.moneymanagement.entity.AssetsEntity;
import com.himorfosis.moneymanagement.entity.CategoryEntity;
import com.himorfosis.moneymanagement.exception.AccountUsedException;
import com.himorfosis.moneymanagement.exception.MessageException;
import com.himorfosis.moneymanagement.model.CategoryModel;
import com.himorfosis.moneymanagement.model.StatusResponse;
import com.himorfosis.moneymanagement.model.response.AssetsCategoryResponse;
import com.himorfosis.moneymanagement.model.response.AssetsResponse;
import com.himorfosis.moneymanagement.repository.AssetsCategoryRepository;
import com.himorfosis.moneymanagement.repository.AssetsRepository;
import com.himorfosis.moneymanagement.repository.UsersRepository;
import com.himorfosis.moneymanagement.security.encryption.Encryption;
import com.himorfosis.moneymanagement.service.ImageStorageService;
import com.himorfosis.moneymanagement.utilities.Util;
import org.apache.logging.log4j.status.StatusData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/assets")
public class AssetsController {

    private String TAG = "AssetsController";
    @Autowired
    AssetsRepository assetsRepo;
    @Autowired
    AssetsCategoryRepository assetsCategroyRepo;
    @Autowired
    ImageStorageService imageStorageService;

    @GetMapping("/all")
    public List<AssetsResponse> assetsAll() {

        List<AssetsResponse> assetsResponses = new ArrayList<>();

        List<AssetsCategoryEntity> listCategoryAssets = assetsCategroyRepo.findAll();

        for (AssetsCategoryEntity item : listCategoryAssets) {

            List<AssetsEntity> listAssets = assetsRepo.findAllAssetsCategoryId(item.getId().toString());

            if (!listAssets.isEmpty()) {
                assetsResponses.add(new AssetsResponse(
                        Encryption.setEncrypt(item.getId().toString()),
                        item.getTitle(),
                        listAssets));
            }

        }

        return assetsResponses;
    }

    @GetMapping("/category_assets")
    public List<AssetsCategoryResponse> categoryAssets() {

        List<AssetsCategoryResponse> assetsResponses = new ArrayList<>();
        List<AssetsCategoryEntity> listCategoryAssets = assetsCategroyRepo.findAll();

        for (AssetsCategoryEntity item : listCategoryAssets) {
                assetsResponses.add(new AssetsCategoryResponse(
                        Encryption.setEncrypt(item.getId().toString()),
                        item.getTitle()
                ));
        }

        return assetsResponses;
    }

    @PostMapping(value = "/createAssetCategory", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> createAssestCategory(
            @RequestParam MultiValueMap<String,String> paramMap) {

        String getTitle = paramMap.getFirst("title");

        AssetsCategoryEntity data = new AssetsCategoryEntity();
        data.setTitle(getTitle);

        assetsCategroyRepo.save(data);

        return new ResponseEntity<>("Success", HttpStatus.OK);

    }

    @PostMapping(value = "/createAssets", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity createAssets(
            @RequestPart(value = "image", required = false) MultipartFile getImage,
            @RequestPart(value = "id_assets_category", required = true) @Valid String getIdAssetsCategory) {

        if (getImage == null || getIdAssetsCategory == null) {
            isError("Please Completed Data");
        } else {

            String typeFile = imageStorageService.checkTypeFileImage(getImage);
            if (typeFile.equals("jpg") || typeFile.equals("png")) {

                String fileImageName = imageStorageService.uploadFile(getImage);
                Long id = Long.parseLong(Encryption.getDecrypt(getIdAssetsCategory));
                isLog("id : " + id);

                AssetsEntity data = new AssetsEntity();
                data.setId_assets_category(id);
                data.setImage_assets(fileImageName);
                data.setImage_assets_url(imageStorageService.URL_ASSETS  + fileImageName);

                assetsRepo.save(data);

                return new ResponseEntity<>("Success", HttpStatus.OK);

            } else {
                isError("Data image must jpg or png");
            }

        }

        return null;

    }

    private void isLog(String message) {
        Util.log(TAG, message);
    }

    private void isError(String message) {
        throw new MessageException(message);
    }



}
