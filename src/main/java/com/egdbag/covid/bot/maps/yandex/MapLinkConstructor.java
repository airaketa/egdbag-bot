package com.egdbag.covid.bot.maps.yandex;

import com.egdbag.covid.bot.registry.subscriptions.Coordinates;
import com.google.common.base.Preconditions;

import java.text.MessageFormat;
import java.util.List;

public final class MapLinkConstructor
{
    private static final String COORDS_TEMPLATE = "https://static-maps.yandex.ru/1.x/?ll={0},{1}&size=450,450&l=map&pt={0},{1},home";
    private static final String POINT_TEMPLATE = "~{0},{1},pm2{2}l{3}";

    private static final String RED_COLOR ="rd";
    private static final String DARK_GREEN_COLOR ="dg";

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

    /**
     * Gets URL for the map with home icon and shops nearby by the given coordinates
     * @param coordinates coordinates of the home icon, cannot be {@code null}
     * @param shopsCoordinates coordinates for the shop points, cannot be {@code null}
     * @return URL for the map with home icon and shops nearby, never {@code null}
     */
    public static String getHomeMapWithShops(Coordinates coordinates, List<Coordinates> shopsCoordinates)
    {
        Preconditions.checkArgument(coordinates != null);
        Preconditions.checkArgument(shopsCoordinates != null);
        return getHomeMapWithPoints(coordinates, shopsCoordinates, RED_COLOR);
    }

    /**
     * Gets URL for the map with home icon and hospitals nearby by the given coordinates
     * @param coordinates coordinates of the home icon, cannot be {@code null}
     * @param hospitalsCoordinates coordinates for the hospitals points, cannot be {@code null}
     * @return URL for the map with home icon and hospitals nearby, never {@code null}
     */
    public static String getHomeMapWithHospitals(Coordinates coordinates, List<Coordinates> hospitalsCoordinates)
    {
        Preconditions.checkArgument(coordinates != null);
        Preconditions.checkArgument(hospitalsCoordinates != null);
        return getHomeMapWithPoints(coordinates, hospitalsCoordinates, DARK_GREEN_COLOR);
    }

    /**
     * Transforms double coordinate to yandex API compatible string
     * @param coord coordinates to transform
     * @return yandex API compatible string coordinates, never {@code null}
     */
    public static String coordToString(double coord)
    {
        return String.valueOf(coord).replace(',', '.');
    }

    private static String getHomeMapWithPoints(Coordinates coordinates, List<Coordinates> points, String color)
    {
        StringBuilder builder = new StringBuilder(getHomeMap(coordinates));
        for (int i = 0; i < points.size(); i++)
        {
            Coordinates point = points.get(i);
            builder.append(MessageFormat.format(POINT_TEMPLATE, coordToString(point.getLatitude()),
                    coordToString(point.getLongitude()), color, i + 1));
        }
        return builder.toString();
    }

    private MapLinkConstructor()
    {
        //don't create
    }
}
