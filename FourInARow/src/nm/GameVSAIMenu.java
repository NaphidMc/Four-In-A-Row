package nm;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.MouseListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import nm.Game.GameMode;

public class GameVSAIMenu
{
   Image menuBackground, text_player, text_computer, players_background,
         checkbox, checkMark, radialButton, playButton, slider, slider_background;
   Rectangle checkboxRect, easyRect, mediumRect, hardRect, playRect;
   Rectangle[] sliderBackgrounds, sliderControls;
   Game game;
   boolean computerGoesFirst = false, checkboxHover, easyHover, mediumHover, hardHover, mousePressed,
           playHover;
   boolean[] sliderHover, sliderPressed;
   
   int selectedDifficulty = 3;
   int[][] playerColorSettingsPositions = {{550, 80}, {550, 315}};
   float[] colors;
   
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
      slider = new Image("resources/slider.png");
      slider_background = new Image("resources/slider_background.png");
      
      checkboxRect = new Rectangle(340, 227, checkbox.getWidth(), checkbox.getHeight());
      
      easyRect = new Rectangle(550, 220, radialButton.getWidth(), radialButton.getHeight());
      mediumRect = new Rectangle(650, 220, radialButton.getWidth(), radialButton.getHeight());
      hardRect = new Rectangle(750, 220, radialButton.getWidth(), radialButton.getHeight());
      
      playRect = new Rectangle(725, 625, playButton.getWidth(), playButton.getHeight());
      
      sliderHover = new boolean[6];
      sliderPressed = new boolean[6];
      sliderBackgrounds = new Rectangle[6];
      sliderControls = new Rectangle[6];
      
      colors = new float[6];
      float[] hsvColor1 = new float[3], hsvColor2 = new float[3];
      java.awt.Color.RGBtoHSB(Game.pieceColors[1].getRed(), Game.pieceColors[1].getBlue(), Game.pieceColors[1].getGreen(), hsvColor1);
      java.awt.Color.RGBtoHSB(Game.pieceColors[2].getRed(), Game.pieceColors[2].getBlue(), Game.pieceColors[2].getGreen(), hsvColor2);
      colors[0] = hsvColor1[0];
      colors[1] = hsvColor1[1];
      colors[2] = hsvColor1[2];
      colors[3] = hsvColor2[0];
      colors[4] = hsvColor2[1];
      colors[5] = hsvColor2[2];
      
      for(int i = 0; i < sliderBackgrounds.length; i++)
      {
         int posX = 0, posY = 0, width = slider_background.getWidth(), height = slider_background.getHeight();
         posX = playerColorSettingsPositions[i / 3][0];
         posY = playerColorSettingsPositions[i / 3][1] + 30 * (i % 3);
         sliderControls[i] = new Rectangle((colors[i] / 1.0f) * 250 + posX, posY - 2, 
                                            slider.getWidth(), slider.getHeight());
         sliderBackgrounds[i] = new Rectangle(posX, posY, width, height);
         
      }
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
      
      // Draw color sliders
      g.drawString("Player Color Settings", sliderBackgrounds[0].getMinX(), sliderBackgrounds[0].getMinY() - 30);
      g.drawString("Computer Color Settings", sliderBackgrounds[3].getMinX(), sliderBackgrounds[3].getMinY() - 30);
      for(int i = 0; i < sliderBackgrounds.length; i++)
      {
         slider_background.draw(sliderBackgrounds[i].getMinX(), sliderBackgrounds[i].getMinY());
         scale = 1.0f;
         if(sliderHover[i])
            scale = 1.15f;
         c = Color.white;
         if(sliderPressed[i])
            c = Color.gray;
         slider.draw(sliderControls[i].getMinX() - (scale - 1) * 5, sliderControls[i].getMinY() - (scale - 1) * 5, scale, c);
         
         if(i == 0 || i == 3)
         {
            g.setColor(Color.black);
            g.drawString((int) (100 * colors[i]) + "", sliderBackgrounds[i].getMinX() + 265, sliderBackgrounds[i].getMinY() - 2);
            g.drawString("Hue", sliderBackgrounds[i].getMinX() + 300, sliderBackgrounds[i].getMinY() - 2);
         }
         else if(i == 1 || i == 4)
         {
            g.setColor(Color.black);
            g.drawString((int) (100 * colors[i])  + "", sliderBackgrounds[i].getMinX() + 265, sliderBackgrounds[i].getMinY() - 2);
            g.drawString("Saturation", sliderBackgrounds[i].getMinX() + 300, sliderBackgrounds[i].getMinY() - 2);
         }
         else
         {
            g.setColor(Color.black);
            g.drawString((int) (100 * colors[i]) + "", sliderBackgrounds[i].getMinX() + 265, sliderBackgrounds[i].getMinY() - 2);
            g.drawString("Value", sliderBackgrounds[i].getMinX() + 300, sliderBackgrounds[i].getMinY() - 2);
         }
      }
      g.drawRect(0, 0, .1f, .1f); // remove
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
      
      for(int i = 0; i < sliderHover.length; i++)
      {
         sliderHover[i] = sliderControls[i].contains(mouseX, mouseY);
      }
      
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
         
         int currentSlider = -1;
         for(int i = 0; i < sliderPressed.length; i++)
            if(sliderPressed[i])
            {
               currentSlider = i;
               break;
            }
         
         if(currentSlider == -1)
         {
            for(int i = 0; i < sliderPressed.length; i++)
            {
               sliderPressed[i] = sliderControls[i].contains(mouseX, mouseY);
            }
         }
      }
      else if(!gc.getInput().isMouseButtonDown(0))
      {
         for(int i = 0; i < sliderPressed.length; i++)
            sliderPressed[i] = false;
      }
      
      int currentSlider = -1;
      for(int i = 0; i < sliderPressed.length; i++)
         if(sliderPressed[i])
         {
            currentSlider = i;
            break;
         }
      
      for(int i = 0; i < sliderHover.length; i++)
      {
         if((currentSlider == -1 || currentSlider == i)
            && (sliderPressed[i] || (!sliderPressed[i] && mousePressed && sliderBackgrounds[i].contains(mouseX, mouseY))))
         {
            if(!sliderPressed[i] && currentSlider == -1)
               sliderPressed[i] = true;
            int coordinate = normalizeSliderCoordinates(mouseX - 5, sliderBackgrounds[i]);
            sliderControls[i].setX(coordinate);
            colors[i] = ((coordinate - sliderBackgrounds[i].getMinX()) / 250);
            break;
         }
      }
      
      java.awt.Color c1RGB = java.awt.Color.getHSBColor(colors[0], colors[1], colors[2]);
      java.awt.Color c2RGB = java.awt.Color.getHSBColor(colors[3], colors[4], colors[5]);
      Game.pieceColors[1] = new Color(c1RGB.getRed(), c1RGB.getGreen(), c1RGB.getBlue());
      Game.pieceColors[2] = new Color(c2RGB.getRed(), c2RGB.getGreen(), c2RGB.getBlue());
   }
   
   public int normalizeSliderCoordinates(int coordinate, Rectangle rect)
   {
      if(coordinate > rect.getMaxX() - 6)
         coordinate = (int) rect.getMaxX() - 6;
      else if(coordinate < rect.getMinX())
         coordinate = (int) rect.getMinX();
      
      return coordinate;
   }
}
