package com.jhomar.ProjectManager.modelos;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="projects")
public class Project {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty(message="El campo de titulo es obligatorio.")
	@Size(min=3, max=30, message="Titulo debe de tener entre 2 y 30 caracteres")
	private String title;
	
	@NotEmpty(message="El campo de description es obligatorio.")
	@Size(min=5, max=255, message="Description debe de tener entre 2 y 30 caracteres")
	private String description;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date due_date;
	
	@Column(updatable=false)
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date created_at;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date updated_at;
	
	//====RELACIONES=========
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_id")
	private User user; //Creador del proyecto -> MappedBy
	
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(
			name="projects_has_users",
			joinColumns=@JoinColumn(name="project_id"), //Mi id
			inverseJoinColumns = @JoinColumn(name="user_id")//el id del otro
	)
	
	private List<User> users; //Los usuarios que se unieron en el proyecto(unidos)

	
	//=======================
	public Project() {

	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public Date getDue_date() {
		return due_date;
	}


	public void setDue_date(Date due_date) {
		this.due_date = due_date;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	public List<User> getUsers() {
		return users;
	}


	public void setUsers(List<User> users) {
		this.users = users;
	}
	
	
	
	
	
	
	
}
