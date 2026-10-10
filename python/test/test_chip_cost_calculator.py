import unittest
from leetcode.min_cost_to_move_chips.chip_cost_calculator import ChipCostCalculator

class TestChipCostCalculator(unittest.TestCase):
    def test_min_cost_empty_list(self):
        positions = []
        self.assertEqual(0, ChipCostCalculator.min_cost_to_move_chips(positions))

    def test_min_cost_singleton_list(self):
        positions = [99]
        self.assertEqual(0, ChipCostCalculator.min_cost_to_move_chips(positions))

    def test_min_cost_all_odd(self):
        self.assertEqual(0, ChipCostCalculator.min_cost_to_move_chips([1, 3, 5, 999]))

    def test_min_cost_all_even(self):
        self.assertEqual(0, ChipCostCalculator.min_cost_to_move_chips([0, 2, 100]))

    def test_min_cost_equal_num_evens_and_odds(self):
        self.assertEqual(3, ChipCostCalculator.min_cost_to_move_chips([0, 4, 8, 1, 5, 9]))

    def test_min_cost_more_evens_than_odds(self):
        self.assertEqual(3, ChipCostCalculator.min_cost_to_move_chips([0, 2, 4, 6, 1, 3, 5]))

    def test_min_cost_more_odds_than_evens(self):
        self.assertEqual(3, ChipCostCalculator.min_cost_to_move_chips([0, 2, 4, 1, 3, 5, 13]))
