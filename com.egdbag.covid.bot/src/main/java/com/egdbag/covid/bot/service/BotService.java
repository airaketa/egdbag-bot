package com.egdbag.covid.bot.service;

import static com.egdbag.covid.bot.service.Keyboards.MAIN_KEYBOARD;
import static com.egdbag.covid.bot.service.Messages.*;

import com.egdbag.covid.bot.BotConfig;
import com.egdbag.covid.bot.maps.IMapService;
import com.egdbag.covid.bot.maps.yandex.YandexMapService;
import com.egdbag.covid.bot.registry.cases.ICasesRegistry;
import com.egdbag.covid.bot.registry.cases.debug.DebugCasesRegistry;
import com.egdbag.covid.bot.registry.cases.debug.DiseaseCase;
import com.egdbag.covid.bot.registry.subscriptions.Coordinates;
import com.egdbag.covid.bot.registry.subscriptions.ISubscriptionRegistry;
import com.egdbag.covid.bot.registry.subscriptions.UserSubscription;
import com.egdbag.covid.bot.registry.subscriptions.debug.DebugSubscriptionRegistry;
import com.egdbag.covid.bot.util.CoordinatesParser;
import com.egdbag.covid.bot.util.MessageBuilder;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mail.im.botapi.BotApiClient;
import ru.mail.im.botapi.BotApiClientController;
import ru.mail.im.botapi.api.entity.AnswerCallbackQueryRequest;
import ru.mail.im.botapi.api.entity.InlineKeyboardButton;
import ru.mail.im.botapi.api.entity.SendTextRequest;
import ru.mail.im.botapi.fetcher.event.CallbackQueryEvent;
import ru.mail.im.botapi.fetcher.event.Event;
import ru.mail.im.botapi.fetcher.event.NewMessageEvent;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

/**
 * Service for managing bot lifecycle
 */
