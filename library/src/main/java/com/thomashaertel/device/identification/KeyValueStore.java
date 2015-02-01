package com.thomashaertel.device.identification;

/**
 * Created by Haertel on 31.01.2015.
 */
public interface KeyValueStore {

    /**
     * Removes all elements from this {@code Map}, leaving it empty.
     *
     * @throws UnsupportedOperationException if removing elements from this {@code Map} is not supported.
     * @see #isEmpty()
     * @see #size()
     */
    public void clear();

    /**
     * Returns whether this {@code Map} contains the specified key.
     *
     * @param key the key to search for.
     * @return {@code true} if this map contains the specified key,
     * {@code false} otherwise.
     */
    public boolean contains(String key);

    /**
     * Returns the value of the mapping with the specified key.
     *
     * @param key the key.
     * @return the value of the mapping with the specified key, or {@code null}
     * if no mapping for the specified key is found.
     */
    public String get(String key);

    /**
     * Maps the specified key to the specified value.
     *
     * @param key   the key.
     * @param value the value.
     * @return the value of any previous mapping with the specified key or
     * {@code null} if there was no mapping.
     * @throws UnsupportedOperationException if adding to this {@code Map} is not supported.
     * @throws ClassCastException            if the class of the key or value is inappropriate for
     *                                       this {@code Map}.
     * @throws IllegalArgumentException      if the key or value cannot be added to this {@code Map}.
     * @throws NullPointerException          if the key or value is {@code null} and this {@code Map} does
     *                                       not support {@code null} keys or values.
     */
    public String put(String key, String value);

    /**
     * Returns an integer hash code for the receiver. {@code Object}s which are equal
     * return the same value for this method.
     *
     * @return the receiver's hash.
     */
    public int hashCode();

    /**
     * Returns whether this map is empty.
     *
     * @return {@code true} if this map has no elements, {@code false}
     * otherwise.
     * @see #size()
     */
    public boolean isEmpty();

    /**
     * Removes a mapping with the specified key from this {@code Map}.
     *
     * @param key the key of the mapping to remove.
     * @return the value of the removed mapping or {@code null} if no mapping
     * for the specified key was found.
     * @throws UnsupportedOperationException if removing from this {@code Map} is not supported.
     */
    public String remove(String key);

    /**
     * Returns the number of mappings in this {@code Map}.
     *
     * @return the number of mappings in this {@code Map}.
     */
    public int size();

}
