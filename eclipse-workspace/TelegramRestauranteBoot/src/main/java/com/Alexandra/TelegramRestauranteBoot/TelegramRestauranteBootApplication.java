package com.Alexandra.TelegramRestauranteBoot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import TelegramBot.Bot;
import TelegramBot.MensajesRecibidos;
import TelegramBot.mensajesEnviados;


@SpringBootApplication
public class TelegramRestauranteBootApplication {

	
    
	public static void main(String[] args) {
		
		
		SpringApplication.run(TelegramRestauranteBootApplication.class, args);
		Bot bot = new Bot();
		
		
	}
  
    
}
