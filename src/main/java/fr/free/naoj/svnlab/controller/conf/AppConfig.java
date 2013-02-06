package fr.free.naoj.svnlab.controller.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.view.ResourceBundleViewResolver;
import org.springframework.web.servlet.view.tiles2.TilesConfigurer;

@Configuration
public class AppConfig extends WebMvcConfigurationSupport {

	@Bean
	public ViewResolver viewResolver() {
		ResourceBundleViewResolver resolver =new ResourceBundleViewResolver();
		
		return resolver;
	}
	
	@Bean
	public TilesConfigurer tilesConfigurer() {
		TilesConfigurer configurer = new TilesConfigurer();
		configurer.setDefinitions(new String[]{"/WEB-INF/tiles.xml"});
		return configurer;
	}
}
