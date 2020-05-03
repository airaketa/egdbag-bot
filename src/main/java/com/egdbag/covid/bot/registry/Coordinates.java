package com.egdbag.covid.bot.registry;

/**
 * Coordinates of the subscribed user
 */
public class Coordinates
{
    private final double latitude;
    private final double longitude;

    public Coordinates(double latitude, double longitude)
    {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
