package fr.unice.polytech.pnsinnov.smartest.cli;

import fr.unice.polytech.pnsinnov.smartest.Context;
import fr.unice.polytech.pnsinnov.smartest.Smartest;

public abstract class Command implements Runnable {
    protected Context context;
    protected Smartest smartest;

    void setContext(Context context) {
        this.context = context;
    }

    void setSmartest(Smartest smartest) {
        this.smartest = smartest;
    }
}
