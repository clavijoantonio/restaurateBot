package com.Alexandra.TelegramRestauranteBoot.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Alexandra.TelegramRestauranteBoot.Model.DetallePedidoModel;
import com.Alexandra.TelegramRestauranteBoot.Repository.IDetallePedidoRepository;

@Service
public class DetallePedidoService {
    
	@Autowired
	private IDetallePedidoRepository detallepedidoRepository;
	
     public DetallePedidoModel insertDetallePedido (DetallePedidoModel detallepedido) {
		
		return detallepedidoRepository.save( detallepedido);
		
	}
    
	 public List<DetallePedidoModel> verDetallePedido() {
		return detallepedidoRepository.findAll();
		 
	 }
	 
	

}
