/*
 * TCSS 305 Fall 2015
 * Assignment 6 - Tetris.
 */

package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.media.CannotRealizeException;
import javax.media.Manager;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import model.Board;

/**
 * This is the class that the main JFrame.
 * 
 * @author Travis Holloway
 * @version Assignment 6: Tetris, December 1 2015
 */
public class TetrisGUI extends JFrame implements Observer {
    
    /*
     * ***********************************   CONSTANTS    *************************************
     */
    
    /**
     * Auto-Generated serial ID.
     */
    private static final long serialVersionUID = -6070741424783941765L;

    /**
     * Constant to set the background color of the GUI.
     */
    private static final Color COLOR = Color.DARK_GRAY;
    
    /**
     * Constant to represent the string that represents the path to the sound for drop.
     */
    private static final String DROP = ".\\sound\\drop.mp3";
    
    /**
     * Constant for the size of the JFrame's border.
     */
    private static final int FRAME_BORDER = 10;
    
    /**
     * Constant for minimum height of the JFrame.
     */
    private static final int HEIGHT = 400;
    
    /**
     * Constant to represent the string that represents the path to the sound for moves.
     */
    private static final String MOVE = ".\\sound\\move.mp3";
    
    /** 
     * The default delay (in milliseconds) for the move timer. 
     */
    private static final int MOVE_DELAY = 1100;
    
    /**
     * Constant for the amount of panels in the right Panel, used to determine
     * how tall the panels should be.
     */
    private static final int PANEL_COUNT = 3;
    
    /**
     * Constant to represent the string that represents the path to the sound for rotating.
     */
    private static final String ROTATE = ".\\sound\\rotate.mp3";
    
    /**
     * Constant for minimum width of the JFrame.
     */
    private static final int WIDTH = 600;
    
    /*
     * *************************************   FIELDS    **************************************
     */
    
    /**
     * Field that provides a reference to the board in BoardPanel.
     */
    private final Board myBoard;
    
    /**
     * JPanel field that provides reference to the BoardPanel JPanel container.
     */
    private final BoardPanel myBoardPanel;
    
    /**
     * A field for the Drop key.
     */
    private int myDropKey;
    
    /**
     * A field for the down key.
     */
    private int myDownKey;
    
    /**
     * Field to represent the custom font for the program.
     */
    private Font myFont;
    
    /**
     * Field to represent the state of the current game.  If true, game is
     * running, if false, game is over.
     */
    private boolean myGameOver;
    
    /**
     * A field for the left key.
     */
    private int myLeftKey;
    
    /**
     * Field that represents the KeyPanel.
     */
    private KeyPanel myKey;
    
    /**
     * Field to represent the KeyboardFocusManager.
     */
    private KeyboardFocusManager myManager;
    
    /**
     * Field that provides reference to boolean to indicate if the game
     * is paused or not.
     */
    private boolean myPause;
    
    /**
     * A field for the left key.
     */
    private int myPauseKey;
    
    /**
     * Field for the audio player.
     */
    private Player myPlayer;
    
    /**
     * Field to provide reference to the PreviewPanel of the program.
     */
    private PreviewPanel myPreviewPanel;
    
    /**
     * A field for the right key.
     */
    private int myRightKey;
    
    /**
     * A field for the rotate key.
     */
    private int myRotateKey; 
    
    /**
     * Field to represent the program's ScorePanel.
     */
    private ScorePanel myScore;
    
    /**
     * Timer field that provides reference to the Timer used to animate the program.
     */
    private final Timer myTimer;
    
    /**
     * Field to represent the JPanel that wraps the other components.  Required 
     * so that the wrapper size will update with changes in JFrame size.
     */
    private JPanel myWrapper;
    
    /*
     * **********************************   CONSTRUCTORS    ***********************************
     */

    /**
     * Constructor for TetrisGui.  Calls super(), instantiates myFrame and
     * myBoardPanel.
     */
    public TetrisGUI() {
        super();
        myBoardPanel = new BoardPanel();
        myTimer = new Timer(MOVE_DELAY, new MoveListener());
        myBoard = ((BoardPanel) myBoardPanel).getBoard();
        myPause = false;
        myGameOver = false;
    }
    
    /*
     * *********************************   PUBLIC METHODS    **********************************
     */
    
