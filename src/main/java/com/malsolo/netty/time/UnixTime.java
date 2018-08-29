package com.malsolo.netty.time;

import java.util.Date;

public class UnixTime {

    /** The time protocol sets the epoch at 1900, the Date class at 1970. This number converts between them. */
    public final static long DIFFERENCE_BETWEEN_EPOCHS = 2208988800L;

    private final long value;

    public UnixTime() {
        this(System.currentTimeMillis() / 1000L + DIFFERENCE_BETWEEN_EPOCHS);
    }

    public UnixTime(long value) {
        this.value = value;
    }

    public long value() {
        return value;
    }

    @Override
    public String toString() {
        return new Date((value() - DIFFERENCE_BETWEEN_EPOCHS) * 1000L).toString();
    }

    @Override
    public boolean equals(Object o) {
        return o != null &&
                o instanceof UnixTime &&
                (o == this || this.value == ((UnixTime) o).value());
    }

    @Override
    public int hashCode() {
        try {
            return Math.toIntExact(this.value);
        } catch (ArithmeticException e) {
            return super.hashCode();
        }
    }
}
