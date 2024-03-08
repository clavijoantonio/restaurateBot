package com.Alexandra.TelegramRestauranteBoot.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Alexandra.TelegramRestauranteBoot.Model.DetallePedidoModel;
import com.Alexandra.TelegramRestauranteBoot.Model.MenuModel;
import com.Alexandra.TelegramRestauranteBoot.Model.TomaPedidoModel;
import com.Alexandra.TelegramRestauranteBoot.Service.DetallePedidoService;

@RestController
@RequestMapping("/api/v1/detallepedido")
public class DetallePedidoController {
    @Autowired
	private DetallePedidoService pedidoservice;
	
	@PostMapping
    public DetallePedidoModel ingresarDetallePedido (@RequestBody DetallePedidoModel detallepedido) {
		
		return this.pedidoservice.insertDetallePedido(detallepedido);
	}
	


	@GetMapping
	public ResponseEntity<Object> get(){ 
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<DetallePedidoModel> list = pedidoservice.verDetallePedido();
			return new ResponseEntity<Object>(list,HttpStatus.OK);
		} 
		catch (Exception e) {
			map.put("No se encontraron pedido", e.getMessage());
			return new ResponseEntity<>( map, HttpStatus.INTERNAL_SERVER_ERROR);
		} 
 	}
	
	
}
