package com.Alexandra.TelegramRestauranteBoot.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="detalle_pedido")
public class DetallePedidoModel {
	
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		@Column(name="id_detalle_pedido")
		private long id_detalle_pedido;
		@Column(name="pedido")
		private long pedido;
		@Column(name="producto")
		private String producto;
		@Column(name="cantidad")
		private int cantidad;
		@Column(name="valor_unitario")
		private double valor_unitario;
		@Column(name="valor_total")
		private double valor_total;
		@Column(name="observacion")
		private String observacon;
		public DetallePedidoModel(long id_detalle_pedido, long pedido, String producto, int cantidad,
				double valor_unitario, double valor_total, String observacon) {
			super();
			this.id_detalle_pedido = id_detalle_pedido;
			this.pedido = pedido;
			this.producto = producto;
			this.cantidad = cantidad;
			this.valor_unitario = valor_unitario;
			this.valor_total = valor_total;
			this.observacon = observacon;
		}
		public DetallePedidoModel() {
			super();
		}
		public long getId_detalle_pedido() {
			return id_detalle_pedido;
		}
		public void setId_detalle_pedido(long id_detalle_pedido) {
			this.id_detalle_pedido = id_detalle_pedido;
		}
		public long getPedido() {
			return pedido;
		}
		public void setPedido(long pedido) {
			this.pedido = pedido;
		}
		public String getProducto() {
			return producto;
		}
		public void setProducto(String producto) {
			this.producto = producto;
		}
		public int getCantidad() {
			return cantidad;
		}
		public void setCantidad(int cantidad) {
			this.cantidad = cantidad;
		}
		public double getValor_unitario() {
			return valor_unitario;
		}
		public void setValor_unitario(double valor_unitario) {
			this.valor_unitario = valor_unitario;
		}
		public double getValor_total() {
			return valor_total;
		}
		public void setValor_total(double valor_total) {
			this.valor_total = valor_total;
		}
		public String getObservacon() {
			return observacon;
		}
		public void setObservacon(String observacon) {
			this.observacon = observacon;
		}
		
		
}