    /**
     * Helper method to assign the KeyFocusManger to the KeyListener.
     */
    public void addKeyManager() {
        //assign the key dispatcher, prevents loss of focus.
        myManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        myManager.addKeyEventDispatcher(new TetrisKeyDispatcher());
        
    }
    
    /**
     * Getter for the font so it can be shared amongst the rest of the program.
     * 
     * @return Returns myFont.
     */
    public Font getFont() {
        return myFont;
    }
    
    /**
     * Setter for myKeys.
     */
    public void setKey() {
        final int value = 500;
        myLeftKey = value;
        myRightKey = value; 
        myDownKey = value; 
        myDropKey = value;
        myRotateKey = value;
    }
    
    /**
     * Method that provides fields to class constants and configures the class
     * JFrame.
     */
    public void start() {
        
        /*
         * Begin by adding a title to the JFrame, a default close operation, and 
         * centering the JFrame.
         */
        setTitle("Tetris");
        
        /*
         * Set the default minimum size of the JFrame so that it will not shrink
         * past the minimum size of the BoardPanel.
         */
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        
        //Set the Icon for the Frame Title.
        final Icon saveIcon = new ImageIcon(".\\icons\\Tetris.png");
        final Image image = ((ImageIcon) saveIcon).getImage();
        setIconImage(image);
        
        /*
         * Create a font for the program using a try block.
         */
        try {
            //create a float for the font size.
            final float size = 40;
            //Make a input stream to retrieve font location.
            final InputStream stream = new FileInputStream(".\\fonts\\a.ttf");
            //Set myFont to the font in the fonts folder.
            myFont = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(Font.PLAIN, size);
            //register the font.
            final GraphicsEnvironment ge = 
                            GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(myFont);
        } catch (final IOException e) {
            e.printStackTrace();
        } catch (final FontFormatException e) {
            e.printStackTrace();
        }
        
        //Call helper method to add containers.
        addContainers();
        
        //Call helper method to assign keys.
        assignKeys();
        
        /*
         * Set the Default Close Operation. Pack the elements of myFrame. Set the minimum
         * to the current size. Make the frame visible. Center it in the middle of
         * the display.
         */
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
    }
    
    @Override
    public void update(final Observable theObservable, final Object theObject) {
        if (theObservable instanceof Board) {
            myWrapper.setPreferredSize(new Dimension((myBoardPanel.getWidth() * 2)
                                                     + FRAME_BORDER * 2,
                                                     myBoardPanel.getHeight()));
            /*
             * set the Timer to MOVE_DELAY times 1 minus the level as a percent.
             * (Example: 1100 * (1 - (.1 * level 5)) = 1100 * (1 - (.5)) or 550).
             */
            final int delay = (int) (MOVE_DELAY * (1 - (.1 * myScore.getLevel())));
            myTimer.setDelay(delay);
            
            repaint();
        } else if (theObservable instanceof KeyPanel) {
            assignKeys();
        }
    }
    
