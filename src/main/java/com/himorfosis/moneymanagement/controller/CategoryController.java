package com.himorfosis.moneymanagement.controller;

import com.himorfosis.moneymanagement.entity.CategoryData;
import com.himorfosis.moneymanagement.entity.CategoryEntity;
import com.himorfosis.moneymanagement.exception.*;
import com.himorfosis.moneymanagement.key.DataKey;
import com.himorfosis.moneymanagement.model.CategoryModel;
import com.himorfosis.moneymanagement.model.StatusResponse;
import com.himorfosis.moneymanagement.repository.CategoryRepository;
import com.himorfosis.moneymanagement.security.encryption.UserEncrypt;
import com.himorfosis.moneymanagement.service.ImageStorageService;
import com.himorfosis.moneymanagement.state.MsgState;
import com.himorfosis.moneymanagement.utilities.DateSetting;
import com.himorfosis.moneymanagement.security.encryption.Encryption;
import com.himorfosis.moneymanagement.utilities.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/category/")
public class CategoryController {

    private String TAG = "CategoryController";

    private String TIME_START = " 00:00:00";
    private String TIME_FINISH = " 23:59:59";
    private String SPEND_TYPE = "spend";
    private String INCOME_TYPE = "income";

    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ImageStorageService imageStorageService;

    @GetMapping("/all")
    public List<CategoryModel> categoryAll() {

        List<CategoryModel> category = new ArrayList<>();

        List<CategoryEntity> listData = categoryRepository.findAll();
        for (CategoryEntity item : listData) {

            category.add(new CategoryModel(
                    Encryption.setEncrypt(String.valueOf(item.getId())),
                    item.getTitle(),
                    item.getType_category(),
                    item.getImage_category(),
                    item.getImage_category_url(),
                    item.getCreated_at(),
                    item.getUpdated_at()
            ));
        }

        return category;
    }

