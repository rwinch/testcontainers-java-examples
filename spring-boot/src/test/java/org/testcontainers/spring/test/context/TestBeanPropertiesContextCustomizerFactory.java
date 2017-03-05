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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.test.util.EnvironmentTestUtils;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.test.context.ContextConfigurationAttributes;
import org.springframework.test.context.ContextCustomizer;
import org.springframework.test.context.ContextCustomizerFactory;
import org.springframework.test.context.MergedContextConfiguration;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;

/**
 * Finds {@link TestBeanProperties} and populates them in the {@link Environment}.
 *
 * @author Rob Winch
 */
public class TestBeanPropertiesContextCustomizerFactory implements ContextCustomizerFactory {

    @Override
    public ContextCustomizer createContextCustomizer(Class<?> testClass,
            List<ContextConfigurationAttributes> configAttributes) {
        return new TestContainersContextCustomizer();
    }

    private static class TestContainersContextCustomizer implements ContextCustomizer {

        @Override
        public void customizeContext(ConfigurableApplicationContext context, MergedContextConfiguration mergedConfig) {
            ConfigurableEnvironment environment = context.getEnvironment();
            String[] properties = getProperties(mergedConfig);
            EnvironmentTestUtils.addEnvironment("testcontainers", environment,
                            properties);
        }

        private String[] getProperties(MergedContextConfiguration mergedConfig) {
            List<String> result = new ArrayList<String>();
            ReflectionUtils.doWithFields(mergedConfig.getTestClass(), new FieldCallback() {
                @Override
                public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                    AnnotationAttributes attributes = AnnotatedElementUtils.getMergedAnnotationAttributes(field, TestBeanProperties.class);
                    String[] properties = attributes == null ? new String[0] : (String[]) attributes.get("value");
                    for (String property : properties) {
                        String[] parts = property.split("=");
                        String key = parts[0];
                        String spel = parts[1];
                        SpelExpressionParser parser = new SpelExpressionParser();
                        Expression expression = parser.parseExpression(spel);

                        Object value = expression.getValue(field.get(null), String.class);
                        result.add(key + "=" + value);
                    }
                }

            });
            return result.toArray(new String[result.size()]);
        }

    }
}
