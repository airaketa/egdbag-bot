package com.egdbag.covid.bot.maps.yandex;

import com.egdbag.covid.bot.maps.yandex.Feature;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown =  true)
public class OrganisationsResponse
{
    private String type;
    private List<Feature> features;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }
}
