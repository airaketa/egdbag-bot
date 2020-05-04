package com.egdbag.covid.bot.registry.cases;

import com.egdbag.covid.bot.registry.cases.debug.DiseaseCase;
import com.egdbag.covid.bot.registry.subscriptions.Coordinates;
import java.util.List;

/**
 * Registry for known cases of disease
 */
public interface ICasesRegistry
{
    List<DiseaseCase> getNearbyCases(Coordinates coordinates, boolean recent);
}
