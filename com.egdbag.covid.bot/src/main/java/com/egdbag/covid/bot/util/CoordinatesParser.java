package com.egdbag.covid.bot.util;

import com.egdbag.covid.bot.registry.subscriptions.Coordinates;
import com.google.common.base.Preconditions;

import java.util.Optional;

/**
 * Util class for parsing geoposition coordinates
 */
public final class CoordinatesParser
{
    private static final String GOOGLE_MAPS_URL = "https://www.google.com/maps/search/?api=1&query=";
    private static final String COORDS_REGEX = "^(-?\\d+(\\.\\d+)?),\\s*(-?\\d+(\\.\\d+)?)$";

    /**
     * Parses google maps geoposition URL into coordinates
     * @param geoposition geoposition URL, cannot be {@code null}
     * @return {@link Optional} with parse coordinates, never {@code null}
     */
    public static Optional<Coordinates> parseGoogleGeoposition(String geoposition)
    {
        Preconditions.checkArgument(geoposition != null);

        int urlIndex = geoposition.indexOf(GOOGLE_MAPS_URL);
        if (urlIndex == -1)
        {
            return Optional.empty();
        }
        String coordsRaw = geoposition.substring(urlIndex + GOOGLE_MAPS_URL.length());
        if (!coordsRaw.matches(COORDS_REGEX))
        {
            return Optional.empty();
        }

        String[] coords = coordsRaw.split(",");
        return Optional.of(new Coordinates(Double.parseDouble(coords[0]), Double.parseDouble(coords[1])));
    }

    private CoordinatesParser()
    {
        //don't create
    }
}
