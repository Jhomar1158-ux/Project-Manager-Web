package com.jhomar.ProjectManager.repositorios;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.jhomar.ProjectManager.modelos.Project;
import com.jhomar.ProjectManager.modelos.User;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Long>{
	
	List<Project> findAll();
	List<Project> findById(long id); //long como tipo de dato primitivo
		
	//Me va a guardar mi proyecto
	Project save(Project nuevoProyecto);
	
	//Lista de proyectos que no son contenidos por mi usuario
	List<Project> findByUsersNotContains(User user); //Selecciona todos los proyectos los cuales no soy parte
	
	//Lista de proyectos de mi usuario
	List<Project> findAllByUsers(User user);//Selecciono todos los proyectos a los que mi usuario pertenece
}
