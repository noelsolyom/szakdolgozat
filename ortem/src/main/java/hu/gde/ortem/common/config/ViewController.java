package hu.gde.ortem.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ViewController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ViewController.class);

	@Value("${viewcontroller.base.path}")
	private String basePath;

	@RequestMapping(value = { "/login", "/main/**" })
	public String login() {
		LOGGER.info("login");
		return "redirect:" + basePath;
	}

}