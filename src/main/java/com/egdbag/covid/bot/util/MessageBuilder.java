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
    private static final Pattern STREET_HOUSE_PATTERN = Pattern.compile("[а-яА-Я0-9\\- ]+, [0-9]+");

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
                boolean first = true;
                builder.append("\n☎");
                for (String phone : organisation.getPhones())
                {
                    if (first)
                    {
                        builder.append(phone);
                        first = false;
                    }
                    else
                    {
                        builder.append(", ");
                        builder.append(phone);
                    }
                }
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
            return address.substring(matcher.start());
        }
        return address;
    }

    private static String indexToEmoji(int index)
    {
        switch(index)
        {
            case 0:
                return "1️";
            case 1:
                return "2️";
            case 2:
                return "3️";
            case 3:
                return "4️";
            case 4:
                return "5️";
            default:
                return "• ";
        }
    }

    private MessageBuilder()
    {
        //don't create
    }
}
