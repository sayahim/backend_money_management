package com.himorfosis.moneymanagement.controller;

import com.himorfosis.moneymanagement.entity.CategoryEntity;
import com.himorfosis.moneymanagement.entity.FinancialEntity;
import com.himorfosis.moneymanagement.exception.DataNotAvailableException;
import com.himorfosis.moneymanagement.exception.DataNotCompleteException;
import com.himorfosis.moneymanagement.exception.MessageException;
import com.himorfosis.moneymanagement.model.ReportCategoryResponse;
import com.himorfosis.moneymanagement.model.response.ReportDetailResponse;
import com.himorfosis.moneymanagement.repository.CategoryRepository;
import com.himorfosis.moneymanagement.repository.ReportsRepository;
import com.himorfosis.moneymanagement.security.encryption.Encryption;
import com.himorfosis.moneymanagement.security.encryption.UserEncrypt;
import com.himorfosis.moneymanagement.utilities.DateSetting;
import com.himorfosis.moneymanagement.utilities.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private String TAG = "ReportController";

    private String TIME_START = " 00:00:00";
    private String TIME_END = " 23:59:59";
    private String ALL_FINANCE = "all";
    private String INCOME_FINANCE = "income";
    private String SPEND_FINANCE = "spend";
    private String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    @Autowired
    ReportsRepository reportsRepository;
    @Autowired
    CategoryRepository categoryRepository;


    @PostMapping(value = "report_category", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public List<ReportCategoryResponse> reportCategory(
            @RequestParam MultiValueMap<String, String> paramMap
    ) throws ParseException {

        String getUserId = paramMap.getFirst("user_id");
        String getDateStart = paramMap.getFirst("date_start");
        String getDateToday = paramMap.getFirst("date_today");
        String getTypeFinance = paramMap.getFirst("type_finance");

        isLog("report start...");

        if (getUserId == null) {
            isBadRequest();
        } else {

            String idUserGenerate = UserEncrypt.generateDecrypt(getUserId);
            String monthSelected = getDateToday.substring(0, 8);

            List<FinancialEntity> financeDatabase = reportsRepository.findReportCategoryFinanceUser(
                    idUserGenerate,
                    getTypeFinance,
                    getDateStart + TIME_START,
                    getDateToday + TIME_END
            );


            // ini error
            List<CategoryEntity> listDataCategory = new ArrayList<>();
            List<CategoryEntity> listCategory = categoryRepository.findCategoryUser(idUserGenerate, getTypeFinance);
            List<CategoryEntity> listCategoryDefault = categoryRepository.findCategoryDefault("0");

            listDataCategory.addAll(listCategory);
            listDataCategory.addAll(listCategoryDefault);

            List<ReportCategoryResponse> responseData = new ArrayList<>();

            long totalNominalMax = 0;
            boolean statusMaxValue = false;
            for (CategoryEntity data: listDataCategory) {

                long totalNominalPerCategory = 0;

                for (FinancialEntity item : financeDatabase) {

                    if (item.getCategory() == null) {
                        CategoryEntity category = categoryRepository.fetchCategoryItem(item.getId_category().toString());
                        if(category.getId() == data.getId()) {
                            if (getTypeFinance.equals(category.getType_category())) {
                                totalNominalPerCategory += item.getNominal();
                            }
                        }
                    } else  {
                        if(item.getCategory().getId() == data.getId()) {
                            totalNominalPerCategory += item.getNominal();
                        }
                    }

                    // check total maximal
                    if (statusMaxValue == false) {
                        totalNominalMax += item.getNominal();
                    }

                }

                if (totalNominalPerCategory != 0) {

                    // count to get percent
                    Long totalNominalCategory = totalNominalPerCategory * 100;
                    Long totalValue = totalNominalCategory / totalNominalMax;
//                    Long totalPercentage = ((Long) totalNominalPerCategory * 100) / (Long) totalNominalMax;

                    responseData.add(
                            new ReportCategoryResponse(
                                    Encryption.setEncrypt(data.getId().toString()),
                                    data.getTitle(),
                                    totalNominalPerCategory,
                                    totalValue,
                                    data.getImage_category_url()
                                    ));

                    statusMaxValue = true;

                }

            }

            return responseData;

        }
        return null;

    }

    @PostMapping(value = "report_category_detail", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<?> reportCategoryDetail(
            @RequestParam MultiValueMap<String, String> paramMap
    ) throws ParseException {

        String getUserId = paramMap.getFirst("user_id");
        String getDateStart = paramMap.getFirst("date_start");
        String getDateToday = paramMap.getFirst("date_today");
        String getIdCategory = paramMap.getFirst("id_category");

        isLog("report start...");
        if (getUserId == null) {
            isBadRequest();
        } else {

            List<ReportDetailResponse.ReportDay> reportDay = new ArrayList<>();
            List<ReportDetailResponse.Data> data = new ArrayList<>();
            String dateMax = getDateToday.substring(8, 10);

            isLog("desc user id : " + UserEncrypt.generateDecrypt(getUserId));
            isLog("desc cat id : " + Encryption.getDecrypt(getIdCategory));
            isLog("getDateStart : " + getDateStart + TIME_START);
            isLog("getDateToday : " + getDateToday + TIME_END);

            List<FinancialEntity> financeDatabase = reportsRepository.findReportCategoryDetailFinanceUser(
                    UserEncrypt.generateDecrypt(getUserId),
                    Encryption.getDecrypt(getIdCategory),
                    getDateStart + TIME_START,
                    getDateToday + TIME_END
            );

            long totalNominalReport = 0;
            for (FinancialEntity it : financeDatabase) {
                totalNominalReport += it.getNominal();

                if (it.getId_category() == Long.valueOf(Encryption.getDecrypt(getIdCategory))) {

                    CategoryEntity category = categoryRepository.fetchCategoryItem(Encryption.getDecrypt(getIdCategory));
                    data.add(new ReportDetailResponse.Data(
                            Encryption.setEncrypt(it.getId().toString()), Encryption.setEncrypt(it.getId_category().toString())
                            , it.getType_financial(), it.getNominal(), it.getNote(), it.getCreated_at(),
                            category.getTitle(), category.getImage_category(), category.getImage_category_url()
                    ));
                }

            }

            for (int i = 0; i < Integer.parseInt(dateMax); i++) {
                int itDay = i+1;
                String day;
                if (itDay < 10) {
                    day = "0" + itDay;
                } else {
                    day = String.valueOf(itDay);
                }

                long totalNominalDay = 0;
                for (FinancialEntity item: financeDatabase) {
                    String monthSelected = item.getUpdated_at().toString().substring(0, 8);
                    String dateSelected = item.getUpdated_at().toString().substring(0, 10);
//                    isLog("=======================");
//                    isLog("date now : " + monthSelected + day);
//                    isLog("date : " + item.getUpdated_at());
                    if (dateSelected.equals(monthSelected + day)) {
                        isLog("it same");
                        totalNominalDay += item.getNominal();
                    }
                }

                if (totalNominalDay != 0) {
                    Long totalPercentage = ((Long) totalNominalDay * 100) / (Long) totalNominalReport;
                    reportDay.add(new ReportDetailResponse.ReportDay(itDay, totalNominalDay, totalPercentage));
                }

            }

            ReportDetailResponse response = new ReportDetailResponse(totalNominalReport, reportDay, data);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        return null;
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
