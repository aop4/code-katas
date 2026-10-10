import unittest
from leetcode.path_sum.path_sum import has_path_sum
from binarytree import build, Node

class TestPathSum(unittest.TestCase):
    def test_has_path_sum_null_root(self):
        self.assertFalse(has_path_sum(None, 0))

    def test_has_path_sum_single_node(self):
        root = Node(5)
        self.assertTrue(has_path_sum(root, 5))
        self.assertFalse(has_path_sum(root, 0))

    def test_has_path_sum_two_nodes(self):
        root = build([2, 3])
        self.assertTrue(has_path_sum(root, 5))
        self.assertFalse(has_path_sum(root, 2))
        self.assertFalse(has_path_sum(root, 3))
    
    def test_has_path_sum_negative_nodes(self):
        root = build([-2, -3])
        self.assertTrue(has_path_sum(root, -5))

    def test_has_path_sum_full_tree(self):
        """
                  2
               3     3
             1  1  1   1   
        """
        root = build([2, 3, 3, 1, 1, 1, 1])
        self.assertTrue(has_path_sum(root, 6))
        self.assertFalse(has_path_sum(root, 5))
        self.assertFalse(has_path_sum(root, 2))

    def test_has_path_sum_not_full_tree(self):
        """
                  2
               3    3
             1        1
        """
        root = build([2, 3, 3, 1, None, None, 1])
        self.assertTrue(has_path_sum(root, 6))
        self.assertFalse(has_path_sum(root, 5))
    
    def test_has_path_sum_zigzag(self):
        """
                    2
             3
                 5
               1 
        """
        root = build([2, 3, None, None, 5, None, None, None, None, 1])
        self.assertTrue(has_path_sum(root, 11))