public class BotService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BotService.class);
    private static final String STOP_COMMAND = "/stop";

    private final ExecutorService executor = new ThreadPoolExecutor(1,5,10,
            TimeUnit.SECONDS, new ArrayBlockingQueue<>(10));

    private final BotApiClient client;
    private final IMapService mapService;
    private final ISubscriptionRegistry subscriptionsRegistry;
    private final ICasesRegistry casesRegistry;

    private BotApiClientController controller;

    /**
     * Initializes bot
     * @param config config containing ICQ bot token and other API keys, cannot be {@code null}
     */
    public BotService(BotConfig config)
    {
        Preconditions.checkArgument(config != null);

        mapService = new YandexMapService(config.getYandexOrganisationsApiKey(), config.getYandexGeocoderApiKey());
        subscriptionsRegistry = new DebugSubscriptionRegistry();
        casesRegistry = new DebugCasesRegistry();

        client = new BotApiClient(config.getIcqToken());
        initializeBot();
    }

    public void stop()
    {
        client.stop();
    }

    private void initializeBot()
    {
        controller = BotApiClientController.startBot(client);
        client.addOnEventFetchListener(events -> {
            for (Event<?> event : events)
            {
                executor.submit(() -> processEvent(event));
            }
        });
    }

    private void processEvent(Event<?> event)
    {
        if (event instanceof NewMessageEvent)
        {
            processMessage((NewMessageEvent) event);
        }
        else if (event instanceof CallbackQueryEvent)
        {
            processCallbackQueryEvent((CallbackQueryEvent) event);
        }
    }

    private void processMessage(NewMessageEvent event)
    {
        String chatId = event.getChat().getChatId();
        String message = event.getText();

        if (Strings.isNullOrEmpty(chatId) || Strings.isNullOrEmpty(message))
        {
            return;
        }

        Optional<Coordinates> optionalCoordinates = CoordinatesParser.parseGoogleGeoposition(message);
        if (optionalCoordinates.isPresent())
        {
            Coordinates coords = optionalCoordinates.get();
            mapService.getAddressForCoordinates(coords).thenAccept(address -> {
                boolean existed =
                        subscriptionsRegistry.addSubscription(new UserSubscription(chatId, coords, checkAddress(address)));
                String mapLink = mapService.getMap(coords);
                if (existed) {
                    sendMessageWithMainKeyboard(chatId, BotService_Geo_updated + '\n' + address + '\n' + mapLink);
                } else {
                    sendMessageWithMainKeyboard(chatId, BotService_Geo_added + '\n' + address + '\n' + mapLink);
                }
            });
            return;
        }

        if (STOP_COMMAND.equals(message))
        {
            subscriptionsRegistry.removeSubscription(chatId);
            return;
        }

        Optional<UserSubscription> optionalUserSubscription = subscriptionsRegistry.getSubscription(chatId);
        if (optionalUserSubscription.isPresent())
        {
            String mapLink = mapService.getMap(optionalUserSubscription.get().getCoordinates());
            sendMessageWithMainKeyboard(chatId, BotService_Current_geo + '\n' + mapLink);
        }
        else
        {
            sendWelcomeMessage(chatId);
        }
    }

    private boolean checkAddress(String address)
    {
        return address.contains("Москва") || address.contains("Московская область");
    }

    private void processCallbackQueryEvent(CallbackQueryEvent event)
    {
        String chatId = event.getMessageChat().getChatId();
        String queryId = event.getQueryId();
        String callbackData = event.getCallbackData();
        if (!Strings.isNullOrEmpty(chatId) && !Strings.isNullOrEmpty(queryId) && !Strings.isNullOrEmpty(callbackData))
        {
            Optional<UserSubscription> optionalSubscription = subscriptionsRegistry.getSubscription(chatId);
            if (optionalSubscription.isPresent())
            {
                switch (callbackData)
                {
                    case Commands.SHOPS_NEARBY:
                        processShopsNearbyQuery(optionalSubscription.get(), chatId, queryId);
                        break;
                    case Commands.PHARMACY_NEARBY:
                        processPharmacyNearbyQuery(optionalSubscription.get(), chatId, queryId);
                        break;
                    case Commands.RECENT_CASES_NEARBY:
                        processCasesNearbyQuery(optionalSubscription.get(), chatId, queryId, true);
                        break;
                    case Commands.ALL_CASES_NEARBY:
                        processCasesNearbyQuery(optionalSubscription.get(), chatId, queryId, false);
                        break;
                    case Commands.TESTS_NEARBY:
                        processTestsNearbyQuery(optionalSubscription.get(), chatId, queryId);
                        break;
                    case Commands.HOSPITALS_NEARBY:
                        processHospitalsNearbyQuery(optionalSubscription.get(), chatId, queryId);
                        break;
                    case Commands.USEFUL_LINKS:
                        processUsefulLinksQuery(chatId, queryId);
                        break;
                    case Commands.UNSUBSCRIBE:
                        processUnsubscribeQuery(chatId, queryId);
                        break;
                }
            }
            else
            {
                answerCallBackQuery(queryId, BotService_No_active_subscriptions);
            }
        }
    }

    private void processUsefulLinksQuery(String chatId, String queryId)
    {
        answerCallBackQuery(queryId, null);
        sendMessageWithMainKeyboard(chatId, BotService_Useful_links);
    }

    private void processUnsubscribeQuery(String chatId, String queryId)
    {
        subscriptionsRegistry.removeSubscription(chatId);
        answerCallBackQuery(queryId, BotService_Unsubscribed);
    }

    private void processCasesNearbyQuery(UserSubscription userSubscription, String chatId, String queryId, boolean recent)
    {
        if (!userSubscription.isInMoscow())
        {
            answerCallBackQuery(queryId, BotService_Supported_only_in_moscow);
            return;
        }

        answerCallBackQuery(queryId, null);
        List<DiseaseCase> nearbyCases = casesRegistry.getNearbyCases(userSubscription.getCoordinates(), recent);
        sendMessage(chatId, mapService.getDiseaseMap(userSubscription.getCoordinates(), nearbyCases));

        if (recent)
        {
            sendMessageWithMainKeyboard(chatId,
        (nearbyCases.size() > 0 ?
                    (BotService_Nearby_recent_cases_amount  + nearbyCases.size())
                    : BotService_No_recent_cases_nearby)
                    + "\n\n" + BotService_Cases_source);
        }
        else
        {
            sendMessageWithMainKeyboard(chatId,
        (nearbyCases.size() > 0 ?
                    (BotService_All_nearby_cases_amount + nearbyCases.size())
                    : BotService_No_cases_nearby)
                    + "\n\n" + BotService_Cases_source);
        }
    }

    private void processPharmacyNearbyQuery(UserSubscription userSubscription, String chatId, String queryId)
    {
        mapService.getNearbyPharmacies(userSubscription.getCoordinates()).thenAccept(organisations -> {
            answerCallBackQuery(queryId, null);
            if (organisations.isEmpty())
            {
                sendMessageWithMainKeyboard(chatId, BotService_No_pharmacies_found);
            }
            else {
                sendMessage(chatId, mapService.getPharmaciesMap(userSubscription.getCoordinates(), organisations));
                sendMessageWithMainKeyboard(chatId, MessageBuilder.convertOrganisationsToMessage(organisations));
            }
        });
    }

    private void processShopsNearbyQuery(UserSubscription userSubscription, String chatId, String queryId)
    {
        mapService.getNearbyShops(userSubscription.getCoordinates()).thenAccept(organisations -> {
            answerCallBackQuery(queryId, null);
            if (organisations.isEmpty())
            {
                sendMessageWithMainKeyboard(chatId, BotService_No_shops_found);
            }
            else {
                sendMessage(chatId, mapService.getShopsMap(userSubscription.getCoordinates(), organisations));
                sendMessageWithMainKeyboard(chatId, MessageBuilder.convertOrganisationsToMessage(organisations));
            }
        });
    }

    private void processTestsNearbyQuery(UserSubscription userSubscription, String chatId, String queryId)
    {
        if (!userSubscription.isInMoscow())
        {
            answerCallBackQuery(queryId, BotService_Supported_only_in_moscow);
            return;
        }

        mapService.getNearbyTests(userSubscription.getCoordinates()).thenAccept(organisations -> {
            answerCallBackQuery(queryId, null);
            if (organisations.isEmpty())
            {
                sendMessageWithMainKeyboard(chatId, BotService_No_test_points_found);
            }
            else {
                sendMessage(chatId, mapService.getTestsMap(userSubscription.getCoordinates(), organisations));
                sendMessageWithMainKeyboard(chatId, MessageBuilder.convertOrganisationsToMessage(organisations)
                        + "\n\u2757" + BotService_Check_info_before_going + "\n\n\u270F" + BotService_Issue_a_pass);
            }
        });
    }

    private void processHospitalsNearbyQuery(UserSubscription userSubscription, String chatId, String queryId)
    {
        mapService.getNearbyHospitals(userSubscription.getCoordinates()).thenAccept(organisations -> {
            answerCallBackQuery(queryId, null);
            if (organisations.isEmpty())
            {
                sendMessageWithMainKeyboard(chatId, BotService_No_hospitals_found);
            }
            else
                {
                sendMessage(chatId, mapService.getHospitalsMap(userSubscription.getCoordinates(), organisations));
                sendMessageWithMainKeyboard(chatId, MessageBuilder.convertOrganisationsToMessage(organisations)
                        + (userSubscription.isInMoscow() ? "\n\u270F" + BotService_Issue_a_pass : ""));
            }
        });
    }

    private void sendWelcomeMessage(String chatId)
    {
        sendMessage(chatId, BotService_Welcome_message);
    }

    private void sendMessage(String chatId, String message)
    {
        sendMessage(
            new SendTextRequest()
                .setChatId(chatId)
                .setText(message));
    }

    private void answerCallBackQuery(String queryId, String message)
    {
        try
        {
            controller.answerCallbackQuery(
                AnswerCallbackQueryRequest
                    .answerText(queryId, message, false)
            );
        }
        catch (IOException e)
        {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private void sendMessageWithMainKeyboard(String chatId, String message)
    {
        sendMessageWithKeyboard(chatId, message, MAIN_KEYBOARD);
    }

    private void sendMessageWithKeyboard(String chatId, String message, List<List<InlineKeyboardButton>> keyboard)
    {
        sendMessage(new SendTextRequest()
            .setKeyboard(keyboard)
            .setChatId(chatId)
            .setText(message));
    }

    private void sendMessage(SendTextRequest sendTextRequest)
    {
        try
        {
            controller.sendTextMessage(sendTextRequest).getMsgId();
        }
        catch (IOException e)
        {
            LOGGER.error(e.getMessage(), e);
        }
    }
}