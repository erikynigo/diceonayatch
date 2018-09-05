package com.diceonayatch.service;

import com.diceonayatch.metadata.GameConstants;
import com.diceonayatch.model.DiceRoll;
import com.diceonayatch.model.EvaluationResult;
import com.diceonayatch.model.ScoreCategory;

import java.util.*;

/**
 * Evaluates the results contained in a DiceRoll object, for
 * a given ScoreCategory, according to the following rules:
 *
 * Ones, Twos, Threes, Fours, Fives, Sixes, Sevens, Eights:
 * Sum of all dice that match the title of the category.  
 * For example {4,4,4,4,5} scores 16 for fours.
 *
 * ThreeOfAKind:
 * Sum of all dice if there are at least three dice that are the
 * same, otherwise zero. For example {1,1,1,2,8} scores 13.
 *
 * FourOfAKind:
 * Sum of all dice if there are at least four dice that are the
 * same, otherwise zero. For example {1,1,1,1,8} scores 12.
 *
 * FullHouse:
 * If there are three of one kind and two of another score 25,
 * otherwise score zero. For example {1,1,1,8,8} scores 25.
 *
 * SmallStraight:
 * If there are four dice in sequence score 30, otherwise zero.
 * For example {1,2,3,4,7} scores 30.
 *
 * LargeStraight:
 * If all five dice fall in sequence score 40, otherwise zero.  
 * For example {3,4,5,6,7} scores 40.
 *
 * AllDifferent: If all five dice have unique values score 40,
 * otherwise zero.  For example {1,2,4,6,8} scores 40.
 *
 * Chance:
 * Sum of all dice. For example {1,2,1,8,8} scores 20.
 *
 * AllSame: If all five dice have the same value score 50,
 * otherwise zero.  For example {1,1,1,1,1} scores 50.
 *
 * @author Erik Ynigo 9/4/18.
 */
public class DiceRollEvaluator
{
    public DiceRollEvaluator()
    {

    }

    /**
     * Given a DiceRoll and a ScoreCategory object, it calculates the score
     * achieved by the rolls in the given category, according to the rules of
     * the game.
     *
     * @param diceRoll The DiceRoll object representing the results for a series
     *                 of rolls.
     * @param category The ScoreCategory we want to evaluate the rolls against.
     *
     * @return An EvaluationResult object containing the total score achieved
     *         by the rolls, as well as the ScoreCategory it was achieved on,
     *         according to the rules of the game.
     */
    public EvaluationResult getScoreForCategory(DiceRoll diceRoll, ScoreCategory category)
    {
        EvaluationResult result = new EvaluationResult();
        result.score = evaluate(diceRoll, category);
        result.category = category;

        return  result;
    }

    /**
     * Given a DiceRoll object, it returns the ScoreCategory where the rolls
     * scored the highest on.
     *
     * @param diceRoll The DiceRoll object representing the results for a series
     *                 of rolls.
     *
     * @return The EvaluationResult object containing the ScoreCategory where the
     *         rolls scored the highest on, as well as the score achieved. If two
     *         or more categories scored equally high, it will return the first
     *         ScoreCategory with the highest score, where the order is determined
     *         by the GameConstants.ALL_SCORE_CATEGORIES array.
     */
    public EvaluationResult getHighestScoringCategory(DiceRoll diceRoll)
    {
        EvaluationResult result = new EvaluationResult();

        // Local reference for more efficient lookup
        final ScoreCategory[] categories = GameConstants.ALL_SCORE_CATEGORIES;

        // Default response
        int maxScore = 0;
        ScoreCategory maxCategory = categories[0];

        // Evaluate all available categories
        for (int i = 0; i < categories.length; i++)
        {
            ScoreCategory category = categories[i];
            int score = evaluate(diceRoll, category);

            // If new score is higher than previous max score, update
            if (score > maxScore)
            {
                maxScore = score;
                maxCategory = category;
            }
        }

        result.score = maxScore;
        result.category = maxCategory;

        return result;
    }


