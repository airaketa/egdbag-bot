package com.egdbag.covid.bot.util;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import java.util.Optional;

public final class PassHelper
{
    private static final String CAR_NUMBER_REGEX = "[авекмнорстухАВЕКМНОРСТУХ]\\d{3}(?<!000)[авекмнорстухАВЕКМНОРСТУХ]{2}\\d{2,3}$";
    private static final String PASS_NUMBER_REGEX = "\\d{10}";

    /**
     * Checks if reason is not bigger than 20 letters
     * @param reason reason to check, cannot be {@code null}
     * @return {@code true} if reason is valid
     */
    public static boolean checkReason(String reason)
    {
        return !Strings.isNullOrEmpty(reason) && reason.length() < 20;
    }

    /**
     * Converts vehicle id (auto or Troika pass) to SMS supported format
     * @param vehicleId vehicle id to convert, cannot be {@code null}
     * @return {@link Optional} with converted vehicle id or null if vehicle id is invalid
     */
    public static Optional<String> processVehicleId(String vehicleId)
    {
        Preconditions.checkArgument(vehicleId != null);

        String idNoSpaces = vehicleId.replace(" ", "");
        if (idNoSpaces.matches(CAR_NUMBER_REGEX))
        {
            return Optional.of('*' + idNoSpaces.toUpperCase() + "***");
        }
        else if (idNoSpaces.matches(PASS_NUMBER_REGEX))
        {
            return Optional.of("**" + idNoSpaces + "**");
        }
        return Optional.empty();
    }

    /**
     * Checks if passport is valid and converts it to SMS supported format
     * @param passport passport id to check, cannot be {@code null}
     * @return {@link Optional} with converted passport id or null if passport id is invalid
     */
    public static Optional<String> checkPassport(String passport)
    {
        Preconditions.checkArgument(passport != null);

        String idNoSpaces = passport.replace(" ", "");
        if (idNoSpaces.matches(PASS_NUMBER_REGEX))
        {
            return Optional.of(idNoSpaces.substring(0, 4) + "*" + idNoSpaces.substring(4));
        }
        return Optional.empty();
    }

    public static String formSmsMessage(String passport, String vehicleId, String reason, String address)
    {
        return "Пропуск*3*1*" + passport + vehicleId + reason + '*' + address;
    }

    private PassHelper()
    {
        //don't create
    }
}
