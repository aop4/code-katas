package com.andrewpuglionesi.leetcode.snapshotmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A map that allows querying values for a key at different points in time. This is accomplished with on-demand
 * snapshots that preserve the state of the map.
 */
public class SnapshotMap<K, V> {
    /**
     * Underlying map for this data structure. Maps a key to its value at each snapshot where the value was updated.
     */
    private final Map<K, List<Snapshot>> dataStore;
    /**
     * The ID of the snapshot currently being constructed. Once snap() is called, this snapshot will be finalized
     * and future updates will apply to the next snapshot.
     */
    private int snapId;

    /**
     * Represents a value at a particular snapshot.
     */
    private class Snapshot {
        private final int snapId;
        private V value;

        /**
         * Creates a new snapshot with the specified snapId and value.
         */
        public Snapshot(final int snapId, final V value) {
            this.snapId = snapId;
            this.value = value;
        }
    }

    /**
     * Initializes an empty SnapshotMap.
     */
    public SnapshotMap() {
        this.dataStore = new HashMap<>();
        this.snapId = 0;
    }

    /**
     * Stores a key-value pair for the current snapshot. If there is already a value stored in the current snapshot,
     * overwrites it.
     * @param key key to update.
     * @param val value to store under the key.
     */
    public void put(final K key, final V val) {
        if (this.dataStore.get(key) == null) {
            final List<Snapshot> snapshots = new ArrayList<>();
            snapshots.add(new Snapshot(this.snapId, val));
            this.dataStore.put(key, snapshots);
        } else {
            final List<Snapshot> snapshots = this.dataStore.get(key);
            final Snapshot latestSnapshot = snapshots.get(snapshots.size() - 1);
            if (this.snapId == latestSnapshot.snapId) {
                latestSnapshot.value = val;
            } else if (this.snapId > latestSnapshot.snapId && !Objects.equals(val, latestSnapshot.value)) {
                snapshots.add(new Snapshot(this.snapId, val));
            }
        }
    }

    /**
     * Finalizes the current snapshot and begins the next one. Any updates to the map after calling this method
     * will be applied to the next snapshot.
     * @return the ID of the snapshot that was finalized. Snapshots begin at 0 and are incremented by 1 with each call
     * to this method.
     */
    public int snap() {
        return this.snapId++;
    }

    /**
     * @return the most recent value for the key, or null if no value was ever set.
     */
    public V get(final K key) {
        return this.get(key, this.snapId);
    }

    /**
     * Gets a value for the specified key at the specified snapshot.
     * @param key key for the value to retrieve.
     * @param snapId integer ID of the snapshot from which to retrieve the value. Snapshots begin at 0 and are
     *               incremented by 1 with each call to {@link SnapshotMap#snap}.
     * @return the value for the given key at the given snapshot, or null if there was no value for the key when the
     * snapshot was taken.
     * @throws IllegalArgumentException if there is no snapshot with the specified snapId.
     */
    public V get(final K key, final int snapId) {
        if (snapId < 0 || snapId > this.snapId) {
            throw new IllegalArgumentException(String.format("No snapshot exists for snapshot ID: %d. Current snapshot ID is %d.", snapId, this.snapId));
        }
        final List<Snapshot> snapshots = this.dataStore.get(key);
        if (snapshots == null || snapshots.isEmpty()) {
            return null;
        }
        final Snapshot firstSnapshot = snapshots.get(0);
        if (snapId < firstSnapshot.snapId) {
            // there is no snapshot available at or before the desired snapId
            return null;
        }
        return findValueAtOrBeforeSnap(snapshots, snapId);
    }

    /**
     * Performs a binary search on a list of snapshots to find the latest snapshot whose snapshot ID is equal to or less
     * than {@code snapId}. {@code snapshots} must sorted in ascending order by snapId.
     * @return the value of the snapshot.
     */
    private V findValueAtOrBeforeSnap(final List<Snapshot> snapshots, final int snapId) {
        int start = 0;
        int middle = 0;
        int end = snapshots.size();
        while (start < end) {
            middle = (start + end) / 2;
            if (snapshots.get(middle).snapId == snapId) {
                return snapshots.get(middle).value;
            } else if (snapshots.get(middle).snapId > snapId) {
                end = middle;
            } else {
                start = middle + 1;
            }
        }
        // an adjustment to the binary search--find the latest snap with a lower ID than snapId if there is no exact match
        int retIndex = middle;
        while (retIndex + 1 < snapshots.size() && snapshots.get(retIndex + 1).snapId < snapId) {
            retIndex++;
        }
        while (retIndex - 1 > 0 && snapshots.get(retIndex).snapId > snapId) {
            retIndex--;
        }
        return snapshots.get(retIndex).value;
    }
}
