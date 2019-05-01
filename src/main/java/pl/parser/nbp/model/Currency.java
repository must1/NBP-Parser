package pl.parser.nbp.model;

import java.util.InputMismatchException;

public enum Currency {
    USD, EUR,AUD,CAD,HUF,CHF,GBP,JPY,CZK,DKK,NOK,SEK,XDR;

    private static final Currency[] copyOfValues = values();

    public static Currency forName(String name) {
        for (Currency value : copyOfValues) {
            if (value.name().equals(name)) {
                return value;
            }
        }
        throw new InputMismatchException("Given currency is bad!");
    }
}
