package fr.unice.polytech.pnsinnov.smartest.explorertree.plugins.runners;


import java.util.Set;

public class DefaultRunner implements TRunner{
    @Override
    public boolean runAllTests(Set<String> toRun) {
        return true;
    }
}
