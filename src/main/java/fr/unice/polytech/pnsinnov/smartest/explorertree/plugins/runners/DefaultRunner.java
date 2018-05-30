package fr.unice.polytech.pnsinnov.smartest.explorertree.plugins.runners;


import java.util.List;

public class DefaultRunner implements TRunner{
    @Override
    public boolean runAllTests(List<String> toRun) {
        return true;
    }
}
