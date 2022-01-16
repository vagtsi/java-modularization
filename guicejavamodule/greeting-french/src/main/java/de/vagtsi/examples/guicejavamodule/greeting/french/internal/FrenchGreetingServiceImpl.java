package de.vagtsi.examples.guicejavamodule.greeting.french.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.vagtsi.examples.guicejavamodule.greeting.core.GreetingService;

public class FrenchGreetingServiceImpl implements GreetingService {
	private static final Logger log = LoggerFactory.getLogger(FrenchGreetingServiceImpl.class);

	public FrenchGreetingServiceImpl() {
		log.info("Created French greeting service");
	}

	@Override
	public String hello() {
		return "Salut!";
	}

}
