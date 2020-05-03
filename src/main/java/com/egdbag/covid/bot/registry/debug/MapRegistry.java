package com.egdbag.covid.bot.registry.debug;

import com.egdbag.covid.bot.registry.IRegistryService;
import com.egdbag.covid.bot.registry.UserSubscription;
import com.google.common.base.Preconditions;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Debug implementation for {@link IRegistryService}
 */
public class MapRegistry implements IRegistryService
{
    private final Map<String, UserSubscription> registry;

    public MapRegistry()
    {
        registry = new ConcurrentHashMap<>();
    }

    @Override
    public List<UserSubscription> getSubscriptions()
    {
        return new ArrayList<>(registry.values());
    }

    @Override
    public Optional<UserSubscription> getSubscription(String id)
    {
        Preconditions.checkArgument(id != null);
        return Optional.ofNullable(registry.get(id));
    }

    @Override
    public boolean addSubscription(UserSubscription subscription)
    {
        Preconditions.checkArgument(subscription != null);
        return registry.put(subscription.getChatId(), subscription) != null;
    }

    @Override
    public boolean removeSubscription(String id)
    {
        Preconditions.checkArgument(id != null);
        return registry.remove(id) != null;
    }
}
