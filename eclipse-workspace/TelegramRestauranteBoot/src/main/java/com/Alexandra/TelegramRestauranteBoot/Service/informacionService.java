package com.Alexandra.TelegramRestauranteBoot.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Alexandra.TelegramRestauranteBoot.Model.informacionModel;
import com.Alexandra.TelegramRestauranteBoot.Repository.informacionRepository;

@Service
public class informacionService {
    
 @Autowired
 informacionRepository repo;
  
   
	public informacionModel  verDatos(String nombre) {

		return repo.findBynombre(nombre);
	}
	
}
