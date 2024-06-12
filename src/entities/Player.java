package entities;

import java.awt.Color;
import java.awt.event.KeyEvent;

import main.GeneralUtil;
import main.ProjectileRegistry;
import main.SpaceGame;

@SuppressWarnings("serial")
/**The player class, handles input and making projectiles**/
public class Player extends EntityBase {
	
	//Unimplemented variables for a boost button
	int boostedSpeed, fuel;
	
	public int iFrames;
	
	public boolean boostCooldown;
	
	/**How quickly can the player fires projectiles**/
	public int firerate = 50;
	
	public Player(int x, int y, int width, int height, SpaceGame game) {
		super(x, y, width, height, game);
		maxSpeed = 3;
	}
	
	/**Prevents the player from dying (for testing)**/
	public void onDead() {
    	health = 10;
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
	
    /**Spawns the player projectiles**/
    public void makeAttack() {
    	if(game.ticks % firerate == 0 && gc.getMouseClick() > 0) {
           game.spawnEntity(ProjectileRegistry.getPlayerProjectile(x, y - 20, 5, 5, game));
    	}
    }
    @Override
    public void move() {
    	vX = 0;
    	vY = 0;
    	handleInput();
    	super.move();
    }
    /**Normalization code, not made by me, credit to Rajdeep**/
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
			vX = (int) ((maxSpeed)*Math.cos(Math.toRadians(normalization)));
			vY = (int) ((maxSpeed)*Math.sin(Math.toRadians(normalization)));
		}
    }
    

}
