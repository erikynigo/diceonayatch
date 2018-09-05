package com.diceonayatch.model;

/**
 * Represents all possible categories a DiceRoll object can be evaluated for.
 *
 * @author Erik Ynigo 9/4/18.
 */
public enum ScoreCategory
{
    ONES(1),
    TWOS(2),
    THREES(3),
    FOURS(4),
    FIVES(5),
    SIXES(6),
    SEVENS(7),
    EIGHTS(8),
    THREE_OF_A_KIND(9),
    FOUR_OF_A_KIND(10),
    FULL_HOUSE(11),
    STRAIGHT_SMALL(12),
    STRAIGHT_LARGE(13),
    ALL_DIFFERENT(14),
    ALL_SAME(15),
    CHANCE(16);

    private final int value;

    ScoreCategory(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }
}
