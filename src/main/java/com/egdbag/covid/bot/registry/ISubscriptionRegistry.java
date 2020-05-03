package com.egdbag.covid.bot.registry;

import java.util.List;
import java.util.Optional;

/**
 * Registry for registering user subscriptions
 */
public interface ISubscriptionRegistry
{
    /**
     * Gets all registered user subscriptions
     * @return list with user subscriptions, never {@code null}
     */
    List<UserSubscription> getSubscriptions();

    /**
     * Gets existing user subscription by its id
     * @param id id of the subscription to get, cannot be {@code null}
     * @return {@link Optional} with user subscription, never {@code null}
     */
    Optional<UserSubscription> getSubscription(String id);

    /**
     * Puts new user subscription in the registry
     * @param subscription subscription to put, cannot be {@code null}
     * @return {@code true} if user already had subscription
     */
    boolean addSubscription(UserSubscription subscription);

    /**
     * Removes subscription for the user with the specified id
     * @param id id of the user to remove subscription for, cannot be {@code null}
     * @return {@code true} if user with specified id had subscription and it was removed
     */
    boolean removeSubscription(String id);
}