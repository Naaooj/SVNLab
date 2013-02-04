package fr.free.naoj.svnlab.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import fr.free.naoj.svnlab.entity.SearchCriteria;
import fr.free.naoj.svnlab.service.RepositoryService;
import fr.free.naoj.svnlab.service.svn.Entry;

@Controller
@RequestMapping("/project")
@SessionAttributes
public class ProjectController {
	
	@Autowired
	private RepositoryService repositoryService;

	@ModelAttribute("search")
	public SearchCriteria getSearchCriteria() {
		return new SearchCriteria();
	}
	
	@RequestMapping("/")
	public String noProjectSelected() {
		return "redirect:/home";
	}
	
	@RequestMapping(value={"/{projectName}", "/{projectName}/**"}, method=RequestMethod.GET)
	public String projectDetails(Model model, @PathVariable("projectName") String projectName, HttpServletRequest request) {
		String path = request.getRequestURL().toString();
		path = "/" + path.substring(path.indexOf(projectName));
		
		SearchCriteria searchCriteria = getSearchCriteria();
		searchCriteria.setPath(path);
		
		List<Entry> entries = repositoryService.getEntries(path);
		
		model.addAttribute("path", path);
		model.addAttribute("projectName", projectName);
		model.addAttribute("entries", entries);
		model.addAttribute("search", searchCriteria);
		
		return "project";
	}
}
