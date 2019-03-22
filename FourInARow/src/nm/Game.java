package nm;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;

import javafx.scene.input.KeyCode;

public class Game extends BasicGame
{
   // GAME STATE
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
                     PIECE_DISTANCE_FROM_EDGE = 25, DISTANCE_BETWEEN_PIECES = 115, BUTTON_HOVER_OFFSETX = 50, 
                     BUTTON_HOVER_OFFSETY = 30;
   private final float BUTTON_HOVER_SCALE = 1.1f;
   
   
   // Game state integers
   private int whoseTurn, winner = 0, aiPlayer = 2, turn, mouseX, mouseY;
   
   // Images
   private Image iRedPiece, iBluePiece, iRestartButton;
   
   // UI Rectangles
   private Rectangle bRestart;
   
   TrueTypeFont font;
   
   private boolean hoverRestart;
   
   int[] locationSelected = {-1, -1};
   
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
      g.setColor(new Color(139, 173, 196));
      g.fillRect(0, 0, 1024, 768);
      // Board rectangle
      g.setColor(new Color(61, 124, 63));
      g.fillRect(BOARD_POSITION_X, BOARD_POSITION_Y, BOARD_WIDTH, BOARD_HEIGHT);
      g.setColor(new Color(139, 173, 196)); 
      // Draw individual pieces
      for(int i = 0; i < board.boardState.length; i++) {
         for(int k = 0; k < board.boardState[i].length; k++) {
            int positionX = BOARD_POSITION_X + PIECE_DISTANCE_FROM_EDGE + DISTANCE_BETWEEN_PIECES * k, 
                positionY = BOARD_POSITION_Y + PIECE_DISTANCE_FROM_EDGE + DISTANCE_BETWEEN_PIECES * i;
            
            if(board.boardState[i][k] == 2)
               g.drawImage(iBluePiece, positionX, positionY);
            else if(board.boardState[i][k] == 1)
               g.drawImage(iRedPiece, positionX, positionY);
            else
               g.fillOval(positionX, positionY, 105, 105);
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
         if(locationSelected[0] != -1)
         {
            if(whoseTurn == 1)
               g.drawImage(iRedPiece, BOARD_POSITION_X + PIECE_DISTANCE_FROM_EDGE + DISTANCE_BETWEEN_PIECES * locationSelected[1], 
                                           BOARD_POSITION_Y + PIECE_DISTANCE_FROM_EDGE + DISTANCE_BETWEEN_PIECES * locationSelected[0]);
            else if(whoseTurn == 2)
               g.drawImage(iBluePiece, BOARD_POSITION_X + PIECE_DISTANCE_FROM_EDGE + DISTANCE_BETWEEN_PIECES * locationSelected[1], 
                                              BOARD_POSITION_Y + PIECE_DISTANCE_FROM_EDGE + DISTANCE_BETWEEN_PIECES * locationSelected[0]);
         }
         
         font.drawString(25, 100, mouseX + ", " + mouseY + " " + (mouseX - BOARD_POSITION_X) / 120); // Writes mouse coordinates
         // f.drawString(25, 75, "It is player " + whoseTurn + "'s turn");
      } 
      else if(gameState == GameState.GAME_OVER) // Game is over, display who won
      {
         g.setColor(new Color(255, 255, 255, 100));
         g.fillRect(0, 0, 1024, 768);
         g.setColor(Color.black);
         if(winner != -1)
            font.drawString(450, 350, "PLAYER " + winner + " WINS");
         else
            font.drawString(450, 350, "It's a tie");
      }
   }

   @Override
   public void init(GameContainer gc) throws SlickException
   {
      // Initialize Images
      iRedPiece = new Image("resources/red_piece.png");
      iBluePiece = new Image("resources/blue_piece.png");
      iRestartButton = new Image("resources/button_restart.png");
      
      // Create rectangles for all buttons
      bRestart = new Rectangle(10, 30, 192, 93);
      
      font = new TrueTypeFont(new java.awt.Font("arial", java.awt.Font.BOLD, 18), true);
      
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

   
   public void checkWinner() 
   {
      winner = board.winnerExists();
      if(winner == 0 && board.boardFull())
         winner = -1;
      
      if(winner != 0)
      {
         gameState = GameState.GAME_OVER;
      }
   }
   
   public void startGame(GameMode mode)
   {
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
