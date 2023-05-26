package utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.*;

import utils.Cell.cellType;

public class GameServer extends JPanel {
	
	Boolean Start;
	Server server;
	Board board;
	BufferedImage ca,mo,qu,sh,sw,la,ic,ju,cs;
	BufferedImage[] background;
	JFrame frame;
	JPanel panel;
	JLabel[][] label;
	double speed;
	String code;
	boolean[] player;
	ArrayList<Client> clients;
	int[][] cursor;
	int[][] queens;
	boolean[] death;
	public static int playercount = 1;
	int count = 0;
	
	public void encode(int f) {
		code = "";
		for(int i=0;i<Board.size;i++) {
			for(int j=0;j<Board.size;j++) {
				if(board.board[i][j].getType()==cellType.Castle)code+="1 ";
				if(board.board[i][j].getType()==cellType.Ice)code+="2 ";
				if(board.board[i][j].getType()==cellType.Jungle)code+="3 ";
				if(board.board[i][j].getType()==cellType.Land)code+="4 ";
				if(board.board[i][j].getType()==cellType.Lava)code+="5 ";
				if(board.board[i][j].getType()==cellType.Mountain)code+="6 ";
				if(board.board[i][j].getType()==cellType.Queen)code+="7 ";
				if(board.board[i][j].getType()==cellType.Shelter)code+="8 ";
				if(board.board[i][j].getType()==cellType.Swamp)code+="9 ";	
				
				code+=(board.board[i][j].getFaction()+" "+board.board[i][j].getTroop()+" ");
			}
		}
		code+=(f+" ");
		code+=(cursor[f][0]+" "+cursor[f][1]+" ");
		if(death[f])code+="2 ";
		else if(Start) code+="1 ";
		else code+="0 ";
	}
	
