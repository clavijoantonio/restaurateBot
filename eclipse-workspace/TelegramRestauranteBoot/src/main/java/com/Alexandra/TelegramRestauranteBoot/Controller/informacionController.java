package com.Alexandra.TelegramRestauranteBoot.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Alexandra.TelegramRestauranteBoot.Model.MenuModel;
import com.Alexandra.TelegramRestauranteBoot.Model.informacionModel;
import com.Alexandra.TelegramRestauranteBoot.Service.informacionService;

@RestController
@RequestMapping(value="/info")
public class informacionController {
    @Autowired
	informacionService servi;
	
    @GetMapping("/{nombre}")
	public ResponseEntity<Object> getbyDescrip(@PathVariable String nombre ){ 
		try {
			informacionModel data  = servi.verDatos(nombre);
			return new ResponseEntity<Object>(data,HttpStatus.OK);
		} 
		catch (Exception e) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("no se encontro la solicitud", e.getMessage());
			return new ResponseEntity<>( map, HttpStatus.INTERNAL_SERVER_ERROR);
		} 
 	}
}
