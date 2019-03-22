package nm;
import org.newdawn.slick.*;

import javafx.scene.input.KeyCode;

public class Game extends BasicGame
{
   // INPUT
   int mouseX, mouseY;
   
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
   int whoseTurn, winner = 0, aiPlayer = 2, turn;
   enum GameState {
      MAIN_MENU,
      ANIMATION,
      GAME_OVER,
      PLAYER_TURN
   }
   GameState gameState;
   
   // Graphics positioning variables
   int boardPositionX = 159, boardPositionY = 23, boardWidth = 840, boardHeight = 720,
       pieceDistanceFromEdge = 25, distanceBetweenPieces = 115;
   
   // Images
   Image redPieceSprite, bluePieceSprite;
   
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
      
      TrueTypeFont f = new TrueTypeFont(new java.awt.Font("arial", java.awt.Font.BOLD, 18), true);
      
      // Draws the board
      // TODO Use real graphics & adjust the scale
      g.setColor(new Color(139, 173, 196));
      g.fillRect(0, 0, 1024, 768);
      g.setColor(new Color(61, 124, 63));
      g.fillRect(boardPositionX, boardPositionY, boardWidth, boardHeight);
      g.setColor(new Color(139, 173, 196));
      for(int i = 0; i < board.boardState.length; i++) {
         for(int k = 0; k < board.boardState[i].length; k++) {
            int positionX = boardPositionX + pieceDistanceFromEdge + distanceBetweenPieces * k, 
                positionY = boardPositionY + pieceDistanceFromEdge + distanceBetweenPieces * i;
            
            if(board.boardState[i][k] == 2)
               g.drawImage(bluePieceSprite, positionX, positionY);
            else if(board.boardState[i][k] == 1)
               g.drawImage(redPieceSprite, positionX, positionY);
            else
               g.fillOval(positionX, positionY, 105, 105);
         }
      }
      
      if(gameState == GameState.PLAYER_TURN)
      {
         // Draws currently selected slot
         if(locationSelected[0] != -1)
         {
            if(whoseTurn == 1)
               g.drawImage(redPieceSprite, boardPositionX + pieceDistanceFromEdge + distanceBetweenPieces * locationSelected[1], 
                                           boardPositionY + pieceDistanceFromEdge + distanceBetweenPieces * locationSelected[0]);
            else if(whoseTurn == 2)
               g.drawImage(bluePieceSprite, boardPositionX + pieceDistanceFromEdge + distanceBetweenPieces * locationSelected[1], 
                                              boardPositionY + pieceDistanceFromEdge + distanceBetweenPieces * locationSelected[0]);
         }
         
         f.drawString(25, 50, mouseX + ", " + mouseY); // Writes mouse coordinates
         f.drawString(25, 75, "It is player " + whoseTurn + "'s turn");
      } 
      else if(gameState == GameState.GAME_OVER) // Game is over, display who won
      {
         g.setColor(new Color(255, 255, 255, 100));
         g.fillRect(0, 0, 1024, 768);
         g.setColor(Color.black);
         if(winner != -1)
            f.drawString(450, 350, "PLAYER " + winner + " WINS");
         else
            f.drawString(450, 350, "It's a tie");
      }
   }

   @Override
   public void init(GameContainer gc) throws SlickException
   {
      redPieceSprite = new Image("resources/red_piece.png");
      bluePieceSprite = new Image("resources/blue_piece.png");
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
      
      if(gameState == GameState.PLAYER_TURN)
      {
         // Gets the slot that is selected
         int column = (mouseX - 159) / 120;
         if(column >= 0 && column <= 6)
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
