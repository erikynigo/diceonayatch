package com.diceonayatch.service;

import com.diceonayatch.metadata.GameConstants;
import com.diceonayatch.model.DiceRoll;

import java.util.Random;

/**
 * Generates a DiceRoll object simulating and storing the results
 * for the number of dice rolls indicated by the game's metadata.
 *
 * @author Erik Ynigo 9/4/18.
 */
public class DiceRollGenerator
{
    private Random random;

    public DiceRollGenerator()
    {
        random = new Random();
    }

    /**
     * Simulates the amount of rolls as indicated by the metadata,
     * stores the results in a DiceRoll object.
     *
     * @return A DiceRoll object containing the results for each roll.
     */
    public DiceRoll roll()
    {
        int[] results = new int[GameConstants.TOTAL_NUMBER_OF_DICE];

        for (int i = 0; i < GameConstants.TOTAL_NUMBER_OF_DICE; i++)
        {
            results[i] = getRandomValue(1, GameConstants.VALUES_PER_DIE);
        }

        DiceRoll diceRoll = new DiceRoll(results);
        return diceRoll;
    }

    /**
     * Generates a random int between the given range, both ends inclusive.
     *
     * @param minVal The min value in the range.
     * @param maxVal The max value in the range.
     *
     * @return A random int that falls within the given range, both ends inclusive.
     */
    private int getRandomValue(int minVal, int maxVal)
    {
        if (minVal >= maxVal)
        {
            throw new IllegalArgumentException("The value for maxVal must be greater than minVal.");
        }

        return random.nextInt((maxVal - minVal) + 1) + minVal;
    }
}
