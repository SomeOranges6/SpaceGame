package spaceGame;

@SuppressWarnings("serial")
abstract class Boss extends GameEntities {
	public int hp, iFrames;
	public int firerate; //If we get time, replace firerate for methods that do custom attacks
	public String bossTitle;
}
	
@SuppressWarnings("serial")
class Boss1 extends Boss {
	Boss1() {
		this.bossTitle="Gargantuar";
		this.hp=123;
		this.iFrames=0;
		this.size=50;
		this.firerate=1000;
		this.x=0;
		this.y=0;
		this.spd = 3;
	}
}

