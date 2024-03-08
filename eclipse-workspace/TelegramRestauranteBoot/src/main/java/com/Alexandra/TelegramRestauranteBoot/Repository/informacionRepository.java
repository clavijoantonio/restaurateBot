package com.Alexandra.TelegramRestauranteBoot.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.Alexandra.TelegramRestauranteBoot.Model.informacionModel;

@Repository
public interface informacionRepository extends JpaRepository<informacionModel, Long>{

    informacionModel findBynombre (String nombre);
	  
}
