package fr.unice.polytech.pnsinnov.smartest.commandline;

public abstract class Command implements Runnable {
    protected Context context;

    public void setContext(Context context) {
        this.context = context;
    }
}
