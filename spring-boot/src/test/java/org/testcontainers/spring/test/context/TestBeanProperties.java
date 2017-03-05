/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.testcontainers.spring.test.context;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.env.Environment;

/**
 * A field level annotation that contains <em>key-value</em> pairs that should
 * be added to the Spring {@link org.springframework.core.env.Environment
 * Environment} before the {@code ApplicationContext} is loaded for the test.
 * The value of the <em>key-value</em> pair is a SpEL expression with the member
 * variable as the Root of the expression.
 *
 * <h3>Example</h3>
 *
 * For example the following class has a property named randomPort on it.
 *
 * <code>
 * public class MyPorts {
 *     public int getRandomPort() {
 *        ...
 *     }
 * }
 * </code>
 *
 * When writing a test, a static member variable can then be used to populate
 * Spring's Environment. Below the {@link Environment} is populated with a name
 * of "thePort" and a value which is the result of invoking
 * "ports.getRandomPort()". The value can be any <a href=
 * "https://docs.spring.io/spring/docs/current/spring-framework-reference/html/expressions.html">SpEL
 * Expression</a> such that the root is the member variable the annotation is
 * placed on.
 *
 * <code>
 * &#64;RunWith(SpringJUnit4ClassRunner.class)
 * public class MyTest {
 *     &#064;TestBeanProperties("thePort=randomPort")
 *     public static final MyPorts ports = new MyPorts();
 * }
 * </code>
 *
 * If you want to provide a layer of indirection (perhaps you use the annotation
 * frequently), then you can leverage {@link TestBeanProperties} as a
 * meta-annotation. For example:
 *
 * <code>
 * &#64;Target(ElementType.FIELD)
 * &#64;Retention(RetentionPolicy.RUNTIME)
 * &#64;Documented
 * &#64;Inherited
 * &#64;TestBeanProperties({"thePort=randomPort"})
 * public &#64;interface TestMyPortsProperties {
 * }
 * </code>
 *
 * Now the test can be changed to:
 *
 * <code>
 * &#64;RunWith(SpringJUnit4ClassRunner.class)
 * public class MyTest {
 *     &#064;TestMyPortsProperties
 *     public static final MyPorts ports = new MyPorts();
 * }
 * </code>
 *
 * @author Rob Winch
 *
 */
@Target({ ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface TestBeanProperties {

	/**
	 * <em>Inlined properties</em> in the form of <em>key-value</em> pairs that
	 * should be added to the Spring
	 * {@link org.springframework.core.env.Environment Environment} before the
	 * {@code ApplicationContext} is loaded for the test. The value of the
	 * <em>key-value</em> pair is a SpEL expression with the member variable as
	 * the Root of the expression. All key-value pairs will be added to the
	 * enclosing {@code Environment} as a single test {@code PropertySource}
	 * with the highest precedence. See the class level javadoc for a complete
	 * example.
	 *
	 */
	String[] value();
}
