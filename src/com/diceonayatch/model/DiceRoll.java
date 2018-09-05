package com.diceonayatch.model;

/**
 * Represents the results from n dice rolls, where n is the number of
 * dice used in the game, as specified by the metadata.
 *
 * @author Erik Ynigo 9/4/18.
 */
public class DiceRoll
{
    private int[] results;

    public DiceRoll(int[] results)
    {
        if (results == null || results.length == 0)
        {
            throw new IllegalArgumentException("DiceRoll object must be initialized " +
                    "with at least one result.");
        }

        this.results = results;
    }

    /**
     * Convenience function that returns the value of the specified roll.
     * Note that results are zero-based. E.g., if the value for the first
     * roll needs to be known, the roll argument should be 0.
     *
     * @param roll The roll number for which the result needs to be known.
     *
     * @return The value for the roll queried.
     */
    public int getResultForRoll(int roll)
    {
        if (roll < 0)
        {
            throw new IllegalArgumentException("Roll number must not be negative.");
        }

        if (roll >= results.length)
        {
            throw new IndexOutOfBoundsException("There is no result for roll " + roll
                    + ". DiceRoll object only contains " + results.length + " results.");
        }

        return results[roll];
    }

    /**
     * Returns the internal array that stores
     * @return
     */
    public int[] getResults()
    {
        return results;
    }

    /**
     * Returns the number of rolls represented by this DiceRoll object
     */
    public int getTotalRolls()
    {
        return results.length;
    }

    /**
     * Iterates through the results and creates a string with the results
     * from each roll.
     *
     * @return Returns the results for each roll in printable form.
     */
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("[");

        for (int i = 0; i < results.length; i++)
        {
            sb.append(results[i]);

            if (i != results.length-1)
            {
                sb.append(", ");
            }
        }

        sb.append("]");
        return sb.toString();
    }
}
