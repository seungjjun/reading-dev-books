package item26;

import java.util.ArrayList;
import java.util.Collection;

public class ParameterizedType {
    private final Collection<Stamp> stamps = new ArrayList<>();

    public void coin() {
        stamps.add(new Coin(...)); // error: incompatible types
    }
}