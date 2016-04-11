/*
 * TCSS 305 Fall 2015
 * Assignment 6 - Tetris.
 */

package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import model.Board;

/**
 * This is the class configures and displays a menu at the top
 * of the JFrame.
 * 
 * @author Travis Holloway
 * @version Assignment 6: Tetris, December 1 2015
 */
public class TetrisMenu extends JMenuBar implements Observer {
    
    /*
     * ***********************************   CONSTANTS    *************************************
     */

    /**
     * Auto-Generated Serial ID.
     */
    private static final long serialVersionUID = -7196560764026132953L;
    
    /**
     * Constant for the starting height of the board.
     */
    private static final int BOARD_HEIGHT = 20;
    
    /**
     * Constant for the starting width of the board.
     */
    private static final int BOARD_WIDTH = 10;
    
    /*
     * *************************************   FIELDS    **************************************
     */
    
    /**
     * Field to provide reference to the board in myBoardPanel.
     */
    private final Board myBoard;
    
    /**
     * Field to provide reference to the current BoardPanel in use by the
     * program.
     */
    private final BoardPanel myBoardPanel;
    
    /**
     * Field to provide reference to the JFrame of the program.
     */
    private final JFrame myFrame;
    
    /**
     * Field to provide reference to the PreviewPanel of the program.
     */
    private final PreviewPanel myPanel;
    
    /**
     * Field to provide reference to the JFrame's Timer.
     */
    private final Timer myTimer;
    
    /*
     * **********************************   CONSTRUCTORS    ***********************************
     */

    /**
     * The Constructor for the TetrisMenu, has the BoardPanel as a parameter
     * in order to access methods and fields in the BoardPanel.
     * 
     * @param theBoardPanel The current BoardPanel in use by the program.
     * @param theFrame The program's main JFrame.
     * @param thePanel the PreviewPanel of the the program.
     * @param theTimer The timer used to animate the program.
     */
    public TetrisMenu(final BoardPanel theBoardPanel,
                      final JFrame theFrame,
                      final PreviewPanel thePanel,
                      final Timer theTimer) {
        super();
        myBoardPanel = theBoardPanel;
        myBoard = myBoardPanel.getBoard();
        myFrame = theFrame;
        myPanel = thePanel;
        myTimer =  theTimer;
        
        //Create a list of menus.
        final List<JMenu> menus = new ArrayList<JMenu>();
        configureMenu(menus);
        
      //add list to the menu.
        for (final JMenu list: menus) {
            add(list);
        }
    }
    
    /*
     * *********************************   PUBLIC METHODS    **********************************
     */
    
    /* (non-Javadoc)
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    @Override
    public void update(final Observable theObservable, final Object theObject) {
        // TODO Auto-generated method stub
    }
    
    /*
     * *********************************   PRIVATE METHODS    *********************************
     */
    
    /**
     * Method to create and configure the File menu.
     * 
     * @return Returns a menu called File that holds NewGame, EndGame,
     * and Exit menus.
     */
    private JMenu addFileMenu() {
        //Create the Menu
        final JMenu file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F);
        
