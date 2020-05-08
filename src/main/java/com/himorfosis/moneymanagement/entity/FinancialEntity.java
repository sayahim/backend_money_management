package com.himorfosis.moneymanagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "financials")
public class FinancialEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long id_category;
    private Long id_user;
    private String code;
    private String type_financial;
    private Long nominal;
    private String note;
    private Timestamp created_at;
    private Timestamp updated_at;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_category", insertable = false, updatable = false)
    @PrimaryKeyJoinColumn
    private CategoryEntity category;
//    @OneToMany(targetEntity= CategoryEntity.class, mappedBy="id_category",cascade=CascadeType.ALL, fetch = FetchType.LAZY) , referencedColumnName = "id", insertable = false, updatable = false
//    private List<CategoryEntity> category;


//    @ManyToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
//    @JoinTable(name = "tten_courseservice_course_program_table", joinColumns = @JoinColumn(name = "course_id", referencedColumnName = "course_id"), inverseJoinColumns = @JoinColumn(name = "program_id", referencedColumnName = "program_id"))
//    @ForeignKey(name="fk_tten_courseservice_course_table_course_id",inverseName="fk_tten_courseservice_program_table_program_id")
//    private List<ProgramEntity> programs;``

//    private List<CategoryEntity> category;
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "id", referencedColumnName = "id")

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "id")

    public FinancialEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId_category() {
        return id_category;
    }

    public void setId_category(Long id_category) {
        this.id_category = id_category;
    }

    public Long getId_user() {
        return id_user;
    }

    public void setId_user(Long id_user) {
        this.id_user = id_user;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType_financial() {
        return type_financial;
    }

    public void setType_financial(String type_financial) {
        this.type_financial = type_financial;
    }

    public Long getNominal() {
        return nominal;
    }

    public void setNominal(Long nominal) {
        this.nominal = nominal;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }

}
