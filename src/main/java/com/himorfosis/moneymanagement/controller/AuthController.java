package com.himorfosis.moneymanagement.controller;

import com.himorfosis.moneymanagement.entity.UsersEntity;
import com.himorfosis.moneymanagement.model.StatusResponse;
import com.himorfosis.moneymanagement.repository.AuthRepository;
import com.himorfosis.moneymanagement.repository.UsersRepository;
import com.himorfosis.moneymanagement.service.ImageStorageService;
import com.himorfosis.moneymanagement.utilities.DateSetting;
import com.himorfosis.moneymanagement.utilities.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AuthController {

    // update password
    // register
    // login

    private String TAG = "AuthController";

    @Autowired
    UsersRepository usersRepo;
    @Autowired
    ImageStorageService imageStorageService;
    @Autowired
    AuthRepository authRepo;

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public StatusResponse create(
            @RequestPart(value = "name", required = true) @Valid String getName,
            @RequestPart(value = "email", required = true) @Valid String getEmail,
            @RequestPart(value = "password", required = true) @Valid String getPassword,
            @RequestPart(value = "password_confirm", required = true) @Valid String getPasswordConfirm
    ) {

        StatusResponse status = new StatusResponse();
        UsersEntity item = new UsersEntity();

        Boolean statusEmail;

        try {

            List<UsersEntity> validationEmail = authRepo.checkEmailUser(getEmail);

            Util.log(TAG, "validationEmail : " + validationEmail);

            if (!validationEmail.isEmpty()) {

                statusEmail = true;
                status.setStatus(404);
                status.setMessage("Email has been used");
            } else {
                statusEmail = false;
            }

        } catch (Exception e) {
            Util.log(TAG, "Exception : " + e.getMessage());
            statusEmail = false;
        }

        Util.log(TAG, "status : " + statusEmail);

        if (statusEmail == true) {

            status.setStatus(404);
            status.setMessage("Email has been used");

        } else {

            if (!getName.isEmpty() && !getEmail.isEmpty() && !getPassword.isEmpty() && !getPasswordConfirm.isEmpty()) {

                //check password
                if (getPassword.equals(getPasswordConfirm)) {

                    // set data
                    item.setName(getName);
                    item.setEmail(getEmail);
                    item.setPassword(getPassword);
                    item.setCreated_at(DateSetting.timestamp());
                    item.setUpdated_at(DateSetting.timestamp());

                    // save data
                    usersRepo.save(item);

                    // set response callback
                    status.setStatus(200);
                    status.setMessage("Register Success");
                } else {

                    // set response callback
                    status.setStatus(400);
                    status.setMessage("Password Tidak Sesuai");
                }

            } else {

                status.setStatus(500);
                status.setMessage("Please complete the data");
            }

        }

        return status;
    }

    @PostMapping(value = "/login", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public StatusResponse login(
            @RequestPart(value = "email", required = true) @Valid String getEmail,
            @RequestPart(value = "password", required = true) @Valid String getPassword
    ) {

        StatusResponse status = new StatusResponse();

        if (!getEmail.isEmpty() && !getPassword.isEmpty()) {

            List<UsersEntity> item = authRepo.checkEmailUser(getEmail);

            // set data
//                item.setName(data.getName());
//                item.setEmail(data.getEmail());
//                item.setPassword(data.getPassword());
//                item.setCreated_at(DateSetting.timestamp());
//                item.setUpdated_at(DateSetting.timestamp());

            if (!item.isEmpty()) {

//                if (item.getPassword().equals(getPassword)) {
//
//                    // set response callback
//                    status.setStatus(200);
//                    status.setMessage("Success Create Note");
//                } else {
//                    status.setStatus(404);
//                    status.setMessage("Wrong Email Or Password");
//                }

            } else {

                status.setStatus(404);
                status.setMessage("Wrong Email Or Password");
            }

        } else {

            status.setStatus(500);
            status.setMessage("Please complete the data");
        }

        return status;
    }

}
