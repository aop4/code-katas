# Flight Path

## Problem statement
Bilbo bought a programmable drone to help him create maps of his lovely homeland, New Zealand. He can program the drone to fly between a series of points in 3-dimensional space. For simplicity's sake, let's represent these points as unitless coordinates of the form (x, y, z). For example, Bilbo might want to launch the drone from the point `(0, 0, 0)`, fly it one unit vertically to reach `(0, 1, 0)`, fly it 2 units to his left to reach `(-2, 1, 0)`, and then fly it 3 units forward to `(-2, 1, 3)`. Bilbo's drone can seamlessly move along all three axes at the same time without stopping or changing direction (e.g., it can fly diagonally between `(0, 0, 0)` and `(1, 2, 3)`). Bilbo wants to know how far his drone will travel on its journeys, so he's submitted a feature request to your drone company.

Given a list of points in a 3-dimensional coordinate space, calculate the length of a path that sequentially connects those points. The path should travel from point to point in the same order they appear in the list.

## Examples
Input: `[(0, 0, 0), (1, 0, 0), (1, 1, 0), (1, 1, 1)]`  
Ouput: `3`  

Explanation: The drone travels 1 unit along the x-axis, 1 unit along the y-axis, and then 1 unit along the z-axis. In total, it has traveled 3 units.

Input: `[(0, 0, 0), (1, 1, 0)]`  
Output: `1.4142135623730951`  

Explanation: This is like traveling in a 2-dimensional coordinate space between (0, 0) and (1, 1). The Pythagorean theorem can be used to calculate the length of that diagonal path:  
√((x₂ - x₁)² + (y₂ - y₁)²) = √((1 - 0)² + (1 - 0)²) = √(1² + 1²) = √2 ≈ 1.414