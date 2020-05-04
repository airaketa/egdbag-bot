package com.egdbag.covid.bot.maps.yandex;

import com.egdbag.covid.bot.maps.IMapService;
import com.egdbag.covid.bot.maps.Organisation;
import com.egdbag.covid.bot.maps.yandex.model.*;
import com.egdbag.covid.bot.registry.cases.debug.DiseaseCase;
import com.egdbag.covid.bot.registry.subscriptions.Coordinates;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Preconditions;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class YandexMapService implements IMapService
{
    private static final String GEOCODER_SEARCH_URL_TEMPLATE = "https://geocode-maps.yandex.ru/1.x/?apikey={0}&format=json&geocode={1},{2}";
    private static final String SEARCH_URL_TEMPLATE = "https://search-maps.yandex.ru/v1/?text={0}&ll={1},{2}&spn={3}&lang=ru_RU&apikey={4}&results=5";
    private static final String SHOPS_SPIN = "0.005,0.005";
    private static final String HOSPITALS_SPIN = "0.15,0.15";
    private static final String SHOPS_PARAM = "%D0%BF%D1%80%D0%BE%D0%B4%D1%83%D0%BA%D1%82%D1%8B"; //продукты
    private static final String PHARMACIES_PARAM = "%D0%B0%D0%BF%D1%82%D0%B5%D0%BA%D0%B8"; //аптеки
    private static final String HOSPITALS_PARAM = "%D0%B1%D0%BE%D0%BB%D1%8C%D0%BD%D0%B8%D1%86%D1%8B"; //больницы
    private static final String TESTS_PARAM = "%D1%82%D0%B5%D1%81%D1%82+%D0%BD%D0%B0+%D0%BA%D0%BE%D1%80%D0%BE%D0%BD%D0%B0%D0%B2%D0%B8%D1%80%D1%83%D1%81"; //тест на коронавирус

    private final String organisationsApiKey;
    private final String geocoderApiKey;
    private final HttpRequester requester = new HttpRequester();

    /**
     *
     * @param organisationsApiKey api key for yandex organizations API, cannot be {@code null}
     * @param geocoderApiKey api key for yandex Geocoder API, cannot be {@code null}
     */
    public YandexMapService(String organisationsApiKey, String geocoderApiKey)
    {
        Preconditions.checkArgument(organisationsApiKey != null);
        Preconditions.checkArgument(geocoderApiKey != null);
        this.organisationsApiKey = organisationsApiKey;
        this.geocoderApiKey = geocoderApiKey;
    }

    @Override
    public String getMap(Coordinates coordinates)
    {
        Preconditions.checkArgument(coordinates != null);
        return MapLinkConstructor.getHomeMap(coordinates);
    }

    @Override
    public String getShopsMap(Coordinates coordinates, List<Organisation> shops)
    {
        Preconditions.checkArgument(coordinates != null);
        Preconditions.checkArgument(shops != null);

        return MapLinkConstructor.getHomeMapWithShops(coordinates,
            shops.stream().map(shop -> shop.getCoordinates()).collect(Collectors.toList()));
    }

    @Override
    public String getPharmaciesMap(Coordinates coordinates, List<Organisation> pharmacies)
    {
        Preconditions.checkArgument(coordinates != null);
        Preconditions.checkArgument(pharmacies != null);

        return MapLinkConstructor.getHomeMapWithPharmacies(coordinates,
                pharmacies.stream().map(pharmacy -> pharmacy.getCoordinates()).collect(Collectors.toList()));
    }

    @Override
    public String getHospitalsMap(Coordinates coordinates, List<Organisation> hospitals)
    {
        Preconditions.checkArgument(coordinates != null);
        Preconditions.checkArgument(hospitals != null);

        return MapLinkConstructor.getHomeMapWithHospitals(coordinates,
            hospitals.stream().map(hospital -> hospital.getCoordinates()).collect(Collectors.toList()));
    }

    @Override
    public String getTestsMap(Coordinates coordinates, List<Organisation> testPoints)
    {
        Preconditions.checkArgument(coordinates != null);
        Preconditions.checkArgument(testPoints != null);

        return MapLinkConstructor.getHomeMapWithTestPoints(coordinates,
            testPoints.stream().map(testPoint -> testPoint.getCoordinates()).collect(Collectors.toList()));
    }

    @Override
    public String getDiseaseMap(Coordinates coordinates, List<DiseaseCase> diseaseCases)
    {
        Preconditions.checkArgument(coordinates != null);
        Preconditions.checkArgument(diseaseCases != null);

        return MapLinkConstructor.getHomeMapWithDiseases(coordinates,
                diseaseCases.stream().map(diseaseCase -> diseaseCase.getCoordinates()).collect(Collectors.toList()));
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
                        SHOPS_SPIN,
                        organisationsApiKey
                )
        )
        .thenApply(this::convertToOrganisations);
    }

    @Override
    public CompletableFuture<List<Organisation>> getNearbyPharmacies(Coordinates coordinates)
    {
        Preconditions.checkArgument(coordinates != null);

        return requester.getOrganisations(
                MessageFormat.format(
                        SEARCH_URL_TEMPLATE,
                        PHARMACIES_PARAM,
                        MapLinkConstructor.coordToString(coordinates.getLongitude()),
                        MapLinkConstructor.coordToString(coordinates.getLatitude()),
                        SHOPS_SPIN,
                        organisationsApiKey
                )
        )
                .thenApply(this::convertToOrganisations);
    }

    @Override
    public CompletableFuture<List<Organisation>> getNearbyTests(Coordinates coordinates)
    {
        Preconditions.checkArgument(coordinates != null);

        return requester.getOrganisations(
                MessageFormat.format(
                        SEARCH_URL_TEMPLATE,
                        TESTS_PARAM,
                        MapLinkConstructor.coordToString(coordinates.getLongitude()),
                        MapLinkConstructor.coordToString(coordinates.getLatitude()),
                        HOSPITALS_SPIN,
                        organisationsApiKey
                )
        ).thenApply(this::convertToOrganisations);
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
                HOSPITALS_SPIN,
                    organisationsApiKey
            )
        ).thenApply(this::convertToOrganisations);
    }

    @Override
    public CompletableFuture<String> getAddressForCoordinates(Coordinates coordinates)
    {
        Preconditions.checkArgument(coordinates != null);

        return requester.getCoordinatesInfo((
                MessageFormat.format(
                        GEOCODER_SEARCH_URL_TEMPLATE,
                        geocoderApiKey,
                        MapLinkConstructor.coordToString(coordinates.getLongitude()),
                        MapLinkConstructor.coordToString(coordinates.getLatitude())
                )
        ))
        .thenApply(this::convertToAddress);
    }

    private String convertToAddress(JsonNode node)
    {
        JsonNode response = node.get("response");
        if (response != null)
        {
            JsonNode geoObjectCollection = response.get("GeoObjectCollection");
            if (geoObjectCollection != null)
            {
                JsonNode featureMember = geoObjectCollection.get("featureMember");
                if (featureMember != null)
                {
                    JsonNode first = featureMember.get(0);
                    if (first != null)
                    {
                        JsonNode geoObject = first.get("GeoObject");
                        if (geoObject != null)
                        {
                            JsonNode metaDataProperty = geoObject.get("metaDataProperty");
                            if (metaDataProperty != null)
                            {
                                JsonNode geocoderMetaData = metaDataProperty.get("GeocoderMetaData");
                                if (geocoderMetaData != null)
                                {
                                    JsonNode address = geocoderMetaData.get("Address");
                                    if (address != null)
                                    {
                                        JsonNode formatted = address.get("formatted");
                                        if (formatted != null)
                                        {
                                            return formatted.toString().replace("\"", "");
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return "";
    }

    private List<Organisation> convertToOrganisations(Response response)
    {
        List<Organisation> organisations = new ArrayList<>();

        if (response != null)
        {
            List<Feature> features = response.getFeatures();
            if (features != null && !features.isEmpty())
            {
                for (Feature feature : features)
                {
                    Properties properties = feature.getProperties();
                    if (properties != null)
                    {
                        CompanyMetaData metadata = properties.getCompanyMetaData();
                        if (metadata != null)
                        {
                            List<Phone> phones = metadata.getPhones();
                            List<String> convertedPhones = null;
                            if (phones != null && !phones.isEmpty())
                            {
                                convertedPhones = phones.stream().map(phone -> phone.getFormatted()).collect(Collectors.toList());
                            }

                            Geometry geometry = feature.getGeometry();
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
