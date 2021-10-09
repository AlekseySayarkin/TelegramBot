package com.telegram.bot.web.bot;

import com.telegram.bot.exception.BeanNotFoundException;
import com.telegram.bot.service.CityService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class CityBot extends TelegramLongPollingBot {

    private final CityService cityService;

    @Value("${bot.citybot.username}")
    private String username;
    @Value("${bot.citybot.token}")
    private String token;

    public CityBot(CityService cityService) {
        this.cityService = cityService;
    }

    @PostConstruct
    public void init() {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(token)) {
            throw new IllegalStateException("Failed to init telegram bot: empty properties");
        }
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(this);

        } catch (TelegramApiException e){
            log.error(e.getMessage());
        }
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            var cityName = update.getMessage().getText();
            String text = getCityInfo(cityName);

            execute(SendMessage.builder()
                    .chatId(update.getMessage().getChatId().toString())
                    .parseMode("html")
                    .text(text)
                    .build());
        }
    }

    private String getCityInfo(String cityName) {
        try {
            var city = cityService.getByName(cityName);
            return "City: " + city.getName() + "\n\n" +
                   "Country: " + city.getCountry().getName() + "\n\n" +
                   "Info: " + city.getInfo()+ "\n\n";
        } catch (BeanNotFoundException e) {
            return e.getMessage() + ": " + cityName;
        }
    }
}
