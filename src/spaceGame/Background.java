package spaceGame;

@SuppressWarnings("serial")
class Background extends GameEntities {
	public int size;
	
	Background() {
		this.x=randNum.nextInt(0,WINB-this.size+1);
		this.y=0;
		this.size=randNum.nextInt(1,5+1);
		this.spd=randNum.nextInt(1,4+1);
	}
}