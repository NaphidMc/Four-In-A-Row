package nm;
import java.util.ArrayList;
import java.util.Random;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Rectangle;

public class Game extends BasicGame
{
   // GAME STATE
   Random random;
   MainMenu menu;
   BoardState board;
   FourInARowAI ai;
   enum GameMode {
         VS_AI,
         HOTSEAT,
         MULTIPLAYER
   };
   GameMode gameMode;
   enum GameState {
      MAIN_MENU,
      ANIMATION,
      GAME_OVER,
      PLAYER_TURN
   }
   GameState gameState;
   
   // Graphics positioning variables
   private final int BOARD_POSITION_X = 180, BOARD_POSITION_Y = 23, BOARD_WIDTH = 840, BOARD_HEIGHT = 720,
                     PIECE_DISTANCE_FROM_EDGE = 20, DISTANCE_BETWEEN_PIECES = 115, BUTTON_HOVER_OFFSETX = 50, 
                     BUTTON_HOVER_OFFSETY = 30;
   private final float BUTTON_HOVER_SCALE = 1.1f;
   
   
   // Game state integers
   private int whoseTurn, winner = 0, aiPlayer = 2, turn, mouseX, mouseY, greyPieceTime, greyPieceTimer, greyPieceTimerInit = 500, 
               greyPieceTimerAcceleration = 75, minGreyTimer = 150, pieceBlinkState,
               pieceAlphaValue = 800, pieceAlphaValueMax = 800;
   private float lineEndX, lineEndY;
   
   // Images
   private Image iGamepiece, iRestartButton, iBoard, iEmptySlot, iBackground, iBoardBoarder, iGreyPiece;
   
   // UI Rectangles
   private Rectangle bRestart;
   
   private Color[] pieceColors;
   
   TrueTypeFont font;
   
   private boolean hoverRestart;
   
   int[] locationSelected = {-1, -1};
   
   int[][] winningPieces; // Stores the coordinates of the winning pieces (if they exist)\
   boolean[][] greyPieces;
   
   public Game(String name) throws SlickException
   {
      super(name);
      
      menu = new MainMenu(this);
      gameState = GameState.MAIN_MENU;
   }

   @Override
   public void render(GameContainer gc, Graphics g) throws SlickException
   {
      if(gameState == GameState.MAIN_MENU)
      {
         menu.render(gc, g);
         return;
      } 
      
      // Draws the board
      // TODO Use real graphics & adjust the scale
      // Background color
      g.drawImage(iBackground, 0, 0);
      // Board rectangle
      g.drawImage(iBoardBoarder, BOARD_POSITION_X - 5, BOARD_POSITION_Y - 5);
      g.drawImage(iBoard, BOARD_POSITION_X, BOARD_POSITION_Y);
      g.setColor(new Color(139, 173, 196)); 
      // Draw individual pieces
      for(int i = 0; i < board.boardState.length; i++) {
         for(int k = 0; k < board.boardState[i].length; k++) {
            int positionX = getPieceX(k), 
                positionY = getPieceY(i);
            
            g.drawImage(iEmptySlot, positionX - 4, positionY - 3);
            
            if(!greyPieces[i][k])
            {
               if(board.boardState[i][k] != 0)
                  iGamepiece.draw(positionX, positionY, .98f, pieceColors[board.boardState[i][k]]);
               else 
                  iEmptySlot.draw(positionX, positionY, .98f);
            }
            else
            {
               iGreyPiece.draw(positionX, positionY, .98f);
            }
         }
      }
      
      // ** Draw UI ** \\
      float scale = 1.0f;
      if(hoverRestart)
         scale = BUTTON_HOVER_SCALE;
      iRestartButton.draw(bRestart.getMinX() - (scale - 1) * BUTTON_HOVER_OFFSETX, bRestart.getMinY() - (scale - 1) * BUTTON_HOVER_OFFSETY, scale);
      scale = 1.0f;
      
      // Draws a piece over the currently selected column if a player is going
      if(gameState == GameState.PLAYER_TURN)
      {
         // Draws currently selected slot
         if(locationSelected[0] != -1 && (gameMode != GameMode.VS_AI || whoseTurn != aiPlayer))
         {
            Color color = new Color(pieceColors[whoseTurn].r, pieceColors[whoseTurn].g, pieceColors[whoseTurn].b, pieceAlphaValue / (float) pieceAlphaValueMax);
            iGamepiece.draw(getPieceX(locationSelected[1]), getPieceY(locationSelected[0]), 1f, color);
         }
         
         font.drawString(25, 100, mouseX + ", " + mouseY + " " + (mouseX - BOARD_POSITION_X) / 120); // Writes mouse coordinates
         // f.drawString(25, 75, "It is player " + whoseTurn + "'s turn");
      } 
      else if(gameState == GameState.GAME_OVER) // Game is over, display who won
      {
         // Draws a line through the winning pieces
         if(winner != -1)
         {
            g.setColor(Color.white);
            g.setLineWidth(3.0f);
            int offsetX = iGamepiece.getWidth() / 2, offsetY = iGamepiece.getHeight() / 2;
            if(allPiecesGrey())
            g.drawLine(getPieceX(winningPieces[0][1]) + offsetX, getPieceY(winningPieces[0][0]) + offsetY, 
                       lineEndX, lineEndY);
         }
         
         g.setColor(Color.black);
         if(winner != -1)
            font.drawString(450, 350, "PLAYER " + winner + " WINS");
         else
            font.drawString(450, 350, "It's a tie");
      }
   }
   
