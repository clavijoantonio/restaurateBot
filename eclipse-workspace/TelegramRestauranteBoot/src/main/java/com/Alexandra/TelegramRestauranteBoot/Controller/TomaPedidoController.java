package com.Alexandra.TelegramRestauranteBoot.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Alexandra.TelegramRestauranteBoot.Model.MenuModel;
import com.Alexandra.TelegramRestauranteBoot.Model.TomaPedidoModel;
import com.Alexandra.TelegramRestauranteBoot.Service.TomaPedidoService;

@RestController
@RequestMapping("/api/v1/pedido")
public class TomaPedidoController {
	
	@Autowired
	private TomaPedidoService pedidoservi;
	
	@PostMapping
    public TomaPedidoModel ingresarMenu (@RequestBody TomaPedidoModel pedido) {
		
		return this.pedidoservi.insertTomaPedido(pedido);
	}
	
	
	@GetMapping("/{chat_id}")
	public ResponseEntity<Object> getById(@PathVariable Long chat_id){ 
		try {
		 List <TomaPedidoModel> data  = pedidoservi.verId_pedido(chat_id);
			return new ResponseEntity<Object>(data,HttpStatus.OK);
		} 
		catch (Exception e) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("message", e.getMessage());
			return new ResponseEntity<>( map, HttpStatus.INTERNAL_SERVER_ERROR);
		} 
 	}

	@GetMapping
	public ResponseEntity<Object> get(){ 
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<TomaPedidoModel> list = pedidoservi.verPedidos();
			return new ResponseEntity<Object>(list,HttpStatus.OK);
		} 
		catch (Exception e) {
			map.put("No se encontraron pedido", e.getMessage());
			return new ResponseEntity<>( map, HttpStatus.INTERNAL_SERVER_ERROR);
		} 
 	}
	
	

}
