package fall2018.csc2017.game_centre;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class ScoreboardTest {
    private Scoreboard scoreboard;

    /**
     * Clears the save file and adds scores to scoreboard for testing purposes
     */
    private void addScores() {
        scoreboard = new Scoreboard("Test");

        scoreboard.addScore("Test1", "2048");
        scoreboard.addScore("Test1", "10");
        scoreboard.addScore("Test1", "2018");
        scoreboard.addScore("Test2", "100");
        scoreboard.addScore("Test2", "200");

    }

    /**
     * Tests whether the list can be sorted in ascending order
     */
    @Test
    public void testSortAscend() {
        addScores();
        scoreboard.sortAscending();
        List<Score> expected = new ArrayList<>();
        expected.add(new Score("Test1", "10"));
        expected.add(new Score("Test2", "100"));
        expected.add(new Score("Test2", "200"));
        expected.add(new Score("Test1", "2018"));
        expected.add(new Score("Test1", "2048"));

        assertEquals(expected, scoreboard.getScores());
    }

    /**
     * Tests whether the list can be sorted in descending order
     */
    @Test
    public void testSortDescend() {
        addScores();
        scoreboard.sortDescending();
        List<Score> expected = new ArrayList<>();
        expected.add(new Score("Test1", "2048"));
        expected.add(new Score("Test1", "2018"));
        expected.add(new Score("Test2", "200"));
        expected.add(new Score("Test2", "100"));
        expected.add(new Score("Test1", "10"));
        expected.sort(Collections.<Score>reverseOrder());

        assertEquals(expected, scoreboard.getScores());
    }

    /**
     * Tests whether an individual player's scores can be listed
     */
    @Test
    public void testGetPlayerScores() {
        addScores();
        scoreboard.sortAscending();

        List<Score> expected = new ArrayList<>();
        expected.add(new Score("Test1", "10"));
        expected.add(new Score("Test1", "2018"));
        expected.add(new Score("Test1", "2048"));

        assertEquals(expected, scoreboard.getScores("Test1"));
    }

}