package net.binarypaper.bdd_vs_unit_testing.greeting;

import org.springframework.stereotype.Service;

@Service
public class GreetingService {

    public String greet(String name) {
        return "Hello " + name;
    }
}
