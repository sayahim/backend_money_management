package com.himorfosis.moneymanagement.controller;

import com.himorfosis.moneymanagement.exception.BadRequestMessageException;
import com.himorfosis.moneymanagement.exception.DataNotCompleteException;
import com.himorfosis.moneymanagement.exception.DataNotFoundException;
import com.himorfosis.moneymanagement.entity.UsersEntity;
import com.himorfosis.moneymanagement.exception.UnsupportedMediaTypeException;
import com.himorfosis.moneymanagement.model.StatusResponse;
import com.himorfosis.moneymanagement.model.UserResponse;
import com.himorfosis.moneymanagement.repository.AuthRepository;
import com.himorfosis.moneymanagement.repository.UsersRepository;
import com.himorfosis.moneymanagement.security.encryption.UserEncrypt;
import com.himorfosis.moneymanagement.service.ImageStorageService;
import com.himorfosis.moneymanagement.service.ProfileStorageService;
import com.himorfosis.moneymanagement.utilities.DateSetting;
import com.himorfosis.moneymanagement.utilities.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    private String TAG = "UsersController";

    @Autowired
    UsersRepository usersRepo;
    @Autowired
    ProfileStorageService profileStorageService;
    @Autowired
    AuthRepository authRepo;

    @GetMapping("/all")
    public List<UserResponse> getAllUsers() {

        List<UserResponse> userData = new ArrayList<>();

        List<UsersEntity> listData = usersRepo.findAll();
        for (UsersEntity item : listData) {

            userData.add(new UserResponse(
                    UserEncrypt.generateEncrypt(String.valueOf(item.getId())),
                    item.getName(),
                    item.getEmail(),
                    item.getPhone_number(),
                    item.getBorn(),
                    item.getGender(),
                    item.getImage_url(),
                    item.getImage(),
                    item.getToken(),
                    item.getActive(),
                    item.getCreated_at(),
                    item.getUpdated_at()
            ));
        }

        return userData;
    }

    @PostMapping(value = "/details", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UserResponse getDetailUsers(
            @RequestPart(value = "id", required = true) @Valid String getId) {

        String decryptId = UserEncrypt.generateDecrypt(getId);
        Util.log(TAG, decryptId);

        UsersEntity item = usersRepo.findById(Long.valueOf(decryptId))
                .orElseThrow(() -> new DataNotFoundException(decryptId));

        UserResponse data = new UserResponse(
                UserEncrypt.generateDecrypt(String.valueOf(item.getId())),
                item.getName(),
                item.getEmail(),
                item.getPhone_number(),
                item.getBorn(),
                item.getGender(),
                item.getImage_url(),
                item.getImage(),
                item.getToken(),
                item.getActive(),
                item.getCreated_at(),
                item.getUpdated_at());

        return data;

    }

    @PostMapping(value = "/create")
    public StatusResponse create(UsersEntity data) {

        StatusResponse status = new StatusResponse();
        UsersEntity item = new UsersEntity();

        if (data != null) {

            // set data
            item.setName(data.getName());
            item.setEmail(data.getEmail());
            item.setPassword(data.getPassword());
            item.setPhone_number(data.getPhone_number());
            item.setCreated_at(DateSetting.timestamp());
            item.setUpdated_at(DateSetting.timestamp());

            // save data
            usersRepo.save(data);

            // set response callback
            status.setStatus(200);
            status.setMessage("Success Create Data");
        } else {
            isDataNotCompleted();
        }

        return status;
    }

    @PostMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProfilUser(
            @RequestPart(value = "image", required = false) MultipartFile getImage,
            @RequestPart(value = "id", required = true) @Valid String getId,
            @RequestPart(value = "name", required = true) @Valid String getName,
            @RequestPart(value = "born", required = true) @Valid String getBorn,
            @RequestPart(value = "gender", required = true) @Valid String getGender,
            @RequestPart(value = "phone", required = true) @Valid String getPhone
            ) {

        isLog("getId : " + getId);
        isLog("getName :" + getName);
        isLog("getBorn : " + getBorn);
        isLog("getGender : " + getGender);
        isLog("getPhone : " + getPhone);
        isLog("getImage : " + getImage);

        if (getName.isEmpty() || getId.isEmpty() || getGender.isEmpty() || getBorn.isEmpty() || getPhone.isEmpty()) {
            isDataNotCompleted();
        } else if(!getGender.equals("M") && !getGender.equals("F")) {
            isBadRequestMessageException("Wrong type data Gender");
        } else {

            String decryptId = UserEncrypt.generateDecrypt(getId);
            isLog("decryptId : " + decryptId);
            Long idUser = (Long.valueOf(decryptId));
            UsersEntity users = usersRepo.findById(idUser)
                    .orElseThrow(() -> new DataNotFoundException(getId));

                if (users == null) {
                    isBadRequestMessageException("Wrong ID Category");
                } else {
                    UsersEntity update = new UsersEntity();
                    if (getImage != null) {
                        isLog("with image");

                        // update dengan gambar
                        String typeFile = profileStorageService.checkTypeFileImage(getImage);
                        isLog("type file : " + typeFile);

                        if (!typeFile.equals("jpg") && !typeFile.equals("png")) {
                            isUnsupportMediaType();
                        } else {

                            // delete image form directory
                            if (users.getImage() != null) {
                                isLog("delete image");
                                profileStorageService.deleteImage(users.getImage());
                            }
                            // save image
                            String fileImageName = profileStorageService.uploadFile(getImage);
                            isLog("save  image");
                            isLog("file image : " + fileImageName);
                            isLog("file image url : " + profileStorageService.URL_ASSETS + fileImageName);

                            update.setId(idUser);
                            update.setName(getName);
                            update.setImage(fileImageName);
                            update.setImage_url(profileStorageService.URL_ASSETS + fileImageName);
                            update.setPhone_number(getPhone);

                            // default data
                            update.setEmail(users.getEmail());
                            update.setPassword(users.getPassword());
                            update.setToken(users.getToken());
                            update.setBorn(DateSetting.convertStringToDateSql(getBorn));
                            update.setGender(getGender);
                            update.setActive(users.getActive());
                            update.setCreated_at(users.getCreated_at());
                            update.setUpdated_at(DateSetting.timestamp());

                            usersRepo.save(update);
                        }

                    } else {

                        isLog("without image");

                        // update data tanpa gambar
                        update.setId(idUser);
                        update.setName(getName);
                        update.setImage(users.getImage());
                        update.setImage_url(users.getImage_url());
                        update.setPhone_number(getPhone);

                        // default data
                        update.setEmail(users.getEmail());
                        update.setPassword(users.getPassword());
                        update.setBorn(DateSetting.convertStringToDateSql(getBorn));
                        update.setGender(getGender);
                        update.setToken(users.getToken());
                        update.setActive(users.getActive());
                        update.setCreated_at(users.getCreated_at());
                        update.setUpdated_at(DateSetting.timestamp());

                        usersRepo.save(update);
                    }

                    return new ResponseEntity<UsersEntity>(update, HttpStatus.OK);

                }
        }

        return null;

    }

    private void isLog(String msg) {
        Util.log(TAG, msg);
    }

    private void isDataNotCompleted() {
         throw new DataNotCompleteException();
    }

    private void isUnsupportMediaType() {
        throw new UnsupportedMediaTypeException();
    }

    private void isBadRequestMessageException(String msg) {
        throw new BadRequestMessageException(msg);
    }

}
