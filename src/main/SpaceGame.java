package main;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import entities.EntityBase;
import entities.IEnemy;
import entities.Player;
import hsa2.GraphicsConsole;

public class SpaceGame {
	
	GraphicsConsole gc = new GraphicsConsole(800,600, "Warp Lane");
	Random rand = new Random();
	ArrayList<EntityBase> entities = new ArrayList<>();
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
		for(EntityBase entity : entities) {
			entity.update();
			
			if(entity instanceof IEnemy)
				((IEnemy) entity).attack();
			
			synchronized(gc) {
				entity.draw();
				drawBackground();
			}
		}
	}
	
	private void setup() {
		   gc.setBackgroundColor(Color.BLACK);
		   gc.setAntiAlias(true);
		   gc.setLocationRelativeTo(null);
		   gc.clear();
		   
		   player = new Player(0,0,10,10);
		   entities.add(player);
	}
	
	public void drawBackground() {
		for(int x = 10; x <= 790; x++) {
			for(int y = 10; y <= 590; y++) {
				if(rand.nextInt(2000) == 0) {
					int c = rand.nextInt(56, 256);
					int b = 246;
					gc.setColor(new Color(c, c, b, 60));
					gc.fillPolygon(GeneralUtil.fourPointStar(x,y, 2));
					
					gc.setColor(new Color(c - 10, c - 10, b - 10));
					gc.fillPolygon(GeneralUtil.fourPointStar(x, y, 1));
				}
			}
	   }
	}
	
}
