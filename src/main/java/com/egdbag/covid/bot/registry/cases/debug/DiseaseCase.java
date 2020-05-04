package com.egdbag.covid.bot.registry.cases.debug;

import com.egdbag.covid.bot.registry.subscriptions.Coordinates;

public class DiseaseCase
{
    private String address;
    private Coordinates coordinates;
    private boolean recent;

    public DiseaseCase(String address, Coordinates coordinates, boolean recent) {
        this.address = address;
        this.coordinates = coordinates;
        this.recent = recent;
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

    public boolean isRecent() {
        return recent;
    }

    public void setRecent(boolean recent) {
        this.recent = recent;
    }
}
