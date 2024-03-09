package TelegramBot;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.Alexandra.TelegramRestauranteBoot.Model.TomaPedidoModel;
import com.Alexandra.TelegramRestauranteBoot.Model.informacionModel;
import com.Alexandra.TelegramRestauranteBoot.Repository.informacionRepository;
import com.Alexandra.TelegramRestauranteBoot.Service.MenuService;
import com.Alexandra.TelegramRestauranteBoot.Service.TomaPedidoService;
import com.Alexandra.TelegramRestauranteBoot.Service.informacionService;

public class ServiciosTelegram extends TelegramLongPollingBot{
	double valorTotal;
	double valorUnitario;
	String valor2;
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
    HttpURLConnection  conn;
   
   TomaPedidoService pedidoService= new TomaPedidoService();
 
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
  

@Override
public void onUpdateReceived(Update update) {
	// TODO Auto-generated method stub
	 try {
            localPath = new File(".").getCanonicalPath();
            System.out.println( localPath);
        } catch (IOException ex) {
           System.out.println("no ecuentro la ruta");
        }
	if (update.hasMessage() && update.getMessage().hasText()) {
        // Set variables
        nombreContacto = update.getMessage().getChat().getFirstName();
        String user_last_name = update.getMessage().getChat().getLastName();
        String user_username = update.getMessage().getChat().getUserName();
        long user_id = update.getMessage().getChat().getId();
        message_text = update.getMessage().getText();
        long chat_id = update.getMessage().getChatId();
        mensage=update.getMessage();
        
        System.out.println(mensage+"  "+nombreContacto);
       // Create a message object object
        
        message.setChatId(update.getMessage().getChatId().toString());
      //VARIABLE QUE ALMACENARA LOS ULTIMOS MENSAJE ENVIADOS
        mensajesRecibidos[usuarioSel] = "";
        
      
       
        leemosDatos();
       // leemosMenu();
        revisionTel();
       
	}
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
	if(message_text!=null) {
    	
    	//etapa[usuarioSel]=1;
    	//System.out.println("hay mensaje");
    	
    }
    
   boolean contactoEncontrado = false;
 //VALIDAMOS QUE EL NOMBRE DE CONTACTO NO ESTE EN LA LISTA DE CONTACTOS DE LO CONTRARIO NOS POSICIONAMOS EN EL ID DEL CONTACTO
   for (int tabSel = 1; tabSel <= cantTabs; tabSel++) {
       //SI EL CONTACTO YA EXISTE EN LA LISTA
       if (nombreContacto.equals(contactos[tabSel])) {
           //ACTIVAMOS LA BANDERA Y DECMOS QUE SI ENCONTRAMOS EL CONTACTO
           contactoEncontrado = true;
           usuarioSel = tabSel;
          // System.out.println( usuarioSel);
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
    
                        
    } catch (TelegramApiException e) {
        e.printStackTrace();
    }
}
       

public void etapa1() throws TelegramApiException {
	
	String listaOpciones = saludo + "\n1.-Menu del dia de hoy\n2.-Toma Pedido\n3.-Horarios\n4.-Acerca de nosotros\n";
	
	message.setText(listaOpciones);
	 
	execute(message);
	
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
                message.setText(listamenu);
                
                
                try {
					execute(message);
				} catch (TelegramApiException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
               
                etapa[usuarioSel] = 3;
            }
            if (opcion == 2) {
                String textotomaPedido = "Bienvenido a nuestro Toma Pedido\n"
            + "\n1.-Almuerzo\n2.-Desayunos\n3.-Bebidas";
                        
                //ESCRIBIMOS EL MENSAJE
                message.setText(textotomaPedido);
                try {
					execute(message);
				} catch (TelegramApiException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
               
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
            message.setText( textoMenuDia);
              
            }
              if (opcion==2) {
            	
            	categoria=1;
  
            	leemosMenu();
                
            	leerRegistroMenu();
              
            	registro.clear();
            //ESCRIBIMOS EL MENSAJE
            message.setText( textoMenuDia);
              
            }
              if (opcion==3) {
              	
              	categoria=3;
    
              	leemosMenu();
                  
              	leerRegistroMenu();
                
              	registro.clear();
              //ESCRIBIMOS EL MENSAJE
              message.setText( textoMenuDia);
                 
              }
            try {
				execute(message);
			} catch (TelegramApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
    System.out.println(mensajesRecibidosSinEspacios);
  int conta=0;
//VALIDAMO SI REALMENTE SE ENVIO UN NUMERO
  if (isNumeric(mensajesRecibidosSinEspacios)) {
      opcion = Integer.parseInt(mensajesRecibidosSinEspacios);
    	   
     
    	 
    	if(producto==null) {
    		 producto= mensage.getText();    
            
        //SI LA OPCION ESTA ENTRE 1 Y 3
        if (opcion >= 1 && opcion <= 3) {
        	textoMenuPedido = "INGRESA EL CODIGO DE TU PRODUCTO "+ "\n";
        	
            if (opcion==1) {
            	
            	categoria=2;
  
            	leerMenuPedido();
                
            	leerRegistroMenuPedido();
            	
            	registroPedido.clear();
            	
                //ESCRIBIMOS EL MENSAJE
                message.setText( textoMenuPedido);
            }
              if (opcion==2) {
            	
            	categoria=1;
                
            	leerMenuPedido();
                
            	leerRegistroMenuPedido();
              
            	registroPedido.clear();
            //ESCRIBIMOS EL MENSAJE
            message.setText( textoMenuPedido);
              
            }
              if (opcion==3) {
              	
              	categoria=3;
    
                leerMenuPedido();
                
            	leerRegistroMenuPedido();
            	
            	registroPedido.clear();
            	
              //ESCRIBIMOS EL MENSAJE
              message.setText( textoMenuPedido);
                 
              }
             
            try {
				execute(message);
			
			} catch (TelegramApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            etapa[usuarioSel] = 4;
           
       
        }//no ingreso la opcion correcta
          else {
            //ESCRIBIMOS EL MENSAJE
        	
        	message.setText("Opcion no valida\n");
            try {
				execute(message);
				
				etapa[usuarioSel] = 1;
				 
			} catch (TelegramApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            etapa[usuarioSel] = 4;
          }
        
       
           }// llave if
    	  
           else if(cantidad==null) {
        	   
        	   objPedido.put("producto",mensage.getText());
        	   
        	   cantidad=mensage.getText();
        	   
        	   System.out.println(objPedido); 
        	  textoCantidadPedido = "INGRESA LA CANTIDAD DE TU PRODUCTO "+ "\n";
        	//ESCRIBIMOS EL MENSAJE
              message.setText( textoCantidadPedido);
        	  try {
  				execute(message);
  				
  				
  				 
  			} catch (TelegramApiException e) {
  				// TODO Auto-generated catch block
  				e.printStackTrace();
  			}
           }else if(cantidad!=null) {
        	   cantidad=mensage.getText();
        	   valorUnitario=Integer.parseInt(valor2);
        	   valorTotal=valorUnitario*Integer.parseInt(cantidad);
        	   objPedido.put("cantidad",mensage.getText());
        	   objPedido.put("valor_unitario",valorUnitario);
        	   objPedido.put("valor_total",valorTotal);
        	   textoOpcionPedido = "INGRESA UNA OPCION "+ "\n"+"\n1.-Agregar otro producto\n2.-Agregar datos de envio";;
        	 //ESCRIBIMOS EL MENSAJE
           	message.setText(textoOpcionPedido);
               try {
       			execute(message);
       		// break;
       		} catch (TelegramApiException e) {
       			// TODO Auto-generated catch block
       			e.printStackTrace();
       		}
        	   etapa[usuarioSel] = 5; 
        	     
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
			   
			   producto=null;
			   cantidad=null;
			   DatosTomaPedido(mensage);
		       DetallesPedido(mensage);
			   etapa2(); 
			   return;
		   }
           if (opcion==2) {
    	  System.out.println("elegi dos");
    	   
            }
       String	 textoenviopedio = "datos envio "+ "\n";
       //ESCRIBIMOS EL MENSAJE
     message.setText( textoenviopedio);
     try {
			execute(message);
			 
		} catch (TelegramApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		  byte[] result= respuesta.readAllBytes();
		  //System.out.println(result);
		  String cntJson="";
		  
		  for(byte tmp:result ) 
			  cntJson +=(char)tmp;
	   
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
		    	registroPedido.add(((JSONObject) obj).getLong("id_producto"));
		    	registroPedido.add(((JSONObject) obj).getString("producto"));
		    	registroPedido.add(((JSONObject) obj).getInt("precio"));
		    	System.out.println(registroPedido);
		    	 
		    	 registrosMenuPedido.add(registroPedido);
		    	 registrosMenuPedido.add(registroPedido);
		     }
		     
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
  public void leerRegistroMenuPedido() {
	  int cont=0;
      int cont2=1;
      int cont3=2;
      String idProducto;
      String producto;
	  for(ArrayList<Object> menu:registrosMenuPedido ) {
		  
		 
		  
    	    if (cont<=menu.size() && cont2<=menu.size()) {
    		
    	    
    	    
    	    idProducto=  menu.get(cont).toString();
    	    
    		cont+=3;
            producto= menu.get(cont2).toString();
			
            cont2+=3;
            
            valor2=menu.get(cont3).toString();
            
            cont3+=3;
            
            textoMenuPedido = textoMenuPedido + idProducto + ".-" + producto + "\n";
           
    	 }
    	         
    } 
	  
  }
  
  public void DatosTomaPedido(Message mensage ) {
  
	  
		     String url="http://localhost:8080/api/v1/pedido/"+mensage.getChatId();
			 String metodo="GET";
			 String url2="http://localhost:8080/api/v1/pedido";
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
			
			JSONObject obj=new JSONObject();
			JSONArray array=new JSONArray();
			obj.put("chatId",mensage.getChatId());
			obj.put("cliente",mensage.getChat().getLastName());
			array.put(obj);
			data= obj.toString();
			System.out.println(data);
			conn.setRequestProperty("Content-type", "application/json");
			conn.setDoOutput(true);
			OutputStream output= conn.getOutputStream();
			output.write(data.getBytes());
			output.flush();
			output.close();
			System.out.println(conn.getResponseCode());
		  }else {
			  System.out.println("el pedido ya esta creado");
		  }
			 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
			 
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
  public JSONObject valorArray(Message mensage) {
	  objPedido.put("chatId",mensage.getChatId());
	  objPedido.put("cliente",mensage.getChat().getLastName());
	  
	 // objPedido.put("cantidad",mensage.getText());
	  
	  DatosPedido.put(objPedido);
	  return objPedido;
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
