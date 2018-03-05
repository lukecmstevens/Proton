package com.lithium.test;

import com.lithium.inject.Inject;
import com.lithium.inject.InjectableObject;

public class SomeInjectableObject extends InjectableObject{
	
	@Inject
	private static MultipleDependency mulitpleStatic;
	
	@Inject
	private SingletonDependency singletonField;
	
	@Inject
	private MultipleDependency multipleField;
	
	
	public static MultipleDependency mulitpleStaticInjectionTest(){
		return mulitpleStatic;
	}

	public SingletonDependency singletonFieldTest() {
		return singletonField;
	}

	public MultipleDependency multipleFieldTest() {
		return multipleField;
	}
}
