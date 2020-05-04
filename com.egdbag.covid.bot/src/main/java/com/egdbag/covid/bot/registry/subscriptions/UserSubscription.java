package com.egdbag.covid.bot.registry.subscriptions;

import com.google.common.base.Preconditions;

/**
 * User subscription for updates
 */
public class UserSubscription
{
    private String chatId;
    private Coordinates coordinates;
    private boolean inMoscow;

    private String destination;
    private String reason;
    private String vehicleId;

    public UserSubscription(String chatId, Coordinates coordinates, boolean inMoscow)
    {
        Preconditions.checkArgument(chatId != null);
        Preconditions.checkArgument(coordinates != null);

        this.chatId = chatId;
        this.coordinates = coordinates;
        this.inMoscow = inMoscow;
    }

    public String getChatId()
    {
        return chatId;
    }

    public void setChatId(String chatId)
    {
        Preconditions.checkArgument(chatId != null);
        this.chatId = chatId;
    }

    public Coordinates getCoordinates()
    {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates)
    {
        Preconditions.checkArgument(coordinates != null);
        this.coordinates = coordinates;
    }

    public boolean isInMoscow() {
        return inMoscow;
    }

    public void setInMoscow(boolean inMoscow) {
        this.inMoscow = inMoscow;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }
}
