package com.himorfosis.moneymanagement.controller;

import com.himorfosis.moneymanagement.entity.FinancialEntity;
import com.himorfosis.moneymanagement.exception.DataNotAvailableException;
import com.himorfosis.moneymanagement.exception.DataNotCompleteException;
import com.himorfosis.moneymanagement.exception.MessageException;
import com.himorfosis.moneymanagement.model.HomepageResponse;
import com.himorfosis.moneymanagement.repository.FinancialsRepository;
import com.himorfosis.moneymanagement.security.encryption.UserEncrypt;
import com.himorfosis.moneymanagement.utilities.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.*;

@RestController
@RequestMapping("/api/")
public class HomepageController {

    private String TIME_START = " 00:00:00";
    private String TIME_END = " 23:59:59";
    private String ALL_FINANCE = "all";
    private String INCOME_FINANCE = "income";
    private String SPEND_FINANCE = "spend";
    private String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    @Autowired
    FinancialsRepository financialsRepository;

    @PostMapping(value = "homepage", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public HomepageResponse homepageUser(
            @RequestParam MultiValueMap<String, String> paramMap
    ) throws ParseException {

        String getUserId = paramMap.getFirst("user_id");
        String getDateToday = paramMap.getFirst("date_today");
        String getDateStart = paramMap.getFirst("date_start");

        if (getUserId == null || getDateToday == null) {
            isBadRequest();
        } else {

            String idUserGenerate = UserEncrypt.generateDecrypt(getUserId);
            String dateMax = getDateToday.substring(8, 10);
            String monthSelectedStart = getDateToday.substring(0, 8);

            isLog("date max : " + dateMax);
            isLog("month max : " +monthSelectedStart);

            long totalIncomeUser = 0;
            long totalSpendingUSer = 0;

            List<FinancialEntity> financeDatabase = financialsRepository.findFinanceUsers(
                    idUserGenerate,
                    getDateStart + TIME_START,
                    getDateToday + TIME_END
            );

            HomepageResponse response = new HomepageResponse();
            List<HomepageResponse.Data> listData = new ArrayList<>();

            for (int pos = 0; pos < Integer.parseInt(dateMax); pos++) {

                List<FinancialEntity> listFinanceByDay = new ArrayList<>();

                long totalIncomeUserByDay = 0;
                long totalSpendingUSerByDay = 0;
                String dateItem = "";

                for (FinancialEntity item : financeDatabase) {

                    String dateSelected = item.getUpdated_at().toString().substring(0, 10);
                    String monthSelected = item.getUpdated_at().toString().substring(0, 8);

                    int day = pos + 1;
                    String dateDay;

                    if (day < 10) {
                        dateDay = "0" + day;
                    } else {
                        dateDay = String.valueOf(day);
                    }

                    if (dateSelected.equals(monthSelected + dateDay)) {
                        dateItem = dateSelected;
                        if (item.getType_financial() == INCOME_FINANCE) {
                            totalIncomeUserByDay += item.getNominal();
                        } else {
                            totalSpendingUSerByDay += item.getNominal();
                        }

                        listFinanceByDay.add(item);
                    }

                    if (pos < 1) {
                        // count total financials user
                        if (item.getType_financial() == INCOME_FINANCE) {
                            totalIncomeUser += item.getNominal();
                        } else {
                            totalSpendingUSer += item.getNominal();
                        }
                    }

                }

                if (!listFinanceByDay.isEmpty()) {
                    listData.add(new HomepageResponse.Data(
                            dateItem,
                            totalIncomeUserByDay,
                            totalSpendingUSerByDay,
                            listFinanceByDay
                    ));

                }

            }

            HomepageResponse.TotalFinanceUser financeUser =
                    new HomepageResponse.TotalFinanceUser(totalIncomeUser, totalSpendingUSer);

            response.setData(listData);
            response.setTotalFinanceUser(financeUser);

            return response;
        }

        return null;

    }

    private void isLog(String message) {
        Util.log("Homepage Controller", message);
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
