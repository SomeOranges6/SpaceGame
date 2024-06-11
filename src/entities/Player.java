package entities;

import java.awt.Color;
import java.awt.event.KeyEvent;

import hsa2.GraphicsConsole;
import main.GeneralUtil;

@SuppressWarnings("serial")
public class Player extends EntityBase {
	
	int boostedSpeed, fuel;
	
	public int iFrames;
	
	public boolean boostCooldown;
	
	public int firerate = 5;
	
	public Player(int x, int y, int width, int height, GraphicsConsole gc) {
		super(x, y, width, height, gc);
	}
	
	public void onDead() {
    	
    }
    
	@Override
    public void update() {
    	makeAttack();
    	super.update();
    }
    
    public void draw() {
    	gc.setColor(new Color(255,255,0));
    	GeneralUtil.rectFromRectangle(this, gc);
    }
    
    public void makeAttack() {
    	if(System.currentTimeMillis() % firerate == 0 && gc.getMouseClick() > 0) {
    		
    	}
    		
    }
    @Override
    public void move() {
    	handleInput();
    	super.move();
    }
    
    public void handleInput() {
    	int normalization = 0;
		if(gc.isKeyDown(KeyEvent.VK_W) || gc.isKeyDown(KeyEvent.VK_UP)) {
			normalization = -90;
		}
		if(gc.isKeyDown(KeyEvent.VK_A) || gc.isKeyDown(KeyEvent.VK_LEFT)) {
			normalization = 180;
		}
		if(gc.isKeyDown(KeyEvent.VK_S) || gc.isKeyDown(KeyEvent.VK_DOWN)) {
			normalization = 90;
		}
		if(gc.isKeyDown(KeyEvent.VK_D) || gc.isKeyDown(KeyEvent.VK_RIGHT)) {
			normalization = 0;
		}
		if((gc.isKeyDown(KeyEvent.VK_W) || gc.isKeyDown(KeyEvent.VK_UP)) && (gc.isKeyDown(KeyEvent.VK_A) || gc.isKeyDown(KeyEvent.VK_LEFT))) {
			normalization = -135;
		}
		if((gc.isKeyDown(KeyEvent.VK_W) || gc.isKeyDown(KeyEvent.VK_UP)) && (gc.isKeyDown(KeyEvent.VK_D) || gc.isKeyDown(KeyEvent.VK_RIGHT))) {
			normalization = -45;
		}
		if((gc.isKeyDown(KeyEvent.VK_S) || gc.isKeyDown(KeyEvent.VK_DOWN)) && (gc.isKeyDown(KeyEvent.VK_A) || gc.isKeyDown(KeyEvent.VK_LEFT))) {
			normalization = 135;
		}
		if((gc.isKeyDown(KeyEvent.VK_S) || gc.isKeyDown(KeyEvent.VK_DOWN)) && (gc.isKeyDown(KeyEvent.VK_D) || gc.isKeyDown(KeyEvent.VK_RIGHT))) {
			normalization = 45;
		}
		if(gc.isKeyDown(KeyEvent.VK_S) || gc.isKeyDown(KeyEvent.VK_A) || gc.isKeyDown(KeyEvent.VK_D) || gc.isKeyDown(KeyEvent.VK_W) || (gc.isKeyDown(KeyEvent.VK_DOWN) || gc.isKeyDown(KeyEvent.VK_LEFT) || gc.isKeyDown(KeyEvent.VK_RIGHT) || gc.isKeyDown(KeyEvent.VK_UP))) {
			vX = (int) ((maxSpeed)*Math.cos(Math.toRadians(normalization)) - (0)*Math.sin(Math.toRadians(normalization)));
			vY = (int) ((maxSpeed)*Math.sin(Math.toRadians(normalization)) + (0)*Math.cos(Math.toRadians(normalization)));
		}
    }
    

}
