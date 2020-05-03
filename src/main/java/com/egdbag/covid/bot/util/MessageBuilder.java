package com.egdbag.covid.bot.util;

import com.egdbag.covid.bot.maps.Organisation;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Util class for building messages
 */
public final class MessageBuilder
{
    private static final Pattern STREET_HOUSE_PATTERN = Pattern.compile("[а-яА-ЯёЁ0-9\\- ]+, [а-я]*[0-9]+");

    /**
     * Converts organisations list to message
     * @param organisations organisations to convert, cannot be {@code null}
     * @return converted message, never {@code null}
     */
    public static String convertOrganisationsToMessage(List<Organisation> organisations)
    {
        Preconditions.checkArgument(organisations != null);

        StringBuilder builder = new StringBuilder();
        for (int i =0; i < organisations.size(); i++)
        {
            Organisation organisation = organisations.get(i);
            builder.append(indexToEmoji(i));
            if (!Strings.isNullOrEmpty(organisation.getName())) {
                builder.append(organisation.getName());
            }

            if (!Strings.isNullOrEmpty(organisation.getAddress()))
            {
                builder.append("\n\uD83C\uDFE0");
                builder.append(trimAddress(organisation.getAddress()));
            }

            if (!Strings.isNullOrEmpty(organisation.getUrl()))
            {
                builder.append("\n\uD83C\uDF0E");
                builder.append(organisation.getUrl());
            }

            if (organisation.getPhones() != null && !organisation.getPhones().isEmpty())
            {
                builder.append("\n\u260E");
                builder.append(organisation.getPhones());
            }

            builder.append('\n');
        }
        return builder.toString();
    }

    private static String trimAddress(String address)
    {
        Matcher matcher = STREET_HOUSE_PATTERN.matcher(address);
        if (matcher.find())
        {
            return address.substring(matcher.start()).trim();
        }
        return address;
    }

    private static String indexToEmoji(int index)
    {
        switch(index)
        {
            case 0:
                return "\u0031\uFE0F\u20E3";
            case 1:
                return "\u0032\uFE0F\u20E3";
            case 2:
                return "\u0033\uFE0F\u20E3";
            case 3:
                return "\u0034\uFE0F\u20E3";
            case 4:
                return "\u0035\uFE0F\u20E3";
            default:
                return "• ";
        }
    }

    private MessageBuilder()
    {
        //don't create
    }
}
