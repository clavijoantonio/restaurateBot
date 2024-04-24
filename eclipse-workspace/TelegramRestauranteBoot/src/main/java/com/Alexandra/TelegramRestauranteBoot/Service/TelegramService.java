package com.Alexandra.TelegramRestauranteBoot.Service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import TelegramBot.Bot;


@Service
public class TelegramService {

	public TelegramService () {
		try {
			Bot service= new Bot();
			
			TelegramBotsApi telegramBot= new TelegramBotsApi(DefaultBotSession.class);
		    telegramBot.registerBot(service);
		
		}catch(Exception e) {
			e.printStackTrace();
		}
	
	}
}
