
package chatbotAED.com.chatbotAED;

import java.util.List;
import java.io.File;
import java.util.ArrayList;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import chatbotAED.dao.ChatbotDao;

public class Chatbot extends TelegramLongPollingBot{
    //01 variáveis -------------------------------------------------------------
	File chatbotFiles;
    String message;
    User user;
    ChatbotDao manegerdb;
    SendMessage response;
    SendAnimation gif;
    SendPhoto img;
    ReplyKeyboardMarkup replykb;
    KeyboardRow kbrow;
    List<KeyboardRow> keyboard;
    String command;
    //02 construtor ------------------------------------------------------------
    public Chatbot(){
    	 command = "";
    	 message = "";
    	 manegerdb = new ChatbotDao();
    }
    //03 metodos ---------------------------------------------------------------
    public void onUpdateReceived(Update update) {
   	 	manegerdb = new ChatbotDao(); 
    	String response = "";
    	String type = "";
		response = askChatbot(update.getMessage().getText());
		if(manegerdb.count==1) type = manegerdb.response[0];
		else type = "default";
		switch(type) {
			case "txt" :
				this.response = new SendMessage();
				this.response.setText(response);
				this.response.setChatId(update.getMessage().getChatId());
				try {
					execute(this.response);
				}
				catch(TelegramApiException e) {
					e.printStackTrace();
				}
			break;
			case "gif":
				gif = new SendAnimation();
				chatbotFiles = new File("C:\\Users\\mathe\\eclipse-workspace\\chatbotAED\\Img\\" + manegerdb.coluna + manegerdb.tabela + ".gif");
				gif.setChatId(update.getMessage().getChatId());
				gif.setCaption(response);
				gif.setAnimation(chatbotFiles);
				try {
					execute(gif);
				}
				catch(TelegramApiException e) {
					e.printStackTrace();
				}
			break;
			case "img":
				img = new SendPhoto();
				chatbotFiles = new File("C:\\Users\\mathe\\eclipse-workspace\\chatbotAED\\Img\\" + manegerdb.coluna + manegerdb.tabela + ".png");
				img.setChatId(update.getMessage().getChatId());
				img.setCaption(response);
				img.setPhoto(chatbotFiles);
				try {
					execute(img);
				}
				catch(TelegramApiException e) {
					e.printStackTrace();
				}
			break;
			case "cmd":
				chatbotFiles = new File("C:\\Users\\mathe\\eclipse-workspace\\chatbotAED\\Img\\" + command + ".gif");
				if(chatbotFiles.exists()) {
					gif = new SendAnimation();
					gif.setChatId(update.getMessage().getChatId());
					gif.setCaption(response);
					gif.setAnimation(chatbotFiles);
					try {
						execute(gif);
					}
					catch(TelegramApiException e) {
						e.printStackTrace();
					}
				}
				else {
					this.response = new SendMessage();
			    	replykb = new ReplyKeyboardMarkup();
			   		kbrow = new KeyboardRow();		
			   		keyboard = new ArrayList<>();
					if(manegerdb.response.length>2) {
						for(int i=2; i<manegerdb.response.length; i++) {
							kbrow.add(manegerdb.response[i]);
						}
						keyboard.add(kbrow);
						replykb.setKeyboard(keyboard);
						replykb.setOneTimeKeyboard(true);	
						this.response.setReplyMarkup(replykb);
					}
					this.response.setText(response);
					this.response.setChatId(update.getMessage().getChatId());
					try {
						execute(this.response);
					}
					catch(TelegramApiException e) {
						e.printStackTrace();
					}
				}
			break;
			default :
				this.response.setText(response);
				this.response.setChatId(update.getMessage().getChatId());
				try{
					execute(this.response);
				}
				catch(TelegramApiException e) {
					e.printStackTrace();
				}
				break;
		}
		user = new User(update.getMessage().getChatId(), update.getMessage().getFrom().getFirstName(),update.getMessage().getText(),response);
		manegerdb.storeData(user);
		manegerdb.resetMessage();
	}
	public String getBotUsername() {
		return "Prof_Leth_bot";
	}
	@Override
	public String getBotToken() {
		return "784289965:AAGmO8g7bAeg23WTnVb5XuCCNG1TQriAYR4";
	}
    
    public String askChatbot(String message){
    	message = message.toLowerCase();
        String word = "";
        int cont = 0;
        if(message.charAt(0)!='/') {
	        for(int i=0; i<=message.length(); i++){
	            for(int j=cont;message.charAt(j)!=' '; j++){
	                word = word+message.charAt(j);
	                cont=j;
	                if(j==message.length()-1)break;
	            }
	            manegerdb.findSimilar(word);
	            cont=cont+2;
	            i=cont;
	            word="";
	            if(manegerdb.verificaTabelaColuna()) i=message.length();
	        }
	        word = manegerdb.getResponse();
        }
        else {
        	command = message.replace("/", "");
        	manegerdb.findCommand(message);
        	word = manegerdb.getResponse();
        }
        return word;
    }
    //metodo pergunta ao chatbot
    /*public void percorreResposta () {
    	for(int i=2; i<manegerdb.response.length; i++) {
			System.out.println(manegerdb.response[i]);
		}
    }*/
}
