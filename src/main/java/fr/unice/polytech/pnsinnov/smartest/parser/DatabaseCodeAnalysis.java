package fr.unice.polytech.pnsinnov.smartest.parser;

import java.util.Set;

public interface DatabaseCodeAnalysis {

    public boolean contain(String cls);
    public void addClass(String cls);
    public void linkClassToTest(String cls, String clsTest);
    public Set<String> getTestLinkToClass(String cls);

}
