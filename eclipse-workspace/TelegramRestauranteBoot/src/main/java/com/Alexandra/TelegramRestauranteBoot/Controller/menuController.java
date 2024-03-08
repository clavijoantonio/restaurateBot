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

import com.Alexandra.TelegramRestauranteBoot.Model.MenuModel;
import com.Alexandra.TelegramRestauranteBoot.Service.MenuService;

@RestController
@RequestMapping("/api/v1/menu")
public class menuController {
	
	@Autowired
	private MenuService menuserv;
	
	@PostMapping
    public MenuModel ingresarMenu (@RequestBody MenuModel menu) {
		
		return this.menuserv.insertMenu(menu);
	}
	
	@GetMapping("/{categoria}")
	public ResponseEntity<Object> getById(@PathVariable Long categoria){ 
		try {
			List <MenuModel> data  = menuserv.findBycategoria(categoria);
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
			List<MenuModel> list = menuserv.verMenu();
			return new ResponseEntity<Object>(list,HttpStatus.OK);
		} 
		catch (Exception e) {
			map.put("No se encontraron Factura", e.getMessage());
			return new ResponseEntity<>( map, HttpStatus.INTERNAL_SERVER_ERROR);
		} 
 	}
}
