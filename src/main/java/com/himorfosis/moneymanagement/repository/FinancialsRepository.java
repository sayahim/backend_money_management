package com.himorfosis.moneymanagement.repository;

import com.himorfosis.moneymanagement.entity.FinancialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FinancialsRepository extends JpaRepository<FinancialEntity, Long> {

    @Query(value = "SELECT * FROM financials WHERE id_user = ?userId ", nativeQuery = true)
    List<FinancialEntity> findAllFinancialUsers(
            String userId);

    @Query(value = "SELECT * FROM financials WHERE id_user =:userId AND updated_at BETWEEN :dateStart AND :dateFinish", nativeQuery = true)
    List<FinancialEntity> findFinanceUsers(
            @Param("userId") String userId,
            @Param("dateStart") String dateStart,
            @Param("dateFinish") String dateEnd);

    @Query(value = "SELECT * FROM financials WHERE id_user =:userId AND type_finance :typeFinance " +
            "AND financials WHERE updated_at BETWEEN :dateStart AND :dateEnd",
            nativeQuery = true)
    List<FinancialEntity> findTypeFinanceUsers(
            @Param("userId") String userId,
            @Param("typeFinance") String typeFinance,
            @Param("dateStart") String dateStart,
            @Param("dateEnd")String dateFinish);


//@Modifying
//@Query("delete from Fruit f where f.name=:name or f.color=:color")
//List<int> deleteFruits(@Param("name") String name, @Param("color") String color);


//    Long id, Long id_category, Long id_user, String code, String type_financial, Long nominal,String note,
//    Timestamp created_at, Timestamp updated_at,String title, String description, String type_category,
//    String image_category, Integer id_user_category,String image_category_url

//    @Query("SELECT new com.roytuts.spring.data.jpa.left.right.inner.cross.join.dto.JoinDto(s.id, f.name, c.name, s.amount) "
//            + "FROM Sale s INNER JOIN s.food f INNER JOIN f.company c")
//    List<JoinDto> fetchDataInnerJoin();

//    SELECT d.name, e.name, e.email, e.address FROM department d INNER JOIN employee e ON d.id = e.dept_id;


}
