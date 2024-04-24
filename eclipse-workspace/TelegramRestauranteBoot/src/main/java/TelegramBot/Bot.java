package TelegramBot;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import TelegramBot.mensajesEnviados.MessageType;


public class Bot extends TelegramLongPollingBot{
	private static final Logger log = Logger.getLogger(Bot.class);
	
	String estado;
	String Paso;
	Long Id_pedidoCliente;
	Long id_chat;
	int opcionEtp6;
	envioDatos Datos;
	Update update=null;
	long id_pedido;
	double valorTotal;
	double valorUnitario;
	int valor2;
	String producto;
	String cantidad;
	long id_informacion;
	String saludo;
	String textoCantidadPedido;
    String textoMenuDia;
    String textoMenuPedido;
    String textoOpcionPedido;
    long categoria;
    String listamenu;
    String localPath = "";
    int opcion;
    int contMesa;
    int[] etapa = new int[100];
    String message_text;
    String nombre;
    String data;
    Message mensage;
    
    List<ArrayList> registrosMenu = new ArrayList<>();
    ArrayList registro = new ArrayList<>();
    List<ArrayList> registrosMenuPedido = new ArrayList<>();
    ArrayList registroPedido = new ArrayList<>();
    JSONArray DatosPedido=new JSONArray();
    JSONObject objPedido=new JSONObject();
    JSONObject DatosCliente=new JSONObject();
    HttpURLConnection  conn;
    Long chat_id;
    private Bot bot;
    Object object;
	private static final int PRIORITY_FOR_SENDER = 1;
    private static final int PRIORITY_FOR_RECEIVER = 3;
  
   
 
	//LISTA DE CONTACTOS QUE MANEJAREMOS
    String[] contactos = new String[100];
  //NUMERO DE CONTACTOS QUE ESTAREMOS MANEJANDO
    int cantTabs = 0;
    //USUARIO SELECCIONADO O TAB SELECCIONADA
    int usuarioSel = 0;
    SendMessage message = new SendMessage(); 
  //MENSAJES RECIBIDOS DESDE WA
    String[] mensajesRecibidos = new String[100];
    String nombreContacto;
   
    public Queue<Object> sendQueue=new ConcurrentLinkedQueue<>();
 
