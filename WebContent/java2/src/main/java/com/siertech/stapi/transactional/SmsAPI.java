
package com.siertech.stapi.transactional;



/*

import br.com.facilitamovel.bean.MO;
import br.com.facilitamovel.bean.Retorno;
import br.com.facilitamovel.bean.SmsMultiplo;
import br.com.facilitamovel.bean.SmsMultiploMessages;
import br.com.facilitamovel.bean.SmsSimples;
import br.com.facilitamovel.service.CheckCredit;
import br.com.facilitamovel.service.ReceiveMessage;
import br.com.facilitamovel.service.SendMessage;

*/

public class SmsAPI {
	
	private static String usuario = "thomazrd";
	private static String senha = "leghacy123";


	
	/*
	
	
	public static void sendSimple(String mensagem, String destinatario) throws Exception {
		// Simple Send
		SmsSimples sms = new SmsSimples();
		sms.setUser(usuario);
		sms.setPassword(senha);
		sms.setDestinatario(destinatario);
		sms.setMessage(mensagem);
		
		Retorno retorno = SendMessage.simpleSend(sms);
		System.out.println("Codigo:" + retorno.getCodigo());
		System.out.println("Descricao:" + retorno.getMensagem());
	}

	
	public static void multiple(String usuario, String senha) throws Exception {
		// Multiple Send
		SmsMultiplo sms = new SmsMultiplo();
		sms.setUser(usuario);
		sms.setPassword(senha);

		// Multiplos destinatarios
		List<String> nmbs = new ArrayList<String>();
		nmbs.add("5199430558");
		nmbs.add("5199430558");
		nmbs.add("5198961100");
		sms.setDestinatarios(nmbs);

		// Chave Interna do Cliente, nao eh obrigatorio
		List<String> chaveCliente = new ArrayList<String>();
		chaveCliente.add("1");
		chaveCliente.add("2");
		chaveCliente.add("3");
		sms.setChaveClientes(chaveCliente);

		sms.setMessage("teste");
		Retorno retorno = SendMessage.multipleSend(sms);

		System.out.println("Codigo:" + retorno.getCodigo());
		System.out.println("Descricao:" + retorno.getMensagem());
	}

	
	
	public static void multipleMsgs(String usuario, String senha) throws Exception{
		// Multiple Send
		SmsMultiploMessages sms = new SmsMultiploMessages();
		sms.setUser(usuario);
		sms.setPassword(senha);

		// Multiplos destinatarios
		List<String> listNmbs = new ArrayList<String>();
		listNmbs.add("5199430558");
		listNmbs.add("5199430558");
		listNmbs.add("5199430558");
		sms.setDestinatarios(listNmbs);

		// Chave Interna do Cliente, nao eh obrigatorio
		List<String> messagesList = new ArrayList<String>();
		messagesList.add("Mensagem 1");
		messagesList.add("Mensagem 2");
		messagesList.add("Mensagem 3");
		sms.setMessages(messagesList);

		// Chave Interna do Cliente, nao eh obrigatorio
		List<String> chaveCliente = new ArrayList<String>();
		chaveCliente.add("1");
		chaveCliente.add("2");
		chaveCliente.add("3");
		sms.setChaveClientes(chaveCliente);

		Retorno retorno = SendMessage.multipleMessagesToMultPhones(sms);
		System.out.println("Codigo:" + retorno.getCodigo());
		System.out.println("Descricao:" + retorno.getMensagem());
	}
	
	
	
	public static void listaMensagensRecebidas(String usuario, String senha){
		try {
			List<MO> caixaEntrada = ReceiveMessage.readUnreadMO(usuario, senha);
			if(caixaEntrada != null && caixaEntrada.size() > 0){
				for (MO mo : caixaEntrada) {
					System.out.println("Telefone:" + mo.getTelefone());
					System.out.println("Data/Hora:" + new SimpleDateFormat("dd/MM/yyyy kk:mm").format(mo.getDataHora()));
					System.out.println("Mensagem:" + mo.getMensagem());
					System.out.println("\n\n");
				}
			} else {
				System.out.println("Nao existem mensagens em sua Caixa de Entrada");
			}
		} catch (Exception e) {
			//Possivelmente LOGIN INVALIDO
			e.printStackTrace();
		}
		
	}
	

	
	public static void checkCredits(String usuario, String senha) throws Exception {
		try {
			System.out.println(CheckCredit.checkRealCredit(usuario, senha));
		} catch (Exception e) {
			//Possivelmente LOGIN INVALIDO
			e.printStackTrace();
		}
	}
	
	
	*/
	
}




