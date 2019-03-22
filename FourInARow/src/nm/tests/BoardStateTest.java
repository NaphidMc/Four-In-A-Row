package nm.tests;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import nm.BoardState;
import nm.FourInARowAI;

public class BoardStateTest
{

   @Test
   public void winnerCheckerTests() {
      // Empty board (No Winner)
      BoardState state = new BoardState(new int[][] {{0, 0, 0, 0, 0, 0, 0},
                                                     {0, 0, 0, 0, 0, 0, 0},
                                                     {0, 0, 0, 0, 0, 0, 0},
                                                     {0, 0, 0, 0, 0, 0, 0},
                                                     {0, 0, 0, 0, 0, 0, 0,},
                                                     {0, 0, 0, 0, 0, 0, 0}});
      assertEquals(0, state.winnerExists());
      
      // Winner horizontal 1's
      state = new BoardState(new int[][] {{0, 0, 0, 0, 0, 0, 0},
                                          {0, 0, 0, 0, 0, 0, 0},
                                          {0, 0, 0, 0, 0, 0, 0},
                                          {0, 0, 0, 0, 0, 0, 0},
                                          {0, 0, 0, 2, 0, 0, 0,},
                                          {1, 1, 1, 1, 2, 2, 2}});
      assertEquals(1, state.winnerExists());
      
      // Winner horizontal 2's
      state = new BoardState(new int[][] {{0, 0, 0, 0, 0, 0, 0},
                                          {0, 0, 0, 0, 0, 0, 0},
                                          {0, 0, 0, 0, 0, 0, 0},
                                          {0, 0, 0, 0, 0, 0, 0},
                                          {0, 0, 0, 1, 0, 0, 0,},
                                          {2, 2, 2, 2, 1, 1, 1}});
      assertEquals(2, state.winnerExists());
      
      // Diagonal 1's
      state = new BoardState(new int[][] {{0, 0, 0, 0, 0, 0, 0},
                                          {0, 0, 0, 0, 0, 0, 0},
                                          {1, 0, 0, 0, 0, 0, 0},
                                          {2, 1, 0, 0, 0, 0, 0},
                                          {1, 2, 1, 1, 0, 0, 0,},
                                          {2, 2, 2, 1, 2, 1, 2}});
      assertEquals(1, state.winnerExists());
      
      // Diagonal 1's
      state = new BoardState(new int[][] {{0, 0, 0, 0, 0, 0, 0},
                                          {0, 0, 0, 0, 1, 0, 0},
                                          {1, 0, 0, 1, 1, 0, 0},
                                          {2, 2, 1, 2, 2, 0, 0},
                                          {1, 1, 2, 1, 1, 2, 0,},
                                          {2, 2, 2, 1, 2, 1, 2}});
      assertEquals(1, state.winnerExists());

      // Down 1's
      state = new BoardState(new int[][] {{0, 0, 0, 0, 0, 0, 0},
                                          {0, 0, 0, 0, 0, 0, 0},
                                          {0, 0, 1, 0, 0, 0, 0},
                                          {0, 2, 1, 0, 0, 0, 0},
                                          {2, 2, 1, 0, 0, 0, 0,},
                                          {2, 2, 1, 0, 0, 0, 0}});
      assertEquals(1, state.winnerExists());
      
      // Down 1's
      state = new BoardState(new int[][] {{0, 0, 0, 0, 0, 0, 0},
                                          {0, 0, 0, 0, 0, 0, 0},
                                          {0, 0, 0, 0, 2, 0, 0},
                                          {0, 1, 1, 0, 2, 0, 0},
                                          {2, 2, 1, 0, 2, 0, 0,},
                                          {2, 1, 1, 0, 2, 0, 0}});
      assertEquals(2, state.winnerExists());
   }
   
