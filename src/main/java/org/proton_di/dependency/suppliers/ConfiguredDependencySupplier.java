package org.proton_di.dependency.suppliers;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.function.Supplier;

import org.proton_di.inject.Injector;

/**
 * A DependencySupplier implementation for dependencies
 * defined in a configuration class.
 * 
 * @author Luke Stevens
 */
public class ConfiguredDependencySupplier extends DependencySupplier {
	
	Method method;
	Object configInstance;
	
	/**
	 * Constructs a new ConfiguredDependencySupplier
	 * @param method The method to be used to create instances of the dependency
	 * @param configInstance An instance of the configuration class
	 */
	public ConfiguredDependencySupplier(Method method, Object configInstance) {
		super(method.getReturnType());
		this.method = method;
		this.configInstance = configInstance;
	}

	/**
	 * Overrides default method for sub dependencies.
	 * Loads parameter types for configured method as
	 * the sub dependencies.
	 */
	@Override
	void loadSubDependencies() {
		Class<?>[] types = method.getParameterTypes();
		subDependencies = Arrays.asList(types);
	}

	@Override
	public Supplier<Object> generateSupplier(Injector injector) {
			Object[] args = injector.getDependencies(method.getParameterTypes());
			Object instance = Modifier.isStatic(method.getModifiers())? null : configInstance;
			
			return () -> tools.invokeMethod(instance, method, args);
	}

}
