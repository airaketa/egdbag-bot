package com.egdbag.covid.bot.maps.yandex;

import com.egdbag.covid.bot.registry.subscriptions.Coordinates;
import com.google.common.base.Preconditions;

import java.text.MessageFormat;
import java.util.List;

public final class MapLinkConstructor
{
    private static final String COORDS_TEMPLATE = "https://static-maps.yandex.ru/1.x/?ll={0},{1}&size=450,450&l=map&pt={0},{1},home";
    private static final String POINT_TEMPLATE = "~{0},{1},{2}";

    private static final int MAX_INDEX = 98;

    private static final String SHOP_POINT ="pm2rd";
    private static final String PHARMACY_POINT ="pm2gn";
    private static final String HOSPITAL_POINT ="pm2bl";
    private static final String TEST_POINT = "pm2dg";
    private static final String DISEASE_POINT = "round";

    /**
     * Gets URL for the map with home icon placed by the given coordinates
     * @param coordinates coordinates of the home icon, cannot be {@code null}
     * @return URL for the map with home icon, never {@code null}
     */
    public static String getHomeMap(Coordinates coordinates)
    {
        Preconditions.checkArgument(coordinates != null);
        return getMap(coordinates) + "&z=15";
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
        return getHomeMapWithPoints(coordinates, shopsCoordinates, SHOP_POINT, true);
    }

    /**
     * Gets URL for the map with home icon and pharmacies nearby by the given coordinates
     * @param coordinates coordinates of the home icon, cannot be {@code null}
     * @param shopsCoordinates coordinates for the pharmacies points, cannot be {@code null}
     * @return URL for the map with home icon and pharmacies nearby, never {@code null}
     */
    public static String getHomeMapWithPharmacies(Coordinates coordinates, List<Coordinates> shopsCoordinates)
    {
        Preconditions.checkArgument(coordinates != null);
        Preconditions.checkArgument(shopsCoordinates != null);
        return getHomeMapWithPoints(coordinates, shopsCoordinates, PHARMACY_POINT, true);
    }

    /**
     * Gets URL for the map with home icon and hospitals nearby by the given coordinates
     * @param coordinates coordinates of the home icon, cannot be {@code null}
     * @param testCoordinates coordinates for the test points, cannot be {@code null}
     * @return URL for the map with home icon and hospitals nearby, never {@code null}
     */
    public static String getHomeMapWithTestPoints(Coordinates coordinates, List<Coordinates> testCoordinates)
    {
        Preconditions.checkArgument(coordinates != null);
        Preconditions.checkArgument(testCoordinates != null);
        return getHomeMapWithPoints(coordinates, testCoordinates, TEST_POINT, true);
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
        return getHomeMapWithPoints(coordinates, hospitalsCoordinates, HOSPITAL_POINT, true);
    }

    /**
     * Gets URL for the map with home icon and diseases nearby by the given coordinates
     * @param coordinates coordinates of the home icon, cannot be {@code null}
     * @param diseaseCoordinates coordinates for the disease cases points, cannot be {@code null}
     * @return URL for the map with home icon and disease cases nearby, never {@code null}
     */
    public static String getHomeMapWithDiseases(Coordinates coordinates, List<Coordinates> diseaseCoordinates)
    {
        Preconditions.checkArgument(coordinates != null);
        Preconditions.checkArgument(diseaseCoordinates != null);
        return getHomeMapWithPoints(coordinates, diseaseCoordinates, DISEASE_POINT, false);
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

    private static String getMap(Coordinates coordinates)
    {
        return MessageFormat.format(COORDS_TEMPLATE, coordToString(coordinates.getLongitude()), coordToString(coordinates.getLatitude()));
    }

    private static String getHomeMapWithPoints(Coordinates coordinates, List<Coordinates> points, String pointType, boolean includeIndex)
    {
        StringBuilder builder = new StringBuilder(getMap(coordinates));
        int i = 0;
        while (i < points.size() && i <= MAX_INDEX)
        {
            Coordinates point = points.get(i);
            builder.append(MessageFormat.format(POINT_TEMPLATE, coordToString(point.getLatitude()),
                    coordToString(point.getLongitude()), pointType));
            if (includeIndex)
            {
                builder.append("l" + (i + 1));
            }
            i++;
        }
        return builder.toString();
    }

    private MapLinkConstructor()
    {
        //don't create
    }
}
