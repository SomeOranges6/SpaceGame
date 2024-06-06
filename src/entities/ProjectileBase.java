package entities;

import java.awt.Color;

import hsa2.GraphicsConsole;

@SuppressWarnings("serial")
public class ProjectileBase extends EntityBase {
	
	public ProjectileBase(int x, int y, int width, int height, GraphicsConsole gc) {
		super(x, y, width, height, gc);
	}


	public int damage;
	
    
	@Override
    public void update() {
    	vY -= 10;
    }
    
    
	@Override
    public void onCollision(EntityBase e) {
    	e.health -= damage;
    	onDead();
    }
	
	@Override
	public void draw() {
		gc.setColor(Color.WHITE);
		gc.drawRect
	}
	
}
