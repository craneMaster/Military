package utils;

import utils.Cell.cellType;

public class Board {
	Cell[][] board;
	public static final int size = 40;
	
	
	
	public Board() {
		board = new Cell[size][size];
		double p;
		for(int i=0;i<size;i++) {
			for(int j=0;j<size;j++) {
				p=Math.random();
				if(p<0.75)board[i][j]=new Cell(cellType.Land);
				else if(p<0.78)board[i][j]=new Cell(cellType.Castle);
				else if(p<0.87)board[i][j]=new Cell(cellType.Mountain);
				else if(p<0.90)board[i][j]=new Cell(cellType.Swamp);
				else if(p<0.93)board[i][j]=new Cell(cellType.Lava);
				else if(p<0.96)board[i][j]=new Cell(cellType.Ice);
				else if(p<0.99)board[i][j]=new Cell(cellType.Jungle);
				else board[i][j]=new Cell(cellType.Shelter);
			}
		}
	}
	
	public boolean move(int x, int y, char dir, double part, int f) {
		if(board[x][y].getTroop()==0)return true;
		if(board[x][y].getFaction()!=f)return true;
		if(board[x][y].getType()==cellType.Ice&&board[x][y].getTroop()<1000)return true;
		int troops = board[x][y].getTroop()-1;
		if(board[x][y].getType()==cellType.Shelter)troops+=1;
		troops*=part;
		
		if(dir=='q'&&x>0&&y>0)return board[x][y].tawa(board[x-1][y-1],troops);
		if(dir=='a'&&x>0)return board[x][y].tawa(board[x-1][y], troops);
		if(dir=='z'&&x>0&&y<size-1)return board[x][y].tawa(board[x-1][y+1], troops);
		if(dir=='w'&&y>0)return board[x][y].tawa(board[x][y-1], troops);
		if(dir=='x'&&y<size-1)return board[x][y].tawa(board[x][y+1], troops);
		if(dir=='e'&&x<size-1&&y>0)return board[x][y].tawa(board[x+1][y-1],troops);
		if(dir=='d'&&x<size-1)return board[x][y].tawa(board[x+1][y],troops);
		if(dir=='c'&&x<size-1&&y<size-1)return board[x][y].tawa(board[x+1][y+1],troops);
		
		if(dir=='t'&&x>0&&y>0)return board[x][y].tawa(board[x-1][y-1],0);
		if(dir=='g'&&x>0)return board[x][y].tawa(board[x-1][y], 0);
		if(dir=='b'&&x>0&&y<size-1)return board[x][y].tawa(board[x-1][y+1], 0);
		if(dir=='y'&&y>0)return board[x][y].tawa(board[x][y-1], 0);
		if(dir=='n'&&y<size-1)return board[x][y].tawa(board[x][y+1], 0);
		if(dir=='u'&&x<size-1&&y>0)return board[x][y].tawa(board[x+1][y-1],0);
		if(dir=='j'&&x<size-1)return board[x][y].tawa(board[x+1][y],0);
		if(dir=='m'&&x<size-1&&y<size-1)return board[x][y].tawa(board[x+1][y+1],0);
		
		if(dir=='f') {
			if(board[x][y].getTroop()>=500&&board[x][y].getType()!=cellType.Castle&&board[x][y].getType()!=cellType.Queen) {
				board[x][y].addtroop(-500);
				board[x][y].setType(cellType.Castle);
			}
		}
		return false;
	}
	
	public void kill(int f1, int f2) {
		for(int i=0;i<Board.size;i++) {
			for(int j=0;j<Board.size;j++) {
				if(board[i][j].getFaction()==f2) {
					board[i][j].half();
					board[i][j].setFaction(f1);
				}
			}
		}
	}
	
	public boolean visible(int x, int y, int f) {
		if(board[x][y].getFaction()==f)return true;
		if(x>0            && y>0           )if(board[x-1][y-1].getFaction()==f)return true;
		if(                  y>0           )if(board[x  ][y-1].getFaction()==f)return true;
		if(x<Board.size-1 && y>0           )if(board[x+1][y-1].getFaction()==f)return true;
		if(x>0                             )if(board[x-1][y  ].getFaction()==f)return true;
		if(x<Board.size-1                  )if(board[x+1][y  ].getFaction()==f)return true;
		if(x>0            && y<Board.size-1)if(board[x-1][y+1].getFaction()==f)return true;
		if(                  y<Board.size-1)if(board[x  ][y+1].getFaction()==f)return true;
		if(x<Board.size-1 && y<Board.size-1)if(board[x+1][y+1].getFaction()==f)return true;
		
		return false;
	}
	
	public static void main(String[] args) {
		
	}
	
}
