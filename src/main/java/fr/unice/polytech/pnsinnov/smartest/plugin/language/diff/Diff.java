package fr.unice.polytech.pnsinnov.smartest.plugin.language.diff;

import fr.smartest.plugin.Module;
import fr.smartest.plugin.Test;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface Diff {

    Set<Test> getTestsRelatedToChanges(List<Module> modules) throws IOException;

}
