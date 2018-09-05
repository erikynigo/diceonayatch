package com.diceonayatch.main;

import com.diceonayatch.model.DiceRoll;
import com.diceonayatch.model.EvaluationResult;
import com.diceonayatch.model.ScoreCategory;
import com.diceonayatch.service.DiceRollEvaluator;
import com.diceonayatch.service.DiceRollGenerator;
import com.diceonayatch.utils.TestUtils;

/**
 * Main class for testing the Dice On A Yatch game.
 * Creates the dice roll generator, evaluator, and tests outcomes.
 *
 * @author Erik Ynigo 9/4/18.
 */
public class Engine
{
    public static void main(String[] args)
    {
        // Example Usage:

        // Generate some rolls
        DiceRollGenerator generator = new DiceRollGenerator();
        DiceRoll diceRoll = generator.roll();

        // Evaluate the rolls
        DiceRollEvaluator evaluator = new DiceRollEvaluator();
        EvaluationResult chanceResult = evaluator.getScoreForCategory(diceRoll, ScoreCategory.CHANCE);
        EvaluationResult bestResult = evaluator.getHighestScoringCategory(diceRoll);

        System.out.println("Rolls: " + diceRoll.toString());
        System.out.println("Score for " + chanceResult.category + " category was " + chanceResult.score);
        System.out.println("Highest scoring category was " + bestResult.category + " with " + bestResult.score);

        // Testing and outcome verification:
        TestUtils.runTests(evaluator);
    }
}
