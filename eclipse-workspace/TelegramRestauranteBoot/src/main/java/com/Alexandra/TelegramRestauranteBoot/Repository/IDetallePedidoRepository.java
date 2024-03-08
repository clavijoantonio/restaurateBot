package com.Alexandra.TelegramRestauranteBoot.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Alexandra.TelegramRestauranteBoot.Model.DetallePedidoModel;

public interface IDetallePedidoRepository extends JpaRepository<DetallePedidoModel,Long> {

}
