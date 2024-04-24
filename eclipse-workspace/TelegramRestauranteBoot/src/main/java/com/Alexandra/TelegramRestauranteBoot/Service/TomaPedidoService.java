package com.Alexandra.TelegramRestauranteBoot.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Alexandra.TelegramRestauranteBoot.Model.TomaPedidoModel;
import com.Alexandra.TelegramRestauranteBoot.Repository.ITomapedidoRepository;



@Service
public class TomaPedidoService {
    @Autowired
	private ITomapedidoRepository repoPedido;
	
    public TomaPedidoModel insertTomaPedido (TomaPedidoModel request) {
		
		return repoPedido.save(request);	
	}
   
    public void actualizarPedido (TomaPedidoModel request,Long pedido_id) {
		
    	Optional<TomaPedidoModel> id_pedido=this.repoPedido.findById(pedido_id);
    	
    	TomaPedidoModel pedido= id_pedido.get();
    	
    	pedido.setDireccion(request.getDireccion());
    	pedido.setObservacion(request.getObservacion());
    	pedido.setTelefono(request.getTelefono());
    	pedido.setPaso(request.getPaso());
    	pedido.setEstado(request.getEstado());
    	repoPedido.saveAndFlush(pedido);
    }
    
	 public List<TomaPedidoModel> verPedidos() {
		return repoPedido.findAll();
		 
	 }
	 

	public List <TomaPedidoModel> verId_pedido(Long chat_id) {
			if(chat_id!=null) {
				return repoPedido.buscar(chat_id);
			}else {
				throw new IllegalArgumentException("no hay chatid");
			}
		
	}
	
	
	
}
