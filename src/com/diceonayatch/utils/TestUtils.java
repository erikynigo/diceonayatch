package com.diceonayatch.utils;

import com.diceonayatch.metadata.GameConstants;
import com.diceonayatch.model.DiceRoll;
import com.diceonayatch.model.EvaluationResult;
import com.diceonayatch.model.ScoreCategory;
import com.diceonayatch.service.DiceRollEvaluator;

/**
 * Utils class to set up DiceRoll scenarios and test behavior.
 *
 * If this were a real project, we would use a proper unit testing
 * framework like JUnit or TestNG. For the purposes of this exercise,
 * however, this should suffice.
 *
 * @author Erik Ynigo 9/4/18.
 */
public class TestUtils
{
    private static int totalTestsRun;
    private static int totalTestsPassed;
    private static int totalTestsFailed;

    public static void runTests(DiceRollEvaluator evaluator)
    {
        init();

        ScoreCategory[] NUMBERS_ONLY = {
            ScoreCategory.ONES,
            ScoreCategory.TWOS,
            ScoreCategory.THREES,
            ScoreCategory.FOURS,
            ScoreCategory.FIVES,
            ScoreCategory.SIXES,
            ScoreCategory.SEVENS,
            ScoreCategory.EIGHTS
        };

        DiceRoll dr = null;
        EvaluationResult result = null;

        // Score-related tests

        // Scenario: rolls containing no duplicate results, evaluated against number-specific
        // categories. Scores should just be the number being evaluated, if contained in DiceRoll.
        // E.g. for ONES = 1, TWOS = 2, ... , FIVES = 5, SIXES = 0, SEVENS = 0, EIGHTS = 0
        dr = new DiceRoll(new int[] {1,2,3,4,5});

        for (int i = 0; i < NUMBERS_ONLY.length; i++)
        {
            ScoreCategory category = NUMBERS_ONLY[i];
            result = evaluator.getScoreForCategory(dr, category);
            int expected = i < dr.getTotalRolls() ? category.getValue() : 0;
            verifyScore(dr, category, expected, result.score);
        }

        // Scenario: if the same number appears multiple times, evaluating the RollDice
        // against the category of the number that appears multiple times should
        // return the number in question multiplied by its frequency
        dr = new DiceRoll(new int[] {4,4,4,4,5});
        result = evaluator.getScoreForCategory(dr, ScoreCategory.FOURS);
        verifyScore(dr, result.category, 16, result.score);

        // Scenario: rolls containing three of a kind, return the sum of all results
        dr = new DiceRoll(new int[] {1,1,1,2,3});
        result = evaluator.getScoreForCategory(dr, ScoreCategory.THREE_OF_A_KIND);
        verifyScore(dr, result.category, 8, result.score);

        // If it contains three of a kind plus a pair it is NOT deemed three of a kind
        // as the whole hand is considered a full house, so in this case it should return 0
        dr = new DiceRoll(new int[] {1,1,1,2,2});
        result = evaluator.getScoreForCategory(dr, ScoreCategory.THREE_OF_A_KIND);
        verifyScore(dr, result.category, 0, result.score);

        // Scenario: roll containing four of a kind, return the sum of all results
        dr = new DiceRoll(new int[] {1,1,1,1,2});
        result = evaluator.getScoreForCategory(dr, ScoreCategory.FOUR_OF_A_KIND);
        verifyScore(dr, result.category, 6, result.score);

        // If it contains all the same numbers, it should NOT be evaluated as four of a kind,
        // as the whole hand is considered all different, which is a different category, and
        // should return 0 in this case
        dr = new DiceRoll(new int[] {1,1,1,1,1});
        result = evaluator.getScoreForCategory(dr, ScoreCategory.FOUR_OF_A_KIND);
        verifyScore(dr, result.category, 0, result.score);

        // Containing a full house should return 25, otherwise 0
        dr = new DiceRoll(new int[] {1,1,1,8,8});
        result = evaluator.getScoreForCategory(dr, ScoreCategory.FULL_HOUSE);
        verifyScore(dr, result.category, GameConstants.SCORE_FULL_HOUSE, result.score);

        dr = new DiceRoll(new int[] {1,1,1,2,3});
        result = evaluator.getScoreForCategory(dr, ScoreCategory.FULL_HOUSE);
        verifyScore(dr, result.category, 0, result.score);

        // Scenario: small straight should return 30, otherwise 0
        dr = new DiceRoll(new int[] {4,2,8,3,1});
        result = evaluator.getScoreForCategory(dr, ScoreCategory.STRAIGHT_SMALL);
        verifyScore(dr, result.category, GameConstants.SCORE_STRAIGTH_SMALL, result.score);

        dr = new DiceRoll(new int[] {8,2,7,1,4});
        result = evaluator.getScoreForCategory(dr, ScoreCategory.STRAIGHT_SMALL);
        verifyScore(dr, result.category, 0, result.score);

        // Scenario: large straights should return 40, otherwise 0
        dr = new DiceRoll(new int[] {5,2,1,3,4});
        result = evaluator.getScoreForCategory(dr, ScoreCategory.STRAIGHT_LARGE);
        verifyScore(dr, result.category, GameConstants.SCORE_STRAIGTH_LARGE, result.score);

        dr = new DiceRoll(new int[] {5,1,4,8,6});
        result = evaluator.getScoreForCategory(dr, ScoreCategory.STRAIGHT_LARGE);
        verifyScore(dr, result.category, 0, result.score);

        // Scenario: All different numbers should return 40, otherwise 0
        dr = new DiceRoll(new int[] {1,2,3,4,5});
        result = evaluator.getScoreForCategory(dr, ScoreCategory.ALL_DIFFERENT);
        verifyScore(dr, result.category, GameConstants.SCORE_ALL_DIFFERENT, result.score);

        dr = new DiceRoll(new int[] {1,2,3,4,4});
        result = evaluator.getScoreForCategory(dr, ScoreCategory.ALL_DIFFERENT);
        verifyScore(dr, result.category, 0, result.score);

        // Scenario: All different numbers should return 40, otherwise 0
        // Large straights evaluated as all different should return 40
        dr = new DiceRoll(new int[] {1,3,5,7,8});
        result = evaluator.getScoreForCategory(dr, ScoreCategory.ALL_DIFFERENT);
        verifyScore(dr, result.category, GameConstants.SCORE_ALL_DIFFERENT, result.score);

        dr = new DiceRoll(new int[] {1,2,3,4,5});
        result = evaluator.getScoreForCategory(dr, ScoreCategory.ALL_DIFFERENT);
        verifyScore(dr, result.category, GameConstants.SCORE_ALL_DIFFERENT, result.score);

        dr = new DiceRoll(new int[] {8,8,8,8,8});
        result = evaluator.getScoreForCategory(dr, ScoreCategory.ALL_DIFFERENT);
        verifyScore(dr, result.category, 0, result.score);

        // Scenario:  All same numbers should return 50, otherwise 0
        dr = new DiceRoll(new int[] {1,1,1,1,1});
        result = evaluator.getScoreForCategory(dr, ScoreCategory.ALL_SAME);
        verifyScore(dr, result.category, GameConstants.SCORE_ALL_SAME, result.score);

        dr = new DiceRoll(new int[] {1,1,1,1,2});
        result = evaluator.getScoreForCategory(dr, ScoreCategory.ALL_SAME);
        verifyScore(dr, result.category, 0, result.score);

        // Scenario: Chance category should return the sum of the results
        dr = new DiceRoll(new int[] {1,2,1,8,8});
        result = evaluator.getScoreForCategory(dr, ScoreCategory.CHANCE);
        verifyScore(dr, result.category, 20, result.score);

        // Category-related tests

        // Scenario: Verify that CHANCE is the highest scoring category for the following
        dr = new DiceRoll(new int[] {1,2,1,8,8});
        result = evaluator.getHighestScoringCategory(dr);
        verifyHighestScoringCategory(dr, ScoreCategory.CHANCE, result.category, result.score);

        // Scenario: Verify that ALL_SAME is the highest scoring category for the following
        dr = new DiceRoll(new int[] {1,1,1,1,1});
        result = evaluator.getHighestScoringCategory(dr);
        verifyHighestScoringCategory(dr, ScoreCategory.ALL_SAME, result.category, result.score);

        // Scenario: Verify that ALL_DIFFERENT is the highest scoring category for the following
        dr = new DiceRoll(new int[] {1,2,4,5,6});
        result = evaluator.getHighestScoringCategory(dr);
        verifyHighestScoringCategory(dr, ScoreCategory.ALL_DIFFERENT, result.category, result.score);

        // Scenario: Verify that STRAIGHT_LARGE is the highest scoring category for the following
        // NOTE: As mentioned before, this is because while ALL_DIFFERENT also scores 40,
        // STRAIGHT_LARGER is the first one found in the GameConstants.ALL_SCORE_CATEGORIES array
        dr = new DiceRoll(new int[] {4,5,6,7,8});
        result = evaluator.getHighestScoringCategory(dr);
        verifyHighestScoringCategory(dr, ScoreCategory.STRAIGHT_LARGE, result.category, result.score);

        // Scenario: Verify that FOUR_OF_A_KIND is the highest scoring category for the following
        dr = new DiceRoll(new int[] {7,8,8,8,8});
        result = evaluator.getHighestScoringCategory(dr);
        verifyHighestScoringCategory(dr, ScoreCategory.FOUR_OF_A_KIND, result.category, result.score);

        // Scenario: Verify that FULL_HOUSE is the highest scoring category for the following
        dr = new DiceRoll(new int[] {1,1,2,2,2});
        result = evaluator.getHighestScoringCategory(dr);
        verifyHighestScoringCategory(dr, ScoreCategory.FULL_HOUSE, result.category, result.score);

        // Scenario: Verify that THREE_OF_A_KIND is the highest scoring category for the following
        // NOTE: As mentioned before, this is because while CHANCE also scores 8,
        // THREE_OF_A_KIND is the first one found in the GameConstants.ALL_SCORE_CATEGORIES array
        dr = new DiceRoll(new int[] {1,1,1,2,3});
        result = evaluator.getHighestScoringCategory(dr);
        verifyHighestScoringCategory(dr, ScoreCategory.THREE_OF_A_KIND, result.category, result.score);

        finish();
    }

