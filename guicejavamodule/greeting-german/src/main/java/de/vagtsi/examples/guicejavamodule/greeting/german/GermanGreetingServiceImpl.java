package de.vagtsi.examples.guicejavamodule.greeting.german;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.vagtsi.examples.guicejavamodule.greeting.core.GreetingService;

public class GermanGreetingServiceImpl implements GreetingService {
	private static final Logger log = LoggerFactory.getLogger(GermanGreetingServiceImpl.class.getSimpleName());

	public GermanGreetingServiceImpl() {
		log.info("Created German greeting service");
	}

	@Override
	public String hello() {
		return "Moin!";
	}

}
