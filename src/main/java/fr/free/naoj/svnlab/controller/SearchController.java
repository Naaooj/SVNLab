package fr.free.naoj.svnlab.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import fr.free.naoj.svnlab.entity.Commit;
import fr.free.naoj.svnlab.entity.SearchCriteria;
import fr.free.naoj.svnlab.service.SearchEngine;

@Controller
@RequestMapping("/search")
@SessionAttributes
public class SearchController {

	@Autowired
	@Qualifier("searchEngine")
	private SearchEngine engine;
	
	@RequestMapping(value="/", method=RequestMethod.POST)
	public String searchCommit(Model model, @ModelAttribute SearchCriteria searchCriteria) {
		List<Commit> commits = engine.search(searchCriteria);
		
		System.out.println(commits.size());
		
		model.addAttribute("commits", commits);
		
		return "results";
	}
}
