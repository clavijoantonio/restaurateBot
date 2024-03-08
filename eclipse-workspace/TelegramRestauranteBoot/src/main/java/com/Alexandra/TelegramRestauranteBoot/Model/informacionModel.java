package com.Alexandra.TelegramRestauranteBoot.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
@Entity
@Table(name="informacion")
public class informacionModel {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_informacion")
	private long id_informacion;
	@Column(name = "nombre")
	private String nombre;
	@Column(name = "descripcion")
	private String descripcion;
	
	
	public informacionModel() {
		super();
	}
	public informacionModel(long id_informacion, String nombre, String descripcion) {
		super();
		this.id_informacion = id_informacion;
		this.nombre = nombre;
		this.descripcion = descripcion;
	}
	public long getId_informacion() {
		return id_informacion;
	}
	public void setId_informacion(long id_informacion) {
		this.id_informacion = id_informacion;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	
}
