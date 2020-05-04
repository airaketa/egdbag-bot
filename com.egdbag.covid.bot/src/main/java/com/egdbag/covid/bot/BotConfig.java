package com.egdbag.covid.bot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.base.Preconditions;

import java.io.File;
import java.io.IOException;

/**
 * Bot config
 */
public class BotConfig
{
    private static final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    private String icqToken;
    private String yandexOrganisationsApiKey;
    private String yandexGeocoderApiKey;

    /**
     *
     * @param icqToken token for ICQ Bot API, cannot be {@code null}
     */
    public void setIcqToken(String icqToken)
    {
        Preconditions.checkArgument(icqToken != null);
        this.icqToken = icqToken;
    }

    /**
     * @param yandexOrganisationsApiKey key for yandex organisations API, cannot be {@code null}
     */
    public void setYandexOrganisationsApiKey(String yandexOrganisationsApiKey)
    {
        Preconditions.checkArgument(yandexOrganisationsApiKey != null);
        this.yandexOrganisationsApiKey = yandexOrganisationsApiKey;
    }

    public String getIcqToken()
    {
        return icqToken;
    }

    public String getYandexOrganisationsApiKey()
    {
        return yandexOrganisationsApiKey;
    }

    public String getYandexGeocoderApiKey() {
        return yandexGeocoderApiKey;
    }

    /**
     * @param yandexGeocoderApiKey key for yandex Geocoder API, cannot be {@code null}
     */
    public void setYandexGeocoderApiKey(String yandexGeocoderApiKey)
    {
        Preconditions.checkArgument(yandexGeocoderApiKey != null);
        this.yandexGeocoderApiKey = yandexGeocoderApiKey;
    }

    /**
     * Initializes bot config from file
     * @param file YAML file with config parameters, cannot be {@code null}
     * @return config, never {@code null}
     * @throws IOException
     */
    public static BotConfig fromFile(File file) throws IOException
    {
        return mapper.readValue(file, BotConfig.class);
    }
}