    /**
     * Internal function to evaluate a given DiceRoll agains a ScoreCategory.
     *
     * @param diceRoll he DiceRoll object representing the results for a series
     *                 of rolls.
     * @param category The ScoreCategory we want to evaluate the rolls against.
     *
     * @return An int representing the total score achieved by the rolls for the
     *         given category, according to the rules of the game.
     */
    private int evaluate(DiceRoll diceRoll, ScoreCategory category)
    {
        if (diceRoll == null)
        {
            throw new IllegalArgumentException("DiceRoll cannot be null.");
        }

        // Generate number frequency table where:
        // key = the result of the roll
        // value = number of times this result has appeared
        HashMap<Integer, Integer> table = new HashMap<Integer, Integer>();

        for (int i = 0; i < diceRoll.getTotalRolls(); i++)
        {
            int result = diceRoll.getResultForRoll(i);

            if (!table.containsKey(result))
            {
                table.put(result, 1);
            }
            else
            {
                int newFrequency = table.get(result) + 1;
                table.put(result, newFrequency);
            }
        }

        // Map to proper evaluating function
        switch (category)
        {
            case ONES:
            case TWOS:
            case THREES:
            case FOURS:
            case FIVES:
            case SIXES:
            case SEVENS:
            case EIGHTS:
                return evaluateSpecificNumber(table, category);
            case THREE_OF_A_KIND:
                return evaluateThreeOfAKind(table);
            case FOUR_OF_A_KIND:
                return evaluateFourOfAKind(table);
            case FULL_HOUSE:
                return evaluateFullHouse(table);
            case STRAIGHT_SMALL:
                return evaluateSmallStraight(table);
            case STRAIGHT_LARGE:
                return evaluateLargeStraight(table);
            case ALL_DIFFERENT:
                return evaluateAllDifferent(table);
            case ALL_SAME:
                return evaluateAllSame(table);
            default:
                return evaluateChance(table);
        }

    }

    private int evaluateSpecificNumber(HashMap<Integer, Integer> table, ScoreCategory category)
    {
        // The enum representing the category also represents the specific number
        // to evaluate. I.e. ScoreCategory.ONES = 1, ScoreCategory.TWOS = 2, etc
        int number = category.getValue();

        // Sum of all results that match the number of the category
        if (table.containsKey(number))
        {
            return number * table.get(number);
        }

        return 0;
    }

    private int evaluateAllSame(HashMap<Integer, Integer> table)
    {
        // If all results are the same, then we should only have one
        // entry in the result frequency table
        return table.size() == 1 ? GameConstants.SCORE_ALL_SAME : 0;
    }

    private int evaluateAllDifferent(HashMap<Integer, Integer> table)
    {
        // If all results are different, then the number of entries in
        // the result frequency table should equal the number of dice
        return table.size() == GameConstants.TOTAL_NUMBER_OF_DICE ?
            GameConstants.SCORE_ALL_DIFFERENT : 0;

    }

    private int evaluateChance(HashMap<Integer, Integer> table)
    {
        // For chance, sum all dice results.
        // An array would be more efficient when adding entries,
        // but I decided to stick with the frequency table for
        // simplicity in this example, as this function will be reused.
        return sumEntries(table);
    }

    private int evaluateThreeOfAKind(HashMap<Integer, Integer> table)
    {
        // If the result frequency table has 3 entries, we need to continue
        // evaluating what the hand is, as:
        // - Three of a kind
        // - Two different pairs and a different number
        // will both generate 3 entries.
        if (table.size() == 3)
        {
            int sum = 0;
            boolean isThreeOfAKind = false;

            for (Map.Entry<Integer, Integer> entry : table.entrySet())
            {
                int result = entry.getKey();
                int frequency = entry.getValue();
                sum += (result * frequency);

                if (frequency == 3)
                {
                    isThreeOfAKind = true;
                }
            }

            return isThreeOfAKind ? sum : 0;
        }

        return 0;
    }

    private int evaluateFourOfAKind(HashMap<Integer, Integer> table)
    {
        // If the result frequency table has 2 entries, we need to continue
        // evaluating what the hand is, as:
        // - Four of a kind
        // - Full house
        // will both generate 3 entries.
        if (table.size() == 2)
        {
            //The frequency for the two results in a four of a kind
            // hand will be either 1 or 4
            boolean isFourOfAKind = checkTwoEntryResult(table, 1, 4);

            return isFourOfAKind ? sumEntries(table) : 0;
        }

        return 0;
    }

