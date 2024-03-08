package com.Alexandra.TelegramRestauranteBoot.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Alexandra.TelegramRestauranteBoot.Model.MenuModel;


public interface IMenuRepository extends JpaRepository<MenuModel,Long>{

	List <MenuModel> findBycategoria(long categoria);
}
