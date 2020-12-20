package com.bolsadeideas.springboot.app.models.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

/*como buena practica toda clase entity debe comenzar por la implementación de la interfáz serializable*/
@Entity
@Table(name = "invoices")
public class Invoice implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String description;
	private String observation;

	@Temporal(TemporalType.DATE)
	@Column(name = "create_at")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date createAt;

	/*
	 * Muchas facturas un cliente, Lazy para evitar que cuando se hace una consulta
	 * de la Invoice tambien haga una consulta del Client
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Client client;

	/*
	 * obtener los items de la factura, tenemos que indicar cuando la relación es en
	 * un solo sentido cual es la llave foránea que va a relacionar en la tabla
	 * (invoice) con (invoice_items) lo indicamos con la
	 * anotación @JoinColumn(nombre_de_la_columna), es decir que vamos a tener un
	 * campo o llave forana "invoice_id" en la tabla "invoice_items"(en la tabla de
	 * la relación)
	 */
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "invoice_id")
	List<InvoiceLineItem> items;

	public Invoice() {
		this.items = new ArrayList<InvoiceLineItem>();
	}

	@PrePersist
	public void prePersist() {
		createAt = new Date();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public List<InvoiceLineItem> getItems() {
		return items;
	}

	public void setItems(List<InvoiceLineItem> items) {
		this.items = items;
	}

	public void addInvoiceLineItem(InvoiceLineItem item) {
		this.items.add(item);
	}

	public Double getTotal() {
		Double total = 0.0;
		int size = items.size();
		for (int i = 0; i < size; i++) {
			total += items.get(i).calculateAmount();

		}
		return total;
	}

	private static final long serialVersionUID = 1L;
}
