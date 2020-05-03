package com.egdbag.covid.bot.maps.yandex.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Properties
{
    private String name;
    private String description;
    @JsonProperty("CompanyMetaData")
    private CompanyMetaData companyMetaData;

    private String clusterCaption;
    private String hintContent;

    public String getClusterCaption() {
        return clusterCaption;
    }

    public void setClusterCaption(String clusterCaption) {
        this.clusterCaption = clusterCaption;
    }

    public String getHintContent() {
        return hintContent;
    }

    public void setHintContent(String hintContent) {
        this.hintContent = hintContent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CompanyMetaData getCompanyMetaData() {
        return companyMetaData;
    }

    public void setCompanyMetaData(CompanyMetaData companyMetaData) {
        this.companyMetaData = companyMetaData;
    }
}
