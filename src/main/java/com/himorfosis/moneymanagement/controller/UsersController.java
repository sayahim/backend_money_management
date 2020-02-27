package com.himorfosis.moneymanagement.controller;

import com.himorfosis.moneymanagement.exception.ResourceNotFoundException;
import com.himorfosis.moneymanagement.entity.UsersEntity;
import com.himorfosis.moneymanagement.model.StatusResponse;
import com.himorfosis.moneymanagement.repository.AuthRepository;
import com.himorfosis.moneymanagement.repository.UsersRepository;
import com.himorfosis.moneymanagement.service.ImageStorageService;
import com.himorfosis.moneymanagement.utilities.DateSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    @Autowired
    UsersRepository usersRepo;
    @Autowired
    ImageStorageService imageStorageService;
    @Autowired
    AuthRepository authRepo;

    @GetMapping("/all")
    public List<UsersEntity> getAllUsers() {
        return usersRepo.findAll();
    }

    @GetMapping("/detail/{id}")
    public UsersEntity getNoteById(@PathVariable(value = "id") Long getId) {
        return usersRepo.findById(getId)
                .orElseThrow(() -> new ResourceNotFoundException("Users", "id", getId));
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

            // set response callback
            status.setStatus(200);
            status.setMessage("Please complete the data");
        }

        return status;
    }

    @PutMapping(value = "update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public StatusResponse updateProfilUser(
            @RequestPart(value = "id", required = true) @Valid String getId,
            @RequestPart(value = "image", required = false) MultipartFile getImage,
            @RequestPart(value = "name", required = true) @Valid String getName) {

        StatusResponse status = new StatusResponse();

        if (getName.isEmpty() || getId.isEmpty() || getImage.isEmpty()) {

            status.setStatus(500);
            status.setMessage("Please complete the data");

        } else {

            Long idUser = Long.valueOf(getId);

            UsersEntity users = usersRepo.findById(idUser)
                    .orElseThrow(() -> new ResourceNotFoundException("Users", "id", idUser));

            if (users != null) {

                UsersEntity update = new UsersEntity();

                if (!getImage.isEmpty()) {

                    // update dengan gambar
                    String typeFile = imageStorageService.checkTypeFileImage(getImage);

                    if (typeFile.equals("jpg") || typeFile.equals("png")) {

                        // delete image form directory
                        imageStorageService.deleteImage(users.getImage());
                        // save image
                        String fileImageName = imageStorageService.uploadFile(getImage);

                        update.setId(idUser);
                        update.setName(getName);
                        update.setImage(fileImageName);

                        // default data
                        update.setEmail(users.getEmail());
                        update.setPassword(users.getPassword());
                        update.setPhone_number(users.getPhone_number());
                        update.setToken(users.getToken());
                        update.setActive(users.getActive());
                        update.setCreated_at(users.getCreated_at());
                        update.setUpdated_at(DateSetting.timestamp());

                        usersRepo.save(update);

                        status.setStatus(200);
                        status.setMessage("Success Update Data");

                    } else {

                        status.setStatus(415);
                        status.setMessage("Data image must jpg or png");
                    }

                } else {

                    // update data tanpa gambar

                    update.setId(idUser);
                    update.setName(getName);
                    update.setImage(users.getImage());

                    // default data
                    update.setEmail(users.getEmail());
                    update.setPassword(users.getPassword());
                    update.setPhone_number(users.getPhone_number());
                    update.setToken(users.getToken());
                    update.setActive(users.getActive());
                    update.setCreated_at(users.getCreated_at());
                    update.setUpdated_at(DateSetting.timestamp());

                    usersRepo.save(update);

                    status.setStatus(200);
                    status.setMessage("Success Update Data");
                }
            } else {

                status.setStatus(500);
                status.setMessage("ID Category not found");
            }
        }

        return status;
    }


}
