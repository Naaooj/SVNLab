package fr.free.naoj.svnlab.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import fr.free.naoj.svnlab.service.CommitService;
import fr.free.naoj.svnlab.service.RepositoryService;

@Controller
@RequestMapping
@SessionAttributes
public class HomeController {

	@Autowired private RepositoryService repositoryService;
	
	@Autowired private CommitService s;
	
	@RequestMapping(value="/")
	public String root() {
		return "redirect:/home";
	}
	
	@RequestMapping(value="/home")
	public String home(Model model) {
		model.addAttribute("projects", repositoryService.getProjects());
		
		return "home";
	}
}
