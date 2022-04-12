package com.jhomar.ProjectManager.servicios;

import java.util.List;
import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.jhomar.ProjectManager.modelos.LoginUser;
import com.jhomar.ProjectManager.modelos.Project;
import com.jhomar.ProjectManager.modelos.User;
import com.jhomar.ProjectManager.repositorios.ProjectRepository;
import com.jhomar.ProjectManager.repositorios.UserRepository;

@Service
public class AppService {
	
	
	@Autowired
	private UserRepository repositorio_user;
	
	@Autowired
	private ProjectRepository repositorio_project;
	
	public User register(User nuevoUsuario, BindingResult result) {
		
		String nuevoEmail = nuevoUsuario.getEmail();
		
		//Revisamos si existe el correo electrónico en BD
		if(repositorio_user.findByEmail(nuevoEmail).isPresent()) {
			result.rejectValue("email", "Unique", "El correo fue ingresado previamente.");
		}
		
		if(! nuevoUsuario.getPassword().equals(nuevoUsuario.getConfirm()) ) {
			result.rejectValue("confirm", "Matches", "Las contraseñas no coiniciden");
		}
		
		if(result.hasErrors()) {
			return null;
		} else {
			//Encriptamos contraseña
			String contra_encr = BCrypt.hashpw(nuevoUsuario.getPassword(), BCrypt.gensalt());
			nuevoUsuario.setPassword(contra_encr);
			//Guardo usuario
			return repositorio_user.save(nuevoUsuario);
		}
		
	}
	
	public User login(LoginUser nuevoLogin, BindingResult result) {
		
		if(result.hasErrors()) {
			return null;
		}
		
		//Buscamos por correo
		Optional<User> posibleUsuario = repositorio_user.findByEmail(nuevoLogin.getEmail());
		if(!posibleUsuario.isPresent()) {
			result.rejectValue("email", "Unique", "Correo ingresado no existe");
			return null;
		}
		
		//Optional -> Un objeto
		//Lista -> .get(0)
		User user_login = posibleUsuario.get();
		
		//Comparamos contraseñas encriptadas
		if(! BCrypt.checkpw(nuevoLogin.getPassword(), user_login.getPassword()) ) {
			result.rejectValue("password", "Matches", "Contraseña inválida");
		}
		
		if(result.hasErrors()) {
			return null;
		} else {
			return user_login; 
		}
		
		
	}
	
	
	public User save_user(User updatedUser) {
		return repositorio_user.save(updatedUser); //save() interno de CrudRepository
	}
	
	public User find_user(Long id) {
		Optional<User> optionalUser = repositorio_user.findById(id); //findById() interno de CrudRepository
		if(optionalUser.isPresent()) {
			return optionalUser.get(); 
		}else {
			return null;
		}
	}
	
	public Project save_project(Project nuevoProyecto) {
		return repositorio_project.save(nuevoProyecto);
	}
	
	public Project find_project(Long id) {
		Optional<Project> optionalProject = repositorio_project.findById(id);
		if(optionalProject.isPresent()) {
			return optionalProject.get(); 
		}else {
			return null;
		}
	}
	
	//============= MOSTRAR AMBAS TABLAS =============
	
	//Lista de Proyectos en los que se encuentra nuestro myUser
	public List<Project> find_my_projects(User myUser){
		return repositorio_project.findAllByUsers(myUser);
	}
	
	//Lista de Proyectos en los que no está nuestro USER
	public List<Project> find_other_projects(User myUser){
		return repositorio_project.findByUsersNotContains(myUser);
	}
	
	//Borramos un Proyecto con el método deleteById propio de CrudRepository
	public void delete_project(Long id) {
		repositorio_project.deleteById(id);
	}
	
	public void save_project_user(Long user_id, Long project_id) {
		
		//Ubicamos nuestros objetos a través de métodos ya creados en AppService
		//Almacenamos nuestro objeto proyecto a través del id
		Project thisProject = find_project(project_id);
		
		//Almacenamos nuestro objeto user a través del id
		User thisUser = find_user(user_id); 
		
		//Retorname la Lista de Proyectos de thisUser y AÑADE thisProject
		thisUser.getProjectsJoined().add(thisProject);
		
		//Guardamos la nueva Lista de Proyectos de thisUser CON thisProject dentro
		repositorio_user.save(thisUser);
		
		
	}
	
	public void remove_project_user(Long user_id, Long project_id) {
		//Ubicamos nuestros objetos a través de métodos ya creados en AppService
		//Almacenamos nuestro objeto proyecto a través del id
		Project thisProject = find_project(project_id);
		//Almacenamos nuestro objeto user a través del id
		User thisUser = find_user(user_id);
		
		//Retorname la Lista de Proyectos de thisUser y REMUEVE thisProject
		thisUser.getProjectsJoined().remove(thisProject);
		
		
		//Guardamos la nueva Lista de Proyectos de thisUser SIN thisProject dentro
		repositorio_user.save(thisUser);
	}
	
	
	
	
	
}
