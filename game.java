import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.font.ImageGraphicAttribute;
import java.awt.geom.Rectangle2D;

import javax.swing.*;

public class game extends JPanel implements ActionListener, KeyListener {
	Rectangle2D pizzaRect, boxRect; 
	
	JFrame frame = new JFrame(); 
	
	private Font f;
	private FontMetrics fm;
	
	Timer frameTimer, secondTimer; 
	
	final int playerSizeY = 200, playerSizeX = 127, boxSize = 150, pizzaSize = 100, boxSpeed = 10, pizzaSpeed = 15, playerSpeed = 10; 
	int xPos = 0, yPos = 300, yPosPlayer = getHeight()/2, yPosPizza = 1000, second = 0, numBoxes = 3;
	int option = 2;
	
	final double playerScale = 1.3; 
	
	boolean up = false, isFired = false, upPlayer = false, downPlayer = false, game = false, currentlyIntersecting = false; 
	
	ImageIcon introImg = new ImageIcon("Intro Background.jpg"); 
	ImageIcon backImg = new ImageIcon("Background Main.jpg");
	static ImageIcon menuIcon = new ImageIcon("IconMsg.png");
	ImageIcon characterImg, boxImg, pizzaImg;
	

	
	public game() {
		
		//Scaling the Players to the adequate amount

		characterImg = new ImageIcon("Player.png"); 
		Image temp = characterImg.getImage().getScaledInstance((int)(playerSizeX*playerScale), (int)(playerSizeY*playerScale), Image.SCALE_SMOOTH); 
		characterImg = new ImageIcon(temp); 
		
		boxImg = new ImageIcon("Pizza Box.png"); 
		temp = boxImg.getImage().getScaledInstance(boxSize, boxSize, Image.SCALE_SMOOTH); 
		boxImg = new ImageIcon(temp); 
		
		pizzaImg = new ImageIcon("Pizza.png"); 
		temp = pizzaImg.getImage().getScaledInstance(pizzaSize, pizzaSize, Image.SCALE_SMOOTH); 
		pizzaImg = new ImageIcon(temp); 
		
		frameTimer = new Timer(17, this); 
		secondTimer = new Timer(1000,this); 

		frameTimer.start(); 
		secondTimer.start(); 
		
		// Set properties of the frame
		frame.setSize(423,512); 
		frame.setLocationRelativeTo(null);
		frame.setContentPane(this);
		frame.setTitle("Pizza Defender");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);
		
		addKeyListener(this);
		setFocusable(true);
		requestFocus();
		
		// Initialize a Font object and FontMetric object
		f = new Font("Britannic Bold", Font.PLAIN, 20);
		fm = getFontMetrics(f);
	}
	
	public void paint(Graphics g) {
		// Repaints the frame and its components
		super.paint(g);
		// Declare and initialize a Graphics2D object
		Graphics2D g2 = (Graphics2D) g;
		
		if (game == false) {
			g2.drawImage(introImg.getImage(), 0, 0, this); 
		}
		
		if (game == true) {
			g2.drawImage(backImg.getImage(), 0, 0, this); 

			g2.drawImage(characterImg.getImage(), 25, yPosPlayer, this); 
		
			g2.drawImage(boxImg.getImage(), getWidth()-160, yPos, this); 
		
			g2.drawImage(pizzaImg.getImage(), xPos, yPosPizza, this); 
			
			// Set the font
			g2.setFont(f);
			Color black = new Color(0, 0, 0);
			g2.setColor(black);
				
			 // Output the number of remaining boxes on center of JFrame
			 g2.drawString("Pizza Count: "  + Integer.toString(numBoxes), ((getWidth() - fm.stringWidth("Pizza Count: "  + Integer.toString(numBoxes))) / 2),
					 getHeight() - (getHeight() / 8) + fm.getAscent() / 2);
		
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == secondTimer) {
			second++;
			if (second >= 3) {
				frame.setSize(900,803); 
				frame.setLocationRelativeTo(null);

				game = true;
			}
		}
		
		if (e.getSource() == frameTimer) {
		 
				if (upPlayer == true) {
				
				 	if (yPosPlayer <= 0){
				 		yPosPlayer -= 0;
				 	}
				 	else
				 	{
					 	yPosPlayer -= playerSpeed; 			 
				 	}
				}
			
				else if (downPlayer == true) {
				
				 	if ((yPosPlayer) + characterImg.getIconHeight() >= getHeight() ){
				 		yPosPlayer += 0;
				 	}
				 	else
				 	{
				 		yPosPlayer += playerSpeed; 
				 	}		
				}
			
			
			if (yPos >= getHeight() - boxSize) {
				up = true; 
			} 
		
			else if (yPos <= 0) {
				up = false; 
			}
		
			if (up) {
				yPos -= boxSpeed; 
			}
		
			else {
				yPos += boxSpeed; 
			}
		
			if (xPos >= getWidth()) {
				isFired = false; 
				currentlyIntersecting = false;
			}
		
		
			xPos += pizzaSpeed; 
		
			repaint(); 
		
			pizzaRect = new Rectangle2D.Double(xPos, yPosPizza, pizzaImg.getIconWidth(), pizzaImg.getIconHeight()); 
			boxRect = new Rectangle2D.Double(getWidth()-160, yPos, boxImg.getIconWidth(), boxImg.getIconHeight()); 
		
			if (pizzaRect.intersects(boxRect) && (currentlyIntersecting == false)) {
				
				numBoxes--;
				currentlyIntersecting = true;
				
				if (numBoxes == 0) {
					JOptionPane.showMessageDialog(null, "Cowabunga!\nYou delivered all the Pizza!", "TMNT PIZZA TIME", JOptionPane.PLAIN_MESSAGE, menuIcon);					
					frameTimer.stop();
					secondTimer.stop();
					option = JOptionPane.showConfirmDialog(null, "Do you want to play again?", "TMNT PIZZA TIME", JOptionPane.YES_NO_OPTION);
					
					if (option == 0) {
						frame.setVisible(false);
						new game(); 
					}
					
					else if (option == 1) {
						JOptionPane.showMessageDialog(null, "Thanks for playing!", "TMNT PIZZA TIME", JOptionPane.PLAIN_MESSAGE, menuIcon);	
					 	System.exit(0);
					}
					
				}
			}
		
		}
	}
	
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE && (isFired == false)) {
			xPos = 25; 
			yPosPizza = yPosPlayer; 
			isFired = true; 
		} 
		
		else if (e.getKeyCode() == KeyEvent.VK_UP) {
			upPlayer = true;
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			downPlayer = true;
		}
	}
	
	public static void main (String args[]) {		
		new game(); 
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		 if (e.getKeyCode() == KeyEvent.VK_UP)
		 {
			 upPlayer = false;
		 }
		 else if (e.getKeyCode() == KeyEvent.VK_DOWN)
		 {
			 downPlayer = false;
		 }
		
	}
}
