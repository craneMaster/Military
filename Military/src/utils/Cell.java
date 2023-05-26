package utils;

public class Cell {
	
	public enum cellType{
		Land, Queen, Castle, Mountain, Swamp, Lava, Ice, Jungle, Shelter;
	//  0.8   /      0.03     0.08     0.02   0.02  0.02  0.02    0.01
	}
	
	private cellType type;
	private int faction;	// 0 = uncaptured(Neutral)
	private int troop;
	
	public Cell(cellType t, int f, int tr) {
		type = t; faction = f; troop = tr;
	}
	
	public Cell(cellType t) {
		type = t; faction = troop = 0;
		if(t==cellType.Castle)troop = (int)(40+11*Math.random());
		//
		//troop = 50; faction = 1;
//		faction = (int)(Math.random()*1.1);
	}
	
	public Cell() {
		type = cellType.Land; faction = 0; troop = 0;
	}
	
	public void addtroop(int x) {
		troop+=x;
	}
	
	public void capturedby(int y) {
		faction = y;
	}
	
	public void half() {
		troop=troop/2+1;
	}
	
	public boolean tawa(Cell c2, int troops) {
		if(troops==0)return true;
		if(this.troop<troops)return false; // command rejected: not enough troops
		if(this.type==cellType.Ice&&troops<1000)return false; // command rejected: ice
		if(c2.type==cellType.Shelter && c2.faction!=0 && this.faction!=c2.faction)return false;		//command rejected: shelter
		if(c2.type==cellType.Mountain)return false; // command rejected: mountain
		
		this.troop-=troops;
		if(this.faction==c2.faction) { // same faction
			c2.troop+=troops;
		}
		else {	// different faction
			if(troops>c2.troop) {
				c2.troop = troops - c2.troop; c2.capturedby(this.faction);
			}
			else c2.troop -= troops;
		}
		return true;
	}
	
	public void grow() {
		if(faction==0)return;
		if(troop==0&&type!=cellType.Land) {
			faction=0;return;
		}
		if(type==cellType.Mountain||type==cellType.Shelter||type==cellType.Ice)return;
		if(type==cellType.Queen||type==cellType.Castle)troop++;
		if(type==cellType.Land) {
			if(Math.random()<0.04*(1+Math.log10(troop)/3))troop++;
		}
		if(type==cellType.Swamp)troop--;
		if(type==cellType.Lava)troop-=Math.max(troop/4,1);
		if(type==cellType.Jungle) {
			double i = 0.523*Math.random()-0.25;
			troop = (int)(troop+troop*i+0.5);
		}
		if(troop==0&&type!=cellType.Land)faction=0;
	}
	
	public static void main(String[] args) {
		
		
		
	}
	
	
	public cellType getType() {
		return type;
	}

	public void setType(cellType type) {
		this.type = type;
	}

	public int getFaction() {
		return faction;
	}

	public void setFaction(int faction) {
		this.faction = faction;
	}

	public int getTroop() {
		return troop;
	}

	public void setTroop(int troop) {
		this.troop = troop;
	}
	
}
