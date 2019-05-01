package pl.parser.nbp.model;

import java.util.ArrayList;
import java.util.List;

public class Rates {
    private List<Float> buyingRates = new ArrayList<>();
    private List<Float> sellingRates = new ArrayList<>();

    public List<Float> getBuyingRates() {
        return buyingRates;
    }

    public List<Float> getSellingRates() {
        return sellingRates;
    }
}
