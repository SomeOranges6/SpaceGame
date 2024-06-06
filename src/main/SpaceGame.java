package main;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

import entities.EntityBase;
import entities.IEnemy;
import entities.Player;
import hsa2.GraphicsConsole;

public class SpaceGame {
	
	GraphicsConsole gcGame = new GraphicsConsole(600,600, "Warp Lane");
	GraphicsConsole gcPaused = new GraphicsConsole(600,600, "Warp Lane");
	GraphicsConsole gcMenu = new GraphicsConsole(600,600, "Warp Lane");
	
	public static final int sleep = 10;
	
	Random rand = new Random();
	
	public static ArrayList<EntityBase> entities = new ArrayList<>();
	
	boolean paused;
	
	Player player;
	
	public static void main(String[] args) {
		new SpaceGame();
	}
	
	SpaceGame(){
		setup();
		while(!paused)
			gameplayLoop();
		
	}
	
	public void gameplayLoop() {
		if(gcGame.isVisible()) {
			
			for(EntityBase entity : entities) {
				entity.update();
				
				if(entity instanceof IEnemy)
					((IEnemy) entity).attack();
				
				synchronized(gcGame) {
					entity.draw();
					drawBackground();
				}
			}
			
		}
	}
	
	private void setup() {
		   gcGame.setBackgroundColor(Color.BLACK);
		   gcGame.setAntiAlias(true);
		   gcGame.setLocationRelativeTo(null);
		   gcGame.clear();
		   
		   player = new Player(0,0,10,10, gcGame);
		   entities.add(player);
	}
	
	private void startMenu() {
		Rectangle startButton = new Rectangle(100,400,400,100);
		
		gcMenu.setVisible(true);
		gcMenu.enableMouse();
		gcMenu.enableMouseMotion();
		gcMenu.setBackground(Color.BLACK);
		
		gcMenu.setColor(new Color(255, 255, 255));
		gcMenu.fillRect(startButton.x, startButton.y, startButton.width, startButton.height);
		while(true) {
			gcMenu.sleep(sleep);
			
			if(gcMenu.getMouseClick() > 0 && startButton.contains(gcMenu.getMouseX(), gcMenu.getMouseY())) {
				gcGame.setVisible(true);
				gcMenu.setVisible(false);
			}
			
		}
		
	}
	
	public void drawBackground() {
		for(int x = 10; x <= 790; x++) {
			for(int y = 10; y <= 590; y++) {
				if(rand.nextInt(2000) == 0) {
					int c = rand.nextInt(56, 256);
					int b = 246;
					gcGame.setColor(new Color(c, c, b, 60));
					gcGame.fillPolygon(GeneralUtil.fourPointStar(x,y, 2));
					
					gcGame.setColor(new Color(c - 10, c - 10, b - 10));
					gcGame.fillPolygon(GeneralUtil.fourPointStar(x, y, 1));
				}
			}
	   }
	}
	
	public void collisionCheck() {
		for(EntityBase entity : entities) {
			if(entity.cont)
		}
	}
	
}
