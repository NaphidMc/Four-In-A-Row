package nm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class FourInARowAI
{
   enum Difficulty 
   {
      EASY, MEDIUM, HARD
   }
   Difficulty aiDifficulty;
   BoardState board;
   
   public FourInARowAI(BoardState board)
   {
      this.board = board;
   }
   
   public FourInARowAI(BoardState board, Difficulty difficulty) 
   {
      this.board = board;
      aiDifficulty = difficulty;
   }
   
   public int[] makeMove(int depth, int aiPlayer, int humanPlayer) 
   {
      Random r = new Random();
      int[] scores = new int[7];
      int[][] tempState = new int[board.boardState.length][board.boardState[0].length];
      
      for(int j = 0; j < board.boardState.length; j++)
         for(int l = 0; l < board.boardState[0].length; l++)
            tempState[j][l] = board.boardState[j][l];
      
      // TODO incorporate depth variable
      for(int j = 0; j < 1; j++)
      {
         // Assesses possible moves for the computer
         for(int i = 0; i < 7; i++)
         {  
            if(board.lowestAvailableSlot(i, tempState)[0] == -1)
               scores[i] = Integer.MIN_VALUE;
            else
            {
               int[] coordinate = {board.lowestAvailableSlot(i, tempState)[0], i};
               tempState[coordinate[0]][coordinate[1]] = aiPlayer;
               scores[i] = boardScore(tempState, aiPlayer);
               
               // Do not check player moves if the ai can win this turn
               if(board.winnerExists(tempState) == aiPlayer)
               {
                  scores[i] = Integer.MAX_VALUE;
                  break;
               }
               
               // System.out.println("****(" + coordinate[0] + " " + i + ")****");
               // Assesses possible moves in response by the player
               for(int k = 0; k < 7; k++)
               {
                  int[] playerCoordinate = {board.lowestAvailableSlot(k, tempState)[0], board.lowestAvailableSlot(k, tempState)[1]};
                  if(playerCoordinate[0] == -1)
                     continue;
                  
                  tempState[playerCoordinate[0]][playerCoordinate[1]] = humanPlayer;
                  int humanScore = boardScore(tempState, aiPlayer);
                  // System.out.println("H: (" + playerCoordinate[0] + " " + k + ") " + humanScore);
                  // for(int[] arr : tempState)
                  //   System.out.println(Arrays.toString(arr));
                  scores[i] += humanScore;
                  tempState[playerCoordinate[0]][playerCoordinate[1]] = 0;
               }
               tempState[coordinate[0]][coordinate[1]] = 0;
            }
         }
      }
      
      // Gets highest score
      int highest = scores[0];
      for(int i = 0; i < scores.length; i++)
      {
         if(scores[i] > highest)
            highest = scores[i];
      }
      
      ArrayList<Integer> idealMoves = new ArrayList<>();
      for(int i = 0; i < scores.length; i++)
      {
         if((scores[i] >= highest) && board.lowestAvailableSlot(i)[0] != -1)
            idealMoves.add(i);
      }
      
      
      
      // Selects a random move from tied high scores
      int move = 0; 
      if(idealMoves.size() > 0) 
         move = idealMoves.get(r.nextInt(idealMoves.size())); 
      else if(idealMoves.size() == 0)
         System.out.println("COMPUTER CAN'T MOVE :(");
      
      return new int[] {board.lowestAvailableSlot(move)[0], board.lowestAvailableSlot(move)[1]};
   }
   
   /**
    * Evaluates a score for the board state passed in
    * @param state
    */
   public int boardScore(int[][] state, int aiPlayer)
   {
      int score = 0;
      int[][] stateVert = new int[state.length][state[0].length];
      int[][] stateHoriz = new int[state.length][state[0].length];
      int[][] stateDiagonalRight = new int[state.length][state[0].length];
      int[][] stateDiagnonalLeft = new int[state.length][state[0].length];
      for(int i = 0; i < state.length; i++)
      {
         for(int k = 0; k < state[0].length; k++)
         {
            stateVert[i][k] = state[i][k];
            stateHoriz[i][k] = state[i][k];
            stateDiagonalRight[i][k] = state[i][k];
            stateDiagnonalLeft[i][k] = state[i][k];
         }
      }
      
      // Check vertical sequences
      for(int i = 0; i < state.length; i++)
      {
         for(int k = 0; k < state[0].length; k++)
         {
            if(state[i][k] == 0)
               continue;
            
            stateVert[i][k] = 0;
            
            // Check for two in a row below
            int tempScore = 0, player = state[i][k];
            if(i + 1 < state.length && stateVert[i + 1][k] == player)
            {
               stateVert[i + 1][k] = 0;
               tempScore = 10;
               
               // Checks for three in a row below
               if(i + 2 < state.length && stateVert[i + 2][k] == player)
               {
                  stateVert[i + 2][k] = 0;
                  tempScore = 500;
                  
                  if(i + 3 < state.length && stateVert[i + 3][k] == player)
                  {
                     stateVert[i + 3][k] = 0;
                     tempScore = 8000;
                  }
               } 
            }
            
            // If there is no way to get 4 in a row score doesn't change
            if(i - 1 < 0 || state[i - 1][k] != 0) // No space above or no match
               tempScore = 0;
            
            if(player != aiPlayer)
               tempScore *= -1;
            
            score += tempScore;
         }
      }
      
      // Check horizontal sequences
      for(int i = 0; i < state.length; i++)
      {
         for(int k = 0; k < state[0].length; k++)
         {
            if(state[i][k] == 0)
               continue;
            
            stateHoriz[i][k] = 0;
            
            // Check for two in a row to the right
            int tempScore = 0, player = state[i][k];
            if(k + 1 < state[0].length && stateHoriz[i][k + 1] == player)
            {
               stateHoriz[i][k + 1] = 0;
               tempScore = 10;
               
               // Checks for three in a row to the right
               if(k + 2 < state[0].length && stateHoriz[i][k + 2] == player)
               {
                  stateHoriz[i][k + 2] = 0;
                  tempScore = 500;
                  
                  if(k + 3 < state[0].length && stateHoriz[i][k + 3] == player)
                  {
                     stateHoriz[i][k + 3] = 0;
                     tempScore = 8000;
                  }
               }
               else if(k + 3 < state[0].length && state[i][k + 2] == 0 && k - 1 >= 0 && state[i][k - 1] == 0)
               {
                  tempScore += 500;
               }
            }
            
            // If there is no way to get 4 in a row score doesn't change
            if((k + 3 >= state[0].length || (state[i][k + 3] != 0 && state[i][k + 3] != player)) 
                  && (k - 1 < 0 || (state[i][k - 1] != 0 && state[i][k - 1] != player)))
               tempScore = 0;
               
            if(player != aiPlayer)
               tempScore *= -1;
            
            score += tempScore;
         }
      }
      
      // Check diagonal right sequences
      for(int i = 0; i < state.length; i++)
      {
         for(int k = 0; k < state[0].length; k++)
         {
            if(state[i][k] == 0)
               continue;
            
            stateDiagonalRight[i][k] = 0;
            
            // Check for two in a row to the right
            int tempScore = 0, player = state[i][k];
            if(k + 1 < state[0].length && i + 1 < state.length && stateDiagonalRight[i + 1][k + 1] == player)
            {
               stateDiagonalRight[i + 1][k + 1] = 0;
               tempScore = 10;
               
               // Checks for three in a row to the right
               if(k + 2 < state[0].length && i + 2 < state.length && stateDiagonalRight[i + 2][k + 2] == player)
               {
                  stateDiagonalRight[i + 2][k + 2] = 0;
                  tempScore = 500;
                  
                  if(k + 3 < state[0].length && i + 3 < state.length && stateDiagonalRight[i + 3][k + 3] == player)
                  {
                     stateDiagonalRight[i + 3][k + 3] = 0;
                     tempScore = 8000;
                  }
               }
            }
            
            // If there is no way to get 4 in a row score doesn't change
            if(i - 1 < 0 || k - 1 < 0 || (state[i - 1][k - 1] != 0 && state[i - 1][k - 1] != player)) // No space above or no match
               tempScore = 0;
            
            if(player != aiPlayer)
               tempScore *= -1;
            
            score += tempScore;
         }
      }
      
      // Check diagonal left sequences
      for(int i = 0; i < state.length; i++)
      {
         for(int k = 0; k < state[0].length; k++)
         {
            if(state[i][k] == 0)
               continue;
            
            stateDiagnonalLeft[i][k] = 0;
            
            // Check for two in a row to the right
            int tempScore = 0, player = state[i][k];
            if(k - 1 >= 0 && i + 1 < state.length && stateDiagnonalLeft[i + 1][k - 1] == player)
            {
               stateDiagnonalLeft[i + 1][k - 1] = 0;
               tempScore = 10;
               
               // Checks for three in a row to the right
               if(k - 2 >= 0 && i + 2 < state.length && stateDiagnonalLeft[i + 2][k - 2] == player)
               {
                  stateDiagnonalLeft[i + 2][k - 2] = 0;
                  tempScore = 500;
                  
                  if(k - 3 >= 0 && i + 3 < state.length && stateDiagnonalLeft[i + 3][k - 3] == player)
                  {
                     stateDiagnonalLeft[i + 3][k - 3] = 0;
                     tempScore = 8000;
                  }
               }
            }
            
            // If there is no way to get 4 in a row score doesn't change
            if(i - 1 < 0 || k + 1 >= state[0].length || (state[i - 1][k + 1] != 0 && state[i - 1][k + 1] != player)) // No space above or no match
               tempScore = 0;
            
            if(player != aiPlayer)
               tempScore *= -1;
             
            score += tempScore;
         }
      }
      
      return score;
   }
}
