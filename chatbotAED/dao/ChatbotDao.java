/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatbotAED.dao;

import java.sql.*;

import chatbotAED.com.chatbotAED.User;

public class ChatbotDao {
    //01 - vaviaveis -----------------------------------------------------------
    String dbUrl = "jdbc:postgresql://localhost:5432/chatbot";
    String username = "postgres";
    String password = "l15789954";
    String sqlBusca = "SELECT similares FROM similares WHERE similares LIKE '";
    String condicaoBusca;
    public String coluna;
    public String tabela;
    public String[] response;
    public int count;
    //02 - variaveis do banco de dados -----------------------------------------
    Connection connect = null;
    Statement stmt = null;
    ResultSet result = null;
    CallableStatement cs = null;
    //03 - construtor ----------------------------------------------------------
    public ChatbotDao(){
        tabela = "";
        coluna = "";
        count = 0;
    }
    public void connection(){
        try{
            // conexão com o banco de dados ------------------------------------
            connect = DriverManager.getConnection(dbUrl, username, password);
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    } //metodo conexão ---------------------------------------------------------
    public void disconection(){
        try{//desconectando do banco de dados ----------------------------------              
            if(stmt!=null){
                stmt.close();
            }
            if(result!=null){
                result.close();
            }
            if(connect!=null){
                connect.close();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }//metodo desconexão -------------------------------------------------------
    public void findSimilar(String similar){
    	response = new String[2];
        //conexão com o banco de dados -----------------------------------------
        connection();
        try{
            /*
                a variavel cs prepara o sql a ser executado --------------------
                executa esse sql e guarda na variavel result -------------------
            */
            cs = connect.prepareCall(sqlBusca+similar+"%'");          
            result = cs.executeQuery();
            result.next();
            String found = result.getString("similares");
            //recebe a palavra do jeito que o banco entende --------------------
            getFound(found);
            if(verificaTabelaColuna()){
            	count = 1;
            	condicaoBusca = " WHERE " + coluna + " IS NOT NULL";
                cs = connect.prepareCall("SELECT "+coluna+" FROM "+tabela+condicaoBusca);
                result = cs.executeQuery();
                result.next();
                storeResult(result.getString(coluna));
            }
        } catch(Exception e){
            
        }
        //desconexão com o banco
        disconection();
    }//metodo de busca no banco a palavra encontrada na mensagem ---------------
    public boolean verificaTabelaColuna() {
    	boolean verifica = false;
    	if(coluna!="" && tabela!="") verifica = true;
    	return verifica;
    }
    public void resetMessage() {
    	coluna="";
    	tabela="";
    	response[0]="";
    	response[1]="";
    	count=0;
    }
    private void storeResult(String response) {
    	this.response = response.split(";");
    	if(this.response[0].charAt(0)=='/') this.response[0]="cmd";
    }
    public void setType(String type) {
    	response[0]=type;
    }
    private void getFound(String found){
        String result = "";
        String clkw = "";
        for(int i=0; i<found.length(); i++){
            if(found.charAt(i)=='/')
                for(int j=i+1;found.charAt(j)!=';';j++){
                    result=result+found.charAt(j);
                    i=j+1;
                }
            if(found.charAt(i)==';')
                for(int j=i+1;j<found.length();j++){
                    clkw = clkw+found.charAt(j);
                    i=j;
                }
        }
        if(verifyClkw(clkw)){
            coluna=result;
        }
        else{
            tabela=result;
        }
    }//metodo que deixa a palavra preparada pro banco entender -----------------
    public boolean verifyClkw(String clkw){
        boolean verified = false;
        if(clkw.equals("cl")){
            verified = true;
        }
        return verified;
    }
    public void findCommand(String command) {
    	response = new String[10];
    	command = command.replace("/", "");
    	if(command.equals("")) {
    		return;
    	}
    	connection();
    	String buscaComando = "SELECT comando FROM command WHERE comando LIKE '/"+command+"%'";
    	try {
    		cs = connect.prepareCall(buscaComando);
    		result = cs.executeQuery();
    		result.next();
    		storeResult(result.getString("comando"));
    		count=1;
    	}
    	catch(Exception e) {

    	}
    	disconection();
    }
    public void storeData(User user) {
    	String insertSql = "INSERT INTO mensagens (idusuario, nomeusuario, mensagemusuario, respostabot) VALUES ";
    	connection();
    	try {
    		cs = connect.prepareCall(insertSql+"('"+user.getIdUser().toString()+"','"+
    				user.getNameUser()+"','"+
    				user.getMessage()+"','"+
    				user.getAnswer()+"')");
    		cs.execute();
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}
    	disconection();
    }
    public String getResponse(){ 
    	if(count==1)return response[1]; 
    	return "Talvez eu n�o tenha entendido o que voc� quis me dizer\nTente me perguntar de outra maneira ou digite /assuntos para ver o que eu sei.";
    }
}
