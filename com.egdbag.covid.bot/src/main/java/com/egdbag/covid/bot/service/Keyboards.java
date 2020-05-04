package com.egdbag.covid.bot.service;

import ru.mail.im.botapi.api.entity.InlineKeyboardButton;

import java.util.List;

import static com.egdbag.covid.bot.service.Messages.*;

public final class Keyboards
{
    public static final List<List<InlineKeyboardButton>> MAIN_KEYBOARD = List.of(
        List.of(InlineKeyboardButton.callbackButton(Keyboards_Recent_cases_nearby, Commands.RECENT_CASES_NEARBY), InlineKeyboardButton.callbackButton(Keyboards_All_cases_nearby, Commands.ALL_CASES_NEARBY)),
        List.of(InlineKeyboardButton.callbackButton(Keyboards_Shops_nearby, Commands.SHOPS_NEARBY)),
        List.of(InlineKeyboardButton.callbackButton(Keyboards_Tests_nearby, Commands.TESTS_NEARBY)),
        List.of(InlineKeyboardButton.callbackButton(Keyboards_Hospitals_nearby, Commands.HOSPITALS_NEARBY)),
        List.of(InlineKeyboardButton.callbackButton(Keyboards_Useful_links, Commands.USEFUL_LINKS)),
        List.of(InlineKeyboardButton.callbackButton(Keyboards_Unsubscribe, Commands.UNSUBSCRIBE))
    );

    private Keyboards()
    {
        //don't create
    }
}
