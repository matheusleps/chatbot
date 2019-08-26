package controller;

import model.User;

public class UserController {
	ConnectionController cc;
	public UserController() {
		cc = new ConnectionController();
	}
	public void storeData(User user) {
    	String insertSql = "INSERT INTO mensagens (idusuario, nomeusuario, mensagemusuario, respostabot) VALUES ";
    	cc.connection();
    	try {
    		cc.cs = cc.connect.prepareCall(insertSql+"('"+user.getIdUser().toString()+"','"+
    				user.getNameUser()+"','"+
    				user.getMessage()+"','"+
    				user.getAnswer()+"')");
    		cc.cs.execute();
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}
    	cc.disconection();
    }
}
