package entities;

import java.awt.Color;

import hsa2.GraphicsConsole;
import main.GeneralUtil;
import main.SpaceGame;

@SuppressWarnings("serial")
public class ProjectileBase extends EntityBase {
	
	/** Solely used for enemies**/
	public boolean aiming;
	
	public ProjectileBase(int x, int y, int width, int height, GraphicsConsole gc) {
		super(x, y, width, height, gc);
	}
	
	public static int getDamage() {
		return 1;
	}
	
	
	@Override
    public void update() {
		if(aiming) {
			double angle = findRotation(x,y, SpaceGame.getPlayer().x, SpaceGame.getPlayer().y);
			
			vX = (int)(maxSpeed * Math.cos(Math.toRadians(angle)) + SpaceGame.getPlayer().x);
			vY = (int)(maxSpeed * Math.sin(Math.toRadians(angle)) + SpaceGame.getPlayer().y);
		} else {
			vY += 10 * maxSpeed;
		}
		super.update();
    }
    
    
	@Override
    public void onCollision(EntityBase e) {
    	e.health -= getDamage();
    	onDead();
    }
	
	public double findRotation(int x1, int y1, int x2, int y2) {
		return Math.toDegrees(Math.atan2(y2-y1, x2-x1));
	}
	
	@Override
	public void draw() {
		gc.setColor(Color.WHITE);
		GeneralUtil.rectFromRectangle(this, gc);
	}
	
	public ProjectileBase playerProjectile = new ProjectileBase(x, y, width, height, gc){

		public static int getDamage() {
			return 1;
		}
		
	};
	
}
