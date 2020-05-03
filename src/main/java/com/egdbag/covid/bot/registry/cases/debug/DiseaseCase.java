package com.egdbag.covid.bot.registry.cases.debug;

import com.egdbag.covid.bot.registry.subscriptions.Coordinates;

public class DiseaseCase
{
    private String address;
    private Coordinates coordinates;

    public DiseaseCase(String address, Coordinates coordinates) {
        this.address = address;
        this.coordinates = coordinates;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }
}
