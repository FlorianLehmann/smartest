package fr.unice.polytech.pnsinnov.smartest.plugin.language.astclasse;

import fr.smartest.plugin.Module;

import java.io.Serializable;
import java.util.Objects;

public class ModuleImpl implements Serializable {

    private String srcPath;
    private String testPath;
    private String outputSrcPath;
    private String outputTestPath;

    public ModuleImpl(Module module) {
        this.srcPath = module.getSrcPath().toString();
        this.testPath = module.getTestPath().toString();
        this.outputSrcPath = module.getCompiledSrcPath().toString();
        this.outputTestPath = module.getCompiledTestPath().toString();
    }

    public String getTestPath() {
        return testPath;
    }

    public String getOutputSrcPath() {
        return outputSrcPath;
    }

    public String getOutputTestPath() {
        return outputTestPath;
    }

    public String getSrcPath() {

        return srcPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModuleImpl module = (ModuleImpl) o;
        return Objects.equals(srcPath, module.srcPath) &&
                Objects.equals(testPath, module.testPath) &&
                Objects.equals(outputSrcPath, module.outputSrcPath) &&
                Objects.equals(outputTestPath, module.outputTestPath);
    }

    @Override
    public int hashCode() {

        return Objects.hash(srcPath, testPath, outputSrcPath, outputTestPath);
    }
}
