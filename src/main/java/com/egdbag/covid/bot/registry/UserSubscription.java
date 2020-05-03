package com.egdbag.covid.bot.registry;

import com.google.common.base.Preconditions;

/**
 * User subscription for updates
 */
public class UserSubscription
{
    private String chatId;
    private Coordinates coordinates;

    public UserSubscription(String chatId, Coordinates coordinates)
    {
        Preconditions.checkArgument(chatId != null);
        Preconditions.checkArgument(coordinates != null);

        this.chatId = chatId;
        this.coordinates = coordinates;
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
}
