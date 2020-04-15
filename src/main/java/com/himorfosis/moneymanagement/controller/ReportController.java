package com.himorfosis.moneymanagement.controller;

import com.himorfosis.moneymanagement.entity.CategoryEntity;
import com.himorfosis.moneymanagement.entity.FinancialEntity;
import com.himorfosis.moneymanagement.exception.DataNotAvailableException;
import com.himorfosis.moneymanagement.exception.DataNotCompleteException;
import com.himorfosis.moneymanagement.exception.MessageException;
import com.himorfosis.moneymanagement.model.ReportCategoryResponse;
import com.himorfosis.moneymanagement.repository.CategoryRepository;
import com.himorfosis.moneymanagement.repository.ReportsRepository;
import com.himorfosis.moneymanagement.security.encryption.UserEncrypt;
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

    @PostMapping(value = "report_category", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public List<ReportCategoryResponse> reportCategory(
            @RequestParam MultiValueMap<String, String> paramMap
    ) throws ParseException {

        String getUserId = paramMap.getFirst("user_id");
        String getDateToday = paramMap.getFirst("date_today");
        String getTypeFinance = paramMap.getFirst("type_finance");

        isLog("report start...");

        if (getUserId == null) {
            isBadRequest();
        } else {

            String idUserGenerate = UserEncrypt.generateDecrypt(getUserId);
            String monthSelected = getDateToday.substring(0, 8);

//            List<FinancialEntity> financialsData = new ArrayList<>();
            List<FinancialEntity> financeDatabase = reportsRepository.findReportCategoryFinanceUser(
                    idUserGenerate,
                    getTypeFinance,
                    monthSelected + "01" + TIME_START,
                    getDateToday + TIME_END
            );

//            financialsData.addAll(financeDatabase);

            isLog("finance size : " + financeDatabase.size());

            // ini error
            List<CategoryEntity> listCategory = categoryRepository.findCategoryUser("0", idUserGenerate);

            isLog("category size : " + listCategory.size());

            Integer totalPercentage = 0;
            for (CategoryEntity data: listCategory) {

                List<FinancialEntity> listFinanceByDay = new ArrayList<>();

                long totalNominal = 0;

                for (FinancialEntity item : financeDatabase) {

                    if(item.getCategory().getId() == data.getId()) {
                        totalNominal += item.getNominal();

                    }

                }

                if (totalNominal != 0) {
                    isLog("category name : " + data.getTitle());
                    isLog("total nominal : " + totalNominal);
                }

            }

//            private String id;
//            private String title;
//            private long total_nominal;
//            private Integer total_percentage;
//            private String image_category_url;

//            List<ReportFinanceCategoryDto> reportCategory = reportsRepository.fetchReportByCategory(
//                    getUserId
//            );
//
//            return reportCategory;

        }
        return null;

    }



}
