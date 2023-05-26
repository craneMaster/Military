package utils;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import utils.Cell.cellType;

public class GameClient extends JPanel implements KeyListener{
	
	Boolean start,death;
	Board clientboard;
	String code;
	ArrayList<Integer> data;
	Client client;
	int id,faction;
	double speed;
	int[] cursor;
	JLabel[][] label;
	JLabel status;
	int partial;
	public static int idCounter;
	BufferedImage ca,mo,qu,sh,sw,la,ic,ju,cs;
	BufferedImage[] background;
	
	JFrame frame;
	
	public void decode() {
		if(code==null||code.equals(""))return;
//		System.out.println("code: "+code);
		String[] str = code.split(" ");
		for(int i=0;i<str.length;i++)data.add(Integer.parseInt(str[i]));
		int tp, fc, tr;
		for(int i=0;i<Board.size;i++) {
			for(int j=0;j<Board.size;j++) {
				tp=data.remove(0); fc=data.remove(0); tr=data.remove(0);

				if(tp==1)clientboard.board[i][j].setType(cellType.Castle);
				if(tp==2)clientboard.board[i][j].setType(cellType.Ice);
				if(tp==3)clientboard.board[i][j].setType(cellType.Jungle);
				if(tp==4)clientboard.board[i][j].setType(cellType.Land);
				if(tp==5)clientboard.board[i][j].setType(cellType.Lava);
				if(tp==6)clientboard.board[i][j].setType(cellType.Mountain);
				if(tp==7)clientboard.board[i][j].setType(cellType.Queen);
				if(tp==8)clientboard.board[i][j].setType(cellType.Shelter);
				if(tp==9)clientboard.board[i][j].setType(cellType.Swamp);

				clientboard.board[i][j].setFaction(fc);
				clientboard.board[i][j].setTroop(tr);
			}
		}
		id = data.remove(0);
		cursor[0]=data.remove(0);
		cursor[1]=data.remove(0);
		int t=data.remove(0);
		if(t>=1)start=true;
		if(t==2)death=true;
//		System.out.println(clientboard.board[2][2].getTroop());
	}
	
	protected void paintComponent(Graphics g) {
//		System.out.println("painted");
		if(!start)return; 
		status.setVisible(false);
		if(death) {
			for(int i=0;i<Board.size;i++) for(int j=0;j<Board.size;j++)label[i][j].setVisible(false);
			status.setVisible(true);
			status.setText("GAME OVER");
			return;
		}
		for(int i=0;i<Board.size;i++) {
			for(int j=0;j<Board.size;j++) {
				if(clientboard.visible(i, j, id))
				{
					//label[i][j].setAlignmentX(SwingConstants.CENTER);
					//label[i][j].setAlignmentY(SwingConstants.CENTER);
//					System.out.println("Visible");
					if(clientboard.board[i][j].getFaction()>0)g.drawImage(background[clientboard.board[i][j].getFaction()], i*20, j*20, null);
					if(clientboard.board[i][j].getType()==cellType.Castle)g.drawImage(ca, i*20, j*20, null);
					if(clientboard.board[i][j].getType()==cellType.Mountain)g.drawImage(mo, i*20, j*20, null);
					if(clientboard.board[i][j].getType()==cellType.Queen)g.drawImage(qu, i*20, j*20, null);
					if(clientboard.board[i][j].getType()==cellType.Shelter)g.drawImage(sh, i*20, j*20, null);
					if(clientboard.board[i][j].getType()==cellType.Swamp)g.drawImage(sw, i*20, j*20, null);
					if(clientboard.board[i][j].getType()==cellType.Lava)g.drawImage(la, i*20, j*20, null);
					if(clientboard.board[i][j].getType()==cellType.Ice)g.drawImage(ic, i*20, j*20, null);
					if(clientboard.board[i][j].getType()==cellType.Jungle)g.drawImage(ju, i*20, j*20, null);
					
					label[i][j].setBounds(i*20,j*20,20,20);
					label[i][j].setText(clientboard.board[i][j].getTroop()+"");
					if(clientboard.board[i][j].getTroop()>999)label[i][j].setText(clientboard.board[i][j].getTroop()/100+"h");
					if(clientboard.board[i][j].getTroop()>9999)label[i][j].setText(clientboard.board[i][j].getTroop()/1000+"k");
					label[i][j].setVisible(true);
					}
				else label[i][j].setVisible(false);
			}
		}
		g.drawImage(cs, cursor[0]*20, cursor[1]*20, null);
		
	}
	
