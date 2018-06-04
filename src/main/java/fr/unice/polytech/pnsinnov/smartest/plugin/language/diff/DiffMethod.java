package fr.unice.polytech.pnsinnov.smartest.plugin.language.diff;

import fr.smartest.plugin.Test;

import java.util.Set;

public class DiffMethod implements Diff {

    public DiffMethod(Set<fr.smartest.plugin.Diff> fileDiff) {
        throw new UnsupportedOperationException(("le constructeur n'est pas créer"));
    }

    @Override
    public Set<Test> getTestsRelatedToChanges() {
        throw new UnsupportedOperationException("getTestsRelatedToChanges scope : METHOD");
    }
}
