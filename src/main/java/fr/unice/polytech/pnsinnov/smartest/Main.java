package fr.unice.polytech.pnsinnov.smartest;

import fr.unice.polytech.pnsinnov.smartest.commandline.Smartest;

public class Main {
    public static void main(String[] args) {
        new Smartest(System.in, System.out, System.err).parse(args);
    }
}