    public Queue<Object> receiveQueue=new ConcurrentLinkedQueue<>(); 
   

@Override
public void onUpdateReceived(Update update) {
	// TODO Auto-generated method stub
 Thread thread = new Thread(new Runnable() {
         @Override
         public void run() {
	log.debug("Receive new Update. updateID: " + update.getUpdateId());
	
	if (update.hasMessage() && update.getMessage().hasText()) {
        // Set variables
        nombreContacto = update.getMessage().getChat().getFirstName();
        String user_last_name = update.getMessage().getChat().getLastName();
        String user_username = update.getMessage().getChat().getUserName();
        long user_id = update.getMessage().getChat().getId();
        message_text = update.getMessage().getText();
        chat_id = update.getMessage().getChatId();
        mensage=update.getMessage();
       
       // Create a message object object
   
        
      //VARIABLE QUE ALMACENARA LOS ULTIMOS MENSAJE ENVIADOS
        mensajesRecibidos[usuarioSel] = "";
       
        leemosDatos();
        revisionTel();
   
	}
}  
});
thread.start();
//System.out.println(thread.getName());

     }


public synchronized SendMessage sendMsg(String s, Long chatId ) {
    SendMessage sendMessage = new SendMessage();
    sendMessage.enableMarkdown(true);
    sendMessage.setChatId(chatId);
    sendMessage.setText(s);
   /* try {
        execute(sendMessage);
    } catch (TelegramApiException e) {
    	log.error(e.getMessage(), e);
    }*/
   return sendMessage;
}
	  
@Override
public String getBotUsername() {
	// TODO Auto-generated method stub
	return "DemoAlexandraBot";
	
}

@Override
public String getBotToken() {
	
	return "6714109081:AAGR0OeYpJ2HMvH4Xqks-8ApO9xJ0cHCS2c";
}

public void revisionTel() {
	if(receiveQueue!=null) {
    	
    	//etapa[usuarioSel]=1;
    	//System.out.println(message_text);
    	   	
    }
    
  boolean contactoEncontrado = false;
 //VALIDAMOS QUE EL NOMBRE DE CONTACTO NO ESTE EN LA LISTA DE CONTACTOS DE LO CONTRARIO NOS POSICIONAMOS EN EL ID DEL CONTACTO
   for (int tabSel = 1; tabSel <= cantTabs; tabSel++) {
       //SI EL CONTACTO YA EXISTE EN LA LISTA
       if (nombreContacto.equals(contactos[tabSel])) {
           //ACTIVAMOS LA BANDERA Y DECMOS QUE SI ENCONTRAMOS EL CONTACTO
           contactoEncontrado = true;
           usuarioSel = tabSel;
           System.out.println( usuarioSel);
          // System.out.println(" aca voy");
           //ROMPEMOS EL CICLO
           break;
       }
   }
  //SI NO SE ENCONTRO EL CONTACTO LO CREAMOS
    if (contactoEncontrado == false) {
        //AUMENTAMOS EL NUMERO DE CONTACTOS
        cantTabs++;
        //ASIGNAMOS EL NOMBRE DEL CONTACTO A LA LISTA DE CONTACTOS
        contactos[cantTabs] = nombreContacto;
        //OBTENEMOS EL ID DEL USUARIO SELECCIONADO
        usuarioSel = cantTabs;
        //ASIGANOS LA ETAPA DEL USUARIO QUE ES LA 1 (ENVIAR TEXTOS D ELOS SORTEOS)
        etapa[usuarioSel] = 1;
    }
    String ultimoMensaje =message_text;
    mensajesRecibidos[usuarioSel] = mensajesRecibidos[usuarioSel] + " " + ultimoMensaje;
   
    try {
    	
    if (etapa[usuarioSel] == 1) {
        //ETAPA 1 DONDE SE ENVIAN LOS MENSAJES DE LOS DIFERENTES SORTEOS
        etapa1();
       return;
    } else if (etapa[usuarioSel] == 2) {
        //VALIDAMOS QUE SI SEA UN NUMERO DEL SORTEO Y PREGUNTAMOS SI QUIERE UN NUMERO AL AZAR Y QUIERE SELECCIONAR SU NUMERO
        //SI NO LLAMAMOS LA ETAPA 1 PARA QUE ENVIE POR WA LOS SORTEOS
        etapa2();
        return;
    }
    if (etapa[usuarioSel] == 3) {
        //VALIDAMOS QUE SI SEA UN NUMERO DEL SORTEO Y PREGUNTAMOS SI QUIERE UN NUMERO AL AZAR Y QUIERE SELECCIONAR SU NUMERO
        //SI NO LLAMAMOS LA ETAPA 2 PARA QUE ENVIE POR WA LOS SORTEOS
        etapa3();
        return;
    }
    
    if (etapa[usuarioSel] == 4) {
        //VALIDAMOS QUE SI SEA UN NUMERO DEL SORTEO Y PREGUNTAMOS SI QUIERE UN NUMERO AL AZAR Y QUIERE SELECCIONAR SU NUMERO
        //SI NO LLAMAMOS LA ETAPA 2 PARA QUE ENVIE POR WA LOS SORTEOS
        etapa4();
        return;
    }
    if (etapa[usuarioSel] == 5) {
        //VALIDAMOS QUE SI SEA UN NUMERO DEL SORTEO Y PREGUNTAMOS SI QUIERE UN NUMERO AL AZAR Y QUIERE SELECCIONAR SU NUMERO
        //SI NO LLAMAMOS LA ETAPA 2 PARA QUE ENVIE POR WA LOS SORTEOS
        etapa5();
        return;
    }
    if (etapa[usuarioSel] == 6) {
        //VALIDAMOS QUE SI SEA UN NUMERO DEL SORTEO Y PREGUNTAMOS SI QUIERE UN NUMERO AL AZAR Y QUIERE SELECCIONAR SU NUMERO
        //SI NO LLAMAMOS LA ETAPA 2 PARA QUE ENVIE POR WA LOS SORTEOS
        etapa6();
        return;
    }
    if (etapa[usuarioSel] == 7) {
        //VALIDAMOS QUE SI SEA UN NUMERO DEL SORTEO Y PREGUNTAMOS SI QUIERE UN NUMERO AL AZAR Y QUIERE SELECCIONAR SU NUMERO
        //SI NO LLAMAMOS LA ETAPA 2 PARA QUE ENVIE POR WA LOS SORTEOS
        etapa7(Datos);
        return;
    }                  
    } catch (TelegramApiException e) {
        e.printStackTrace();
    }
}
      

public void etapa1() throws TelegramApiException {
	
	String listaOpciones = saludo + "\n1.-Menu del dia de hoy\n2.-Toma Pedido\n3.-Horarios\n4.-Acerca de nosotros\n";
	
	/*message.setText(listaOpciones);
	 
	execute(message);*/
	sendQueue.add(sendMsg(listaOpciones,chat_id));
	
	threadMensage();
	
	etapa[usuarioSel] = 2;

	
}
public void etapa2() {
    //VARIABLE QUE RECIBE LAS OPCIONES COLOCADAS POR EL USUARIO DE WA
    int opcion;
    //QUITAMOS ESPACIOS EN BLANCO
    String mensajesRecibidosSinEspacios = mensajesRecibidos[usuarioSel].replace(" ", "");
    System.out.println(mensajesRecibidosSinEspacios);
    //VALIDAMO SI REALMENTE SE ENVIO UN NUMERO
    if (isNumeric(mensajesRecibidosSinEspacios)) {
        opcion = Integer.parseInt(mensajesRecibidosSinEspacios);
        //SI LA OPCION ESTA ENTRE 1 Y 4
        if (opcion >= 1 && opcion <= 4) {
        	
            if (opcion == 1) {
                String textoMenuDia = "MENU DEL DIA DE HOY\n";
                //VARIABLE QE CUENTA LOS REGISTROS
                int cont = 0;
                //OBTENEMOS EL DIA DE HOY
                LocalDate today = LocalDate.now();
                DayOfWeek dayOfWeek = today.getDayOfWeek();
            
                //ESCRIBIMOS EL MENSAJE
                listamenu = textoMenuDia + "\n1.-Almuerzo\n2.-Desayunos\n3.-Bebidas";
               // message.setText(listamenu);
                
                sendQueue.add(sendMsg( listamenu,chat_id));
                
                etapa[usuarioSel] = 3;
            }
            if (opcion == 2) {
                String textotomaPedido = "Bienvenido a nuestro Toma Pedido\n"
                                          + "\n1.-Almuerzo\n2.-Desayunos\n3.-Bebidas";
                 
                CrearPedido(mensage);
                //ESCRIBIMOS EL MENSAJE
                sendQueue.add(sendMsg( textotomaPedido,chat_id));
               
                etapa[usuarioSel] = 4;
            }
            if (opcion == 3) {
               // String textoMenuHorarios = "HORARIOS\n" + lunes + "\n" + martes + "\n" + miercoles + "\n" + jueves + "\n" + viernes + "\n" + sabado + "\n" + domingo + "\n";
                //ESCRIBIMOS EL MENSAJE
               // message.setText(textoMenuHorarios);
                try {
					execute(message);
				} catch (TelegramApiException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
              
                etapa[usuarioSel] = 1;
            }
            if (opcion == 4) {
               // String textoMenuAcerca = "ACERCA DE NOSOTROS\n" + telefono + "\n" + direccion + "\n" + domicilio + "\n" + pagina + "\n";
                //ESCRIBIMOS EL MENSAJE
                //message.setText(textoMenuAcerca);
                try {
					execute(message);
				} catch (TelegramApiException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
               
                etapa[usuarioSel] = 1;
            }
        } //NO SE SELECCIONO LA OPCION CORRECTA
        else {
            //ESCRIBIMOS EL MENSAJE
        	message.setText("Opcion no valida\n");
            try {
				execute(message);
			} catch (TelegramApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
           
            etapa[usuarioSel] = 1;
        }
    } else {
        //ESCRIBIMOS EL MENSAJE
    	message.setText("Opcion no valida\n");
        try {
			execute(message);
		} catch (TelegramApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        etapa[usuarioSel] = 1;
    }
}
public void etapa3() {
	
    //VARIABLE QUE RECIBE LAS OPCIONES COLOCADAS POR EL USUARIO DE WA
    int opcion;
    //QUITAMOS ESPACIOS EN BLANCO
    String mensajesRecibidosSinEspacios = mensajesRecibidos[usuarioSel].replace(" ", "");
    System.out.println(mensajesRecibidosSinEspacios);
    //VALIDAMO SI REALMENTE SE ENVIO UN NUMERO
    if (isNumeric(mensajesRecibidosSinEspacios)) {
        opcion = Integer.parseInt(mensajesRecibidosSinEspacios);
        //SI LA OPCION ESTA ENTRE 1 Y 7
        if (opcion >= 1 && opcion <= 3) {
            textoMenuDia = "MENU DE " + opcionMenu(opcion) + "\n";
            
            if (opcion==1) {
            	
            	categoria=2;
  
            	leemosMenu();
                
            	leerRegistroMenu();
            	
            	registro.clear();
              
            //ESCRIBIMOS EL MENSAJE
            
            	sendQueue.add(sendMsg( textoMenuDia,chat_id));
              
            }
              if (opcion==2) {
            	
            	categoria=1;
  
            	leemosMenu();
                
            	leerRegistroMenu();
              
            	
            //ESCRIBIMOS EL MENSAJE
            	sendQueue.add(sendMsg( textoMenuDia,chat_id));
           
              
            }
              if (opcion==3) {
              	
              	categoria=3;
    
              	leemosMenu();
                  
              	leerRegistroMenu();
                
              	registro.clear();
              //ESCRIBIMOS EL MENSAJE
              	sendQueue.add(sendMsg( textoMenuDia,chat_id));
              
                 
              }
             
            etapa[usuarioSel] = 1;
        } else {
            //ESCRIBIMOS EL MENSAJE
        	
        	message.setText("Opcion no valida\n");
            try {
				execute(message);
			} catch (TelegramApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            etapa[usuarioSel] = 1;
          
        }
    } else {
        //ESCRIBIMOS EL MENSAJE
    	message.setText("Opcion no valida\n");
        try {
			execute(message);
		} catch (TelegramApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        etapa[usuarioSel] = 1;
      
    }
}

public void etapa4() {
	
	
	
	 //VARIABLE QUE RECIBE LAS OPCIONES COLOCADAS POR EL USUARIO DE WA
    int opcion;   
    //QUITAMOS ESPACIOS EN BLANCO
    String mensajesRecibidosSinEspacios = mensajesRecibidos[usuarioSel].replace(" ", "");
    //System.out.println(mensajesRecibidosSinEspacios);
  int conta=0;
//VALIDAMO SI REALMENTE SE ENVIO UN NUMERO
  if (isNumeric(mensajesRecibidosSinEspacios)) {
      opcion = Integer.parseInt(mensajesRecibidosSinEspacios);
      
      getDatosCliente(mensage); 
  	 
      Datos=envioDatos.valueOf(Paso);
      
      System.out.println(Datos);
      
      switch (Datos) {
	    case PRODUCTO:  
	    	
        //SI LA OPCION ESTA ENTRE 1 Y 3
        if (opcion >= 1 && opcion <= 3) {
        	textoMenuPedido = "INGRESA EL CODIGO DE TU PRODUCTO "+ "\n";
        	
            if (opcion==1) {
            	
            	categoria=2;
  
            	leerMenuPedido();
            	
            	
            	receiveQueue.add(DatosCliente.put("paso","CANTIDAD"));
            	ActualizaDatosTomaPedido(mensage,object);
            	
                //ESCRIBIMOS EL MENSAJE
               
            	 
            }
              if (opcion==2) {
            	
            	categoria=1;
                
            	leerMenuPedido();
               
            	receiveQueue.add(DatosCliente.put("paso","CANTIDAD"));
            	ActualizaDatosTomaPedido(mensage,object);
            	
            //ESCRIBIMOS EL MENSAJE
          
              
            }
              if (opcion==3) {
              	
              	categoria=3;
    
              	leerMenuPedido();
              	
              	receiveQueue.add(DatosCliente.put("paso","CANTIDAD"));
              	ActualizaDatosTomaPedido(mensage,object);
            	
              //ESCRIBIMOS EL MENSAJE
             
                 
              }
              sendQueue.add(sendMsg( textoMenuPedido,chat_id));
          
            
            etapa[usuarioSel] = 4;
        }//no ingreso la opcion correcta
          else {
            //ESCRIBIMOS EL MENSAJE
  
        	  sendQueue.add(sendMsg("Opcion no valida\n",chat_id));;
				
				etapa[usuarioSel] = 1;
				 
			
            etapa[usuarioSel] = 4;
          }
       
      break;
      
  case CANTIDAD:
	  
	  objPedido.put("producto",mensajesRecibidosSinEspacios.toString());
	   
	   cantidad=mensajesRecibidosSinEspacios.toString();
	   
	   System.out.println(objPedido); 
	  textoCantidadPedido = "INGRESA LA CANTIDAD DE TU PRODUCTO "+ "\n";
	 
	  receiveQueue.add(DatosCliente.put("paso","PASO3"));
	  ActualizaDatosTomaPedido(mensage,object);
  	 
	//ESCRIBIMOS EL MENSAJE
	  sendQueue.add(sendMsg( textoCantidadPedido,chat_id));
    
	  
	  break;
	  
  case PASO3:
 
	   cantidad=mensajesRecibidosSinEspacios.toString();
	   valorUnitario=valor2;
	   valorTotal=valorUnitario*Integer.parseInt(cantidad);
	   objPedido.put("cantidad",mensajesRecibidosSinEspacios.toString());
	   objPedido.put("valor_unitario",valorUnitario);
	   objPedido.put("valor_total",valorTotal);
	   textoOpcionPedido = "INGRESA UNA OPCION "+ "\n"+"\n1.-Agregar otro producto\n2.-Agregar datos de envio";
	   
	   receiveQueue.add(DatosCliente.put("paso","PASO4"));
	   ActualizaDatosTomaPedido(mensage,object);
	 //ESCRIBIMOS EL MENSAJE
	   
	  sendQueue.add(sendMsg( textoOpcionPedido,chat_id));
  	
      
      break;
      
  case PASO4:
	   etapa5();
	   return;
      } 
      
      
   }else {
   //ESCRIBIMOS EL MENSAJE
	message.setText("Opcion no valida\n");
   try {
		execute(message);
	} catch (TelegramApiException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
   
   etapa[usuarioSel] = 1;
     }
 
  etapa[usuarioSel] = 4;
  
 
        
}  
	           
public void etapa5() {
	
	 //VARIABLE QUE RECIBE LAS OPCIONES COLOCADAS POR EL USUARIO DE WA
   int opcion;
   //QUITAMOS ESPACIOS EN BLANCO
   String mensajesRecibidosSinEspacios = mensajesRecibidos[usuarioSel].replace(" ", "");
   
 //VALIDAMO SI REALMENTE SE ENVIO UN NUMERO
   if (isNumeric(mensajesRecibidosSinEspacios)) {
       opcion = Integer.parseInt(mensajesRecibidosSinEspacios);

	   if(opcion >= 1 && opcion <= 2) {
		   
		   if(opcion==1) {
			  
			   mensajesRecibidos[usuarioSel]="2"; 
			   
		       DetallesPedido(mensage);
			   etapa2(); 
			   return;
		   }
           if (opcion==2) {
        	      
        	   receiveQueue.add(DatosCliente.put("paso","DIRECCION"));
        	   ActualizaDatosTomaPedido(mensage,object);
     	    
    	     etapa6();
    	     return;
            }
       String textoenviopedio = "datos envio "+ "\n";
       //ESCRIBIMOS EL MENSAJE
       sendQueue.add(sendMsg( textoenviopedio,chat_id));
  
    etapa[usuarioSel] = 4;
  	  
        }else {
        	  //ESCRIBIMOS EL MENSAJE
           	
           	message.setText("Opcion no valida\n");
               try {
    				execute(message);
    			} catch (TelegramApiException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
               
               etapa[usuarioSel] = 1;
          
        }
       
       } else {
           //ESCRIBIMOS EL MENSAJE
       	
       	message.setText("Opcion no valida\n");
           try {
				execute(message);
			} catch (TelegramApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
           
           etapa[usuarioSel] = 1;
         
       }
   
}
public void etapa6() {
	
  //QUITAMOS ESPACIOS EN BLANCO
  String mensajesRecibidosSinEspacios = mensajesRecibidos[usuarioSel];
  if (Datos!=envioDatos.FINALIZAR) { 
	  getDatosCliente(mensage); 
	  ActualizaDatosTomaPedido(mensage,object);  
	  Datos=envioDatos.valueOf(Paso);
      
      System.out.println(Datos);
     switch (Datos) {
	    case  DIRECCION:
	    	String direccion= "INGRESA LA DIRECION DE ENVIO "+ "\n";
   	 //ESCRIBIMOS EL MENSAJE
	    	sendQueue.add(sendMsg( direccion,chat_id));
        
	        break;
	    case TELEFONO:
	    	String telefono= "INGRESA UN NUMERO DE CONTACTO "+ "\n";
	      	 //ESCRIBIMOS EL MENSAJE
	    	sendQueue.add(sendMsg( telefono,chat_id));
	         
	        break;
	    case OBSERVACION:
	    	String observacion= "INGRESA ALGUNA NOTA SOBRE TU PEDIDO O DATOS "+ "\n";
	      	 //ESCRIBIMOS EL MENSAJE
	    	sendQueue.add(sendMsg( observacion,chat_id));
	        
	        break;
	    case FINALIZAR:
	    	
	    	receiveQueue.add(DatosCliente.put("estado","CERRADO")); 
	    	ActualizaDatosTomaPedido(mensage,object);
	      DetallesPedido(mensage); 
	      String finalizar= "GRACIAS POR TU COMPRA "+ "\n";
	      	 //ESCRIBIMOS EL MENSAJE
	      sendQueue.add(sendMsg(finalizar,chat_id));
	         
	        break;
	    }
     
	  etapa[usuarioSel] = 7;
  }else {
		etapa[usuarioSel] = 1;
		}
	   
  
}
    
   
public void etapa7(envioDatos envios) {
	
	//QUITAMOS ESPACIOS EN BLANCO
	  String mensajesRecibidosSinEspacios = mensajesRecibidos[usuarioSel];

	  switch (Datos) {
	    case  DIRECCION:
	    	receiveQueue.add(DatosCliente.put("direccion",mensajesRecibidosSinEspacios));
	    	receiveQueue.add(DatosCliente.put("paso","TELEFONO"));
	    	
	    	ActualizaDatosTomaPedido(mensage,object);
	    	
	    	//TomaDatosCliente(Datos);
	        break;
	    case TELEFONO:
	    	receiveQueue.add(DatosCliente.put("telefono",mensajesRecibidosSinEspacios));
	    	receiveQueue.add(DatosCliente.put("paso","OBSERVACION"));
	    	
	    	ActualizaDatosTomaPedido(mensage,object);
	     
	        break;
	    case OBSERVACION:
	    	receiveQueue.add(DatosCliente.put("observacion",mensajesRecibidosSinEspacios));
	    	receiveQueue.add(DatosCliente.put("paso","FINALIZAR"));
	    	ActualizaDatosTomaPedido(mensage,object);;
	    	
	    	
	        break;
	    case FINALIZAR:
	      System.out.println("estoy en finalizar");
	      
	        break;
	 }
	etapa6();
	
	
}
public enum envioDatos{
	  DIRECCION,TELEFONO,OBSERVACION,FINALIZAR,PRODUCTO,CANTIDAD,PASO3,PASO4
	  
	 
}
public String opcionMenu(int dia) {
    if (dia == 1) {
        return "ALMUERZOS";
    }
    if (dia == 2) {
        return "DESAYUNOS";
    }
    if (dia == 3) {
        return "BEBIDAS";
    }
  
    return "";
}
//FUNCION QUE ESPERA UN TIEMPO
public void pausa(long sleeptime) {
    try {
        Thread.sleep(sleeptime);
    } catch (InterruptedException ex) {
    }
}
private void leemosDatos() {
 
	try {
		URL url= new URL("http://localhost:8080/info/saludo");
		HttpURLConnection conn=(HttpURLConnection)url.openConnection();
		conn.setRequestMethod("GET");
		//conn.connect();
		
		int responsecode= conn.getResponseCode();
		if(responsecode!=200) {
			throw new RuntimeException("error el codigo " + responsecode);
		}else {
			
		  InputStream respuesta= conn.getInputStream();
		  BufferedReader br = new BufferedReader(new InputStreamReader(respuesta,"UTF-8"));
		  
		  String cntJson=br.readLine();

	       JSONObject jsonObject = new JSONObject(cntJson);
	       
	       saludo=(String)jsonObject.get("descripcion");
	  
		}
		
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
 }
  private void leemosMenu() {
     
		try {
			URL url= new URL("http://localhost:8080/api/v1/menu/"+ categoria);
			HttpURLConnection conn=(HttpURLConnection)url.openConnection();
			conn.setRequestMethod("GET");
			int responsecode= conn.getResponseCode();
			if(responsecode!=200) {
				throw new RuntimeException("error el codigo " + responsecode);
			}else {
				
			  InputStream respuesta= conn.getInputStream();
			  byte[] result= respuesta.readAllBytes();
			  String cntJson="";
			  
			  for(byte tmp:result ) 
				  cntJson +=(char)tmp;
		  
			  JSONArray jsonArray = new JSONArray(cntJson);
			  
		    for(Object obj:jsonArray  ) {
		    	 registro.add(((JSONObject) obj).getString("producto"));
		    	 registro.add(((JSONObject) obj).getInt("precio"));
		    	 
		    	 registrosMenu.add(registro);
		    	 registrosMenu.add(registro);
		     }
		     
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
  public void leerRegistroMenu() {
	  int cont=0;
      int cont2=1;
      String comida;
      String valor;
	  for(ArrayList<Object> registros:registrosMenu ) {
			
    	    if (cont<=registros.size() && cont2<=registros.size()) {
    		comida=  registros.get(cont).toString();
    	
    		cont+=2;
            valor= registros.get(cont2).toString();
			
            cont2+=2;
			
			textoMenuDia = textoMenuDia + comida + ": $" + valor + "\n";
			
    	 }
    	         
    } 
	  
  }
	private void leerMenuPedido() {
		  
		JSONObject jsonObj=null;
		Long idProducto;
	    String producto;
		try {
			URL url= new URL("http://localhost:8080/api/v1/menu/"+ categoria);
			HttpURLConnection conn=(HttpURLConnection)url.openConnection();
			conn.setRequestMethod("GET");
			int responsecode= conn.getResponseCode();
			
			if(responsecode!=200) {
				throw new RuntimeException("error el codigo " + responsecode);
			}else {
				
			  InputStream respuesta= conn.getInputStream();
			  byte[] result= respuesta.readAllBytes();
			  String cntJson="";
			  
			  for(byte tmp:result ) 
				  cntJson +=(char)tmp;
		  
			  JSONArray jsonArray = new JSONArray(cntJson);
			 
			  for(Object obj:jsonArray  ) {
				  idProducto=(((JSONObject) obj).getLong("id_producto"));
				  producto=(((JSONObject) obj).getString("producto"));
		  	      valor2=(((JSONObject) obj).getInt("precio"));
		    	textoMenuPedido = textoMenuPedido + idProducto + ".-" + producto + "\n";
		    	System.out.println(textoMenuPedido);
		    
		     }
		 
			  respuesta.close();
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     
		
       
    }
  
  
  public void CrearPedido(Message mensage ) {
 
		     String url="http://localhost:8080/api/v1/pedido/"+mensage.getChatId();
			 String metodo="GET";
	
			 try {
				  conn= conexionApi(url,metodo);
				  int responsecode= conn.getResponseCode();

				  InputStream respuesta= conn.getInputStream();
				  byte[] result= respuesta.readAllBytes();
				  String cntJson="";
				  
				  for(byte tmp:result ) 
					  cntJson +=(char)tmp;
				  
				  JSONArray jsonArray = new JSONArray(cntJson);
				  
				  for(Object obj:jsonArray  ) {
					  id_pedido=(((JSONObject) obj).getLong("pedido_id"));
					  id_chat=(((JSONObject) obj).getLong("chat_id"));
					  estado = (((JSONObject) obj).getString("estado"));
					  System.out.println(estado);

				     }
				  if(id_chat==null|| estado.equalsIgnoreCase("CERRADO") || id_chat== mensage.getChatId() ) {
					    String url2="http://localhost:8080/api/v1/pedido";
						String metodo2="POST";
					    conn= conexionApi(url2,metodo2);
						//JSONArray array=new JSONArray();
						DatosCliente.put("chatId",mensage.getChatId());
						DatosCliente.put("cliente",mensage.getChat().getFirstName());
						DatosCliente.put("paso","PRODUCTO");
		                DatosCliente.put("estado","ABIERTO");
						//array.put(DatosCliente);
						data= DatosCliente.toString();
						System.out.println(data);
						conn.setRequestProperty("Content-type","application/json");
						conn.setDoOutput(true);
						OutputStream output= conn.getOutputStream();
						output.write(data.getBytes());
						output.flush();
						output.close();
						respuesta.close();
						System.out.println(conn.getResponseCode());
				  }else {
					  System.out.println("el pedido ya esta creado");
				  }
				  
			 } catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
 	 
    } 
  
public void getDatosCliente(Message mensage) {
	String url="http://localhost:8080/api/v1/pedido/"+mensage.getChatId();
	 String metodo="GET";
	 

	 try {
		  conn= conexionApi(url,metodo);
		  int responsecode= conn.getResponseCode();
      
		  InputStream respuesta= conn.getInputStream();
		  byte[] result= respuesta.readAllBytes();
		  String cntJson="";
		  
		  for(byte tmp:result ) 
			  cntJson +=(char)tmp;
		  
		  JSONArray jsonArray = new JSONArray(cntJson);
		  for(Object obj:jsonArray  ) {
			  Id_pedidoCliente=(((JSONObject) obj).getLong("pedido_id"));
			  Paso = (((JSONObject) obj).getString("paso"));
			  System.out.println(Paso);
		     }
		  respuesta.close();
	 } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
}
  
 public void ActualizaDatosTomaPedido( Message mensage,Object dato) { 
	    
  
		try {
		 String url3="http://localhost:8080/api/v1/pedido/"+ Id_pedidoCliente;
	     String metodo3="PUT"; 
	     
		  if(Id_pedidoCliente!=0) {
	    System.out.println(Id_pedidoCliente);
	    conn= conexionApi(url3,metodo3);
		JSONArray array=new JSONArray();
		//JSONObject DatosClienteTel=new JSONObject();
		//DatosCliente.put("paso",dato);
		//DatosCliente.put("estado",dato);
		array.put(DatosCliente);
		data= DatosCliente.toString();
		System.out.println(data);
		conn.setRequestProperty("Content-type","application/json");
		conn.setDoOutput(true);
		OutputStream output= conn.getOutputStream();
		output.write(data.getBytes());
		output.flush();
		output.close();
		
		System.out.println(conn.getResponseCode()); 
}else {
	  System.out.println("no hay pedido");
}
	 
} catch (Exception e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}     

}
/* public void ActualizaDatosTomaPedido( Message mensage) {
	   
	 RestTemplate restTemplate = new RestTemplate();

       String resourceUrl = "http://localhost:8080/api/v1/pedido/"+mensage.getChatId();

       // Fetch JSON response as String wrapped in ResponseEntity
       List<?> products
       = restTemplate.getForObject(resourceUrl, List.class);;
       
       
       
       System.out.println(products);
 }*/
public void registroCola() {
	Thread thread = new Thread(new Runnable() { 
	@Override
	    public void run() {
		log.info("[STARTED] MsgSender.  Bot class: " + bot);
		  try {
	        while (true) {
	            for (object = receiveQueue.poll(); object != null; object = receiveQueue.poll()) {
	            	log.debug("Get new msg to send " + object);
	            	ActualizaDatosTomaPedido(mensage,object);
	            	
	            	System.out.println(object);
	            }
	            try {
                  Thread.sleep(1000);
              } catch (InterruptedException e) {
                  log.error("Take interrupt while operate msg list", e);
              }
	        } 
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
	 
	}  
	});
	thread.start();
	System.out.println(thread.getName());

}

public void threadMensage() {
	Thread thread = new Thread(new Runnable() { 
	@Override
	    public void run() {
		log.info("[STARTED] MsgSender.  Bot class: " + bot);
		  try {
	        while (true) {
	            for (object = sendQueue.poll(); object != null; object = sendQueue.poll()) {
	            	log.debug("Get new msg to send " + object);
	            	send(object);
	            	System.out.println(object);
	            }
	            try {
                  Thread.sleep(500);
              } catch (InterruptedException e) {
                  log.error("Take interrupt while operate msg list", e);
              }
	        } 
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
	} 
	private void send(Object object) throws TelegramApiException {
		if (object instanceof BotApiMethod) {   
		BotApiMethod<Message> message = (BotApiMethod<Message>) object;
            
		  execute(message);
		}else {
			System.out.println("no es");
		}
	}
	});
	thread.start();
	System.out.println(thread.getName());

}
	
 public void DetallesPedido(Message mensage ) {

	 String url="http://localhost:8080/api/v1/pedido/"+mensage.getChatId();
	 String metodo="GET";
	 String url2="http://localhost:8080/api/v1/detallepedido";
	 String metodo2="POST";
	 long id_pedido=0;
	 try {
		  conn= conexionApi(url,metodo);
		  int responsecode= conn.getResponseCode();

		  InputStream respuesta= conn.getInputStream();
		  byte[] result= respuesta.readAllBytes();
		  String cntJson="";
		  
		  for(byte tmp:result ) 
			  cntJson +=(char)tmp;
		  
		  JSONArray jsonArray = new JSONArray(cntJson);
		  for(Object obj:jsonArray  ) {
			  id_pedido=(((JSONObject) obj).getLong("pedido_id"));
			 
		     }
		  if(id_pedido!=0) {
		  
		    conn= conexionApi(url2,metodo2);
			objPedido.put("pedido",id_pedido);
		    DatosPedido.put(objPedido);
			String data= objPedido.toString();
			System.out.println(data);
			conn.setRequestProperty("Content-type", "application/json");
			conn.setDoOutput(true);
			OutputStream output= conn.getOutputStream();
			output.write(data.getBytes());
			
			output.flush();
			output.close();
			System.out.println(conn.getResponseCode());
		  }	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 
    } 
  public HttpURLConnection conexionApi (String url,String metodo)  {
	  conn=null;
	  try {   
	  URL Url= new URL(url);
		conn=(HttpURLConnection)Url.openConnection();
		conn.setRequestMethod(metodo);
		
		
	 }catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 return conn;
  }

  //FUNCION QUE VALIDA SI LA CADENA DE TEXTO ES UN NUMERO
    public boolean isNumeric(String cadena) {
        try {
            Float.parseFloat(cadena);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
   
}


