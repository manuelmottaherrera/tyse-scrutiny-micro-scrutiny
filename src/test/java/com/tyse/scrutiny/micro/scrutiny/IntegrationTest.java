package com.tyse.scrutiny.micro.scrutiny;

import com.tyse.scrutiny.micro.scrutiny.config.AsyncSyncConfiguration;
import com.tyse.scrutiny.micro.scrutiny.config.EmbeddedKafka;
import com.tyse.scrutiny.micro.scrutiny.config.EmbeddedSQL;
import com.tyse.scrutiny.micro.scrutiny.config.JacksonConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { TyseScrutinyMicroScrutinyApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedSQL
@EmbeddedKafka
public @interface IntegrationTest {
    // 5s is Spring's default https://github.com/spring-projects/spring-framework/blob/main/spring-test/src/main/java/org/springframework/test/web/reactive/server/DefaultWebTestClient.java#L106
    String DEFAULT_TIMEOUT = "PT5S";

    String DEFAULT_ENTITY_TIMEOUT = "PT5S";
}
