package com.Alexandra.TelegramRestauranteBoot.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="pedido")
public class TomaPedidoModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="pedido_id")
	private long pedido_id;
	@Column(name="chat_id")
	private long chat_id;
	@Column(name="cliente")
	private String cliente;
	@Column(name="direccion")
	private String direccion;
	@Column(name="telefono")
	private String telefono;
	@Column(name="observacion")
	private String observacion;
	


	public TomaPedidoModel(long pedido_id, long chat_id, String cliente, String direccion, String telefono,
			String observacion) {
		super();
		this.pedido_id = pedido_id;
		this.chat_id = chat_id;
		this.cliente = cliente;
		this.direccion = direccion;
		this.telefono = telefono;
		this.observacion = observacion;
	}



	public TomaPedidoModel() {
		super();
	}



	public long getPedido_id() {
		return pedido_id;
	}


	public void setPedido_id(long pedido_id) {
		this.pedido_id = pedido_id;
	}

	

	public long getChat_id() {
		return chat_id;
	}



	public void setChatId(long chat_id) {
		this.chat_id = chat_id;
	}



	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	
	
}
