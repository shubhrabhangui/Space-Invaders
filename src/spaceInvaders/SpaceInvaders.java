package spaceInvaders;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class SpaceInvaders extends JFrame implements Commons, ActionListener {

	int width, height;
	
	JButton play = new JButton("Resume");
	JButton level = new JButton("Difficulty Setting");
	JButton newGame = new JButton("New Game");
	JButton mainMenu = new JButton("Main Menu");
	JButton mainMenu2 = new JButton("Main Menu");
	
	JPanel panel = new JPanel();
	JPanel menu = new JPanel();
	JPanel game = new JPanel();
	JPanel levels = new JPanel();
	CardLayout layout = new CardLayout();
	
	JButton one = new JButton("1");
	JButton two = new JButton("2");
	JButton three = new JButton("3");
	JButton four = new JButton("4");
	JButton five = new JButton("5");
	
	volatile boolean gameStarted = false;
	
	GameEnvironment board = new GameEnvironment();
	
    public SpaceInvaders() {
        initUI();
    }

    private void initUI() {

    	panel.setLayout(layout);        
	    addButtons(); 

	    setSize(600, 700);
	    setResizable(false);
	    setLocationRelativeTo(null);
	    setVisible(true);
	    setTitle("Space Invaders");
	    setDefaultCloseOperation(EXIT_ON_CLOSE);
	    requestFocus();

    }

    private void addButtons() {
    	
    	JLabel gameTitle = new JLabel("Space Invaders");
	    gameTitle.setForeground(Color.WHITE);
	    gameTitle.setFont(new Font("Tahoma", Font.BOLD, 60));
	    gameTitle.setHorizontalAlignment(SwingConstants.CENTER);
	    gameTitle.setBounds(10, 80, 574, 246);
	    menu.add(gameTitle);
	    
    	play.setBounds(209, 373, 171, 55);
		play.addActionListener(this);
	    level.setBounds(209, 439, 171, 55);
	    level.addActionListener(this);
	    newGame.setBounds(209, 548, 171, 55);
	    newGame.addActionListener(this);
	    menu.setLayout(null);

	    //menu buttons
	    menu.add(play);
	    menu.add(level);
	    menu.add(newGame);
	    game.setLayout(new BorderLayout(0, 0));

	    //background colors
	    game.setBackground(Color.BLACK);
	    menu.setBackground(Color.DARK_GRAY);

	    //adding children to parent Panel
	    panel.add(menu,"Menu");
	    panel.add(game,"Game");
	    mainMenu.addActionListener(this);
	    
	    //game buttons
	    game.add(mainMenu, BorderLayout.NORTH);
	    game.add(board, BorderLayout.CENTER);
	    
	    levels.setBackground(Color.DARK_GRAY);
	    panel.add(levels, "Levels");
	    levels.setLayout(null);
	    
	    
	    one.setBounds(147, 138, 297, 71);
	    one.addActionListener(this);
	    levels.add(one);
	    
	    
	    two.setBounds(147, 220, 297, 71);
	    two.addActionListener(this);
	    levels.add(two);
	    
	    
	    three.setBounds(147, 302, 297, 71);
	    three.addActionListener(this);
	    levels.add(three);
	    
	    
	    four.setBounds(147, 384, 297, 71);
	    four.addActionListener(this);
	    levels.add(four);
	    
	    
	    five.setBounds(147, 466, 297, 71);
	    five.addActionListener(this);
	    levels.add(five);
	    
	    mainMenu2.setBounds(147, 579, 297, 70);
	    mainMenu2.addActionListener(this);
	    levels.add(mainMenu2);
	    
	    JLabel lblLevelSelection = new JLabel("DIFFICULTY SETTING");
	    lblLevelSelection.setForeground(Color.WHITE);
	    lblLevelSelection.setHorizontalAlignment(SwingConstants.CENTER);
	    lblLevelSelection.setFont(new Font("Tahoma", Font.BOLD, 50));
	    lblLevelSelection.setBounds(10, 0, 574, 139);
	    levels.add(lblLevelSelection);

	    getContentPane().add(panel);
	    layout.show(panel,"Menu");
	}
    
	public void actionPerformed(ActionEvent event) {

		Object source = event.getSource();
		if (source == newGame) {
			gameStarted = true;
	    	layout.show(panel, "Game");
	    	GameEnvironment.paused = false;
	    	board.requestFocus();
	    	board.createGameEnvironment();
	    } else if (source == play) {
	    	if(gameStarted == true) {
	    		layout.show(panel, "Game");
	    		GameEnvironment.paused = false;
	    		board.requestFocus();
	    	} else {
	    		JOptionPane.showMessageDialog(null, "Game hasn't started yet! Please start a game first!");
	    	}
	    } else if (source == level){
	    	layout.show(panel, "Levels");
	    } else if (source == mainMenu){
	    	GameEnvironment.paused = true;
	        layout.show(panel, "Menu");
	    } else if (source == one) {
	    	GameEnvironment.speedOfGame = 10;
	    } else if (source == two) {
	    	GameEnvironment.speedOfGame = 9;
	    } else if (source == three) {
	    	GameEnvironment.speedOfGame = 7;
	    } else if (source == four) {
	    	GameEnvironment.speedOfGame = 6;
	    } else if (source == five) {
	    	GameEnvironment.speedOfGame = 5;
	    } else if (source == mainMenu2) {
	    	layout.show(panel, "Menu");
	    }
	}

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            SpaceInvaders ex = new SpaceInvaders();
            ex.setVisible(true);
        });
    }
}