        //Create newGame, endGame, and exit JMenuItems.
        final JMenuItem newGame = new JMenuItem("NewGame");
        final JMenuItem endGame = new JMenuItem("EndGame");
        final JMenuItem exit = new JMenuItem("Exit");
        
        
        /*
         * Configure the newGame menu. Set the Mnemonic and set the action 
         * listener using a inner class.
         */
        newGame.setMnemonic(KeyEvent.VK_N);
        newGame.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent theEvent) {
                myBoard.newGame(BOARD_WIDTH, BOARD_HEIGHT, null);
                myBoardPanel.chooseMusic();
                ((TetrisGUI) myFrame).addKeyManager();
                myTimer.start();
                newGame.setEnabled(false);
                endGame.setEnabled(true);
            }
        });
        
        /*
         * Configure the endGame menu. Set the Mnemonic and set the action 
         * listener using a inner class.
         */
        endGame.setMnemonic(KeyEvent.VK_N);
        endGame.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent theEvent) {
                myBoardPanel.setGameState(true);
                myTimer.stop();
                newGame.setEnabled(true);
                endGame.setEnabled(false);
            }
        });
        
      //Disable endGame.
        endGame.setEnabled(false);
        
        /*
         * Configure the exit menu. Set the Mnemonic and set the action 
         * listener using a inner class.
         */
        exit.setMnemonic(KeyEvent.VK_X);
        exit.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent theEvent) {
                myFrame.dispatchEvent(new WindowEvent(myFrame,
                                                       WindowEvent.WINDOW_CLOSING));
            }
        });
        
        //Add the menu's to the list of menus and add separators between them.        
        file.add(newGame);
        file.addSeparator();
        file.add(endGame);
        file.addSeparator();
        file.add(exit);
        
        //Return the File Menu.
        return file;
    }
        
    /**
     * Method to create and configure the Help menu.
     * 
     * @return Returns a menu called About that pops a JMEssagePane with basic
     * information about the program.
     */
    private JMenu addHelpMenu() {
      //Create the Menu
        final JMenu help = new JMenu("Help");
        help.setMnemonic(KeyEvent.VK_H);
        
        //Create a Menu that pops a JOptionPane with information about the program.
        final JMenuItem about = new JMenuItem("About...");
        about.setMnemonic(KeyEvent.VK_A);
        final Icon icon = new ImageIcon(".\\icons\\tetris.png");
        about.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent theEvent) {
                
                JOptionPane.showOptionDialog(about, "TCSS 305 Tetris, Autumn"
                                             + " 2015\nWritten by Travis Holloway",
                                             "About", JOptionPane.DEFAULT_OPTION,
                                JOptionPane.QUESTION_MESSAGE, icon, new Object[] {}, null);
            }
        });
        
      //Create a Menu that pops a JOptionPane with the bibliography of the resources used.
        final JMenuItem bib = new JMenuItem("Bibliography");
        bib.setMnemonic(KeyEvent.VK_B);
        bib.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent theEvent) {
                BufferedReader reader = null;
                try {
                    String input = "";
                    String line = "";
                    reader = new BufferedReader(new FileReader(".\\bibliography\\bib.txt"));
                    while ((line = reader.readLine()) != null) {
                        final StringBuilder builder = new StringBuilder(input);
                        builder.append(line);
                        builder.append("\n");
                        input = builder.toString();
                    }
                    JOptionPane.showMessageDialog(bib, input);
                    reader.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        });
        help.add(about);
        help.add(bib);
        return help;
    }
    
    /**
     * A method to to create a Options Menu.
     * 
     * @return A Menu that provides 3 options: a grid, a thickness selection, and
     * a menu that opens a JPanel for choosing the color.
     */
    private JMenu addOptionMenu() {
        //Create the Menu
        final JMenu options = new JMenu("Options");
        options.setMnemonic(KeyEvent.VK_O);
        
        //create and configure the Grid menu item.
        final JCheckBoxMenuItem grid = new JCheckBoxMenuItem("Grid");
        grid.setMnemonic(KeyEvent.VK_G);
        grid.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent theEvent) {
                final boolean gridState = grid.isSelected();
                myBoardPanel.setGrid(gridState);
            }
        });
        
      //create and configure the Mute Music menu item.
        final JCheckBoxMenuItem muteMusic = new JCheckBoxMenuItem("Mute Music");
        muteMusic.setMnemonic(KeyEvent.VK_M);
        muteMusic.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent theEvent) {
                final boolean muteMusicState = muteMusic.isSelected();
                myBoardPanel.setMuteMusic(muteMusicState);
                myBoardPanel.chooseMusic();
                
            }
        });
        
        //create and configure the Mute Sound menu item.
        final JCheckBoxMenuItem muteSound = new JCheckBoxMenuItem("Mute Sounds");
        muteSound.setMnemonic(KeyEvent.VK_S);
        muteSound.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent theEvent) {
                final boolean muteSoundState = muteSound.isSelected();
                myBoardPanel.setMuteSound(muteSoundState);
                
            }
        });
        
        //create and configure the ships menu item.
        final JCheckBoxMenuItem ships = new JCheckBoxMenuItem("Blocks as Ships");
        ships.setMnemonic(KeyEvent.VK_B);
        ships.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent theEvent) {
                final boolean shipState = ships.isSelected();
                myBoardPanel.setShips(shipState);
                myPanel.setShips(shipState);
            }
        });
        
        //Add menu items to the Options menu.
        options.add(grid);
        options.add(muteMusic);
        options.add(muteSound);
        options.add(ships);
        
        //Return the File Menu.
        return options;
    }
        
    /**
     * Method to configure the JMenuBar.
     * 
     * @param theMenu the List of menu's that contains all the main
     * menu's of the JMenuBar.
     */
    private void configureMenu(final List<JMenu> theMenu) {
        //Add the menu's to the List of Menu's.
        theMenu.add(addFileMenu());
        theMenu.add(addOptionMenu());
        theMenu.add(addHelpMenu());
    }
}
