package com.egdbag.covid.bot.registry.util;

import com.egdbag.covid.bot.registry.Coordinates;
import com.google.common.base.Preconditions;

import java.text.MessageFormat;

public final class MapLinkConstructor
{
    private static final String COORDS_TEMPLATE = "https://static-maps.yandex.ru/1.x/?ll={0},{1}&z=16&size=450,450&l=map&pt={0},{1},home";

    /**
     * Gets URL for the map with home icon placed by the given coordinates
     * @param coordinates coordinates of the home icon, cannot be {@code null}
     * @return URL for the map with home icon, never {@code null}
     */
    public static String getHomeMap(Coordinates coordinates)
    {
        Preconditions.checkArgument(coordinates != null);
        return MessageFormat.format(COORDS_TEMPLATE, coordToString(coordinates.getLongitude()),
                coordToString(coordinates.getLatitude()));
    }

    private static String coordToString(double coord)
    {
        return String.valueOf(coord).replace(',', '.');
    }

    private MapLinkConstructor()
    {
        //don't create
    }
}
