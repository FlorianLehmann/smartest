package fr.unice.polytech.pnsinnov.smartest.plugin.language.diff;

import fr.smartest.plugin.Test;

import java.io.IOException;
import java.util.Set;

public interface Diff {

    Set<Test> getTestsRelatedToChanges() throws IOException;

}
