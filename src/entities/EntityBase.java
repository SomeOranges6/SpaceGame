package entities;

import java.awt.Rectangle;
import java.util.Random;

import hsa2.GraphicsConsole;
import main.SpaceGame;

@SuppressWarnings("serial")
public abstract class EntityBase extends Rectangle {
	
	GraphicsConsole gc;
	
	public int x,y;
	
	static Random rand = new Random();
	
    public int vX, vY;
    
    public int health = 1;
    
    public int width,height;
    
    public int rotation;
    
    public int maxSpeed;
    
    public EntityBase(int x, int y, int width, int height, GraphicsConsole gc) {
		super(x,y,width,height);
		this.gc = gc;
	}
    
    public void onDead() {
    	SpaceGame.entities.remove(this);
    }
    
    public void update() {
    	
    }
    
    public void draw() {
    	
    }
    
    public void onCollision(EntityBase e) {
    	
    }
    
    
    
    
    
    
    
}
