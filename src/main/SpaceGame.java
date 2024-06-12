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
	
	public final GraphicsConsole gcGame = new GraphicsConsole(600,600, "Warp Lane");
	//GraphicsConsole gcPaused = new GraphicsConsole(600,600, "Warp Lane");
	public final GraphicsConsole gcMenu = new GraphicsConsole(600,600, "Warp Lane");
	
	public static final int sleep = 20;
	
	Random rand = new Random();


	private final ArrayList<EntityBase> entities = new ArrayList<>();
	private final  ArrayList<ProjectileBase> projectiles = new ArrayList<>();
	//Caches entities to be spawned in the next tick
	public  ArrayList<EntityBase> entitiesToSpawn = new ArrayList<>();
	public  ArrayList<EntityBase> entitiesToRemove = new ArrayList<>();
	
	boolean paused;

	public int ticks;
	
	static Player player;
	
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
			gcGame.sleep(sleep);
			ticks++;
			if(ticks >= 1028){
				ticks = 1;
			}
			for(EntityBase toSpawn : entitiesToSpawn){
				addEntityToList(toSpawn);
			}
			for(EntityBase toRemove : entitiesToRemove){
				deleteEntityFromList(toRemove);
			}
			entitiesToSpawn.clear();
			entitiesToRemove.clear();

			for(EntityBase entity : entities) {
				entity.update();

				if(entity instanceof IEnemy)
					((IEnemy) entity).attack();

				synchronized(gcGame) {
					if(!backgroundDrawn) {
						gcGame.clear();
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
		entitiesToSpawn.add(e);
	}
	
	public void removeEntity(EntityBase e) {
		entitiesToRemove.add(e);
	}

	private void addEntityToList(EntityBase e) {
		entities.add(e);
		if(e instanceof ProjectileBase)
			projectiles.add((ProjectileBase) e);
	}

	private void deleteEntityFromList(EntityBase e) {
		entities.remove(e);
		if(e instanceof ProjectileBase)
			projectiles.remove((ProjectileBase) e);
	}
}
