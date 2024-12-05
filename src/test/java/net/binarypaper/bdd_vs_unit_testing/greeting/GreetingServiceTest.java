package net.binarypaper.bdd_vs_unit_testing.greeting;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class GreetingServiceTest {

    @TestConfiguration
    static class GreetingServiceTestConfiguration {

        @Bean
        public GreetingService greetingService() {
            return new GreetingService();
        }
    }

    @Autowired
    private GreetingService greetingService;

    @Test
    void greetJohn() {
        String greeting = greetingService.greet("John");
        Assertions.assertEquals("Hello John", greeting);
    }

    @Test
    void greetWilliam() {
        String greeting = greetingService.greet("William");
        Assertions.assertEquals("Hello William", greeting);
    }
}
