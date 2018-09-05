Implementation for the "Dice on a Yacht" game. 

This game is played with five eight-sided dice. The five dice are rolled, and the result is scored againsta a category, according to the rules. The implementation also includes a function to determine the highest scoring category for a set of rolls.

Here is how to score each of the categories:

Ones, Twos, Threes, Fours, Fives, Sixes, Sevens, Eights: 
Sum of all dice that match the title of the category.  For example {4,4,4,4,5} scores 16 for fours.

ThreeOfAKind: 
Sum of all dice if there are at least three dice that are the same, otherwise zero. For example {1,1,1,2,8} scores 13.

FourOfAKind: 
Sum of all dice if there are at least four dice that are the same, otherwise zero. For example {1,1,1,1,8} scores 12.

FullHouse: 
If there are three of one kind and two of another score 25, otherwise score zero. For example {1,1,1,8,8} scores 25.

SmallStraight: 
If there are four dice in sequence score 30, otherwise zero. For example {1,2,3,4,7} scores 30.

LargeStraight: 
If all five dice fall in sequence score 40, otherwise zero. For example {3,4,5,6,7} scores 40.

AllDifferent: 
If all five dice have unique values score 40, otherwise zero. For example {1,2,4,6,8} scores 40.

Chance: 
Sum of all dice. For example {1,2,1,8,8} scores 20.

AllSame: 
If all five dice have the same value score 50, otherwise zero. For example {1,1,1,1,1} scores 50.
