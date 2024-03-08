package com.Alexandra.TelegramRestauranteBoot.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.Alexandra.TelegramRestauranteBoot.Model.MenuModel;
import com.Alexandra.TelegramRestauranteBoot.Repository.IMenuRepository;



@Service
public class MenuService {
	
	@Autowired
	IMenuRepository menuRepor;
	
	public MenuModel insertMenu (MenuModel menu) {
		
		return menuRepor.save(menu);
		
	}
	 public List<MenuModel> verMenu() {
		return menuRepor.findAll();
		 
	 }
	 
	public List <MenuModel> findBycategoria(Long categoria) {
		return  menuRepor.findBycategoria(categoria);
	}
}
