package chatbotAED.com.chatbotAED;

public class User {
	//usuário
	Long idUser;
	String nameUser, message, answer;
	public User(Long id, String name, String message, String answer) {
		idUser = id;
		nameUser = name;
		this.message = message;
		this.answer = answer;
	}
	@Override
	public String toString() {
		String idUser = ""+this.idUser;
		return idUser;
	}
	public Long getIdUser() {
		return idUser;
	}
	public String getNameUser() {
		return nameUser;
	}
	public String getMessage() {
		return message;
	}
	public String getAnswer() {
		return answer;
	}
}
