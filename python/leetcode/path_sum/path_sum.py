from binarytree import Node

def has_path_sum(root: Node, target_sum: int) -> bool:
    """Determines whether a binary tree has a root-to-leaf path where the sum of the nodes' values
    is equal to target_sum.
    All nodes' values must be numeric.
    
    Args:
        root: The root of the binary tree.
        target_sum: The path-sum to search for.
    Returns:
        true if there is a root-to-leaf path where the sum is target_sum.
    """
    if root is None:
        # this will occur in cases where the parent has one null child and one non-null child
        return False
    target_sum = target_sum - root.val
    if root.left is None and root.right is None:
        # the node is a leaf
        return target_sum == 0
    return has_path_sum(root.left, target_sum) or has_path_sum(root.right, target_sum)
