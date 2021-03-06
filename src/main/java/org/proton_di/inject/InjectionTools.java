package org.proton_di.inject;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.proton_di.dependency.exceptions.DependencyCreationException;
import org.proton_di.dependency.exceptions.MissingConstructorException;
import org.proton_di.dependency.suppliers.DependencySupplier;
import org.proton_di.inject.config.Inject;

/**
 * A tools class to separate out stateless methods
 * from the {@link Injector}.
 * 
 * @author Luke Stevens
 *
 */
public class InjectionTools {
	
	/**
	 * Determines whether a field is injectable e.g.
	 * whether a dependency should be injected into it.
	 * @param f The field to check
	 * @param checkStatic Whether the field is static or not. e.g. if
	 * this is true then this method will only return true for static
	 * injectable methods, and vice-versa
	 * @return True if injectable, false if not.
	 */
	public boolean isInjectable(Field f, boolean checkStatic){
		return f.getAnnotation(Inject.class) != null && (checkStatic == Modifier.isStatic(f.getModifiers()));
	}
	
	/**
	 * Gets a constructor types for the injection constructor to
	 * use for the given.
	 * @param c The class to get a constructor for.
	 * @return The parameter types for constructor annotated with
	 * <code>@Inject</code> if one exists, otherwise the default constructor.
	 * @throws DependencyCreationException If there are multiple 
	 * constructors annotated with <code>@Inject</code>
	 */
	public Class<?>[] getConstructorTypes(Class<?> c){
		Class<?>[] types = {};
		
		for(Constructor<?> con : c.getDeclaredConstructors()){
			if(con.getAnnotation(Inject.class) != null){
				if(types.length > 0) throw new DependencyCreationException("Multiple constructors annotated with @Inject.", c);
				else types = con.getParameterTypes();
			}
		}
		
		return types;
	}
	
	/**
	 * Constructs an object, given the classes to use to
	 * select the appropriate constructor, and the parameters
	 * to pass to it.
	 * @param c The class to construct an instance of.
	 * @param classes An array of classes matching the constrcutor
	 * declaration.
	 * @param params The parameters to pass to the constructor.
	 * @return A new instance of the object, constructed using the
	 * supplied parameters.
	 * @throws DependencyCreationException If there is
	 * not constructor matching the supplied parameters.
	 */
	public <T> T construct(Class<T> c, Class<?>[] classes, Object[] params){
		try {
			Constructor<T> con = c.getDeclaredConstructor(classes);
			con.setAccessible(true);
			return con.newInstance(params);
		} catch(Exception e){
			 throw new MissingConstructorException(c, classes);
		}
	}

	/**
	 * Invokes a method using an object instance and arguments.
	 * @param o The object instance to call the method on.
	 * If the method is static then this should be null.
	 * @param m The method to invoke.
	 * @param args The arguments to pass to the method.
	 * @return The Object returned by the method invoked.
	 */
	public Object invokeMethod(Object o, Method m, Object...args){
		try{
			m.setAccessible(true);
			return m.invoke(o, args);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new DependencyCreationException("Root cause: " + e.getMessage(), o.getClass());
		}
	}
	
	/**
	 * Determines if a DependencySupplier corresponding to a
	 * class is in a list of dependency suppliers to be loaded.
	 * @param c The class
	 * @param toInit The list of Dependency Suppliers to be initialised
	 * @return True if in the list, false if not.
	 */
	public boolean toBeLoaded(Class<?> c, List<DependencySupplier> toInit){
		for(DependencySupplier dep : toInit){
			if(dep.getDependencyClass().equals(c) || dep.getAssignableClasses().contains(c)) return true;
		}
		return false;
	}
	
	/**
	 * Gets all non-static fields annotated with `@Inject` in this
	 * class and, recursively, all of it's superclasses.
	 * @param c The class to get all injectable fields for.
	 * @return All injectable fields from a class and it's
	 * superclasses.
	 */
	public List<Field> getAllInjectableFields(Class<?> c){
		List<Field> fields = new ArrayList<>();
		if(c == null) return fields;
		
		for(Field f : c.getDeclaredFields()){
			if(isInjectable(f, false)) fields.add(f);
		}
		
		fields.addAll(getAllInjectableFields(c.getSuperclass()));
		return fields;
	}

}
