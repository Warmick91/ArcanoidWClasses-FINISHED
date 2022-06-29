package arcanoid;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;


@SuppressWarnings("serial")
public class PanelGry extends JPanel implements ActionListener {		
	
	//Auxiliary objects
	private Random randGen = new Random();
	private Timer timer;
	
	
	//Number of rows and columns
	private int numOfRows = randGen.nextInt(5)+4;
	private int numOfColumns = randGen.nextInt(5)+4;
	
	//Dimensions
	private final int WIN_WIDTH = 600;
	private final int WIN_HEIGHT= 500;	
	private int paddleWidth;
	private int paddleHeight = 10;
	private final int ballRadius = 20;
	private int brickWidth = 50;
	private int brickHeight = 10;
	private int brickInterspace = 5;
	private int leftPadding = (WIN_WIDTH - (brickWidth * numOfColumns + brickInterspace*(numOfColumns - 1))) / 2;
	private int upperPadding = 10;	
	
	
	//Ball's position
	private int bx = (WIN_WIDTH - ballRadius) / 2;
	private int by = (WIN_HEIGHT - ballRadius) / 2;
	//Ball's movement
	private int dx = 3;
	private int dy = -3;
	private void ballMvt() {
		if(bx <= 0 || bx > WIN_WIDTH - ballRadius) {
			dx = dx * -1;
		}		
		if(by <= 0 || by + ballRadius >= py && bx > px && bx < px + paddleWidth) {
			dy = dy * -1;
		}
		bx += dx;
		by += dy;
	}
	
	
	//Difficulty settings
	String difficulty;
	private void chooseDiff() {
	String diff = JOptionPane.showInputDialog("Choose a difficulty level:\n1. Easy\n2. Normal\n3. Hard\n4. Your soul will be devoured");
	int diffNum = Integer.parseInt(diff);
	switch(diffNum) {
		case 1: difficulty = "Easy"; this.paddleWidth = 150; dx = 2; dy = -2;
		break;
		case 3: difficulty = "Hard"; this.paddleWidth = 70; dx = 4; dy = -4;
		break;
		case 4: difficulty = "VeryHard"; this.paddleWidth = 10; dx = 10; dy = -10; 
		break;
		default: difficulty = "Normal"; this.paddleWidth = 100; dx = 3; dy = -3;
		}
	}
	
	
	//Hit check
	private boolean wasHit[][] = new boolean[numOfColumns][numOfRows];	
	
	//Bricks' positions
	private int brickX[][] = new int[numOfColumns][numOfRows];
	private int brickY[][] = new int[numOfColumns][numOfRows];
	//Paddle's position
	private int px = (WIN_WIDTH - paddleWidth) / 2 ;
	private int py = WIN_HEIGHT - paddleHeight;

	public void brickCollision() {
		for(int i = 0; i < numOfColumns; i++) {
			for(int j = 0; j < numOfRows; j++) {
				if(wasHit[i][j]==false) {
					if(bx > brickX[i][j]
					&& bx < brickX[i][j] + brickWidth
					&& by + ballRadius > brickY[i][j]
					&& by < brickY[i][j] + brickHeight) {
						dy = -dy;
						wasHit[i][j] = true;
					} else if (by > brickY[i][j]
							&& by < brickY[i][j] + brickHeight
							&& bx + ballRadius > brickX[i][j]
							&& bx < brickX[i][j] + brickWidth) {
						dx = -dx;
						wasHit[i][j] = true;				
					}
				}
			}
		}
	}
	
	
	//Constructor
	public PanelGry() {
		chooseDiff();
		initComponents();
		setPreferredSize(new Dimension(WIN_WIDTH, WIN_HEIGHT));
		for(int i = 0; i < numOfColumns; i++) {
			for(int j = 0; j < numOfRows; j++) {
				brickX[i][j] = i * (brickWidth + brickInterspace) + leftPadding;
				brickY[i][j] = j * (brickHeight + brickInterspace) + upperPadding;
				wasHit[i][j] = false;
			}
		}
		timer = new Timer(10, this);
		timer.start();
						
		this.addKeyListener(new Control());		
		
	}
	
	//Initializer
	void initComponents() {	
		this.setBackground(Color.black);
		this.setVisible(true);
	}
	
	//Blocks sketching
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		sketch(g);
	}
	
	//Main sketch
	private void sketch(Graphics g) {
		if(gameState == "ongoing") {
		sketchPaddle(g);
		sketchBall(g);
		sketchBricks(g);
		} else {
			theEnd(g);
		}
	}
	
	//Sketch components
	private void sketchPaddle(Graphics g) {
		g.setColor(Color.red);
		g.fillRect(px, py, paddleWidth, paddleHeight);
		g.setColor(Color.white);
		g.drawRect(px, py, paddleWidth, paddleHeight);
	}
	
	private void sketchBall(Graphics g) {
		g.setColor(Color.red);
		g.fillOval(bx, by, ballRadius, ballRadius);
		g.setColor(Color.white);
		g.drawOval(bx, by, ballRadius, ballRadius);
	}
	
	private void sketchBricks(Graphics g) {
		for(int i = 0; i < numOfColumns; i++) {
			for(int j = 0; j < numOfRows; j++) {
				if(!wasHit[i][j]) {
					g.setColor(Color.red);
					g.fillRect(brickX[i][j], brickY[i][j], brickWidth, brickHeight);
					g.setColor(Color.white);
					g.drawRect(brickX[i][j], brickY[i][j], brickWidth, brickHeight);
				}				
			}
		}
	}

	//Action
	@Override
	public void actionPerformed(ActionEvent e) {
		checkGameState();
		brickCollision();
		ballMvt();
		repaint();
	}	
	
	
	//Control of the paddle
	private class Control extends KeyAdapter {
		
		@Override
		public void keyPressed (KeyEvent e) {
			int key = e.getKeyCode();
			
			if(key == KeyEvent.VK_LEFT && px > 0) {
				px -= 10;
				//System.out.println("LeftTest");
			}
			
			if(key == KeyEvent.VK_RIGHT && px < WIN_WIDTH - paddleWidth) {
				px += 10;
				//System.out.println("RightTest");
			}
		}
	}
	
	
	//Status of the game and the ending
	String gameState = "ongoing";
	private void checkGameState() {
		if(by > WIN_HEIGHT) {
			gameState = "YOU LOST. \nYOUR SOUL IS MINE.";			
		}
		int notHit = 0;
		for(int i = 0; i < numOfColumns; i++) {
			for(int j = 0; j < numOfRows; j++) {
				if(wasHit[i][j] == false) {
					notHit += 1;
				}
			}
		}
		if(notHit==0) {
			gameState = "YOU GOT AWAY WITH YOUR SOUL THIS TIME.\n SOON.";
		}
	}
	
	private void theEnd(Graphics g) {
		Font font = new Font("Helvetica", Font.BOLD, 14);
		FontMetrics metr = getFontMetrics(font);
		g.setColor(Color.red);
		g.setFont(font);
		g.drawString(gameState, (WIN_WIDTH-metr.stringWidth(gameState))/2, WIN_HEIGHT/2);		
	}
	
	
}
