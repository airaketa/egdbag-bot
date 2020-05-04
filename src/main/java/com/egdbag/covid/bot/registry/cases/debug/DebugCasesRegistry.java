package com.egdbag.covid.bot.registry.cases.debug;

import com.egdbag.covid.bot.maps.yandex.model.Feature;
import com.egdbag.covid.bot.maps.yandex.model.Properties;
import com.egdbag.covid.bot.registry.cases.ICasesRegistry;
import com.egdbag.covid.bot.registry.subscriptions.Coordinates;
import com.google.common.base.Strings;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DebugCasesRegistry implements ICasesRegistry
{
    private static final String SUITABLE_CLUSTER_CAPTION = "Актуальный период";
    private static final double RADIUS = 0.025;

    private final HttpRequester requester = new HttpRequester();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private final List<DiseaseCase> cases;

    public DebugCasesRegistry()
    {
        cases = new CopyOnWriteArrayList<>();
        scheduler.scheduleAtFixedRate(this::pollDiseaseCases, 0 , 1, TimeUnit.DAYS);
    }

    @Override
    public List<DiseaseCase> getNearbyCases(Coordinates coordinates, boolean recent)
    {
        Stream<DiseaseCase> stream = cases.stream().filter(diseaseCase -> isNearby(coordinates, diseaseCase));
        if (recent)
        {
            stream = stream.filter(DiseaseCase::isRecent);
        }
        return stream.collect(Collectors.toList());
    }

    private boolean isNearby(Coordinates userCoordinates, DiseaseCase diseaseCase)
    {
        Coordinates diseaseCoordinates = diseaseCase.getCoordinates();

        //TODO yes, we know
        return Math.abs(diseaseCoordinates.getLatitude() - userCoordinates.getLongitude()) <= RADIUS &&
                Math.abs(diseaseCoordinates.getLongitude() - userCoordinates.getLatitude()) <= RADIUS;
    }

    private void pollDiseaseCases()
    {
        requester.getDiseaseCases().thenAccept(response -> {
            List<Feature> features = response.getFeatures();
            if (features != null && !features.isEmpty())
            {
                List<DiseaseCase> newCases = features
                    .stream()
                    .map(this::convert)
                    .collect(Collectors.toList());

                if (!newCases.isEmpty())
                {
                    cases.clear();
                    cases.addAll(newCases);
                }
            }
        });
    }

    private DiseaseCase convert(Feature feature)
    {
        List<Double> coordinates = feature.getGeometry().getCoordinates();
        boolean recent = isRecent(feature);

        return new DiseaseCase(feature.getProperties().getHintContent(),
                new Coordinates(coordinates.get(1), coordinates.get(0)), recent);
    }

    private boolean isRecent(Feature feature)
    {
        Properties properties = feature.getProperties();
        if (properties != null)
        {
            return SUITABLE_CLUSTER_CAPTION.equals(properties.getClusterCaption());
        }
        return false;
    }
}