	public GameServer() {
//		clients.add(new Client("69.69.69.69",69)); // Leave space for clients[0]
		
		Start = false;
		board = new Board();
		frame = new JFrame();
		panel = new JPanel();
		label = new JLabel[Board.size][Board.size];
		player= new boolean[13];
		cursor= new int[13][2];
		queens= new int[13][2];
		death = new boolean[13];
		
		for(int i=0;i<=12;i++)death[i]=false;

		Font font = new Font("Dialog",Font.PLAIN,10);
		this.setLayout(null);
		for(int i=0;i<Board.size;i++) {
			for(int j=0;j<Board.size;j++) {
				label[i][j] = new JLabel("",JLabel.CENTER);
				label[i][j].setFont(font);
				label[i][j].setBounds(i*20,j*20,20,20);
				if(board.board[i][j].getType()!=cellType.Mountain)label[i][j].setText(board.board[i][j].getTroop()+"");
				label[i][j].setVisible(true);
				frame.add(label[i][j]);
				label[i][j].setAlignmentX(JLabel.CENTER);
				label[i][j].setAlignmentY(JLabel.CENTER);
			}
		}
		
		background = new BufferedImage[15];
		
		try {
			ca = ImageIO.read(new File("Castle.png"));
			mo = ImageIO.read(new File("Mountain.png"));
			qu = ImageIO.read(new File("Queen.png"));
			sh = ImageIO.read(new File("Shelter.png"));
			sw = ImageIO.read(new File("Swamp.png"));
			la = ImageIO.read(new File("Lava.png"));
			ic = ImageIO.read(new File("Ice.png"));
			ju = ImageIO.read(new File("Jungle.png"));
			cs = ImageIO.read(new File("cursor.png"));
			
			for(int i=1;i<=12;i++) {
				background[i] = ImageIO.read(new File("Player_"+i+".png"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		frame.getContentPane().add(this);
		frame.setSize(Board.size*20,Board.size*20+30);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		
		server = new Server(420);
	}
	
	protected void paintComponent(Graphics g) {
		for(int i=0;i<Board.size;i++) {
			for(int j=0;j<Board.size;j++) {
				//if(board.visible(i, j, 1))
				{
				if(board.board[i][j].getFaction()>0)g.drawImage(background[board.board[i][j].getFaction()], i*20, j*20, null);
				if(board.board[i][j].getType()==cellType.Castle)g.drawImage(ca, i*20, j*20, null);
				if(board.board[i][j].getType()==cellType.Mountain)g.drawImage(mo, i*20, j*20, null);
				if(board.board[i][j].getType()==cellType.Queen)g.drawImage(qu, i*20, j*20, null);
				if(board.board[i][j].getType()==cellType.Shelter)g.drawImage(sh, i*20, j*20, null);
				if(board.board[i][j].getType()==cellType.Swamp)g.drawImage(sw, i*20, j*20, null);
				if(board.board[i][j].getType()==cellType.Lava)g.drawImage(la, i*20, j*20, null);
				if(board.board[i][j].getType()==cellType.Ice)g.drawImage(ic, i*20, j*20, null);
				if(board.board[i][j].getType()==cellType.Jungle)g.drawImage(ju, i*20, j*20, null);
				
				label[i][j].setText(board.board[i][j].getTroop()+"");
				if(board.board[i][j].getTroop()>999)label[i][j].setText(board.board[i][j].getTroop()/100+"h");
				if(board.board[i][j].getTroop()>9999)label[i][j].setText(board.board[i][j].getTroop()/1000+"k");
				label[i][j].setVisible(true);
				}
				//else label[i][j].setVisible(false);
			}
		}
		
		for(int i=1;i<playercount;i++)g.drawImage(cs, cursor[i][0]*20, cursor[i][1]*20, null);
	}
	
	public static void main(String[] args) {
		GameServer gs = new GameServer();
		gs.frame.getContentPane().add(gs);

    	System.out.println("running");
		gs.speed = 5;
		
		Runnable helloRunnable = new Runnable() {
			
		    public void run() {
		    	
		    	for(int i=0;i<Board.size;i++) {
		    		for(int j=0;j<Board.size;j++) {
		    			gs.board.board[i][j].grow();
		    		}
		    	}
		    	
		    	for(int i=0;i<Server.cap;i++) {
		    	
		    	try {
		    		if(gs.server.in[i].available() > 0) {
//		    			System.out.println("loop");
			    		String str = gs.server.in[i].readUTF();
//			    		System.out.println(str);
			    		if(str.charAt(0)>='0'&&str.charAt(0)<='9') {
			    				gs.count++;
			    				int f = Integer.parseInt(str); // unneccesary
			    				int x,y;
			    				x=(int)(Math.random()*Board.size);
			    				y=(int)(Math.random()*Board.size);
			    				while(gs.board.board[x][y].getType()!=cellType.Land) {
			    				x=(int)(Math.random()*Board.size);
			    				y=(int)(Math.random()*Board.size);
			    				}
			    				gs.queens[i+1][0]=x; gs.queens[i+1][1]=y;
			    				gs.board.board[x][y].setType(cellType.Queen);
			    				gs.board.board[x][y].setFaction(i+1);
			    				gs.board.board[x][y].setTroop(1);
			    				gs.cursor[i+1][0]=x;
			    				gs.cursor[i+1][1]=y;
//			    				gs.server.out[i].writeUTF(""+playercount);
					    		playercount++;
			    		}
			    		else if(str!=null&&str!="") {
			    			int p = Integer.parseInt(str.substring(1));
			    			double part;
			    			if(p==0)part=1;
			    			else part = 0.1*p;
			    			boolean bool = gs.board.move(gs.cursor[i+1][0], gs.cursor[i+1][1], str.charAt(0), part, i+1);
			    			if(bool) {
			    				if(str.charAt(0)=='q'||str.charAt(0)=='w'||str.charAt(0)=='e')gs.cursor[i+1][1]--;
			    				if(str.charAt(0)=='z'||str.charAt(0)=='x'||str.charAt(0)=='c')gs.cursor[i+1][1]++;
			    				if(str.charAt(0)=='q'||str.charAt(0)=='a'||str.charAt(0)=='z')gs.cursor[i+1][0]--;
			    				if(str.charAt(0)=='e'||str.charAt(0)=='d'||str.charAt(0)=='c')gs.cursor[i+1][0]++;
			    				
			    				if(str.charAt(0)=='t'||str.charAt(0)=='y'||str.charAt(0)=='u')gs.cursor[i+1][1]--;
			    				if(str.charAt(0)=='b'||str.charAt(0)=='n'||str.charAt(0)=='m')gs.cursor[i+1][1]++;
			    				if(str.charAt(0)=='t'||str.charAt(0)=='g'||str.charAt(0)=='b')gs.cursor[i+1][0]--;
			    				if(str.charAt(0)=='u'||str.charAt(0)=='j'||str.charAt(0)=='m')gs.cursor[i+1][0]++;
			    			}
			    		}
		    		}
		    		if(gs.count==Server.cap)gs.Start=true;
			    	gs.encode(i+1);
			    	gs.server.out[i].writeUTF(gs.code);
				
	    		} catch (IOException e) {
					System.out.println("Excepted");
					e.printStackTrace();
				}
		    	
		    	if(gs.Start && gs.board.board[gs.queens[i+1][0]][gs.queens[i+1][1]].getFaction()!=i+1) {
		    		System.out.println(gs.queens[Server.cap][0]+" "+gs.queens[Server.cap][1]);
		    		gs.board.board[gs.queens[i+1][0]][gs.queens[i+1][1]].setType(cellType.Castle);
		    		gs.board.kill(gs.board.board[gs.queens[i+1][0]][gs.queens[i+1][1]].getFaction(), i+1);
		    		gs.death[i+1]=true;
		    	}
		    	}
		    	gs.frame.repaint();
		    	gs.repaint();
		    	
		    	
		    }
		  
		};
		
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(helloRunnable, 0, (int)(1000/gs.speed), TimeUnit.MILLISECONDS);
		    
//		while(true) {
//			gs.frame.repaint(); gs.repaint();
			//System.out.println("In true loop");
//		}
	}
	
}
