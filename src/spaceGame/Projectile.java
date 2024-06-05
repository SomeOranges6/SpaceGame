package spaceGame;

@SuppressWarnings("serial")
abstract class Projectile extends GameEntities {
	double rotation = -90; //The degree angle which projectiles travel (may be changed to radians)
	double findRotation(int x1, int y1, int x2, int y2) {
		double rotation = 0;
		rotation = Math.toDegrees(Math.atan2(y2-y1, x2-x1));
		return rotation; 
	}
}

@SuppressWarnings("serial")
class Player_lineProjectile extends Projectile {
	Player_lineProjectile(int x, int y) {
		this.x=x+this.size/2;
		this.y=y+this.size/2;
		this.spd=8;
		this.size=4;
	}
}
	
@SuppressWarnings("serial")
class Enemy_lineProjectile extends Projectile {
	Enemy_lineProjectile(int x, int y) {
		this.x=x+this.size/2;
		this.y=y+this.size/2;
		this.spd=5;
		this.size=4;
		this.rotation=-90;
	}
		
	Enemy_lineProjectile(int x, int y, int size) {
		this.x=x+this.size/2;
		this.y=y+this.size/2;
		this.spd=5;
		this.size=size;
		this.rotation=-90;
	}
		
	Enemy_lineProjectile(int x, int y, double set_rotation) {
		this.x=x+this.size/2;
		this.y=y+this.size/2;
		this.spd=5;
		this.size=4;
		this.rotation=set_rotation;
	}
		
	Enemy_lineProjectile(int x, int y, int size, double set_rotation) {
		this.x=x+this.size/2;
		this.y=y+this.size/2;
		this.spd=5;
		this.size=size;
		this.rotation=set_rotation;
	}
}
	
/*class enemy_homingProjectile extends Projectile { //essentially a homing missile.
		
}*/