	public GameClient() {
//		super();
		start = false;
		death = false;
		cursor = new int[2];
		data = new ArrayList<Integer>();
		client = new Client("172.16.200.173", 420);
		idCounter++;
		//id = idCounter;
		
		clientboard = new Board();
		frame = new JFrame();
		frame.setSize(Board.size*20,Board.size*20+30);
		this.setSize(Board.size*20,Board.size*20+30);
		frame.setVisible(true);
		frame.getContentPane().add(this);
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		addKeyListener(new KeyBoard());
		this.setFocusable(true);

		label = new JLabel[Board.size][Board.size];
		
		Font font = new Font("Dialog",Font.PLAIN,10);
		this.setLayout(null);
		for(int i=0;i<Board.size;i++) {
			for(int j=0;j<Board.size;j++) {
				label[i][j] = new JLabel("",JLabel.CENTER);
				frame.add(label[i][j]);
				this.add(label[i][j]);
				label[i][j].setFont(font);
				label[i][j].setBounds(i*20,j*20,20,20);
//				label[i][j].setText("x");
				label[i][j].setVisible(true);
				//label[i][j].setAlignmentX(SwingConstants.CENTER);
				//label[i][j].setAlignmentY(SwingConstants.CENTER);
			}
		}
		font = new Font("Dialog",Font.PLAIN,Board.size);
		status=new JLabel("Waiting for other players ...",JLabel.CENTER);
		frame.add(status);
		this.add(status);
		status.setFont(font);
		status.setBounds(0, 0, Board.size*20, Board.size*20);
		status.setVisible(true);
		
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
		
		try {
//			client.out.writeUTF(""+GameServer.playercount);
			client.out.writeUTF(""+id);
//			id=Integer.parseInt(client.in.readUTF());
//			System.out.println(id);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		GameClient gc = new GameClient();
		gc.speed=5;
		gc.frame.getContentPane().add(gc);
		
		Runnable helloRunnable = new Runnable() {
			
		    public void run() {
				//gc.repaint();
			//	System.out.println("print");
//		    	System.out.println(gc.frame.getWidth());
			try {
				gc.code = gc.client.in.readUTF();
				gc.decode();
			} catch (IOException e) {
				e.printStackTrace();
			}
			gc.repaint();
			gc.frame.repaint();
		}
		};
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(helloRunnable, 0, (int)(1000/gc.speed), TimeUnit.MILLISECONDS);
	}
	
	private class KeyBoard implements KeyListener{

		public void keyTyped(KeyEvent e) {
			
		}
	
		public void keyPressed(KeyEvent e) {
				try {
					if(e.getKeyCode() == KeyEvent.VK_Q)client.out.writeUTF("q"+partial);
					if(e.getKeyCode() == KeyEvent.VK_E)client.out.writeUTF("e"+partial);
					if(e.getKeyCode() == KeyEvent.VK_Z)client.out.writeUTF("z"+partial);
					if(e.getKeyCode() == KeyEvent.VK_C)client.out.writeUTF("c"+partial);
					if(e.getKeyCode() == KeyEvent.VK_W||e.getKeyCode() == KeyEvent.VK_UP)client.out.writeUTF("w"+partial);
					if(e.getKeyCode() == KeyEvent.VK_A||e.getKeyCode() == KeyEvent.VK_LEFT)client.out.writeUTF("a"+partial);
					if(e.getKeyCode() == KeyEvent.VK_S||e.getKeyCode() == KeyEvent.VK_X||e.getKeyCode() == KeyEvent.VK_DOWN)client.out.writeUTF("x"+partial);
					if(e.getKeyCode() == KeyEvent.VK_D||e.getKeyCode() == KeyEvent.VK_RIGHT)client.out.writeUTF("d"+partial);
					
					if(e.getKeyCode() == KeyEvent.VK_T)client.out.writeUTF("t"+partial);
					if(e.getKeyCode() == KeyEvent.VK_G)client.out.writeUTF("g"+partial);
					if(e.getKeyCode() == KeyEvent.VK_B)client.out.writeUTF("b"+partial);
					if(e.getKeyCode() == KeyEvent.VK_Y)client.out.writeUTF("y"+partial);
					if(e.getKeyCode() == KeyEvent.VK_N)client.out.writeUTF("n"+partial);
					if(e.getKeyCode() == KeyEvent.VK_U)client.out.writeUTF("u"+partial);
					if(e.getKeyCode() == KeyEvent.VK_J)client.out.writeUTF("j"+partial);
					if(e.getKeyCode() == KeyEvent.VK_M)client.out.writeUTF("m"+partial);
					
					if(e.getKeyCode() == KeyEvent.VK_F)client.out.writeUTF("f"+partial);
//
					if(e.getKeyCode() == KeyEvent.VK_1)partial=1;
					if(e.getKeyCode() == KeyEvent.VK_2)partial=2;
					if(e.getKeyCode() == KeyEvent.VK_3)partial=3;
					if(e.getKeyCode() == KeyEvent.VK_4)partial=4;
					if(e.getKeyCode() == KeyEvent.VK_5||e.getKeyCode() == KeyEvent.VK_SPACE)partial=5;
					if(e.getKeyCode() == KeyEvent.VK_6)partial=6;
					if(e.getKeyCode() == KeyEvent.VK_7)partial=7;
					if(e.getKeyCode() == KeyEvent.VK_8)partial=8;
					if(e.getKeyCode() == KeyEvent.VK_9)partial=9;
					if(e.getKeyCode() == KeyEvent.VK_0)partial=0;
//					
					if(e.getKeyCode() <= KeyEvent.VK_Z&&e.getKeyCode() >= KeyEvent.VK_A)partial=0;
					if(e.getKeyCode() == KeyEvent.VK_UP||e.getKeyCode() == KeyEvent.VK_DOWN)partial=0;
					if(e.getKeyCode() == KeyEvent.VK_LEFT||e.getKeyCode() == KeyEvent.VK_RIGHT)partial=0;
					
				} catch (IOException e1) {
					e1.printStackTrace();
				}
		}
	
		public void keyReleased(KeyEvent e) {
			
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
