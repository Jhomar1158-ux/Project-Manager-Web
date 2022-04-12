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
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="users")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty(message="El campo de firstname es obligatorio.")
	@Size(min=2, max=30, message="Nombre debe de tener entre 2 y 30 caracteres")
	private String firstname;
	
	@NotEmpty(message="El campo de lastname es obligatorio.")
	@Size(min=2, max=30, message="Nombre debe de tener entre 2 y 30 caracteres")
	private String lastname;
	
	@NotEmpty(message="El campo de email es obligatorio.")
	@Email(message="Ingrese un correo electrónico válido")
	private String email;
	
	@NotEmpty(message="El campo de passsword es obligatorio.")
	@Size(min=6, max=128, message="La contraseña debe de ser entre 6 y 128 caracteres")
	private String password;
	
	@Transient //Es un atributo TEMPORAL, NO LO NECESITO EN MI BASE DE DATOS
	@NotEmpty(message="El campo de confirmación es obligatorio.")
	@Size(min=6, max=128, message="La confirmación de contraseña debe de ser entre 6 y 128 caracteres")
	private String confirm;
	
	//===RELACIONES===
	
	@OneToMany(mappedBy ="user", fetch=FetchType.LAZY)
	private List<Project> myProjects;//Los que proyectos que cree
	
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(
			name="projects_has_users",
			joinColumns=@JoinColumn(name="user_id"), //joinColumn -> Aquel que yo tengo //Mi id
			inverseJoinColumns=@JoinColumn(name="project_id") //inverse -> El que se relaciona con el proyecto
	)
	private List<Project> projectsJoined; // Proyectos a los que me uní
	
	
	//================
	@Column(updatable=false)
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date created_at;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date updated_at;
	
	@PrePersist
    protected void onCreate(){
        this.created_at = new Date();
    }
	
    @PreUpdate
    protected void onUpdate(){
        this.updated_at = new Date();
    }
	
	//Constructor
	public User() {
		
	}
	
	//Setters and Getters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirm() {
		return confirm;
	}

	public void setConfirm(String confirm) {
		this.confirm = confirm;
	}

	public List<Project> getMyProjects() {
		return myProjects;
	}

	public void setMyProjects(List<Project> myProjects) {
		this.myProjects = myProjects;
	}

	public List<Project> getProjectsJoined() {
		return projectsJoined;
	}

	public void setProjectsJoined(List<Project> projectsJoined) {
		this.projectsJoined = projectsJoined;
	}
	
	
	
	
	
	
	
}
