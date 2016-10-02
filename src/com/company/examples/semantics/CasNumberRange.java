package com.company.examples.semantics;

import java.util.concurrent.atomic.AtomicReference;

public class CasNumberRange {
    static class IntRange {
        final int lower;
        final int upper;

        IntRange(int lower, int upper) {
            this.lower = lower;
            this.upper = upper;
        }

        public int getLower() {
            return lower;
        }

        public int getUpper() {
            return upper;
        }
    }

    private final AtomicReference<IntRange> value = new AtomicReference<>(new IntRange(0, 0));

    public int getLower() {
        return value.get().getLower();
    }

    public int getUpper() {
        return value.get().getUpper();
    }

    public void setLower(int lower) {
        while (true) {
            IntRange range = value.get();
            IntRange newRange = new IntRange(lower, range.getUpper());
            if(value.compareAndSet(range, newRange)) {
                return;
            }
        }
    }

    public void setUpper(int upper) {
        while (true) {
            IntRange range = value.get();
            IntRange newRange = new IntRange(range.getLower(), upper);
            if(value.compareAndSet(range, newRange)) {
                return;
            }
        }
    }
}
