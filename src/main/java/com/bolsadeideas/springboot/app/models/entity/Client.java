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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "clients")
public class Client implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty
	@Column(name = "first_name")
	private String firstName;

	@NotEmpty
	@Column(name = "last_name")
	private String lastName;

	@NotEmpty
	@Email
	private String email;

	// usamos @Column pues que las convenciones son distintos NON_camell_case
	@NotNull
	@Column(name = "create_at")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date createAt;

	// atributo para la imagen de fichero

	private String photo;

	// un cliente puede tener varias facturas
	// mappedBy seria el atributo de la otra clase de la relacion, es bidireccional
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "client")
	private List<Invoice> invoices;

	public Client() {
		invoices = new ArrayList<Invoice>();
	}

	/**
	 * creamos método prepersist es decir antes de que se guarde en la base de datos
	 * para asignar la fecha de creación, pero para que se ejecute este metodo como
	 * un evento que es parte del ciclo de vida de la clase entity, manejada con el
	 * entity manager tendra que ir decorada con la anotacion {@code @PrePersist},
	 * asi se va a llamar justo antes de invocar el método presist(entity) del
	 * Entity Manager, antes de insertar el registro en la base de datos
	 */
	/*
	 * @PrePersist public void prePersist() { createAt = new Date(); }
	 */

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public List<Invoice> getInvoices() {
		return invoices;
	}

	public void setInvoices(List<Invoice> invoices) {
		this.invoices = invoices;
	}

	/**
	 * De manera opcional pero recomendada agregamos el método addInvoice, la idea
	 * de este método es agregar factura por factura en la clase Client, tal como si
	 * fuera un objeto de coleccion, es parecida al setInvoices pero en ves de
	 * guardar una lista, guarda las facturas una por una
	 */
	public void addInvoice(Invoice invoice) {
		invoices.add(invoice);
	}

}
