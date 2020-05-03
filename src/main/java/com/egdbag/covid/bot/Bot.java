package com.egdbag.covid.bot;

import ru.mail.im.botapi.BotApiClient;
import ru.mail.im.botapi.BotApiClientController;
import ru.mail.im.botapi.api.entity.SendTextRequest;
import ru.mail.im.botapi.fetcher.event.Event;

import java.io.IOException;

public class Bot
{
    public static void main(String[] args) throws IOException
    {
        BotService bot = new BotService("001.0140280018.1752178673:752321177");

        System.in.read();
        bot.stop(); // stop when work done
    }
}
