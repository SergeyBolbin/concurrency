package com.company.examples.threadsafety;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class HiddenIterator {
    private final Set<Integer> set = new HashSet<>();

    public synchronized void add(Integer i) {
        set.add(i);
    }

    public synchronized void remove(Integer i) {
        set.remove(i);
    }

    public void addTenThings() {
        Random r = new Random();
        for (int i = 0; i < 10; i++)
            add(r.nextInt());

        /**
         * The addTenThings method could throw ConcurrentModificationException, because the collection is being iterated
         * by toString in the process of preparing the debugging message. Of course, the real problem is that HiddenIterator is
         * not thread-safe; the HiddenIterator lock should be acquired before using set in the println call, but debugging and
         * logging code commonly neglect to do this.
         */
        System.out.println("DEBUG: added ten elements to " + set);
    }
}
