package org.proton_di.inject.config;

import org.proton_di.inject.InjectionManager;

/**
 * An abstract class to allow dependencies to be
 * injected during the construction of an object.<br>
 * 
 * When any object extending this is instantiated, this
 * super constructor will call the injector to inject
 * the required dependencies before any other initialisation
 * takes place.
 * 
 * @author Luke Stevens
 */
public abstract class InjectableObject {
	
	public InjectableObject(){
		InjectionManager.getInjector(this.getClass()).injectDependencies(this);
	}

}