   public int getPieceX(int col) 
   {
      return BOARD_POSITION_X + PIECE_DISTANCE_FROM_EDGE + DISTANCE_BETWEEN_PIECES * col;
   }
   
   public int getPieceY(int row)
   {
      return BOARD_POSITION_Y + PIECE_DISTANCE_FROM_EDGE + DISTANCE_BETWEEN_PIECES * row;
   }

   @Override
   public void init(GameContainer gc) throws SlickException
   {
      // Initialize Images
      iRestartButton = new Image("resources/button_restart.png");
      iGamepiece = new Image("resources/game_piece.png");
      iBoard = new Image("resources/board.png");
      iEmptySlot = new Image("resources/empty_slot.png");
      iBackground = new Image("resources/background.png");
      iBoardBoarder = new Image("resources/board_border.png");
      iGreyPiece = new Image("resources/game_over_piece.png");
      
      // Create rectangles for all buttons
      bRestart = new Rectangle(5, 30, 192, 93);
      
      font = new TrueTypeFont(new java.awt.Font("arial", java.awt.Font.BOLD, 18), true);
      random = new Random();
      menu.init();
   }

   @Override
   public void update(GameContainer gc, int deltaT) throws SlickException
   {
      if(gameState == GameState.MAIN_MENU)
      {
         menu.update(gc, deltaT);
         return;
      }
      
      mouseX = gc.getInput().getMouseX();
      mouseY = gc.getInput().getMouseY();
      
      // Exit game with escape
      if(gc.getInput().isKeyDown(Input.KEY_ESCAPE))
      {
         System.exit(0);
      }
      
      updateUI(gc);

      
      if(gameState == GameState.PLAYER_TURN)
      {
         // Gets the slot that is selected
         int column = (mouseX - BOARD_POSITION_X) / 120;
         if((mouseX - BOARD_POSITION_X) >= 0 && column >= 0 && column <= 6)
            locationSelected = board.lowestAvailableSlot(column);
         else 
            locationSelected = new int[] {-1, -1};
         
         if(gc.getInput().isMousePressed(0) && locationSelected[0] != -1 && (whoseTurn != aiPlayer || gameMode != GameMode.VS_AI))
         {
            board.boardState[locationSelected[0]][locationSelected[1]] = whoseTurn;
            
            checkWinner();
            turn++;
         }
         
         if(pieceBlinkState == 0)
            pieceAlphaValue -= deltaT;
         else if(pieceBlinkState == 1)
            pieceAlphaValue += deltaT;
         if(pieceAlphaValue <= 250 || pieceAlphaValue >= pieceAlphaValueMax + 150)
         {
            if(pieceBlinkState == 0)
            {
               pieceBlinkState = 1;
            }
            else 
            {
               pieceBlinkState = 0;
            }
         }
      }
      else if(gameState == GameState.GAME_OVER)
      {
         int tempEndX, tempEndY, tempBeginX, tempBeginY;
         // Grey out more pieces if they aren't all grey yet
         if(!allPiecesGrey())
         {
            if(lineEndX == -1)
               lineEndX = getPieceX(winningPieces[0][1]) + iGamepiece.getWidth() / 2;
            if(lineEndY == -1)
               lineEndY = getPieceY(winningPieces[0][0]) + iGamepiece.getHeight() / 2;
            
            greyPieceTime -= deltaT;
            if(greyPieceTime <= 0)
            {
               greyOutNewPiece();
               greyPieceTimer -= greyPieceTimerAcceleration;
               if(greyPieceTimer < minGreyTimer)
                  greyPieceTimer = minGreyTimer;
               greyPieceTime = greyPieceTimer;
            }
         }
         // Otherwise work on the line
         else if(lineEndX != getPieceX(winningPieces[3][1]) + iGamepiece.getWidth() / 2  
                 || lineEndY != getPieceY(winningPieces[3][0]) + iGamepiece.getHeight() / 2)
         {
            tempEndX = getPieceX(winningPieces[3][1]);
            tempEndY = getPieceY(winningPieces[3][0]);
            tempBeginX = getPieceX(winningPieces[0][1]);
            tempBeginY = getPieceY(winningPieces[0][0]);
            
            float scale = .35f;
            
            int xDiff = tempEndX - tempBeginX;
            if(xDiff < 0)
               lineEndX -= deltaT * scale;
            else if(xDiff > 0)
               lineEndX += deltaT * scale;
            
            int yDiff = tempEndY - tempBeginY;
            if(yDiff < 0)
               lineEndY -= deltaT * scale;
            else if(yDiff > 0)
               lineEndY += deltaT * scale;
            
            if(xDiff > 0 && lineEndX > tempEndX + iGamepiece.getWidth() / 2)
               lineEndX = tempEndX + iGamepiece.getWidth() / 2;
            else if(xDiff < 0 && lineEndX < tempEndX + iGamepiece.getWidth() / 2)
               lineEndX = tempEndX + iGamepiece.getWidth() / 2;
            
            if(yDiff > 0 && lineEndY > tempEndY + iGamepiece.getHeight() / 2)
               lineEndY = tempEndY + iGamepiece.getHeight() / 2;
            else if(yDiff < 0 && lineEndY < tempEndY + iGamepiece.getHeight() / 2)
               lineEndY = tempEndY + iGamepiece.getHeight() / 2;
         }
      }
      
      if(gameMode == GameMode.VS_AI)
      {
         if(whoseTurn == aiPlayer && winner == 0)
         {
            int[] aiPlayerMove = ai.makeMove(1, aiPlayer, 1);
            // AI player can't make any moves (It's a tie)
            if(aiPlayerMove[0] == -1) 
            {
               gameState = GameState.GAME_OVER;
               winner = -1;
            }
            else 
               board.boardState[aiPlayerMove[0]][aiPlayerMove[1]] = aiPlayer;
            checkWinner();
            turn++;
         }
      }
      
      whoseTurn = (turn % 2) + 1;
   }
   
