package item20.skeletal;

import java.util.List;
import java.util.Objects;

public class AbstractList {
    static List<Integer> intArrayAsList(int[] a) {
        Objects.requireNonNull(a);

        return new java.util.AbstractList<>() {
            @Override
            public Integer get(int index) {
                return a[index];
            }

            @Override
            public Integer set(int index, Integer val) {
                int oldVal = a[index];
                a[index] = val;
                return oldVal;
            }

            @Override
            public int size() {
                return a.length;
            }
        };
    }
}
