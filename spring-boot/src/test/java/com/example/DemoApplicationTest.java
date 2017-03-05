package com.example;

import static org.rnorth.visibleassertions.VisibleAssertions.*;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.spring.test.context.TestContainersRedisProperties;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DemoApplication.class,webEnvironment = WebEnvironment.RANDOM_PORT)
public class DemoApplicationTest {

    @ClassRule
    @TestContainersRedisProperties
    public static GenericContainer redis = new GenericContainer("redis:3.0.6").withExposedPorts(6379);

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    public void simpleTest() {
        String fooResource = "/foo";

        info("putting 'bar' to " + fooResource);
        restTemplate.put(fooResource, "bar");

        assertEquals("value is set", "bar", restTemplate.getForObject(fooResource, String.class));
    }
}