   @Test
   public void lowestAvailableSlotTests() 
   {
      // Empty board first column
      BoardState state = new BoardState(new int[][] {{0, 0, 0, 0, 0, 0, 0},
                                                     {0, 0, 0, 0, 0, 0, 0},
                                                     {0, 0, 0, 0, 0, 0, 0},
                                                     {0, 0, 0, 0, 0, 0, 0},
                                                     {0, 0, 0, 0, 0, 0, 0,},
                                                     {0, 0, 0, 0, 0, 0, 0}});
      assertArrayEquals(new int[] {5, 0}, state.lowestAvailableSlot(0));
      
      // Empty board last column
      state = new BoardState(new int[][] {{0, 0, 0, 0, 0, 0, 0},
                                          {0, 0, 0, 0, 0, 0, 0},
                                          {0, 0, 0, 0, 0, 0, 0},
                                          {0, 0, 0, 0, 0, 0, 0},
                                          {0, 0, 0, 0, 0, 0, 0,},
                                          {0, 0, 0, 0, 0, 0, 0}});
      assertArrayEquals(new int[] {5, 6}, state.lowestAvailableSlot(6));
      
      // Used board middle column
      state = new BoardState(new int[][] {{0, 0, 0, 0, 0, 0, 0},
                                          {0, 0, 0, 0, 0, 0, 0},
                                          {0, 0, 0, 0, 0, 0, 0},
                                          {0, 0, 0, 2, 0, 0, 0},
                                          {0, 0, 0, 1, 0, 0, 0,},
                                          {0, 0, 0, 2, 0, 0, 0}});
      assertArrayEquals(new int[] {2, 3}, state.lowestAvailableSlot(3));
      
      // Full board
      state = new BoardState(new int[][] {{1, 2, 1, 1, 1, 1, 1},
                                          {2, 2, 1, 1, 1, 1, 1},
                                          {1, 2, 1, 1, 1, 1, 1},
                                          {2, 2, 1, 2, 1, 1, 1},
                                          {1, 2, 2, 1, 1, 1, 1,},
                                          {2, 2, 1, 2, 1, 1, 1}});
      assertArrayEquals(new int[] {-1, -1}, state.lowestAvailableSlot(3));
   }
   
   @Test
   public void boardScoreTests() 
   {
      BoardState state = new BoardState(new int[][] {{0, 0, 0, 0, 0, 0, 0},
                                                     {0, 0, 0, 0, 0, 0, 0},
                                                     {0, 0, 0, 0, 0, 0, 0},
                                                     {1, 0, 0, 0, 0, 0, 0},
                                                     {1, 0, 0, 0, 0, 0, 0,},
                                                     {1, 2, 0, 0, 0, 0, 0}});
      
      FourInARowAI ai = new FourInARowAI(state);
      assertEquals(-100, ai.boardScore(state.boardState, 2));
   }
   
   @Test
   public void aiTest()
   {
      BoardState state = new BoardState(new int[][] {{0, 0, 0, 0, 0, 0, 0},
         {0, 0, 0, 0, 0, 0, 0},
         {0, 0, 0, 0, 0, 0, 0},
         {1, 0, 0, 0, 0, 0, 0},
         {1, 0, 0, 0, 0, 0, 0,},
         {1, 0, 0, 0, 0, 0, 0}});
      FourInARowAI ai = new FourInARowAI(state);
      // int[] move = ai.makeMove(0, 2, 1);
      // System.out.println("1: " + Arrays.toString(move));
      // assertArrayEquals(new int[]{2, 0}, move);

      state = new BoardState(new int[][] {{0, 0, 0, 0, 0, 0, 0},
         {0, 0, 0, 0, 0, 0, 0},
         {0, 0, 0, 0, 0, 0, 0},
         {0, 0, 0, 0, 0, 0, 0},
         {0, 0, 0, 0, 0, 0, 0,},
         {0, 0, 1, 1, 0, 0, 0}});
      ai = new FourInARowAI(state);
      // move = ai.makeMove(0, 2, 1);
      // System.out.println("2: " + Arrays.toString(move));
      // assertArrayEquals(new int[]{5, 4}, move);

      state = new BoardState(new int[][] {{0, 0, 0, 0, 0, 0, 0},
                                          {0, 0, 0, 0, 0, 0, 0},
                                          {0, 0, 0, 0, 0, 0, 0},
                                          {0, 0, 0, 0, 0, 0, 0},
                                          {0, 0, 0, 2, 2, 0, 0,},
                                          {0, 1, 1, 1, 2, 0, 0}});
      ai = new FourInARowAI(state);
      int[] move = ai.makeMove(0, 2, 1);
      System.out.println("3: " + Arrays.toString(move));
      assertArrayEquals(new int[]{5, 0}, move);
   }

}
