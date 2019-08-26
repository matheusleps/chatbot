package config;

public class Config {
	String botName;
	String botToken;
	public Config() {
		botName = "YOUR_BOT_NAME_HERE";
		botToken = "YOUR_BOT_TOKEN_HERE";
	}
	public String getName() {
		return botName;
	}
	
	public String getToken() {
		return botToken;
	}
}
