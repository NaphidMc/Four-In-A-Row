package nm;

public class BoardState
{

   public int boardState[][] = new int[6][7];
   
   public BoardState()
   {
      boardState = new int[][] {{0, 0, 0, 0, 0, 0, 0},
                                {0, 0, 0, 0, 0, 0, 0},
                                {0, 0, 0, 0, 0, 0, 0},
                                {0, 0, 0, 0, 0, 0, 0},
                                {0, 0, 0, 0, 0, 0, 0},
                                {0, 0, 0, 0, 0, 0, 0}};
   }
   
   public BoardState(int[][] startingState)
   {
      boardState = startingState;
   }
   
   /**
    * Determines if there is a winner and returns who it is
    * @return 0 For no winner
    *         1 For player 1 wins
              2 For player 2 wins*/
   public int winnerExists() 
   {
      return winnerExists(boardState);
   }
   
   public boolean boardFull()
   {
      for(int i = 0; i < boardState.length; i++)
      {
         for(int k = 0; k < boardState[i].length; k++)
            if(boardState[i][k] == 0)
               return false;
      }
      return true;
   }
   
   /**
    * Returns the coordinates of the 4 winning pieces
    */
   public int[][] getWinningPieces()
   {
      for(int i = 0; i < boardState.length; i++)
      {
         for(int k = 0; k < boardState[i].length; k++)
         {
            int firstNum = boardState[i][k];
            
            if(firstNum == 0)
               continue;
            
            // Check for matches to the right
            if(k + 3 < boardState[i].length) // Ensures that 3 slots exist to the right of this slot
            {
               if(boardState[i][k + 1] == firstNum && boardState[i][k + 2] == firstNum && boardState[i][k + 3] == firstNum)
               {
                  // Returns the coordinates
                  return new int[][] {{i, k},
                                      {i, k + 1},
                                      {i, k + 2},
                                      {i, k + 3}};
               }
            }
            
            // Check for matches below
            if(i + 3 < boardState.length) // Ensures that 3 slots exist below this slot
            {
               if(boardState[i + 1][k] == firstNum && boardState[i + 2][k] == firstNum && boardState[i + 3][k] == firstNum)
               {
                  // Returns the coordinates
                  return new int[][] {{i, k},
                                      {i + 1, k},
                                      {i + 2, k},
                                      {i + 3, k}};
               }
            }
            
            // Check for matches down and to the right
            if(i + 3 < boardState.length && k + 3 < boardState[i].length) // Ensures that there are 3 diagonals below and to the right
            {
               if(boardState[i + 1][k + 1] == firstNum && boardState[i + 2][k + 2] == firstNum && boardState[i + 3][k + 3] == firstNum)
               {
                  // Returns the coordinates
                  return new int[][] {{i, k},
                                      {i + 1, k + 1},
                                      {i + 2, k + 2},
                                      {i + 3, k + 3}};
               }
            }
            
            // Check for matches down and to the left
            if(i + 3 < boardState.length && k - 3 >= 0) // Ensures that there are 3 diagonals below and to the left
            {
               if(boardState[i + 1][k - 1] == firstNum && boardState[i + 2][k - 2] == firstNum && boardState[i + 3][k - 3] == firstNum)
               {
                  // Returns the coordinates
                  return new int[][] {{i, k},
                                      {i + 1, k - 1},
                                      {i + 2, k - 2},
                                      {i + 3, k - 3}};
               }
            }
         }
      }
      return null;
   }
   
   public int winnerExists(int[][] tempState)
   {
      for(int i = 0; i < tempState.length; i++)
      {
         for(int k = 0; k < tempState[i].length; k++)
         {
            int firstNum = tempState[i][k];
            
            if(firstNum == 0)
               continue;
            
            // Check for matches to the right
            if(k + 3 < tempState[i].length) // Ensures that 3 slots exist to the right of this slot
            {
               if(tempState[i][k + 1] == firstNum && tempState[i][k + 2] == firstNum && tempState[i][k + 3] == firstNum)
                  return firstNum;
            }
            
            // Check for matches below
            if(i + 3 < tempState.length) // Ensures that 3 slots exist below this slot
            {
               if(tempState[i + 1][k] == firstNum && tempState[i + 2][k] == firstNum && tempState[i + 3][k] == firstNum)
                  return firstNum;
            }
            
            // Check for matches down and to the right
            if(i + 3 < tempState.length && k + 3 < tempState[i].length) // Ensures that there are 3 diagonals below and to the right
            {
               if(tempState[i + 1][k + 1] == firstNum && tempState[i + 2][k + 2] == firstNum && tempState[i + 3][k + 3] == firstNum)
                  return firstNum;
            }
            
            // Check for matches down and to the left
            if(i + 3 < tempState.length && k - 3 >= 0) // Ensures that there are 3 diagonals below and to the left
            {
               if(tempState[i + 1][k - 1] == firstNum && tempState[i + 2][k - 2] == firstNum && tempState[i + 3][k - 3] == firstNum)
                  return firstNum;
            }
         }
      }
      return 0;
   }
   
   /**
    * Gets the lowest available slot in the column
    * @param column
    * @return
    */
   public int[] lowestAvailableSlot(int column)
   {
      for(int i = boardState.length - 1; i >= 0; i--)
      {
         if(boardState[i][column] == 0)
            return new int[] {i, column};
      }
      return new int[] {-1, -1};
   }
   
   public int[] lowestAvailableSlot(int column, int[][] tempState)
   {
      for(int i = tempState.length - 1; i >= 0; i--)
      {
         if(tempState[i][column] == 0)
            return new int[] {i, column};
      }
      return new int[] {-1, -1};
   }
}
