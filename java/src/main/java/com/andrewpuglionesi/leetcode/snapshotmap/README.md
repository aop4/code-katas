This was loosely based off of [Leetcode #1146: Snapshot Array](https://leetcode.com/problems/snapshot-array/), which
involves building an array data structure that allows you to take a snapshot of the data and then query the array by
snapshot to see what data it held at a previous point in time.

As I was thinking about it, a few possibilities crossed my mind. You could use an array, access it normally, and
then make a new copy of it every time there's a new snapshot. The obvious drawback is the extra space needed to do this
in cases where updates are sparse. So instead of doing that, you could have one array that stores, at
each index, a list of the values stored at every snapshot. There are no space savings in this case unless you exclude
snapshots from the list when the value didn't change during that snapshot.

Assuming there are a lot of snapshots, you might be able to speed up accessing a sparse snapshot list by taking advantage
of its natural ordering and using a binary search to find the first snapshot before the requested snapshot ID.

Finally, if the data are very sparse and many indices are never actually used, you might as well ditch the array and use
a map with integer keys instead. That led me to this implementation of a more generic snapshot map.

I found this problem interesting because a lot of databases use the concept of snapshots to manage transactions, e.g. to
prevent reading uncommitted data.