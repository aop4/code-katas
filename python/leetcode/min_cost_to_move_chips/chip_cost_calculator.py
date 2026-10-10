from typing import List

class ChipCostCalculator:
    """This is an interesting problem as a logic puzzle. Once you figure out the solution, it's
    pretty trivial to implement it. The key is that shifting chips by an even number of spaces has
    no cost. Therefore, shifting a chip from an odd position to any other odd position costs
    nothing, and shifting a chip from an even position to any other even position costs nothing.
    The most you will ever have to pay to move a chip anywhere on the board is therefore 1, when
    you have to move it from an even position to any odd position or vice versa. So the minimum
    cost can be found by counting up the number of even and odd chips. If there are fewer even
    chips, the cheapest way to line up all chips is by moving all the even chips to an arbitrary
    odd space, at a cost of 1 per chip. All the odd chips can be moved to that space for free.
    """

    def min_cost_to_move_chips(positions: List[int]) -> int:
        """Given a list containing the positions of chips in a linear arrangement, calculates
        the minimum cost to move all chips to the same position if moving chips by 2 spaces
        has no cost and moving a chip by a single space has a cost of 1."""
        num_even_chips = len([chip for chip in positions if chip % 2 == 0])
        num_odd_chips = len(positions) - num_even_chips
        return min(num_even_chips, num_odd_chips)
