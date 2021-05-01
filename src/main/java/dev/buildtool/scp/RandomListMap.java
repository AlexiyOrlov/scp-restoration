package dev.buildtool.scp;

import dev.buildtool.satako.RandomizedList;

import java.util.HashMap;

//promote
public class RandomListMap<K, V> extends HashMap<K, RandomizedList<V>> {

    public V getRandom(K key) {
        return get(key) == null ? null : get(key).getRandom();
    }
}
