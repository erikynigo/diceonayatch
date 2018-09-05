package com.diceonayatch.metadata;

import com.diceonayatch.model.ScoreCategory;

/**
 * Game constants used in the game.
 *
 * @author Erik Ynigo 9/4/18.
 */
public class GameConstants
{
    public static int TOTAL_NUMBER_OF_DICE = 5;

    public static int VALUES_PER_DIE = 8;

    public static int SCORE_ALL_SAME = 50;
    public static int SCORE_ALL_DIFFERENT = 40;
    public static int SCORE_STRAIGTH_LARGE = 40;
    public static int SCORE_STRAIGTH_SMALL = 30;
    public static int SCORE_FULL_HOUSE = 25;

    public static ScoreCategory[] ALL_SCORE_CATEGORIES = {
            ScoreCategory.ONES,
            ScoreCategory.TWOS,
            ScoreCategory.THREES,
            ScoreCategory.FOURS,
            ScoreCategory.FIVES,
            ScoreCategory.SIXES,
            ScoreCategory.SEVENS,
            ScoreCategory.EIGHTS,
            ScoreCategory.THREE_OF_A_KIND,
            ScoreCategory.FOUR_OF_A_KIND,
            ScoreCategory.FULL_HOUSE,
            ScoreCategory.STRAIGHT_SMALL,
            ScoreCategory.STRAIGHT_LARGE,
            ScoreCategory.ALL_DIFFERENT,
            ScoreCategory.ALL_SAME,
            ScoreCategory.CHANCE
    };
}
