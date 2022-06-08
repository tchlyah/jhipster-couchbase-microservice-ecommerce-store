package com.jhipster.demo.product;

import com.jhipster.demo.product.CouchbaseTestContainerExtension;
import com.jhipster.demo.product.ProductApp;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = ProductApp.class)
@ExtendWith(CouchbaseTestContainerExtension.class)
public @interface IntegrationTest {
}
