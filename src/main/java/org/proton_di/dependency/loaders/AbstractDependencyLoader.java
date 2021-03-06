package org.proton_di.dependency.loaders;

import java.util.ArrayList;
import java.util.List;

import org.proton_di.dependency.suppliers.DependencySupplier;
import org.proton_di.scanner.ClassPath;

public abstract class AbstractDependencyLoader implements DependencyLoader {
	
	final ClassPath classpath = ClassPath.getInstance();
	final List<DependencySupplier> dependencySuppliers = new ArrayList<>();

	@Override
	public List<DependencySupplier> getDependencies() {
		List<Class<?>> classes = getClasses();
		for(Class<?> c : classes){
			if(shouldLoad(c)) loadClass(c);
		}
		return dependencySuppliers;
	}
	
	abstract List<Class<?>> getClasses();
	
	abstract boolean shouldLoad(Class<?> c);
	
	abstract void loadClass(Class<?> c);

}
