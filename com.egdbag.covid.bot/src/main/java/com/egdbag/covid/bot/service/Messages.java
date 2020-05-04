package com.egdbag.covid.bot.service;

public final class Messages
{
    public static String Keyboards_Recent_cases_nearby = "Недавние\nслучаи рядом";
    public static String Keyboards_All_cases_nearby = "Все\nслучаи рядом";
    public static String Keyboards_Shops_nearby = "Магазины поблизости";
    public static String Keyboards_Hospitals_nearby = "Больницы поблизости";
    public static String Keyboards_Tests_nearby = "Где сдать тест";
    public static String Keyboards_Useful_links = "Полезные ссылки";
    public static String Keyboards_Unsubscribe = "Удалить мои геоданные";

    public static String BotService_Geo_updated = "Геотег обновлен";
    public static String BotService_Geo_added = "Геотег добавлен";
    public static String BotService_Current_geo = "Текущий геотег:";
    public static String BotService_No_active_subscriptions = "Отправьте свое местоположение";
    public static String BotService_Useful_links = "Новости пандемии\nnews.mail.ru/story/incident/coronavirus\nОформление пропуска по Москве nedoma.mos.ru\nБесплатный тест на COVID-19\nhelp.yandex.ru/covid19-test\nСайт минздрава РФ covid19.rosminzdrav.ru\nСтатистика по миру coronavirus.jhu.edu/map\n";
    public static String BotService_Unsubscribed = "Геоданные удалены";
    public static String BotService_Nearby_recent_cases_amount = "Количество недавних госпитализаций в радиусе 1 км: ";
    public static String BotService_No_recent_cases_nearby = "В радиусе 1 км недавних госпитализаций в вашем районе не обнаружено.";
    public static String BotService_All_nearby_cases_amount = "Общее количество госпитализаций в радиусе 1 км: ";
    public static String BotService_No_cases_nearby = "В радиусе 1 км госпитализаций не зарегистрировано.";
    public static String BotService_Cases_source = "Источник: coronavirus.mash.ru";
    public static String BotService_Welcome_message = "Этот бот помогает отслеживать случаи заражения COVID-19 в указанном районе. Чтобы начать пользоваться, необходимо отправить свое местоположение.";
    public static String BotService_Check_info_before_going = "Рекомендуем проверить актуальность информации перед поездкой на сайте лаборатории";
    public static String BotService_Issue_a_pass = "Оформить пропуск nedoma.mail.ru";
    public static String BotService_Supported_only_in_moscow = "К сожалению, данная информация доступна только по Москве и МО";

    public static String BotService_No_shops_found = "К сожалению, магазинов поблизости не найдено";
    public static String BotService_No_hospitals_found = "К сожалению, больниц поблизости не найдено";
    public static String BotService_No_test_points_found = "К сожалению, пунктов сдачи анализов на COVID-19 не найдено";

    private Messages()
    {
        //don't create
    }
}
