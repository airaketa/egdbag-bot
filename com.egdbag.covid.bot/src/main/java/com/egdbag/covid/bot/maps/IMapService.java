package com.egdbag.covid.bot.maps;

import com.egdbag.covid.bot.registry.cases.debug.DiseaseCase;
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
     * Gets URL for the map which displays given coordinates and surrounding it list of pharmacies
     * @param coordinates  coordinates to get map for, cannot be {@code null}
     * @param shops list of pharmacies coordinates, cannot be {@code null}
     * @return URL for the map, never {@code null}
     */
    String getPharmaciesMap(Coordinates coordinates, List<Organisation> shops);

    /**
     * Gets URL for the map which displays given coordinates and surrounding it list of еуые
     * @param coordinates  coordinates to get map for, cannot be {@code null}
     * @param hospitals list of hospitals coordinates, cannot be {@code null}
     * @return URL for the map, never {@code null}
     */
    String getHospitalsMap(Coordinates coordinates, List<Organisation> hospitals);

    /**
     * Gets URL for the map which displays given coordinates and surrounding it list of test points
     * @param coordinates  coordinates to get map for, cannot be {@code null}
     * @param testPoints list of test points coordinates, cannot be {@code null}
     * @return URL for the map, never {@code null}
     */
    String getTestsMap(Coordinates coordinates, List<Organisation> testPoints);

    /**
     * Gets URL for the map which displays given coordinates and surrounding disease cases nearby
     * @param coordinates coordinates to get map for, cannot be {@code null}
     * @param diseaseCases list of disease cases, cannot be {@code null}
     * @return URL for the map, never {@code null}
     */
    String getDiseaseMap(Coordinates coordinates, List<DiseaseCase> diseaseCases);

    /**
     * Gets list of shops nearby of the specified coordinates
     * @param coordinates coordinates to search shops nearby for, cannot be {@code null}
     * @return {@link Future} with list of shops, never {@code null}
     */
    CompletableFuture<List<Organisation>> getNearbyShops(Coordinates coordinates);

    /**
     * Gets list of pharmacies nearby of the specified coordinates
     * @param coordinates coordinates to search pharmacies nearby for, cannot be {@code null}
     * @return {@link Future} with list of pharmacies, never {@code null}
     */
    CompletableFuture<List<Organisation>> getNearbyPharmacies(Coordinates coordinates);

    /**
     * Gets list of points of delivery of tests nearby of the specified coordinates
     * @param coordinates coordinates to search hospitals nearby for, cannot be {@code null}
     * @return {@link Future} with list of points, never {@code null}
     */
    CompletableFuture<List<Organisation>> getNearbyTests(Coordinates coordinates);

    /**
     * Gets list of hospitals nearby of the specified coordinates
     * @param coordinates coordinates to search hospitals nearby for, cannot be {@code null}
     * @return {@link Future} with list of hospitals, never {@code null}
     */
    CompletableFuture<List<Organisation>> getNearbyHospitals(Coordinates coordinates);

    /**
     * Gets address by coordinates
     * @param coordinates coordinates to get address for, cannot be {@code null}
     * @return {@link CompletableFuture} with address string, never {@code null}
     */
    CompletableFuture<String> getAddressForCoordinates(Coordinates coordinates);
}
