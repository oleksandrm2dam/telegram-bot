package main;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Wifibot extends TelegramLongPollingBot {

	@Override
	public void onUpdateReceived(Update arg0) {
		String messageTextSend = "te he hackeado payaso";
		
		final String messageTextReceived = arg0.getMessage().getText();
		final long chatId = arg0.getMessage().getChatId();
		
		SendMessage message = new SendMessage().setChatId(chatId).setText(messageTextSend);
		
		try {
			execute(message);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String getBotUsername() {
		return "Wifibot";
	}
	
	@Override
	public String getBotToken() {
		return "AQUÍ TOKEN!!!!";
	}
	
}