    /*
     * ********************************   PRIVATE METHODS    *********************************
     */
    
    
    /**
     * Helper method for start() that adds the Containers to myFrame.
     */
    private void addContainers() {
        //Create a wrapper JPanel to hold the GUI elements and set it to horizontal BoxLayout.
        myWrapper = new JPanel();
        myWrapper.setLayout(new BorderLayout());
        myWrapper.setBackground(COLOR);
        
        /*
         * Set the preferred dimension of wrapper to whatever the width of the
         * BoardPanel is times 2, and set the height to be the same as BoardPanel.
         */
        myWrapper.setPreferredSize(new Dimension((myBoardPanel.getWidth() * 2)
                                               + FRAME_BORDER * 2,
                                               myBoardPanel.getHeight()));
        
        /*
         * Create 2 border objects. The raised border is the outer boarder and 
         * will contain the title.  The lowered bevel is the inner boarder.
         */
        final Border raisedbevel = BorderFactory.createLineBorder(Color.YELLOW, 8, true);
        final Border loweredbevel = BorderFactory.createEmptyBorder(FRAME_BORDER,
                                                                    FRAME_BORDER,
                                                                    FRAME_BORDER,
                                                                    FRAME_BORDER);
        
        /*
         * Create a titled boarder using boarder factory.  
         */
        final Border titled = BorderFactory.createTitledBorder(raisedbevel,
                                                               "Tetris Wars",
                                                               TitledBorder.
                                                               DEFAULT_JUSTIFICATION,
                                                               TitledBorder.DEFAULT_POSITION,
                                                               myFont,
                                                               Color.YELLOW);
        myWrapper.setBorder(BorderFactory.createCompoundBorder(titled, loweredbevel));
        
        /*
         * Create a JPanel called right for the display to the right of the BoardPanel.
         * Set it's layout to vertical, color to Light Gray, and it's size to the same 
         * width and height as the BoardPanel.
         */
        final JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.PAGE_AXIS));
        right.setBackground(COLOR);
        
        addRightConatainers(right);
        
        //add the BoardPanel and right panel.
        myWrapper.add(myBoardPanel, BorderLayout.CENTER);
        myWrapper.add(right, BorderLayout.EAST);
        
        //Add the wrapper to myFrame.
        add(myWrapper);
        
        //Create a JMenuBar and add it to myFrame.
        final TetrisMenu menu = new TetrisMenu(myBoardPanel, this, myPreviewPanel, myTimer);
        setJMenuBar(menu);
    }
    
    /**
     * Helper method to add container panels for the right panel.
     * 
     * @param thePanel The panel to add the panels too.
     */
    private void addRightConatainers(final JPanel thePanel) {
        //Create an float that represents a custom size for the border font.
        final float size = 20;
        
        /*
         * Create 3 wrapper panels to hold the 3 unique panels, give them all BorderLayout
         * so they will stretch the unique panels correctly.  Set their size to be the same 
         * as the width of the BoardPanel and the height to be one 3rd of the BoardPanel.
         */
        final JPanel one = new JPanel();
        final JPanel two = new JPanel();
        final JPanel three = new JPanel();
        
        final List<JPanel> panels = new ArrayList<JPanel>();
        panels.add(one);
        panels.add(two);
        panels.add(three);
        
        for (final JPanel temp: panels) {
            temp.setLayout(new BorderLayout());
            temp.setBackground(COLOR);
        }
        
        final Border raisedbevel = BorderFactory.createLineBorder(Color.YELLOW, 5, true);
        final Border loweredbevel = BorderFactory.createLoweredBevelBorder();

        final Border titledOne = BorderFactory.createTitledBorder(raisedbevel,
                                                         "Next Piece",
                                                         TitledBorder.
                                                         DEFAULT_JUSTIFICATION,
                                                         TitledBorder.DEFAULT_POSITION,
                                                         myFont.deriveFont(size),
                                                         Color.YELLOW);
        one.setBorder(BorderFactory.createCompoundBorder(titledOne, loweredbevel));
        
        final Border titledTwo = 
                        BorderFactory.createTitledBorder(raisedbevel,
                                                         "Score",
                                                         TitledBorder.
                                                         DEFAULT_JUSTIFICATION,
                                                         TitledBorder.DEFAULT_POSITION,
                                                         myFont.deriveFont(size),
                                                         Color.YELLOW);
        two.setBorder(BorderFactory.createCompoundBorder(titledTwo, loweredbevel));
                 
        final Border titledThree = 
                        BorderFactory.createTitledBorder(raisedbevel,
                                                         "Key Configuration",
                                                         TitledBorder.
                                                         DEFAULT_JUSTIFICATION,
                                                         TitledBorder.DEFAULT_POSITION,
                                                         myFont.deriveFont(size),
                                                         Color.YELLOW);
        three.setBorder(BorderFactory.createCompoundBorder(titledThree, loweredbevel));
        
        //Add the wrapper panels to the right panel.
        thePanel.add(one);
        thePanel.add(Box.createVerticalStrut(FRAME_BORDER));
        thePanel.add(two);
        thePanel.add(Box.createVerticalStrut(FRAME_BORDER));
        thePanel.add(three); 
        
        for (final JPanel temp: panels) {
            temp.setPreferredSize(new Dimension(myBoardPanel.getWidth(),
                                            myBoardPanel.getHeight() / PANEL_COUNT));
        }
        
      //Create the 3 unique panels that represent the preview, score and keys.
        myPreviewPanel = new PreviewPanel(myBoardPanel);
        myScore = new ScorePanel(myBoardPanel, myFont);
        myKey = new KeyPanel();
        final JPanel keys = myKey.getPanel();
        
        //Add the unique panels to the wrapper panels.
        one.add(myPreviewPanel, BorderLayout.CENTER);
        two.add(myScore, BorderLayout.CENTER);
        three.add(keys, BorderLayout.CENTER);
        
      //Add observers to the Board in myBoardPanel.
        myBoard.addObserver(myBoardPanel);
        myBoard.addObserver((Observer) myPreviewPanel);
        myBoard.addObserver((Observer) myScore);
        myBoard.addObserver(this);
        
        //Add myFrame to the observers of key.
        myKey.addObserver(this);
    }
        
    /**
     * Helper method to assign keys.
     */
    private void assignKeys() {
        //Create an array of buttons using getCode from myKeys.
        final int[] buttons = myKey.getCode();
        
        //Assign each button the KeyCode values from getCode.
        myLeftKey = buttons[0];
        myRightKey = buttons[1];
        myDownKey = buttons[2];
        myDropKey = buttons[3];
        myRotateKey = buttons[4];
        myPauseKey = buttons[5];
    }
       
    /**
     * A class to control move events for the Timer.
     * 
     * @author Travis Holloway
     * @version Assignment 5: PowerPaint, November 9 2015
     */
    private class MoveListener implements ActionListener {
        @Override
        public void actionPerformed(final ActionEvent theEvent) {
            myGameOver = myBoard.isGameOver();
            if (!myGameOver) {
                myBoard.step();
            }
        }
    }
    
  /**
   * The key listener for the Tetris Program. 
   * 
   * @author Travis Holloway
   * @version Assignment 5: PowerPaint, November 9 2015
   */
    private class TetrisKeyDispatcher implements KeyEventDispatcher {
        @Override
            public boolean dispatchKeyEvent(final KeyEvent theEvent) {
            if (whichKey(theEvent) == myLeftKey) {
                //Call moveLeft.
                myBoard.moveLeft();
                final File file = new File(MOVE);
                playSound(file);
            } else if (whichKey(theEvent) == myRightKey) {
                //Call moveLeft.
                myBoard.moveRight();
                final File file = new File(MOVE);
                playSound(file);
            } else if (whichKey(theEvent) == myDownKey) {
                //Call moveLeft.
                myBoard.moveDown();
                final File file = new File(MOVE);
                playSound(file);
            } else if (whichKey(theEvent) == myDropKey) {
                //Call moveLeft.
                myBoard.hardDrop();
                final File file = new File(DROP);
                playSound(file);
            } else if (whichKey(theEvent) == myRotateKey) {
                //Call moveLeft.
                myBoard.rotate();
                final File file = new File(ROTATE);
                playSound(file);
            } else if (whichKey(theEvent) == myPauseKey) {
              //bitwise boolean swap.
                myPause ^= true;
                if (myPause) {
                    myBoardPanel.setPaused();
                    myTimer.stop();
                    setKey();
                } else if (!myBoardPanel.isGameState()) {
                    myBoardPanel.setPaused();
                    myTimer.start();
                    assignKeys();
                }
            }
            return false;
        }
        
        /**
         * Helper method to reduce complexity in key dispatcher.
         * 
         * @param theEvent The event passed to the key dispatcher and then passed
         * to this method.
         * @return Returns space unless theEvent is a KEY_TYPED event in which case
         * it returns the char of the theEvent.
         */
        private int whichKey(final KeyEvent theEvent) {
            //set an initial value that won't be used .
            int returnCode = 0;
            /*
             * Check if theEvent is a KEY_TYPED event, if so, set the returnChar to
             * the value of theEvent's char.
             */
            if (theEvent.getID() == KeyEvent.KEY_PRESSED) {
                returnCode = theEvent.getKeyCode();
            }        
            return returnCode;
        }
        
        /**
         * Helper method for TetrisKeyDispatcher that control the sound played.
         * 
         * @param theFile The file which represents the source of the sound to be played.
         */
        private void playSound(final File theFile) {
            if (!myBoardPanel.isMuteSound()) {
                myPlayer = (Player) myBoardPanel.getPlayer();
                try {
                    myPlayer = Manager.createRealizedPlayer(theFile.toURI().toURL());
                } catch (final NoPlayerException | CannotRealizeException | IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } 
                myPlayer.start();
            }
        }
    }
}