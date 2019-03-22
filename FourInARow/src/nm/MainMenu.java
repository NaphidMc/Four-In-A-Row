package nm;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import nm.Game.GameMode;

public class MainMenu
{
   Game game;
   Image mainMenuBackground, iPlayAI, iPlayHotseat, iPlayOnline, iExit;
   Rectangle bPlayAI, bPlayHotseat, bPlayOnline, bExit;
   boolean hoverPlayAI, hoverPlayHotseat, hoverPlayOnline, hoverExit;
   private final float BUTTON_SCALE_ON_HOVER = 1.2f, DEFAULT_BUTTON_SCALE = 1.0f;
   private final int HOVER_OFFSETX = 150, HOVER_OFFSETY = 50;
   
   public MainMenu(Game g)
   {
      game = g;
   }
   
   public void init() throws SlickException
   {
      // Initialize menu images
      mainMenuBackground = new Image("resources/main_menu_background.png");
      iPlayAI = new Image("resources/button_play_vs_ai.png");
      iPlayHotseat = new Image("resources/button_multiplayer_hotseat.png");
      iPlayOnline = new Image("resources/button_multiplayer_online.png");
      iExit = new Image("resources/button_exit.png");
      
      // Initialize button rectangles
      int buttonWidth = 278, buttonHeight = 133;
      // Creates rectangles for the buttons to help detect if the mouse is over them
      bPlayAI = new Rectangle(30, 360, buttonWidth, buttonHeight);
      bPlayHotseat = new Rectangle(374, 360, buttonWidth, buttonHeight);
      bPlayOnline = new Rectangle(700, 360, buttonWidth, buttonHeight);
      bExit = new Rectangle(374, 559, buttonWidth, buttonHeight);
   }
   
   public void render(GameContainer gc, Graphics g)
   {
      // Draw the menu background
      g.drawImage(mainMenuBackground, 0, 0);
      
      // Draw buttons
      float scale = DEFAULT_BUTTON_SCALE;
      if(hoverPlayAI) // Buttons get bigger when the mouse is over them
         scale = BUTTON_SCALE_ON_HOVER;
      // When scaled, buttons tend to go down and to the right, HOVER_OFFSETX & Y are meant to counteract this
      iPlayAI.draw(bPlayAI.getMinX() - (scale - 1) * HOVER_OFFSETX, bPlayAI.getMinY() - (scale - 1) * HOVER_OFFSETY, scale);
      
      scale = DEFAULT_BUTTON_SCALE;
      if(hoverPlayHotseat)
         scale = BUTTON_SCALE_ON_HOVER;
      iPlayHotseat.draw(bPlayHotseat.getMinX() - (scale - 1) * HOVER_OFFSETX, bPlayHotseat.getMinY() - (scale - 1) * HOVER_OFFSETY, scale);
      
      scale = DEFAULT_BUTTON_SCALE;
      if(hoverPlayOnline)
         scale = BUTTON_SCALE_ON_HOVER;
      iPlayOnline.draw(bPlayOnline.getMinX() - (scale - 1) * HOVER_OFFSETX, bPlayOnline.getMinY() - (scale - 1) * HOVER_OFFSETY, scale);
      
      scale = DEFAULT_BUTTON_SCALE;
      if(hoverExit)
         scale = BUTTON_SCALE_ON_HOVER;
      iExit.draw(bExit.getMinX() - (scale - 1) * HOVER_OFFSETX, bExit.getMinY() - (scale - 1) * HOVER_OFFSETY, scale);
   }
   
   public void update(GameContainer gc, int deltaT)
   {
      int mouseX = gc.getInput().getMouseX(),
          mouseY = gc.getInput().getMouseY();
      
      // Resets hover variables
      hoverPlayAI = false;
      hoverPlayHotseat = false;
      hoverPlayOnline = false;
      hoverExit = false;
      
      // Check if the mouse is over individual buttons
      if(bPlayAI.contains(mouseX, mouseY))
      {
         hoverPlayAI = true;
      }
      else if(bPlayHotseat.contains(mouseX, mouseY))
      {
         hoverPlayHotseat = true;
      }
      else if(bPlayOnline.contains(mouseX, mouseY))
      {
         hoverPlayOnline = true;
      }
      else if(bExit.contains(mouseX, mouseY))
      {
         hoverExit = true;
      }
      
      // The user is clicking, check to see if they are on a button
      if(gc.getInput().isMousePressed(0))
      {
         if(hoverPlayAI)
            playVsAI();
         else if(hoverPlayHotseat)
            playHotseat();
         else if(hoverPlayOnline)
            playOnline();
         else if(hoverExit)
            exit();
      }
   }
   
   public void playVsAI()
   {
      game.startGame(GameMode.VS_AI);
   }
   
   public void playHotseat()
   {
      game.startGame(GameMode.HOTSEAT);
   }
   
   public void playOnline()
   {
      
   }
   
   public void exit()
   {
      System.exit(0);
   }
}
