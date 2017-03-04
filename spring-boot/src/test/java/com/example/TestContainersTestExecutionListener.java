package com.example;

import org.springframework.boot.test.util.EnvironmentTestUtils;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;
import org.testcontainers.containers.GenericContainer;

public class TestContainersTestExecutionListener implements TestExecutionListener {

	@Override
	public void beforeTestClass(TestContext testContext) throws Exception {

		ConfigurableEnvironment environment = (ConfigurableEnvironment) testContext.getApplicationContext().getEnvironment();
		GenericContainer redis = (GenericContainer) testContext.getTestClass().getSuperclass().getDeclaredField("redis").get(null);
		EnvironmentTestUtils.addEnvironment("testcontainers", environment,
                "spring.redis.host=" + redis.getContainerIpAddress(),
                "spring.redis.port=" + redis.getMappedPort(6379));
	}

	@Override
	public void prepareTestInstance(TestContext testContext) throws Exception {
	}

	@Override
	public void beforeTestMethod(TestContext testContext) throws Exception {
	}

	@Override
	public void afterTestMethod(TestContext testContext) throws Exception {
	}

	@Override
	public void afterTestClass(TestContext testContext) throws Exception {
	}

}
