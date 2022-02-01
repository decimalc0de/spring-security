package com.decimalcode.qmed.api.counsel.service;

import com.decimalcode.qmed.utils.List2StringConverter;
import com.decimalcode.qmed.utils.Map2StringConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@Entity
@Accessors(fluent = true)
@SuppressWarnings("unused")
public class CounselModel {
    @Id
    @GeneratedValue(
        strategy = GenerationType.AUTO)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long id;

    private String name;
    private String counsellor;
    private String description;

    @Column(name = "treatment")
    @Convert(converter = List2StringConverter.class)
    private List<String> treatment;

    @Column(name = "dosage")
    @Convert(converter = Map2StringConverter.class)
    private Map<String, String> dosage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTreatment() {
        return treatment;
    }

    @Transient
    @JsonIgnore
    public void addTreatment(String treatment) {
        if(treatment != null)
            if(!treatment.trim().isEmpty())
                this.treatment.add(treatment);
    }

    public void setTreatment(ArrayList<String> treatment) {
        this.treatment = treatment;
    }

    public Map<String, String> getDosage() {
        return dosage;
    }

    public void setDosage(Map<String, String> dosage) {
        this.dosage = dosage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCounsellor() {
        return counsellor;
    }

    public void setCounsellor(String counsellor) {
        this.counsellor = counsellor;
    }

}
