package com.jhomar.ProjectManager.controladores;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.jhomar.ProjectManager.modelos.LoginUser;
import com.jhomar.ProjectManager.modelos.Project;
import com.jhomar.ProjectManager.modelos.User;
import com.jhomar.ProjectManager.servicios.AppService;

@Controller
public class UserController {
	
	@Autowired
	private AppService servicio;
	
	@GetMapping("/")
	public String index(@ModelAttribute("nuevoUsuario") User nuevoUsuario,
						@ModelAttribute("nuevoLogin") LoginUser nuevoLogin) {
		/*model.addAttribute("nuevoUsuario", new User());
		model.addAttribute("nuevoLogin", new LoginUser());*/
		
		return "index.jsp";
	}
	
	@PostMapping("/register")
	public String register(@Valid @ModelAttribute("nuevoUsuario") User nuevoUsuario,
						   BindingResult result, Model model, HttpSession session) {
		
		servicio.register(nuevoUsuario, result);
		if(result.hasErrors()) {
			model.addAttribute("nuevoLogin", new LoginUser());
			return "index.jsp";
		}
		
		session.setAttribute("user_session", nuevoUsuario);
		return "redirect:/dashboard";
		
	}
	
	@PostMapping("/login")
	public String login(@Valid @ModelAttribute("nuevoLogin") LoginUser nuevoLogin,
						BindingResult result, Model model, HttpSession session) {
							
		User user = servicio.login(nuevoLogin, result);
		if(result.hasErrors()) {
			model.addAttribute("nuevoUsuario", new User());
			return "index.jsp";
		}
		
		session.setAttribute("user_session", user);
		return "redirect:/dashboard";
							
	}
	
	@GetMapping("/dashboard")
	public String dashboard(HttpSession session, Model model) {
		//Tiene Session
		User currentUser = (User)session.getAttribute("user_session");
		
		if(currentUser == null) {
			return "redirect:/";
		}
		//===========
		
		//Creamos nuestras 2 listas
		
		//Lista de Proyectos en los que ESTAMOS DENTRO
		List<Project> mis_proyectos = servicio.find_my_projects(currentUser); 
		
		//Lista de Proyectos en los que NO ESTAMOS
		List<Project> resto_proyectos = servicio.find_other_projects(currentUser);
		
		//Pasamos nuestras listas al .jsp
		model.addAttribute("mis_proyectos", mis_proyectos);
		model.addAttribute("resto_proyectos", resto_proyectos);
		
		return "dashboard.jsp";
	}
	
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.removeAttribute("user_session");
		return "redirect:/";
	}
	
}