    private static void init()
    {
        totalTestsRun = 0;
        totalTestsPassed = 0;
        totalTestsFailed = 0;

        System.out.println("\n--------------------------");
        System.out.println("--- Initializing Tests ---");
        System.out.println("--------------------------\n");
    }

    private static void verifyScore(DiceRoll diceRoll, ScoreCategory category, int expectedScore, int actualScore)
    {
        boolean pass = actualScore == expectedScore;
        String status = pass ? "PASSED" : "--FAILED--";
        String format = "Rolls: %-10s Category: %-18s Expected Score: %-4s Actual Score: %-4s Status %s%n";
        System.out.printf(format, diceRoll.toString(), category, expectedScore, actualScore, status);

        updateStats(pass);
    }

    private static void verifyHighestScoringCategory(DiceRoll diceRoll, ScoreCategory expectedCategory, ScoreCategory actualCategory, int score)
    {
        boolean pass = expectedCategory == actualCategory;
        String status = pass ? "PASSED" : "--FAILED--";
        String format = "Rolls: %-10s Expected Highest Scoring Category: %-18s Actual Highest Scoring Category: %-18s Score: %-4s Status %s%n";
        System.out.printf(format, diceRoll.toString(), expectedCategory, actualCategory, score, status);

        updateStats(pass);
    }

    private static void updateStats(boolean passed)
    {
        totalTestsRun++;

        if (passed)
        {
            totalTestsPassed++;
        }
        else
        {
            totalTestsFailed++;
        }
    }

    private static void finish()
    {
        String format = "\nFinished running %s tests.\tPassed: %s ✔\tFailed: %s ✘\n";
        System.out.printf(format, totalTestsRun, totalTestsPassed, totalTestsFailed);
    }
}
