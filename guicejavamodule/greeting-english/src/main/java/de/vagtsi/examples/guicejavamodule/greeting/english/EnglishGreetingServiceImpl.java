package de.vagtsi.examples.guicejavamodule.greeting.english;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.vagtsi.examples.guicejavamodule.greeting.core.ExampleCoreService;
import de.vagtsi.examples.guicejavamodule.greeting.core.GreetingService;

public class EnglishGreetingServiceImpl implements GreetingService {
	private static final Logger log = LoggerFactory.getLogger(EnglishGreetingServiceImpl.class.getSimpleName());

	public EnglishGreetingServiceImpl(ExampleCoreService exampleCoreService) {
		log.info("Created English greeting service with injected core service: {}", exampleCoreService.sayHello());
	}

	@Override
	public String hello() {
		return "Hello!";
	}

}