    private int evaluateSmallStraight(HashMap<Integer, Integer> table)
    {
       return evaluateStraight(table, true);
    }

    private int evaluateLargeStraight(HashMap<Integer, Integer> table)
    {
        return evaluateStraight(table, false);
    }

    private int evaluateStraight(HashMap<Integer, Integer> table, boolean asSmall)
    {
        // Evaluate whether the numbers are in sequence.
        // Small straight: 4 numbers in sequence out of 5
        // Large straight: all 5 numbers in sequence

        // First check, to see if the frequency table has at least 4 different
        // entries. If it doesn't, there cannot be a straight
        int size = table.size();
        if (size < 4)
        {
            return 0;
        }

        // Next, convert keys (which are the results) into an array and
        // sort in ascending order
        // NOTE: The keys come from a set, so there will be no duplicates.
        // This allows us to check the size at the end to determine if it's a
        // small straight (length of 4) or a large straight (length of 5)
        Integer[] results = table.keySet().toArray(new Integer[size]);
        Arrays.sort(results);

        int gaps = 0;

        for (int i = 0; i < results.length - 1; i++)
        {
            int cur = results[i];
            int next = results[i + 1];

            // Found a gap, keep track if it
            if (cur != next - 1)
            {
                gaps++;
            }
        }

        // Return the proper score depending on how we are to evaluate the straight.
        // If we are to evaluate it as a small straight, we would need to make sure
        // that there are no  more than 1 gap.
        // Notice that a small straight could have 0 gaps if:
        // - A result was repeated, so there are actually only 4 keys instead of 5
        // - The straight is actually a large straight being evaluated as small
        if (asSmall && gaps <= 1)
        {
            return GameConstants.SCORE_STRAIGTH_SMALL;
        }
        // If we are to evaluate as a large straight, then size must be equal to 5,
        else if (size == GameConstants.TOTAL_NUMBER_OF_DICE && gaps == 0)
        {
            return GameConstants.SCORE_STRAIGTH_LARGE;
        }

        return 0;
    }

    private int evaluateFullHouse(HashMap<Integer, Integer> table)
    {
        // If the result frequency table has 2 entries, we need to continue
        // evaluating what the hand is, as:
        // - Four of a kind
        // - Full house
        // will both generate 3 entries.
        if (table.size() == 2)
        {
            //The frequency for the two results in a full house
            // hand will be either 2 or 3
            boolean isFullHouse = checkTwoEntryResult(table, 2, 3);

            return isFullHouse ? GameConstants.SCORE_FULL_HOUSE : 0;
        }

        return 0;
    }

    /**
     * Determines whether or not a two entry result frequency table for a five Dice game contains
     * a four of a kind hand, or a full house hand, by iterating through the entries and evaluating
     * the frequencies against the given allowed frequencies. E.g., if checking against a full house,
     * the allowed frequencies would be 2 and 3. If checking against a four of a kind, the allowed
     * frequencies would be 1 and 4.
     *
     * @param table             The result frequency table stating the result as the key, and
     *                          the value as the number of times the result appeared in the hand
     * @param allowedFrequency1 The first allowed frequency
     * @param allowedFrequency2 The second allowed frequency
     *
     * @return true whether the frequencies in the table match the allowed frequencies,
     *         false otherwise.
     */
    private boolean checkTwoEntryResult(HashMap<Integer, Integer> table, int allowedFrequency1,
        int allowedFrequency2)
    {
        for (int result : table.keySet())
        {
            int frequency = table.get(result);

            if (frequency != allowedFrequency1 && frequency != allowedFrequency2)
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Adds the results in the given result frequency table.
     *
     * @param table The result frequency table stating the result as the key, and
     *              the value as the number of times the result appeared in the hand
     *
     * @return An int representing the sum of all results times their frequencies.
     */
    private int sumEntries(HashMap<Integer, Integer> table)
    {
        int sum = 0;

        for (Map.Entry<Integer, Integer> entry : table.entrySet())
        {
            int val = entry.getKey() * entry.getValue();
            sum += val;
        }

        return sum;
    }
}
