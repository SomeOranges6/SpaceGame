package spaceGame;

@SuppressWarnings("serial")
class Player extends GameEntities {
	int firerate = 500; //in ms
	public int fasterSpd, jetFuel, fuelDrain;
	public int hp, iFrames, active_iFrames; //iFrames are in ms
	public int rotation=0; //in degrees, rotation is used to normalize movement. it constantly changes.
	
	Player() {
		this.x=300-this.size/2;
		this.y=500-this.size/2;
		this.spd=3;
		this.fasterSpd=8;
		this.jetFuel=500;
		this.fuelDrain = 5;
		this.firerate = 500;
		this.hp = 3;
		this.iFrames=750; //The intention is firerate of enemies divide and player iframes multiply by 1.5 on medium, 2x on easy.
		this.active_iFrames=0;
		this.size=20;
		this.rotation=0;
	}
	
	Player(int x, int y) {
		this.x=x-this.size/2;
		this.y=y-this.size/2;
		this.spd=2;
		this.fasterSpd=5;
		this.jetFuel=500;
		this.fuelDrain = 1;
		this.firerate = 500;
		this.hp = 3;
		this.iFrames=750;
		this.active_iFrames=0;
		this.size=20;
		this.rotation=0;
	}
}
