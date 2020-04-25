package com.himorfosis.moneymanagement.repository;

import com.himorfosis.moneymanagement.entity.CategoryEntity;
import com.himorfosis.moneymanagement.entity.FinancialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.TypedQuery;
import java.util.List;

public interface ReportsRepository extends JpaRepository<FinancialEntity, Long> {

//    @Query(value = "SELECT * FROM financials WHERE id_user =:userId AND type_finance :typeFinance " +
//            "AND financials WHERE updated_at BETWEEN :dateStart AND :dateEnd",
//            nativeQuery = true)
//    List<FinancialEntity> findReportCategoryFinanceUser(
//            @Param("userId") String userId,
//            @Param("typeFinance") String typeFinance,
//            @Param("dateStart") String dateStart,
//            @Param("dateEnd")String dateFinish);

    @Query(value = "SELECT * FROM financials WHERE id_user =:userId AND updated_at BETWEEN :dateStart AND :dateFinish", nativeQuery = true)
    List<FinancialEntity> findFinanceUsers(
            @Param("userId") String userId,
            @Param("dateStart") String dateStart,
            @Param("dateFinish") String dateEnd);

    @Query(value = "SELECT * FROM financials WHERE id_user =:userId AND type_financial =:typeFinance " +
            "AND updated_at BETWEEN :dateStart AND :dateEnd ORDER BY updated_at DESC", nativeQuery = true)
    List<FinancialEntity> findReportCategoryFinanceUser(
            @Param("userId") String userId,
            @Param("typeFinance") String typeFinance,
            @Param("dateStart") String dateStart,
            @Param("dateEnd") String dateEnd);

    @Query(value = "SELECT * FROM financials WHERE id_user =:userId AND id_category =:idCategory " +
            "AND updated_at BETWEEN :dateStart AND :dateEnd ORDER BY updated_at DESC", nativeQuery = true)
    List<FinancialEntity> findReportCategoryDetailFinanceUser(
            @Param("userId") String userId,
            @Param("idCategory") String idCategory,
            @Param("dateStart") String dateStart,
            @Param("dateEnd") String dateEnd);

//    @Query(value = "SELECT * FROM financials WHERE id_user =:userId AND updated_at BETWEEN :dateStart AND :dateFinish", nativeQuery = true)
//    List<FinancialEntity> findFinanceUsers(
//            @Param("userId") String userId,
//            @Param("dateStart") String dateStart,
//            @Param("dateFinish") String dateEnd); , c.title, c.image_category

//    @Query(value = "SELECT new com.himorfosis.moneymanagement.dto.FinanceDto(f.type_financial, f.nominal, f.code, f.code) " +
//            "FROM financials f INNER JOIN f.category c", nativeQuery = true)
//    List<FinanceDto> fetchReportFinance();


//    TypedQuery<FinancialEntity> query
//            = entityManager.createQuery("SELECT e.department FROM Employee e", FinancialEntity.class);
//    List<FinancialEntity> resultList = query.getResultList();

//    @Query(value = "SELECT new com.himorfosis.moneymanagement.dto.ReportFinanceCategoryDto" +
//            "(f.id, f.id_category, f.id_user, f.code, c.type_category, c.image_category, c.image_category_url) " +
//            "FROM financials f INNER JOIN category c ON f.id_category = c.id", nativeQuery = true)
//            "WHERE id_user =:userId")
//            " AND type_finance :typeFinance")
//            "AND financials WHERE updated_at BETWEEN :dateStart AND :dateEnd")
//    List<ReportFinanceCategoryDto> fetchReportByCategory(
//            @Param("userId") String userId
//    );

}
