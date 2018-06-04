package fr.unice.polytech.pnsinnov.smartest.plugin.language.diff;

import fr.smartest.plugin.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DiffMethod implements Diff {

    private List<fr.smartest.plugin.Diff> fileDiff;

    public DiffMethod(Set<fr.smartest.plugin.Diff> fileDiff) {
        this.fileDiff = new ArrayList<>();
    }

    @Override
    public Set<Test> getTestsRelatedToChanges() {
        throw new UnsupportedOperationException("getTestsRelatedToChanges scope : METHOD");
    }
}
