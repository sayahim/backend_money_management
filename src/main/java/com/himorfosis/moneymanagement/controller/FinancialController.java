package com.himorfosis.moneymanagement.controller;

import com.himorfosis.moneymanagement.entity.CategoryEntity;
import com.himorfosis.moneymanagement.entity.FinancialEntity;
import com.himorfosis.moneymanagement.model.response.StatusResponse;
import com.himorfosis.moneymanagement.repository.FinancialsRepository;
import com.himorfosis.moneymanagement.utilities.DateSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.rmi.runtime.Log;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/api/financials")
public class FinancialController {

    @Autowired
    FinancialsRepository financialsRepository;

//    private Long id;
//    private Long id_category;
//    private Long id_user;
//    private String code;
//    private String type_financial;
//    private Long nominal;
//    private String note;
//    private Timestamp created_at;
//    private Timestamp updated_at;

    @GetMapping("/")
    public List<FinancialEntity> getAllFinancials() {
        return financialsRepository.findAll();
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public StatusResponse financialsCreate(
            @RequestPart(value = "id_category", required = true) @Valid Long getIdCategory,
            @RequestPart(value = "id_user", required = true) @Valid Long getIdUser,
            @RequestPart(value = "type_financial", required = true) @Valid Long getTypeFinancials,
            @RequestPart(value = "nominal", required = true) @Valid Long getNominal,
            @RequestPart(value = "note", required = true) @Valid String getNote) {

        StatusResponse status = new StatusResponse();
        FinancialEntity create = new FinancialEntity();

        if (getIdUser == null || getNominal == null || getIdCategory == null ||
                getTypeFinancials == null || getNote.isEmpty()) {

            status.setStatus(500);
            status.setMessage("Please complete the data");

        } else {

            // set data
            create.setId_category(getIdCategory);
            create.setId_user(getIdUser);
            create.setNominal(getNominal);
            create.setNote(getNote);

            create.setCode("FIN_" + getIdUser + getIdCategory + DateSetting.generateNameByDateTime());
            create.setCreated_at(DateSetting.timestamp());
            create.setUpdated_at(DateSetting.timestamp());

            financialsRepository.save(create);

            status.setStatus(200);
            status.setMessage("Success Create Data");
        }

        return status;
    }

}
