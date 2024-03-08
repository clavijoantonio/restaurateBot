package com.Alexandra.TelegramRestauranteBoot.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.Alexandra.TelegramRestauranteBoot.Model.TomaPedidoModel;

public interface ITomapedidoRepository extends JpaRepository<TomaPedidoModel,Long> {
   
	@Query(value= "SELECT * FROM pedido WHERE chat_id=?1",
			nativeQuery=true)
	public List <TomaPedidoModel> buscar(Long chat_id);
	
}
