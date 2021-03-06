package de.vagtsi.examples.guicejavamodule.greeting.core;

import javax.inject.Singleton;

import com.google.inject.Provides;

import de.vagtsi.examples.guicejavamodule.api.PluginModule;
import de.vagtsi.examples.guicejavamodule.api.SimpleExtensionRegistry;
import de.vagtsi.examples.guicejavamodule.greeting.core.internal.ExampleCoreServiceImpl;

public class GreetingCoreModule extends PluginModule {
	@Override
	protected void configure() {
		//create and register extension registry
		bindExtensionRegistry(SimpleExtensionRegistry.create(GreetingService.class));
	}

	// provide example service to be injected into child plugins/injectors
	@Provides
	@Singleton
	public ExampleCoreService exampleCoreService() {
		return new ExampleCoreServiceImpl();
	}
}
