package com.egdbag.covid.bot;

import com.egdbag.covid.bot.registry.Coordinates;
import com.egdbag.covid.bot.registry.IRegistryService;
import com.egdbag.covid.bot.registry.UserSubscription;
import com.egdbag.covid.bot.registry.debug.MapRegistry;
import com.egdbag.covid.bot.registry.util.CoordinatesParser;
import com.egdbag.covid.bot.registry.util.MapLinkConstructor;
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
    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final List<List<InlineKeyboardButton>> keyboard;

    private final BotApiClient client;
    private final IRegistryService registryService;

    private BotApiClientController controller;

    /**
     * Initializes bot
     * @param token ICQ bot token, cannot be {@code null}
     */
    public BotService(String token)
    {
        Preconditions.checkArgument(token != null);

        registryService = new MapRegistry();
        keyboard = createKeyboard();

        client = new BotApiClient(token);
        initializeBot();

        scheduler.scheduleAtFixedRate(this::checkNewCases, 0, 10, TimeUnit.SECONDS);
    }

    private List<List<InlineKeyboardButton>> createKeyboard()
    {
        return List.of(
            List.of(InlineKeyboardButton.callbackButton("Случаи поблизости", Commands.CASES_NEARBY)),
            List.of(InlineKeyboardButton.callbackButton("Магазины поблизости", Commands.SHOPS_NEARBY)),
            List.of(InlineKeyboardButton.callbackButton("Больницы поблизости", Commands.HOSPITALS_NEARBY)),
            List.of(InlineKeyboardButton.callbackButton("Отписаться", Commands.UNSUBSCRIBE))
        );
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
            boolean existed =
                    registryService.addSubscription(new UserSubscription(chatId, coords));
            String mapLink = MapLinkConstructor.getHomeMap(coords);
            if (existed)
            {
                sendMessageWithKeyboard(chatId, "Подписка обновлена.\n" + mapLink);
            }
            else
            {
                sendMessageWithKeyboard(chatId, "Подписка добавлена.\n" + mapLink);
            }
            sendStatistics(chatId);
            return;
        }

        if (STOP_COMMAND.equals(message))
        {
            registryService.removeSubscription(chatId);
            return;
        }

        if (registryService.getSubscription(chatId).isPresent())
        {
            sendStatistics(chatId);
        }
        else
        {
            sendWelcomeMessage(chatId);
        }
    }

    private void processCallbackQueryEvent(CallbackQueryEvent event)
    {
        String chatId = event.getMessageChat().getChatId();
        String queryId = event.getQueryId();
        String callbackData = event.getCallbackData();
        if (!Strings.isNullOrEmpty(chatId) && !Strings.isNullOrEmpty(queryId) && !Strings.isNullOrEmpty(callbackData))
        {
            Optional<UserSubscription> optionalSubscription = registryService.getSubscription(chatId);
            if (optionalSubscription.isPresent())
            {
                switch (callbackData)
                {
                    case Commands.SHOPS_NEARBY:
                        processCasesNearbyQuery(optionalSubscription.get(), chatId, queryId);
                        break;
                    case Commands.CASES_NEARBY:
                        processShopsNearbyQuery(optionalSubscription.get(), chatId, queryId);
                        break;
                    case Commands.HOSPITALS_NEARBY:
                        processHospitalsNearbyQuery(optionalSubscription.get(), chatId, queryId);
                        break;
                    case Commands.UNSUBSCRIBE:
                        processUnsubscribeQuery(chatId, queryId);
                        break;
                }
            }
            else
            {
                answerCallBackQuery(queryId, "Нет активных подписок.");
            }
        }
    }

    private void processUnsubscribeQuery(String chatId, String queryId)
    {
        registryService.removeSubscription(chatId);
        answerCallBackQuery(queryId, "Подписка отменена.");
    }

    private void processCasesNearbyQuery(UserSubscription userSubscription, String chatId, String queryId)
    {
        //TODO
    }

    private void processShopsNearbyQuery(UserSubscription userSubscription, String chatId, String queryId)
    {
        //TODO
    }
    private void processHospitalsNearbyQuery(UserSubscription userSubscription, String chatId, String queryId)
    {
        //TODO
    }

    private void checkNewCases()
    {
        for (UserSubscription subscription : registryService.getSubscriptions())
        {
            //TODO
            sendMessage(subscription.getChatId(), "Появилась обновленная информация по вашему району...");
        }
    }

    private void sendWelcomeMessage(String chatId)
    {
        sendMessage(chatId, "Этот бот помогает отслеживать случаи заражения COVID-19 в вашем районе. Для того, чтобы подписаться на обновления в вашем районе, пришлите ваше местоположение.");
    }

    private void sendStatistics(String chatId)
    {
        //TODO
        sendMessage(chatId, "Количество башкиров в вашем районе зашкаливает");
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

    private void sendMessageWithKeyboard(String chatId, String message)
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