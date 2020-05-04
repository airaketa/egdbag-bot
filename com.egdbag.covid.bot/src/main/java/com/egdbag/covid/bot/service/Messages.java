package com.egdbag.covid.bot.service;

public final class Messages
{
    public static String Keyboards_Recent_cases_nearby = "Недавние\nслучаи рядом";
    public static String Keyboards_All_cases_nearby = "Все\nслучаи рядом";
    public static String Keyboards_Shops_nearby = "Магазины\nпоблизости";
    public static String Keyboards_Pharmacy_nearby = "Аптеки\nпоблизости";
    public static String Keyboards_Hospitals_nearby = "Ближайшие больницы";
    public static String Keyboards_Tests_nearby = "Где сдать тест";
    public static String Keyboards_Get_a_pass = "Оформить пропуск";
    public static String Keyboards_Useful_links = "Полезные ссылки";
    public static String Keyboards_Unsubscribe = "Удалить мои геоданные";
    public static String Keyboards_No_passport = "Не хочу отправлять";
    public static String Keyboards_Done = "Готово";

    public static String Keyboards_Cancel_pass = "Отмена оформления пропуска";

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
    public static String BotService_Supported_only_in_moscow = "Доступно только в Москве и МО";
    public static String BotService_No_shops_found = "К сожалению, магазинов поблизости не найдено";
    public static String BotService_No_pharmacies_found = "К сожалению, аптек поблизости не найдено";
    public static String BotService_No_hospitals_found = "К сожалению, больниц поблизости не найдено";
    public static String BotService_No_test_points_found = "К сожалению, пунктов сдачи анализов на COVID-19 не найдено";

    public static String BotService_Pass_intro = "Внимание!\nБот позволяет сгенерировать только код для отправки заявки на получение цифрового пропуска по СМС.\nЭто не пропуск!\nДля оформления заявки необходимо скопировать полученный код и самостоятельно отправить его на номер 7377 и ожидать ответного сообщения с номером пропуска.\n\nБот не хранит персональную информацию пользователя.";
    public static String BotService_Send_geo = "Отправьте местоположение назначения";
    public static String BotService_Destination = "Место назначения:";
    public static String BotService_Enter_reason = "Введите причину поездки (до 20 символов)";
    public static String BotService_Enter_vehicle_id = "Тип транспорта. Введите автомобильный номер личного ТС, либо номер карты \"Тройка\"";
    public static String BotService_Enter_passport = "Отправьте серию и номер паспорта РФ";
    public static String BotService_Message_is_ready = "Сообщение готово! Отправьте его на номер 7377";
    public static String BotService_Passport_series_and_number = "серия*номер";


    private Messages()
    {
        //don't create
    }
}
