package net.binarypaper.bdd_vs_unit_testing.bdd;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.qameta.allure.Story;

@Story("""
        As a greeter
        I want to greet a person
        So that they feel welcome
        """)
@SpringBootTest
class GreetingFeature {

    @Autowired
    private GreetingDSL greeter;

    @Test
    void greetJohn() {
        greeter.givenIAmSpeakingToAClientWithName("John")
                .whenIGreetTheClient()
                .thenIShouldSay("Hello John");
    }

    @Test
    void greetWilliam() {
        greeter.givenIAmSpeakingToAClientWithName("William");
        greeter.whenIGreetTheClient();
        greeter.thenIShouldSay("Hello William");
    }
}
