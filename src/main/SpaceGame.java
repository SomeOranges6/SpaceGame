package main;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

import entities.EntityBase;
import entities.IEnemy;
import entities.Player;
import entities.ProjectileBase;
import hsa2.GraphicsConsole;

public class SpaceGame {
	
	public GraphicsConsole gcGame = new GraphicsConsole(600,600, "Warp Lane");
	//GraphicsConsole gcPaused = new GraphicsConsole(600,600, "Warp Lane");
	GraphicsConsole gcMenu = new GraphicsConsole(600,600, "Warp Lane");
	
	public static final int sleep = 20;
	
	Random rand = new Random();
	
	public  ArrayList<EntityBase> entities = new ArrayList<>();
	public  ArrayList<ProjectileBase> projectiles = new ArrayList<>();
	
	boolean paused;
	
	static Player player;
	public int[][] starPos = new int [500][2];
	
	public ArrayList<Integer[]> stars = new ArrayList<>();
	
	public static void main(String[] args) {
		new SpaceGame();
	}
	
	SpaceGame(){
		setup();
		while(gcGame.isVisible())
			gameplayLoop();
		
	}
	
	public void gameplayLoop() {
			boolean backgroundDrawn = false;
			for(EntityBase entity : entities) {
					gcGame.sleep(sleep);
					entity.update();
					
					if(entity instanceof IEnemy)
						((IEnemy) entity).attack();
					
					synchronized(gcGame) {
						gcGame.clear();
						if(!backgroundDrawn) {
							drawBackground();
							backgroundDrawn = true;
						}
						entity.draw();
					}
				
			}
			
	}
	
	private void setup() {
		   
		
		   gcGame.setVisible(false);
		   gcGame.setBackgroundColor(Color.BLACK);
		   gcGame.setAntiAlias(true);
		   gcGame.setLocationRelativeTo(null);
		   gcGame.enableMouse();
		   gcGame.clear();
		   
		   gcMenu.setVisible(true);
		   gcMenu.setBackgroundColor(Color.BLACK);
		   gcMenu.setAntiAlias(true);
		   gcMenu.setLocationRelativeTo(null);
		   gcMenu.clear();
		   
		   startMenu();
	}
	
	private void startMenu() {
		Rectangle startButton = new Rectangle(100,400,400,100);
		
		gcMenu.enableMouse();
		gcMenu.enableMouseMotion();
		
		gcMenu.setColor(new Color(0, 255, 0));
		gcMenu.fillRect(startButton.x, startButton.y, startButton.width, startButton.height);
		while(true) {
			gcMenu.sleep(sleep);
			
			if(gcMenu.getMouseClick() > 0 && startButton.contains(gcMenu.getMouseX(), gcMenu.getMouseY())) {
				gcGame.setVisible(true);
				gcMenu.setVisible(false);
				player = new Player(300,300,10,10, this);
				entities.add(player);
				break;   
			}
			
		}
		
	}
	boolean initial = true;
	
	public void drawBackground() {
		if(initial) {
			for(int i = 0; i < 300 ; i++) {
				stars.add( new Integer[]{rand.nextInt(10, 590),rand.nextInt(10, 590)});
		    }
			initial = false;
		}
		
		for(Integer[] coord : stars) {
			int c = rand.nextInt(56, 256);
			int b = 246;
			gcGame.setColor(new Color(c, c, b, 20));
			gcGame.fillPolygon(GeneralUtil.fourPointStar(coord[0],coord[1], 2));
		
			coord[1]--;
			gcGame.setColor(new Color(c - 10, c - 10, b - 10, 60));
			gcGame.fillPolygon(GeneralUtil.fourPointStar(coord[0], coord[1], 1));
		}
		
		stars.add(new Integer[]{rand.nextInt(10, 590),gcGame.getHeight() - 30});
		if(stars.get(0)[1] > 600)
			stars.remove(0);
		
   }
	
	
	public void collisionCheck() {
		for(EntityBase entity : entities) {
			for(ProjectileBase projectile : projectiles) {
				if(entity.contains(projectile)) {
					projectile.onCollision(entity);
				}
			}
		}
	}
	
	public static Player getPlayer() {
		return player;
	}
	
	public void spawnEntity(EntityBase e) {
		entities.add(e);
	}
	
	public void deleteEntity(EntityBase e) {
		entities.remove(e);
	}
}
