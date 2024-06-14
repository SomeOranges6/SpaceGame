package entities;

import java.awt.Rectangle;
import java.util.Random;

import hsa2.GraphicsConsole;
import main.SpaceGame;

/**Base class which every entity extends from **/
@SuppressWarnings("serial")
public abstract class EntityBase extends Rectangle {
	
	GraphicsConsole gc;
	
	SpaceGame game;
	
	static Random rand = new Random();
	
    public int vX, vY;
    
    public int health = 1;
    
    public int width,height;
    
    public int rotation;
    
    public int maxSpeed;
    
	//Whether entities should bounce off the edges
    public boolean bounce = true;

    public EntityBase(int x, int y, int width, int height, SpaceGame game) {
		super(x,y,width,height);
		this.gc = game.gcGame;
		this.game = game;
	}
    
	/**ticks every milisecond, handles moving and checking for death**/
    public void update() {
    	move();
        if(health <= 0)
            onDead();
    }
	
	/**If the entity dies, remove it  **/
    public void onDead() {
        game.removeEntity(this);
    }
    
	/**Handles the rendering of the entities**/
    public abstract void draw()
    
	//**Basic movement method **/
    public void move() {
    	
	   if(x + vX >= gc.getWidth() - width || x + vX <= 0) {
	 	  // vX *= bounce ? -1 : 0;
	   }
	    
	   if(y + vY >= gc.getHeight() - height || y + vY <= 0) {
	 	  // vY *= bounce ? -1 : 0;
	   }
 	  
 	   x += vX;
 	   y += vY;
    }
    
	/**Handles collision with a projectile or entity**/
    public abstract void onCollision(EntityBase e)
    
    
    
    
    
    
    
}
