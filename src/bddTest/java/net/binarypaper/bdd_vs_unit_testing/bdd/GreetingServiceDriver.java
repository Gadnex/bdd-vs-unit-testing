package net.binarypaper.bdd_vs_unit_testing.bdd;

import org.junit.jupiter.api.Assertions;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import io.qameta.allure.Step;
import net.binarypaper.bdd_vs_unit_testing.greeting.GreetingService;

@Service
@Lazy
public class GreetingServiceDriver implements GreetingDSL {

    private final GreetingService greetingService;

    public GreetingServiceDriver(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    private String name;
    private String greeting;

    @Override
    @Step("Given I am speaking to a client with name {name}")
    public GreetingDSL givenIAmSpeakingToAClientWithName(String name) {
        this.name = name;
        return this;
    }

    @Override
    @Step("When I greet the client")
    public GreetingDSL whenIGreetTheClient() {
        greeting = greetingService.greet(name);
        return this;
    }

    @Override
    @Step("Then I should say {greeting}")
    public GreetingDSL thenIShouldSay(String greeting) {
        Assertions.assertEquals(greeting, this.greeting);
        return this;
    }

}
