package com.jhipster.demo.notification;

import com.jhipster.demo.notification.NotificationApp;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import tech.jhipster.config.JHipsterConstants;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = NotificationApp.class)
@ExtendWith(CouchbaseTestContainerExtension.class)
@ActiveProfiles(JHipsterConstants.SPRING_PROFILE_TEST)
public @interface IntegrationTest {
}
