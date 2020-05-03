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
    private String yandexMapsKey;

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
     *
     * @param yandexMapsKey key for yandex maps API, cannot be {@code null}
     */
    public void setYandexMapsKey(String yandexMapsKey)
    {
        Preconditions.checkArgument(yandexMapsKey != null);
        this.yandexMapsKey = yandexMapsKey;
    }

    public String getIcqToken()
    {
        return icqToken;
    }

    public String getYandexMapsKey()
    {
        return yandexMapsKey;
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
