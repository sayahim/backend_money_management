package com.himorfosis.moneymanagement.controller;

import com.himorfosis.moneymanagement.entity.CategoryEntity;
import com.himorfosis.moneymanagement.entity.FinancialEntity;
import com.himorfosis.moneymanagement.entity.UsersEntity;
import com.himorfosis.moneymanagement.exception.MessageException;
import com.himorfosis.moneymanagement.exception.ResourceNotCompletedException;
import com.himorfosis.moneymanagement.exception.ResourceNotFoundException;
import com.himorfosis.moneymanagement.model.FinancialsModel;
import com.himorfosis.moneymanagement.model.StatusResponse;
import com.himorfosis.moneymanagement.repository.CategoryRepository;
import com.himorfosis.moneymanagement.repository.FinancialsRepository;
import com.himorfosis.moneymanagement.repository.UsersRepository;
import com.himorfosis.moneymanagement.utilities.DateSetting;
import com.himorfosis.moneymanagement.security.encryption.Encryption;
import com.himorfosis.moneymanagement.utilities.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/financials")
public class FinancialController {

    private String TAG = "FinancialController";

    @Autowired
    FinancialsRepository financialsRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    UsersRepository usersRepository;

    @GetMapping("/")
    public List<FinancialsModel> getAllFinancials() {

        List<FinancialEntity> dataFinancials = financialsRepository.findAll();
        List<FinancialsModel> listData = new ArrayList<>();

        if (dataFinancials == null) {

            new MessageException("Data Tidak Tersedia");

        } else {

            for (FinancialEntity item : dataFinancials) {

                listData.add(new FinancialsModel(
                        Encryption.setEncrypt(String.valueOf(item.getId())),
                        Encryption.setEncrypt(String.valueOf(item.getId_category())),
                        Encryption.setEncrypt(String.valueOf(item.getId_user())),
                        item.getCode(),
                        item.getType_financial(),
                        item.getNominal(),
                        item.getNote(),
                        item.getCreated_at(),
                        item.getCreated_at())
                );
            }
        }

        return listData;
    }

    @PostMapping(value = "/findAllFinancialUsers", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<FinancialsModel> findAllFinancialUsers(
            @RequestPart(value = "user_id", required = true) @Valid String getUserId) {

        List<FinancialsModel> listFinancials = new ArrayList<>();

        if (getUserId == null) {

            new ResourceNotCompletedException(500, "Please complete the data");
        } else {

            String idDecrypt = Encryption.getDecrypt(getUserId);

            Util.log(TAG, "id : " + idDecrypt);

            List<FinancialEntity> financialsData = financialsRepository.findAllFinancialUsers(idDecrypt);

            for (FinancialEntity item : financialsData) {

                listFinancials.add(new FinancialsModel(
                        Encryption.setEncrypt(String.valueOf(item.getId())),
                        Encryption.setEncrypt(String.valueOf(item.getId_category())),
                        Encryption.setEncrypt(String.valueOf(item.getId_user())),
                        item.getCode(),
                        item.getType_financial(),
                        item.getNominal(),
                        item.getNote(),
                        item.getCreated_at(),
                        item.getCreated_at()
                        )
                );

            }

        }

        return listFinancials;
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FinancialEntity> financialsCreate(
            @RequestPart(value = "note", required = false) String getNote,
            @RequestPart(value = "id_category", required = true) String getIdCategory,
            @RequestPart(value = "id_user", required = true) String getIdUser,
            @RequestPart(value = "type_financial", required = true) String getTypeFinancials,
            @RequestPart(value = "nominal", required = true) String getNominal
            ) {

        FinancialEntity create = new FinancialEntity();

        if (getIdUser == null || getNominal == null || getIdCategory == null || getTypeFinancials == null ) {

            return new ResponseEntity<>(create, HttpStatus.BAD_REQUEST);
        } else {

            Long idCategory = Long.valueOf(getIdCategory);
            Long idUser = Long.valueOf(getIdUser);
            Long nominal = Long.valueOf(getNominal);

            UsersEntity userCheck = usersRepository.findId(idCategory);

            CategoryEntity categoryCheck = categoryRepository.findById(idUser)
                    .orElseThrow(() -> new ResourceNotFoundException(getIdCategory));

            if (getTypeFinancials.equals("spend") || getTypeFinancials.equals("income")) {

                if (userCheck != null && categoryCheck != null) {

                    // set data
                    create.setId_category(idCategory);
                    create.setId_user(idUser);
                    create.setNominal(nominal);
                    create.setNote(getNote);
                    create.setType_financial(getTypeFinancials);

                    create.setCode("FIN_" + DateSetting.generateNameByDateTime() + getIdCategory + getIdUser);
                    create.setCreated_at(DateSetting.timestamp());
                    create.setUpdated_at(DateSetting.timestamp());

                    financialsRepository.save(create);

                    Util.log(TAG, String.valueOf(create.getId()));

                    return new ResponseEntity<>(create, HttpStatus.OK);
                }

            } else {

                return new ResponseEntity<>(create, HttpStatus.BAD_REQUEST);
            }

        }

        return null;
    }

        @DeleteMapping(value ="delete/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
            public StatusResponse financialsDelete(
                @RequestPart(value = "id", required = true)@Valid String getIdFinancial){

                StatusResponse status = new StatusResponse();

                String idDecrypt = Encryption.getDecrypt(getIdFinancial);

//                FinancialEntity data = financialsRepository.findById(Long.valueOf(idDecrypt));
            FinancialEntity data = financialsRepository.findById(Long.valueOf(idDecrypt))
                    .orElseThrow(() -> new ResourceNotFoundException(idDecrypt));

                Util.log(TAG, "data : " + data);

                if (data != null) {

                    financialsRepository.delete(data);

                    status.setStatus(200);
                    status.setMessage("Success Deteled Data");

                } else {

                    // set response callback
                    status.setStatus(404);
                    status.setMessage("Data not available");
                }

                return status;
            }

}
