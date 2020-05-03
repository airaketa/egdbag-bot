package com.egdbag.covid.bot.maps;

import com.egdbag.covid.bot.registry.subscriptions.Coordinates;

import java.util.List;

public class Organisation
{
    private String name;
    private String address;
    private String url;
    private List<String> phones;
    private Coordinates coordinates;

    public Organisation(String name, String address, String url, List<String> phones, Coordinates coordinates) {
        this.name = name;
        this.address = address;
        this.url = url;
        this.phones = phones;
        this.coordinates = coordinates;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getPhones() {
        return phones;
    }

    public void setPhones(List<String> phones) {
        this.phones = phones;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }
}
