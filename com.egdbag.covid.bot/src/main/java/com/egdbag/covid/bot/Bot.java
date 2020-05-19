package com.egdbag.covid.bot;

import com.egdbag.covid.bot.service.BotService;
import com.google.common.base.Strings;

import java.io.File;
import java.io.IOException;

public class Bot
{
    public static void main(String[] args) throws IOException
    {
        String config = getenv("CONFIG_FILE", "config.yaml");
        File file = new File(config);
        if (!file.exists())
        {
            System.out.println("Could not find config file: " + config);
            return;
        }
        BotConfig botConfig = BotConfig.fromFile(file);

        BotService bot = new BotService(botConfig);

        while (System.in.read() != 1)
        {
            //nop
        }
        bot.stop();
    }

    private static String getenv(String name, String def)
    {
        String value = System.getenv(name);
        if (Strings.isNullOrEmpty(value))
        {
            value = def;
        }
        return value;
    }
}
