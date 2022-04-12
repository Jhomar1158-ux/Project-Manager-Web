package com.jhomar.ProjectManager.controladores;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jhomar.ProjectManager.modelos.Project;
import com.jhomar.ProjectManager.modelos.User;
import com.jhomar.ProjectManager.servicios.AppService;

@Controller
@RequestMapping("/projects")
public class ProjectController {
	
	@Autowired
	private AppService servicio;
	
	@GetMapping("/new")
	public String new_project(@ModelAttribute("project") Project project, HttpSession session) {
		//Revisa la session
		User currentUser=(User) session.getAttribute("user_session");
		
		if(currentUser == null) {
			return "redirect:/";
		}
		
		return "newProject.jsp";
		
	}
	
	@PostMapping("/create")
	public String create_project(@Valid @ModelAttribute("project") Project project,
								BindingResult result,
								HttpSession session) {
		//Revisa la session
		User currentUser=(User) session.getAttribute("user_session");
		
		if(currentUser == null) {
			return "redirect:/";
		}
		//==
		
		if(result.hasErrors()) {
			return "newProject.jsp";
		}else {
			
			Project nuevoProyecto = servicio.save_project(project);
			
			User myUser = servicio.find_user(currentUser.getId());
			
			myUser.getProjectsJoined().add(nuevoProyecto); 
			
			servicio.save_user(myUser);
			
			return "redirect:/dashboard";
			
		}
		
	}
	
	//Borramos el proyecto
	@DeleteMapping("/delete/{project_id}")
	public String delete_project(@PathVariable("project_id") Long project_id,
									HttpSession session) {
		//Revisa la session
		User currentUser=(User) session.getAttribute("user_session");
		
		if(currentUser == null) {
			return "redirect:/";
		}
		//==
		
		//Quiero eliminar este proyecto de manera ABSOLUTA(para todos)
		servicio.delete_project(project_id);
		
		return "redirect:/dashboard";
		
	}
	
	// PASO 1 para el UPDATED
	@GetMapping("/edit/{id}")
	public String edit_project(@PathVariable("id") Long id,
								@ModelAttribute("project") Project project,
								HttpSession session,
								Model model) {
		//Revisa la session
		User currentUser=(User) session.getAttribute("user_session");
		
		if(currentUser == null) {
			return "redirect:/";
		}
		//==
		
		//Quiero que me guardes el objeto proyecto que tiene ese id
		Project project_edit = servicio.find_project(id);
		
		model.addAttribute("project", project_edit);
		
		return "editProject.jsp";
		
	}
	
	//PASO 2 para el UPDATED
	//<%@ page isErrorPage="true" %>    Agregar este tag
	@PutMapping("update")
	public String update_project(@Valid @ModelAttribute("project") Project projectForm, BindingResult result) {
		if(result.hasErrors()) {
			return "editProject.jsp";
		}else {
			//Encuentra el objeto Project del update
			Project thisProject = servicio.find_project(projectForm.getId());
			//Quiero que "thisProject" le pase una lista de users a "projectForm"
			projectForm.setUsers(thisProject.getUsers()); 
			//Ahora nuestro nuevo Proyecto tiene la lista de users del anterior proyecto
			
			//Guardamos los nuevos valores de nuestro Proyecto
			servicio.save_project(projectForm);
			
			return "redirect:/dashboard";
			
			
		}
		
	}
	
	@GetMapping("/join/{project_id}")
	public String join_project(@PathVariable("project_id") Long project_id,
								HttpSession session) {
		//Revisa la session
		User currentUser=(User) session.getAttribute("user_session");
		
		if(currentUser == null) {
			return "redirect:/";
		}
		//==
		
		//Quiero guardar este proyecto en mi lista
		servicio.save_project_user(currentUser.getId(), project_id);
		
		return "redirect:/dashboard";
	}
	
	@GetMapping("/leave/{project_id}")
	public String leave_project(@PathVariable("project_id") Long project_id,
								HttpSession session) {
		//Revisa la session
		User currentUser=(User) session.getAttribute("user_session");
		
		if(currentUser == null) {
			return "redirect:/";
		}
		//==
		
		//Quiero eliminar project_id de mi Lista de proyectos
		servicio.remove_project_user(currentUser.getId(), project_id);
		
		return "redirect:/dashboard";
		
	}
	
	@GetMapping("/{project_id}")
	public String show_project(@PathVariable("project_id") Long project_id, HttpSession session, Model model) {
		//Revisa la session
		User currentUser=(User) session.getAttribute("user_session");
		
		if(currentUser == null) {
			return "redirect:/";
		}
		//==
		
		//Ubicamos al proyecto a través del project_id
		Project thisProject = servicio.find_project(project_id);
		
		//Agregamos el Proyecto a el contenedor model
		model.addAttribute("project", thisProject);
		
		return "showProject.jsp";
		
	}
	
	
	
	
}
