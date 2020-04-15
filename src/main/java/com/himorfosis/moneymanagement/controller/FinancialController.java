package com.himorfosis.moneymanagement.controller;

import com.himorfosis.moneymanagement.entity.CategoryEntity;
import com.himorfosis.moneymanagement.entity.FinancialEntity;
import com.himorfosis.moneymanagement.entity.UsersEntity;
import com.himorfosis.moneymanagement.exception.*;
import com.himorfosis.moneymanagement.model.FinancialsResponse;
import com.himorfosis.moneymanagement.model.StatusResponse;
import com.himorfosis.moneymanagement.repository.CategoryRepository;
import com.himorfosis.moneymanagement.repository.FinancialsRepository;
import com.himorfosis.moneymanagement.repository.UsersRepository;
import com.himorfosis.moneymanagement.security.encryption.UserEncrypt;
import com.himorfosis.moneymanagement.utilities.DateSetting;
import com.himorfosis.moneymanagement.security.encryption.Encryption;
import com.himorfosis.moneymanagement.utilities.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/financials")
public class FinancialController {

    private String TAG = "FinancialController";

    private String TIME_START = " 00:00:00";
    private String TIME_END = " 23:59:59";
    private String ALL_FINANCE = "all";
    private String INCOME_FINANCE = "income";
    private String SPEND_FINANCE = "spend";
    private String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    @Autowired
    FinancialsRepository financialsRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    UsersRepository usersRepository;

    @GetMapping("/all")
    public List<FinancialsResponse> getAllFinancials() {

        List<FinancialEntity> dataFinancials = financialsRepository.findAll();
        List<FinancialsResponse> listData = new ArrayList<>();

        if (dataFinancials == null) {
            isNotAvailable();
        } else {

            for (FinancialEntity item : dataFinancials) {

                listData.add(new FinancialsResponse(
                        Encryption.setEncrypt(String.valueOf(item.getId())),
                        Encryption.setEncrypt(String.valueOf(item.getId_category())),
                        Encryption.setEncrypt(String.valueOf(item.getId_user())),
                        item.getCode(),
                        item.getType_financial(),
                        item.getNominal(),
                        item.getNote(),
                        item.getCategory().getTitle(),
                        item.getCategory().getDescription(),
                        item.getCategory().getImage_category_url(),
                        item.getCreated_at(),
                        item.getCreated_at())
                );
            }
        }

        return listData;
    }

    @GetMapping("/all_test")
    public List<FinancialEntity> getAllTest() {

        List<FinancialEntity> dataFinancials = financialsRepository.findAll();

        for (FinancialEntity item: dataFinancials) {
            if (item != null) {
//                isLog("title : " + item.getCategory().getTitle());
            }
        }

        return dataFinancials;
    }

    @PostMapping(value = "/financialsUser", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public List<FinancialsResponse> findAllFinancialUsers(
            @RequestParam MultiValueMap<String,String> paramMap
            ) throws ParseException {

        List<FinancialsResponse> listFinancials = new ArrayList<>();

        String getUserId = paramMap.getFirst("user_id");
        String getDateStart = paramMap.getFirst("date_start");
        String getDateEnd = paramMap.getFirst("date_end");
        String getTypeFinance = paramMap.getFirst("type_finance");

        if (getUserId == null) {
            isBadRequest();
        } else {

            List<FinancialEntity> financialsData = new ArrayList<>();

            String idUserGenerate = UserEncrypt.generateDecrypt(getUserId);
            SimpleDateFormat format = new SimpleDateFormat(DATE_PATTERN);
            Date dateStart = format.parse(getDateStart + TIME_START);
            Date dateEnd = format.parse(getDateEnd + TIME_END);

            isLog("id : " + idUserGenerate);
            isLog("date start: " + dateStart.toString());
            isLog("date end: " + dateEnd.toString());
            isLog("type finance : " + getTypeFinance);

            if (getTypeFinance.equals(ALL_FINANCE)) {

                List<FinancialEntity> financeDatabase = financialsRepository.findFinanceUsers(
                        idUserGenerate,
                        getDateStart + TIME_START,
                        getDateEnd + TIME_END
                );

                financialsData.addAll(financeDatabase);

            } else {

                List<FinancialEntity> financeDatabase = financialsRepository.findTypeFinanceUsers(
                        idUserGenerate,
                        getTypeFinance,
                        getDateStart + TIME_START,
                        getDateEnd + TIME_END
                );

                financialsData.addAll(financeDatabase);
            }

            isLog("list size : " + financialsData.size());

            for (FinancialEntity item : financialsData) {

                listFinancials.add(new FinancialsResponse(
                                Encryption.setEncrypt(String.valueOf(item.getId())),
                                Encryption.setEncrypt(String.valueOf(item.getId_category())),
                                UserEncrypt.generateEncrypt(String.valueOf(item.getId_user())),
                                item.getCode(),
                                item.getType_financial(),
                                item.getNominal(),
                                item.getNote(),
                                item.getCategory().getTitle(),
                                item.getCategory().getDescription(),
                                item.getCategory().getImage_category_url(),
                                item.getCreated_at(),
                                item.getCreated_at()
                        )
                );

            }

        }

        return listFinancials;
    }

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<FinancialEntity> financialsCreate(
            @RequestParam MultiValueMap<String,String> paramMap
    ) {
        String getNote = paramMap.getFirst("note");
        String getIdCategory = paramMap.getFirst("id_category");
        String getIdUser = paramMap.getFirst("id_user");
        String getTypeFinancials = paramMap.getFirst("type_financial");
        String getNominal = paramMap.getFirst("nominal");

        FinancialEntity create = new FinancialEntity();

        if (getIdUser == null || getNominal == null || getIdCategory == null || getTypeFinancials == null) {
            isBadRequest();
        } else {

            Long idCategory = Long.valueOf(Encryption.getDecrypt(getIdCategory));
            Long idUser = Long.valueOf(UserEncrypt.generateDecrypt(getIdUser));
            Long nominal = Long.valueOf(getNominal);

            isLog("id cat : " + idCategory);
            isLog("id user : " + idUser);

            UsersEntity userCheck = usersRepository.findById(idUser)
                    .orElseThrow(() -> new ResourceNotFoundException(getIdUser));

            CategoryEntity categoryCheck = categoryRepository.findById(Long.valueOf(idCategory))
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
                isBadRequest();
            }

        }

        return null;
    }

    @DeleteMapping(value = "delete/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public StatusResponse financialsDelete(
            @RequestPart(value = "id", required = true) @Valid String getIdFinancial) {

        StatusResponse status = new StatusResponse();

        String idDecrypt = Encryption.getDecrypt(getIdFinancial);
        FinancialEntity data = financialsRepository.findById(Long.valueOf(idDecrypt))
                .orElseThrow(() -> new ResourceNotFoundException(idDecrypt));

        if (data != null) {
            financialsRepository.delete(data);
            status.setStatus(200);
            status.setMessage("Success Deteled Data");
        } else {
            isNotAvailable();
        }

        return status;
    }

    private void isLog(String message) {
        Util.log(TAG, message);
    }

    private void isError(String message) {
        throw new MessageException(message);
    }

    private void isBadRequest() {
        throw new DataNotCompleteException();
    }

    private void isNotAvailable() {
        throw new DataNotAvailableException();
    }

}
