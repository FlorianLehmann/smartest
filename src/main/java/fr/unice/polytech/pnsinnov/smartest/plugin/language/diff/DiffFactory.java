package fr.unice.polytech.pnsinnov.smartest.plugin.language.diff;

import java.util.Set;

public class DiffFactory {

    private Set<String> fileDiff;

    public DiffFactory(Set<String> fileDiff) {
        this.fileDiff = fileDiff;
    }

    public Diff build(Scope scope) throws InvalidScopeTests {
        switch (scope) {
            case CLASS:
                return new DiffClass(fileDiff);
            case METHOD:
                return new DiffMethod(fileDiff);
        }
        throw new InvalidScopeTests();
    }

}
