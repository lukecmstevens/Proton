package com.lithium.test.suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.lithium.test.cases.TestClassPath;
import com.lithium.test.cases.TestDependenciesWithDependencies;
import com.lithium.test.cases.TestExtensionInjection;
import com.lithium.test.cases.TestInjectionExceptions;
import com.lithium.test.cases.TestInjectorManagedConstruction;
import com.lithium.test.cases.TestManualInjection;
import com.lithium.test.cases.TestStaticInjection;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	TestClassPath.class,
	TestStaticInjection.class,
	TestManualInjection.class,
	TestDependenciesWithDependencies.class,
	TestExtensionInjection.class,
	TestInjectionExceptions.class,
	TestInjectorManagedConstruction.class
	})
public class InjectionTestSuite {

}
