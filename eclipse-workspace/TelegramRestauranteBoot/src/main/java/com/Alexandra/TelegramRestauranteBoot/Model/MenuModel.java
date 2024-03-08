package com.Alexandra.TelegramRestauranteBoot.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="menu")
public class MenuModel {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
	private long id_producto;
	@Column(name = "producto")
	private String producto;
	@Column(name = "categoria")
	private long categoria;
	@Column(name = "precio")
	private int precio;
	
	
	

	public MenuModel(long id_producto, String producto, long categoria, int precio) {
		super();
		this.id_producto = id_producto;
		this.producto = producto;
		this.categoria = categoria;
		this.precio = precio;
	}




	public MenuModel() {
		super();
	}


	

	public long getId_producto() {
		return id_producto;
	}


	public void setId_producto(int id_producto) {
		this.id_producto = id_producto;
	}


	public String getProducto() {
		return producto;
	}

	public void setProducto(String producto) {
		this.producto = producto;
	}

	public long getCategoria() {
		return categoria;
	}

	public void setCategoria(int categoria) {
		this.categoria = categoria;
	}

	public int getPrecio() {
		return precio;
	}

	public void setPrecio(int precio) {
		this.precio = precio;
	}
	
	
	
	
}
