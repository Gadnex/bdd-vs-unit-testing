package net.binarypaper.bdd_vs_unit_testing.bdd;

public interface GreetingDSL {

    GreetingDSL givenIAmSpeakingToAClientWithName(String name);

    GreetingDSL whenIGreetTheClient();

    GreetingDSL thenIShouldSay(String greeting);

}
