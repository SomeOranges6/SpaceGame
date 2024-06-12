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
	
	//Graphics console for the game
	public final GraphicsConsole gcGame = new GraphicsConsole(600,600, "Warp Lane");
	//GraphicsConsole gcPaused = new GraphicsConsole(600,600, "Warp Lane");
	//Graphics console for the (unfinished) menu
	public final GraphicsConsole gcMenu = new GraphicsConsole(600,600, "Warp Lane");
	
	public static final int sleep = 20;
	
	Random rand = new Random();

    //Lists of entities to update, and projectiles to check for collision 
	private final ArrayList<EntityBase> entities = new ArrayList<>();
	private final  ArrayList<ProjectileBase> projectiles = new ArrayList<>();
	
	//Caches entities to be spawned in the next tick
	public  ArrayList<EntityBase> entitiesToSpawn = new ArrayList<>();
	//Caches entites to be removed
	public  ArrayList<EntityBase> entitiesToRemove = new ArrayList<>();
	
	//For a future implementation of a pause screen
	boolean paused;

	public int ticks;
	
	//The player object, everything related to the player is handled in here
	static Player player;
	
	//Stores the coordinates for stars to load
	public ArrayList<Integer[]> stars = new ArrayList<>();
	
	public static void main(String[] args) {
		new SpaceGame();
	}
	
	SpaceGame(){
		setup();
		//Checks if the game screen is visible, so the game does not run while paused
		while(gcGame.isVisible())
			gameplayLoop();
		
	}
	
	/**Handles everything that requires constant updates, such as updating entities to spawn or remove**/
	public void gameplayLoop() {
			boolean backgroundDrawn = false;
			gcGame.sleep(sleep);
			/*
			* To reduce needless complexity, I utilized a simple tick based 
			* accumulator to handle constant events, such as a constant event that happens every 60 seconds
			*/
			ticks++;
			//Resets much before the int limit in order to prevent overflow
			if(ticks >= 1028){
				ticks = 1;
			}
			
			for(EntityBase toSpawn : entitiesToSpawn){
				addEntityToList(toSpawn);
			}
			for(EntityBase toRemove : entitiesToRemove){
				deleteEntityFromList(toRemove);
			}
			//Remove the entities from the spawn cache
			//The cache exists to prevent changing a list while it is running, which results in instability
			entitiesToSpawn.clear();
			entitiesToRemove.clear();

			//Iterates through every updateable entity, to render them
			for(EntityBase entity : entities) {
				entity.update();
				
				//instanceof check to handle enemy attacks
				if(entity instanceof IEnemy)
					((IEnemy) entity).attack();
				
				/*Ideally, the background would not be handled here
				  However, two syncronized blocks resultd in instability*/
				//Renders all entities and the background
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
	
	/**Handles all the basic setup needed for the menu and game**/
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
		   gcMenu.enableMouse();
		   gcMenu.enableMouseMotion();
		   gcMenu.clear();
		   
		   startMenu();
	}
	
	/**Loads the start menu**/
	private void startMenu() {
		Rectangle startButton = new Rectangle(100,400,400,100);
		
		gcMenu.setColor(new Color(0, 255, 0));
		gcMenu.fillRect(startButton.x, startButton.y, startButton.width, startButton.height);
		
		//keep running until the player clicks the start button
		while(true) {
			gcMenu.sleep(sleep);
			
			if(gcMenu.getMouseClick() > 0 && startButton.contains(gcMenu.getMouseX(), gcMenu.getMouseY())) {
				gcGame.setVisible(true);
				gcMenu.setVisible(false);
				//Initialize the player
				entities.add(new Player(300,300,10,10, this));
				break;   
			}
			
		}
		
	}

	boolean initial = true;
	public void drawBackground() {
		//Loads the initial starscape
		if(initial) {
			for(int i = 0; i < 300 ; i++) {
				stars.add( new Integer[]{rand.nextInt(10, 590),rand.nextInt(10, 590)});
		    }
			initial = false;
		}
		
		//Iterates over every star, and moves them upward
		for(Integer[] coord : stars) {
			int c = rand.nextInt(56, 256);
			int b = 246;
			gcGame.setColor(new Color(c, c, b, 20));
			gcGame.fillPolygon(GeneralUtil.fourPointStar(coord[0],coord[1], 2));
		
			coord[1]--;
			gcGame.setColor(new Color(c - 10, c - 10, b - 10, 60));
			gcGame.fillPolygon(GeneralUtil.fourPointStar(coord[0], coord[1], 1));
		}
		//Add a star at the bottom
		stars.add(new Integer[]{rand.nextInt(10, 590),gcGame.getHeight() - 30});
		//if the star goes too far upward, remove it
		if(stars.get(0)[1] > 600)
			stars.remove(0);
		
   }
	
	/**Iterates over every entity, and checks their collision with a projectile**/
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
	
	//Method for caching an entity to spawn, this is the method that other classes should use for spawning entities
	public void spawnEntity(EntityBase e) {
		entitiesToSpawn.add(e);
	}
	//Likewise, but with removing
	public void removeEntity(EntityBase e) {
		entitiesToRemove.add(e);
	}
	
	//These two methods should stay private, no outside class should interact with them
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
