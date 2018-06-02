package fr.unice.polytech.pnsinnov.smartest.cli;

import fr.unice.polytech.pnsinnov.smartest.Context;

public abstract class Command implements Runnable {
    protected Context context;

    public void setContext(Context context) {
        this.context = context;
    }
}