   public void updateUI(GameContainer gc)
   {
      hoverRestart = false;
      
      if(bRestart.contains(mouseX, mouseY))
         hoverRestart = true;
   }

   public void restart()
   {
      startGame(gameMode);
   }
   
   public void mouseClicked(int button, int x, int y, int clickCount)
   {
      if(hoverRestart && button == 0)
         restart();
   }

   public void greyOutNewPiece()
   {
      ArrayList<Integer[]> possibilities = new ArrayList<>();
      for(int i = 0; i < board.boardState.length; i++)
      {
         for(int k = 0; k < board.boardState[i].length; k++)
         {
            if(!greyPieces[i][k] && !isWinningPiece(i, k) && board.boardState[i][k] != 0)
               possibilities.add(new Integer[] {i, k});
         }
      }
      
      Integer[] result = possibilities.get(random.nextInt(possibilities.size()));
      greyPieces[result[0]][result[1]] = true;
   }
   
   public boolean allPiecesGrey()
   {
      for(int i = 0; i < greyPieces.length; i++)
      {
         for(int k = 0; k < greyPieces[i].length; k++)
         {
            if(isWinningPiece(i, k) || board.boardState[i][k] == 0)
               continue;
            if(!greyPieces[i][k])
               return false;
         }
      }
      return true;
   }
   
   public boolean isWinningPiece(int row, int col)
   {
      for(int i = 0; i < winningPieces.length; i++)
      {
         if(winningPieces[i][0] == row && winningPieces[i][1] == col)
            return true;
      }
      return false;
   }
   
   public void checkWinner() 
   {
      winner = board.winnerExists();
      if(winner == 0 && board.boardFull())
         winner = -1;
      
      if(winner != 0)
      {
         if(winner != -1)
            winningPieces = board.getWinningPieces();
         gameState = GameState.GAME_OVER;
      }
   }
   
   public void startGame(GameMode mode)
   {
      lineEndX = -1;
      lineEndY = -1;
      greyPieceTimer = greyPieceTimerInit;
      greyPieceTime = greyPieceTimer;
      pieceColors = new Color[3];
      pieceColors[0] = Color.white;
      pieceColors[1] = new Color(0, 127, 93);
      pieceColors[2] = new Color(0, 93, 127);
      greyPieces = new boolean[6][7];
      turn = 0;
      board = new BoardState();
      gameMode = mode;
      whoseTurn = 1;
      gameState = GameState.PLAYER_TURN;
      if(mode == GameMode.VS_AI)
         ai = new FourInARowAI(board);
      winner = 0;
   }
}
