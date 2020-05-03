package com.egdbag.covid.bot.maps.yandex;

import com.egdbag.covid.bot.maps.IMapService;
import com.egdbag.covid.bot.maps.Organisation;
import com.egdbag.covid.bot.registry.subscriptions.Coordinates;
import com.google.common.base.Preconditions;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class YandexMapService implements IMapService
{
    private static final String SEARCH_URL_TEMPLATE = "https://search-maps.yandex.ru/v1/?text={0}&ll={1},{2}&spn=0.15,0.10&lang=ru_RU&apikey={3}&results=5";
    private static final String SHOPS_PARAM = "продукты";
    private static final String HOSPITALS_PARAM = "больницы";

    private final String apiKey;
    private final HttpRequester requester = new HttpRequester();

    /**
     *
     * @param apiKey api key for yandex organizations API, cannot be {@code null}
     */
    public YandexMapService(String apiKey)
    {
        Preconditions.checkArgument(apiKey != null);
        this.apiKey = apiKey;
    }

    @Override
    public String getMap(Coordinates coordinates)
    {
        Preconditions.checkArgument(coordinates != null);
        return MapLinkConstructor.getHomeMap(coordinates);
    }

    @Override
    public CompletableFuture<List<Organisation>> getNearbyShops(Coordinates coordinates)
    {
        Preconditions.checkArgument(coordinates != null);

        return requester.getOrganisations(
                MessageFormat.format(
                        SEARCH_URL_TEMPLATE,
                        SHOPS_PARAM,
                        MapLinkConstructor.coordToString(coordinates.getLongitude()),
                        MapLinkConstructor.coordToString(coordinates.getLatitude()),
                        apiKey
                )
        )
        .thenApply(this::convertToOrganisations);
    }

    @Override
    public CompletableFuture<List<Organisation>> getNearbyHospitals(Coordinates coordinates)
    {
        Preconditions.checkArgument(coordinates != null);

        return requester.getOrganisations(
            MessageFormat.format(
                SEARCH_URL_TEMPLATE,
                HOSPITALS_PARAM,
                MapLinkConstructor.coordToString(coordinates.getLongitude()),
                MapLinkConstructor.coordToString(coordinates.getLatitude()),
                apiKey
            )
        ).thenApply(this::convertToOrganisations);
    }

    @Override
    public String getShopsMap(Coordinates coordinates, List<Organisation> shops)
    {
        Preconditions.checkArgument(shops != null);

        return MapLinkConstructor.getHomeMapWithShops(coordinates,
                shops.stream().map(shop -> shop.getCoordinates()).collect(Collectors.toList()));
    }

    @Override
    public String getHospitalsMap(Coordinates coordinates, List<Organisation> hospitals)
    {
        Preconditions.checkArgument(hospitals != null);

        return MapLinkConstructor.getHomeMapWithHospitals(coordinates,
                hospitals.stream().map(shop -> shop.getCoordinates()).collect(Collectors.toList()));
    }

    private List<Organisation> convertToOrganisations(OrganisationsResponse response)
    {
        List<Organisation> organisations = new ArrayList<>();

        if (response != null)
        {
            List<Feature> features = response.getFeatures();
            if (features != null && !features.isEmpty())
            {
                for (Feature feature : features)
                {
                    Feature.Properties properties = feature.getProperties();
                    if (properties != null)
                    {
                        Feature.Properties.CompanyMetaData metadata = properties.getCompanyMetaData();
                        if (metadata != null)
                        {
                            List<Feature.Properties.CompanyMetaData.Phone> phones = metadata.getPhones();
                            List<String> convertedPhones = null;
                            if (phones != null && !phones.isEmpty())
                            {
                                convertedPhones = phones.stream().map(phone -> phone.getFormatted()).collect(Collectors.toList());
                            }

                            Feature.Geometry geometry = feature.getGeometry();
                            Coordinates coords = null;
                            if (geometry != null)
                            {
                                coords = new Coordinates(geometry.getCoordinates().get(0), geometry.getCoordinates().get(1));
                            }

                            organisations.add(new Organisation(metadata.getName(), metadata.getAddress(), metadata.getUrl(), convertedPhones, coords));
                        }
                    }
                }
            }


        }

        return organisations;
    }
}
