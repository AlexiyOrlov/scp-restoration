package dev.buildtool.scp;

public interface Ageable {

    /**
     * Turn into adult
     */
    default void grow() {

    }

    /**
     * @return current growth time
     */
    default int growthTime() {
        return 0;
    }

    boolean isBaby();
}
