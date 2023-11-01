package chapter09;

import java.util.*;

public class RawType {
    private final Collection stamps = new ArrayList();

    public void coin() {
        stamps.add(new Coin(...)); // unchecked call

        for (Iterator i = stamps.iterator(); i.hasNext();) {
            Stamp stamp = (Stamp) i.next();
            stamp.cancel();
        }
    }

    public static void main(String[] args) {
        List<String> strings = new ArrayList<>();

        unsafeAdd(strings, Integer.valueOf(42));
        String s = strings.get(0);
    }

    private static void unsafeAdd(List list, Object o) {
        list.add(o); // warning: [unchecked] unchecked call to add(E) as a member of the raw type List
    }

    static int numElementsInCommon(Set s1, Set s2) {
        int result = 0;
        for (Object o1 : s1) {
            if (s2.contains(o1)) {
                result++;
            }
        }
        return result;
    }

    private void instanceOf() {
        if (o instanceof Set) {      // 로 타입
            Set<?> s = (Set<?>) o;   // 와일드카드 타입
            ...
        }
    }
}
