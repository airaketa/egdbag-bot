package com.egdbag.covid.bot.maps.yandex.model;

public class Phone
{
    private String type;
    private String formatted;

    public Phone(String type, String formatted) {
        this.type = type;
        this.formatted = formatted;
    }

    public Phone()
    {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFormatted() {
        return formatted;
    }

    public void setFormatted(String formatted) {
        this.formatted = formatted;
    }
}