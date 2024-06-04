package entities;

import java.awt.Rectangle;
import java.util.Random;

@SuppressWarnings("serial")
public abstract class EntityBase extends Rectangle {
	
	public int x,y;
	
	static Random rand = new Random();
	
    public int vX, vY;
    
    public int health = 1;
    
    public int width,height;
    
    public EntityBase(int x, int y, int width, int height) {
		super(x,y,width,height);
	}
    
    public void onDead() {
    	
    }
    
    public void update() {
    	
    }
    
    
    
    
    
    
    
}
