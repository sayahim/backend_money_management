package com.himorfosis.moneymanagement.controller;

import com.himorfosis.moneymanagement.entity.UsersEntity;
import com.himorfosis.moneymanagement.model.response.StatusResponse;
import com.himorfosis.moneymanagement.repository.AuthRepository;
import com.himorfosis.moneymanagement.repository.UsersRepository;
import com.himorfosis.moneymanagement.service.ImageStorageService;
import com.himorfosis.moneymanagement.utilities.DateSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class AuthController {

    // update password
    // register
    // login

    @Autowired
    UsersRepository usersRepo;
    @Autowired
    ImageStorageService imageStorageService;
    @Autowired
    AuthRepository authRepo;

    @PostMapping(value = "/register")
    public StatusResponse create(UsersEntity data) {

        StatusResponse status = new StatusResponse();
        UsersEntity item = new UsersEntity();

        UsersEntity validationEmail = authRepo.findByEmail(data.getEmail());

//        UsersModel data = authRepo.findById(data.getEmail())
//                .orElseThrow(() -> new ResourceNotFoundException("Category ", "id", getId));;

        if (validationEmail != null) {

            status.setStatus(404);
            status.setMessage("Email has been used");

        } else {

            if (data != null) {

                if (!data.getName().isEmpty() && !data.getEmail().isEmpty() && !data.getPassword().isEmpty()) {

                    // set data
                    item.setName(data.getName());
                    item.setEmail(data.getEmail());
                    item.setPassword(data.getPassword());
                    item.setCreated_at(DateSetting.timestamp());
                    item.setUpdated_at(DateSetting.timestamp());

                    // save data
                    usersRepo.save(data);

                    // set response callback
                    status.setStatus(200);
                    status.setMessage("Success Create Data");

                } else {

                    status.setStatus(500);
                    status.setMessage("Please complete the data");
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

                UsersEntity item = authRepo.findByEmail(getEmail);

                // set data
//                item.setName(data.getName());
//                item.setEmail(data.getEmail());
//                item.setPassword(data.getPassword());
//                item.setCreated_at(DateSetting.timestamp());
//                item.setUpdated_at(DateSetting.timestamp());

                if (item != null) {

                    if (item.getPassword().equals(getPassword)) {

                        // set response callback
                        status.setStatus(200);
                        status.setMessage("Success Create Note");
                    } else {
                        status.setStatus(404);
                        status.setMessage("Wrong Email Or Password");
                    }

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
