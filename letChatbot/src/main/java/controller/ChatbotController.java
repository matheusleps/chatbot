/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.sql.*;

public class ChatbotController {
    //01 - vaviaveis -----------------------------------------------------------
    String sqlBusca = "SELECT similares FROM similares WHERE similares LIKE '";
    String condicaoBusca;
    public String coluna;
    public String tabela;
    public String[] response;
    public int count;
    //02 - variaveis do banco de dados -----------------------------------------
    ConnectionController cc;
    //03 - construtor ----------------------------------------------------------
    public ChatbotController(){
    	cc = new ConnectionController();
        tabela = "";
        coluna = "";
        count = 0;
    }
    public void findSimilar(String similar){
    	response = new String[2];
        //conex√£o com o banco de dados -----------------------------------------
        cc.connection();
        try{
            /*
                a variavel cs prepara o sql a ser executado --------------------
                executa esse sql e guarda na variavel result -------------------
            */
            cc.cs = cc.connect.prepareCall(sqlBusca+similar+"%'");          
            cc.result = cc.cs.executeQuery();
            cc.result.next();
            String found = cc.result.getString("similares");
            //recebe a palavra do jeito que o banco entende --------------------
            getFound(found);
            if(verificaTabelaColuna()){
            	count = 1;
            	condicaoBusca = " WHERE " + coluna + " IS NOT NULL";
                cc.cs = cc.connect.prepareCall("SELECT "+coluna+" FROM "+tabela+condicaoBusca);
                cc.result = cc.cs.executeQuery();
                cc.result.next();
                storeResult(cc.result.getString(coluna));
            }
        } catch(Exception e){
            
        }
        //desconex√£o com o banco
        cc.disconection();
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
    	cc.connection();
    	String buscaComando = "SELECT comando FROM command WHERE comando LIKE '/"+command+"%'";
    	try {
    		cc.cs = cc.connect.prepareCall(buscaComando);
    		cc.result = cc.cs.executeQuery();
    		cc.result.next();
    		storeResult(cc.result.getString("comando"));
    		count=1;
    	}
    	catch(Exception e) {

    	}
    	cc.disconection();
    }
    public String getResponse(){ 
    	if(count==1)return response[1]; 
    	return "Talvez eu n„o tenha entendido o que vocÍ quis me dizer\nTente me perguntar de outra maneira ou digite /assuntos para ver o que eu sei.";
    }
}
