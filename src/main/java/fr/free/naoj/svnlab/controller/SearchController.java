package fr.free.naoj.svnlab.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import fr.free.naoj.svnlab.entity.SearchCriteria;

@Controller
@RequestMapping("/search")
@SessionAttributes
public class SearchController {

	@RequestMapping(value="/", method=RequestMethod.POST)
	public String searchCommit(@ModelAttribute SearchCriteria searchCriteria) {
		return "";
	}
}
