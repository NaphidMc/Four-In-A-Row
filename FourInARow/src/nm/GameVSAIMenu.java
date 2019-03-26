package nm;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import nm.Game.GameMode;

public class GameVSAIMenu
{
   Image menuBackground, text_player, text_computer, players_background,
         checkbox, checkMark, radialButton, playButton;
   Rectangle checkboxRect, easyRect, mediumRect, hardRect, playRect;
   Game game;
   boolean computerGoesFirst = false, checkboxHover, easyHover, mediumHover, hardHover, mousePressed,
         playHover;
   int selectedDifficulty = 3;
   
   public GameVSAIMenu(Game g)
   {
      game = g;
   }
   
   public void init() throws SlickException
   {
      menuBackground = new Image("resources/menu_background.png");
      text_player = new Image("resources/text_player.png");
      text_computer = new Image("resources/text_computer.png");
      players_background = new Image("resources/players_background.png");
      checkbox = new Image("resources/checkbox.png");
      checkMark = new Image("resources/check.png");
      radialButton = new Image("resources/radial_button.png");
      playButton = new Image("resources/button_play.png");
      
      checkboxRect = new Rectangle(340, 227, checkbox.getWidth(), checkbox.getHeight());
      
      easyRect = new Rectangle(550, 220, radialButton.getWidth(), radialButton.getHeight());
      mediumRect = new Rectangle(650, 220, radialButton.getWidth(), radialButton.getHeight());
      hardRect = new Rectangle(750, 220, radialButton.getWidth(), radialButton.getHeight());
      
      playRect = new Rectangle(725, 625, playButton.getWidth(), playButton.getHeight());
   }
   
   public void render(GameContainer gc, Graphics g)
   {
      g.drawImage(menuBackground, 0, 0);
      players_background.draw(5, 10);
      Game.iGamepiece.draw(20, 25, Game.pieceColors[1]);
      Game.iGamepiece.draw(20, 140, Game.pieceColors[2]);
      g.drawImage(text_player, 130, 55);
      g.drawImage(text_computer, 130, 165);
      g.setColor(Color.black);
      
      // Draw checkbox
      g.drawString("Computer Goes First:", 155, 225);
      Color c = Color.white;
      if(mousePressed && checkboxHover)
         c = Color.gray;
      float scale = 1.0f;
      if(checkboxHover)
         scale = 1.15f;
      checkbox.draw(checkboxRect.getMinX() - (scale - 1) * 8, checkboxRect.getMinY() - (scale - 1) * 8, scale, c);
      if(computerGoesFirst)
         checkMark.draw(checkboxRect.getMinX(), checkboxRect.getMinY() - 3);
      
      // Draw difficulty settings
      g.drawString("AI Difficulty", 605, 185);
      
      g.drawString("Easy", easyRect.getMinX() - 10, easyRect.getMinY() + 18);
      g.drawString("Medium", mediumRect.getMinX() - 18, mediumRect.getMinY() + 18);
      g.drawString("Hard", hardRect.getMinX() - 10, hardRect.getMinY() + 18);
      
      scale = 1.0f;
      if(easyHover)
         scale = 1.15f;
      if(easyHover && mousePressed)
         c = Color.gray;
      else 
         c = Color.white;
      radialButton.draw(easyRect.getMinX() - (scale - 1) * 8, easyRect.getMinY() - (scale - 1) * 8, scale, c);
      
      scale = 1.0f;
      if(mediumHover)
         scale = 1.15f;
      if(mediumHover && mousePressed)
         c = Color.gray;
      else 
         c = Color.white;
      radialButton.draw(mediumRect.getMinX() - (scale - 1) * 8, mediumRect.getMinY() - (scale - 1) * 8, scale, c);
      
      scale = 1.0f;
      if(hardHover)
         scale = 1.15f;
      if(hardHover && mousePressed)
         c = Color.gray;
      else 
         c = Color.white;
      radialButton.draw(hardRect.getMinX() - (scale - 1) * 8, hardRect.getMinY() - (scale - 1) * 8, scale, c);
      
      float tempX = 0, tempY = 0;
      if(selectedDifficulty == 1)
      {
         tempX = easyRect.getMinX();
         tempY = easyRect.getMinY();
      }
      else if(selectedDifficulty == 2)
      {
         tempX = mediumRect.getMinX();
         tempY = mediumRect.getMinY();
      }
      else
      {
         tempX = hardRect.getMinX();
         tempY = hardRect.getMinY();
      }
      g.setColor(Color.black);
      g.fillOval(tempX + 3, tempY + 3, 10, 10);
      
      // Draw play button
      scale = 1.0f;
      if(playHover)
         scale = 1.1f;
      playButton.draw(playRect.getMinX() - (scale - 1) * 75, playRect.getMinY() - (scale - 1) * 75, scale);
   }
   
   public void update(GameContainer gc, int deltaT)
   {
      int mouseX = gc.getInput().getMouseX(),
          mouseY = gc.getInput().getMouseY();
      
      checkboxHover = checkboxRect.contains(mouseX, mouseY);
      easyHover = easyRect.contains(mouseX, mouseY);
      mediumHover = mediumRect.contains(mouseX, mouseY);
      hardHover = hardRect.contains(mouseX, mouseY);
      playHover = playRect.contains(mouseX, mouseY);
      
      mousePressed = gc.getInput().isMouseButtonDown(0);
      
      if(gc.getInput().isMousePressed(0))
      {
         if(checkboxHover)
            computerGoesFirst = !computerGoesFirst;
         
         if(easyHover)
            selectedDifficulty = 1;
         else if(mediumHover)
            selectedDifficulty = 2;
         else if(hardHover)
            selectedDifficulty = 3;
         
         if(playHover)
         {
            int firstPlayer = computerGoesFirst ? 2 : 1;
            game.startGame(GameMode.VS_AI, selectedDifficulty, firstPlayer);
         }
      }
   }
}
