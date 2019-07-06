package chatbotAED.com.chatbotAED;

import java.awt.Desktop;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import chatbotAED.dao.ChatbotDao;

public class Main {

	public static void main(String[] args) {		
		 ApiContextInitializer.init();
         TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
         try {
             telegramBotsApi.registerBot(new Chatbot());
         } catch (TelegramApiException e) {
             e.printStackTrace();
         }
		 /*Desktop desktop = Desktop.getDesktop();
		Chatbot chatbot = new Chatbot();
		String comando = chatbot.askChatbot();
		System.out.println(comando);TESTE*/
	}
}