package com.egdbag.covid.bot.maps;

import com.egdbag.covid.bot.registry.subscriptions.Coordinates;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * Service for searching for organizations on map and generating static map images with
 */
public interface IMapService
{
    /**
     * Gets URL for the map which displays given coordinates and surrounding area
     * @param coordinates coordinates to get map for, cannot be {@code null}
     * @return URL for the map, never {@code null}
     */
    String getMap(Coordinates coordinates);

    /**
     * Gets URL for the map which displays given coordinates and surrounding it list of shops
     * @param coordinates  coordinates to get map for, cannot be {@code null}
     * @param shops list of shop coordinates, cannot be {@code null}
     * @return URL for the map, never {@code null}
     */
    String getShopsMap(Coordinates coordinates, List<Organisation> shops);

    /**
     * Gets URL for the map which displays given coordinates and surrounding it list of hospitals
     * @param coordinates  coordinates to get map for, cannot be {@code null}
     * @param hospitals list of hospitals coordinates, cannot be {@code null}
     * @return URL for the map, never {@code null}
     */
    String getHospitalsMap(Coordinates coordinates, List<Organisation> hospitals);

    /**
     * Gets list of shops nearby of the specified coordinates
     * @param coordinates coordinates to search shops nearby for, cannot be {@code null}
     * @return {@link Future} with list of shops, never {@code null}
     */
    CompletableFuture<List<Organisation>> getNearbyShops(Coordinates coordinates);

    /**
     * Gets list of hospitals nearby of the specified coordinates
     * @param coordinates coordinates to search hospitals nearby for, cannot be {@code null}
     * @return {@link Future} with list of hospitals, never {@code null}
     */
    CompletableFuture<List<Organisation>> getNearbyHospitals(Coordinates coordinates);
}