    @PostMapping(value = "/type_finance", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public List<CategoryModel> getCategorySpending(
            @RequestParam MultiValueMap<String,String> paramMap) {

        String getTypeFinance = paramMap.getFirst("type");

        List<CategoryModel> category = new ArrayList<>();

        if (getTypeFinance.equals(DataKey.Spend) || getTypeFinance.equals(DataKey.Income)) {

            List<CategoryEntity> listData = categoryRepository.findByCategoryTypeFinance(getTypeFinance);
            for (CategoryEntity item : listData) {

                isLog("image url : " + item.getImage_category_url());
                InputStream stream = getClass().getResourceAsStream("/com/baeldung/produceimage/image.jpg");
                category.add(new CategoryModel(
                        Encryption.setEncrypt(String.valueOf(item.getId())),
                        item.getTitle(),
                        item.getType_category(),
                        item.getImage_category(),
                        item.getImage_category_url(),
                        item.getCreated_at(),
                        item.getUpdated_at()
                ));
            }

        } else {
            isError("Wrong Type Finance");
        }

        return category;
    }

    @PostMapping(value = "/sortByDate", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public List<CategoryModel> categorySortByDate(
            @RequestParam MultiValueMap<String,String> paramMap
    ) {

        String getStart = paramMap.getFirst("date_start");
        String getFinish = paramMap.getFirst("date_end");

        List<CategoryModel> category = new ArrayList<>();

        List<CategoryEntity> listData = categoryRepository.sortDateBy(getStart + TIME_START , getFinish + TIME_FINISH);
        for (CategoryEntity item : listData) {

            category.add(new CategoryModel(
                    Encryption.setEncrypt(String.valueOf(item.getId())),
                    item.getTitle(),
                    item.getType_category(),
                    item.getImage_category(),
                    item.getImage_category_url(),
                    item.getCreated_at(),
                    item.getUpdated_at()
            ));
        }

        return category;
    }

    @PostMapping(value = "/details", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<CategoryModel> categoryDetails(
            @RequestParam MultiValueMap<String,String> paramMap) {

        String getId = paramMap.getFirst("id");
        String getDescrypt = Encryption.getDecrypt(getId);

        CategoryEntity item = categoryRepository.findById(Long.valueOf(getDescrypt))
                .orElseThrow(() -> new DataNotFoundException(getId));

        CategoryModel data = new CategoryModel(
                Encryption.setEncrypt(String.valueOf(item.getId())),
                item.getTitle(),
                item.getType_category(),
                item.getImage_category(),
                item.getImage_category_url(),
                item.getCreated_at(),
                item.getUpdated_at());

        return new ResponseEntity<CategoryModel>(data, HttpStatus.OK);
    }

    @PostMapping(value = "/userCreate", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<?> categoryUserCreate(
            @RequestParam MultiValueMap<String,String> paramMap) throws Exception {

        String assets = paramMap.getFirst("assets");
        String title = paramMap.getFirst("title");
        String user_id = paramMap.getFirst("user_id");
        String type_category = paramMap.getFirst("type_category");
        String getUserId = UserEncrypt.generateDecrypt(user_id);

        StatusResponse status = new StatusResponse();
        CategoryEntity item = new CategoryEntity();

        if (title.isEmpty()) {
            isDataNotComplete();
        } else {

            item.setTitle(title);
            item.setImage_category(assets);
            item.setImage_category_url(imageStorageService.URL_ASSETS + assets);
            item.setType_category(type_category);
            item.setId_user_category(Integer.valueOf(getUserId));
            item.setCreated_at(DateSetting.timestamp());
            item.setUpdated_at(DateSetting.timestamp());
            categoryRepository.save(item);

        }

        status.setStatus(200);
        status.setMessage("Success Create Data");

        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    @PostMapping(value = "/userUpdate", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<?> categoryUserUpdate(
            @RequestParam MultiValueMap<String,String> paramMap) throws Exception {

        String assets = paramMap.getFirst("assets");
        String title = paramMap.getFirst("title");
        String description = paramMap.getFirst("description");
        String user_id = paramMap.getFirst("user_id");
        String type_category = paramMap.getFirst("type_category");
        String getUserId = UserEncrypt.generateDecrypt(user_id);

        StatusResponse status = new StatusResponse();
        CategoryEntity item = new CategoryEntity();

        if (title.isEmpty() || description.isEmpty()) {
            isDataNotComplete();
        } else {

            item.setId(Long.parseLong(user_id));
            item.setTitle(title);
            item.setImage_category(assets);
            item.setImage_category_url(imageStorageService.URL_ASSETS + assets);
            item.setType_category(type_category);
            item.setId_user_category(Integer.valueOf(getUserId));
            item.setCreated_at(DateSetting.timestamp());
            item.setUpdated_at(DateSetting.timestamp());
            categoryRepository.save(item);

        }

        status.setStatus(200);
        status.setMessage("Success Update Data");

        return new ResponseEntity<>(status, HttpStatus.OK);
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
            isDataNotComplete();
        } else {

            if (getImage != null) {

                // update dengan gambar
                String typeFile = imageStorageService.checkTypeFileImage(getImage);

                if (typeFile.equals("jpg") || typeFile.equals("png")) {

                    // save image
                    String fileImageName = imageStorageService.uploadFile(getImage);

                    item.setTitle(getTitle);
                    item.setImage_category(fileImageName);
                    item.setType_category(getTypeCategory);
                    item.setId_user_category(Integer.valueOf(getUserId));
                    item.setImage_category_url(imageStorageService.URL_ASSETS + fileImageName);
                    item.setCreated_at(DateSetting.timestamp());
                    item.setUpdated_at(DateSetting.timestamp());

                } else {
                    isUnsupportMediaType();
                }

            } else {

                // set data
                item.setTitle(getTitle);
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

    @PutMapping(value = "update", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public StatusResponse categoryUpdate(
            @RequestParam MultiValueMap<String,String> paramMap,
            @RequestParam MultiValueMap<String, MultipartFile> paramFile) {

        String getTitle = paramMap.getFirst("title");
        String getDescription = paramMap.getFirst("description");
        String getId = paramMap.getFirst("id");
        MultipartFile getImage = paramFile.getFirst("image");

        StatusResponse status = new StatusResponse();

        if (getTitle.isEmpty() || getDescription.isEmpty()) {
            isDataNotComplete();
        } else {

            Long idUser = Long.valueOf(getId);

            CategoryEntity checkCategory = categoryRepository.findById(idUser)
                    .orElseThrow(() -> new DataNotFoundException(idUser));

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

                        update.setImage_category(fileImageName);
                        update.setImage_category_url(imageStorageService.URL_ASSETS + fileImageName);
                        update.setCreated_at(checkCategory.getCreated_at());
                        update.setUpdated_at(DateSetting.timestamp());
                    } else {
                        isUnsupportMediaType();
                    }

                } else {

                    // update data tanpa gambar
                    update.setId(idUser);
                    update.setTitle(getTitle);

                    update.setImage_category(checkCategory.getImage_category());
                    update.setImage_category_url(checkCategory.getImage_category_url());
                    update.setCreated_at(checkCategory.getCreated_at());
                    update.setUpdated_at(DateSetting.timestamp());
                }

                categoryRepository.save(update);

                status.setStatus(200);
                status.setMessage("Success Update Data");
            } else {

                isError("ID Category not found");
//
//                status.setStatus(500);
//                status.setMessage("ID Category not found");
            }
        }

        return status;
    }

    @DeleteMapping(value = "delete", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public StatusResponse delete(@RequestPart(value = "id", required = true) @Valid String getId) {

        System.out.println("category id : " + getId);
        Long id = Long.parseLong(Encryption.getDecrypt(getId));

        CategoryEntity category = categoryRepository.findById(id).orElseThrow(() -> new DataNotFoundException(id));
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
            isError("Data not available");
        }

        return status;
    }

    @PostMapping(value = "createPrivateAdm", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public StatusResponse createPrivateAdm(
            @RequestPart(value = "pass", required = true) @Valid String getPass)  {

        StatusResponse response = new StatusResponse();

        if (getPass.equals("$idehim")) {
            for (CategoryData data :listDataCreateCategory()) {

                CategoryEntity item = new CategoryEntity();

                item.setTitle(data.getName());
                item.setImage_category(data.getImage());
                item.setType_category(data.getType());
                item.setId_user_category(0);
                item.setCreated_at(DateSetting.timestamp());
                item.setUpdated_at(DateSetting.timestamp());

                categoryRepository.save(item);
            }
        } else {
            isError(MsgState.FAILED);
        }

        return response;
    }

    private List<CategoryData> listDataCreateCategory() {

        List<CategoryData> item = new ArrayList<>();

        item.add(new CategoryData("Hiburan", "ic_accoustic_guitar", SPEND_TYPE));
        item.add(new CategoryData("Penginapan", "ic_bed", SPEND_TYPE));
        item.add(new CategoryData("Belanja", "ic_belanja", SPEND_TYPE));
        item.add(new CategoryData("Kendaraan", "ic_car", SPEND_TYPE));
        item.add(new CategoryData("Peliharaan", "ic_cat", SPEND_TYPE));
        item.add(new CategoryData("Kopi", "ic_coffee", SPEND_TYPE));
        item.add(new CategoryData("Makanan", "ic_eat", SPEND_TYPE));
        item.add(new CategoryData("Keluarga", "ic_family", SPEND_TYPE));
        item.add(new CategoryData("Hiburan", "ic_game", SPEND_TYPE));
        item.add(new CategoryData("Pakaian", "ic_hanger", SPEND_TYPE));
        item.add(new CategoryData("Rumah", "ic_house", SPEND_TYPE));
        item.add(new CategoryData("Elektronik", "ic_laptop", SPEND_TYPE));
        item.add(new CategoryData("Kesehatan", "ic_medical", SPEND_TYPE));
        item.add(new CategoryData("Minuman", "ic_milkshake", SPEND_TYPE));
        item.add(new CategoryData("Buku", "ic_open_book", SPEND_TYPE));
        item.add(new CategoryData("Sedekah", "ic_payment", SPEND_TYPE));
        item.add(new CategoryData("Tanaman", "ic_plant", SPEND_TYPE));
        item.add(new CategoryData("Motor", "ic_scooter", SPEND_TYPE));
        item.add(new CategoryData("Belanja", "ic_shopping_bag", SPEND_TYPE));
        item.add(new CategoryData("Furnitur", "ic_sofa", SPEND_TYPE));
        item.add(new CategoryData("Persewaan", "ic_store", SPEND_TYPE));
        item.add(new CategoryData("Pajak", "ic_tax", SPEND_TYPE));
        item.add(new CategoryData("Tiket", "ic_ticket", SPEND_TYPE));
        item.add(new CategoryData("Pendidikan", "ic_toga", SPEND_TYPE));
        item.add(new CategoryData("Peralatan", "ic_utilities", SPEND_TYPE));

        item.add(new CategoryData("Gaji", "ic_money", INCOME_TYPE));
        item.add(new CategoryData("Penjualan", "ic_payment", INCOME_TYPE));
        item.add(new CategoryData("Persewaan", "ic_store", INCOME_TYPE));
        item.add(new CategoryData("Hadiah", "ic_trophy", INCOME_TYPE));


        return item;
    }

    private void isLog(String message) {
        Util.log(TAG, message);
    }

    private void isError(String message) {
        throw new MessageException(message);
    }
    private void isUnsupportMediaType() {
        throw new UnsupportedMediaTypeException();
    }
    private void isDataNotComplete() { throw new DataNotCompleteException(); }


}
