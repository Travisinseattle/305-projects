/*
 * TCSS 305 Fall 2015
 * Assignment 6 - Tetris.
 */

package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import model.AbstractPiece;
import model.Board;

/**
 * This is the JPanel that displays the next piece.  
 * 
 * @author Travis Holloway
 * @version Assignment 6: Tetris, November 9 2015
 */
public class PreviewPanel extends JPanel implements Observer {
    
    /*
     * ***********************************   CONSTANTS    *************************************
     */
    
    /**
     * Auto-generated serial ID.
     */
    private static final long serialVersionUID = 2500924605259207155L;
    
    /**
     * Constant for use with Color settings, represents red, green, and blue values, as well as
     * alpha.
     */
    private static final int COLOR_VALUES = 100;
    
    /**
     * Constant for the values in a rounded rectangle.  
     */
    private static final int ROUNDING = 20;
    
    /*
     * *************************************   FIELDS    **************************************
     */
    
    /**
     * Integer field to provide reference to the dimensions of the blocks.
     */
    private int myBlockSize;
    
    /**
     * Field that provides a reference to the Board that is being used by
     * myBoardPanel.
     */
    private Board myBoard;

    /**
     * Field that provides a reference to the Board passed to the 
     * constructor.
     */
    private final BoardPanel myBoardPanel;
    
    /**
     * Field for the list that holds the shapes that represent the current
     * piece.  
     */
    private List<Shape> myNextBlocks;
    
    /**
     * Field for the boolean that determines if blocks should be displayed as blocks or as
     * ships.  (True is ships.)
     */
    private boolean myShips;
    
    /*
     * **********************************   CONSTRUCTORS    ***********************************
     */

    /**
     * Constructor for PreviewPanel.
     * 
     * @param theBoardPanel A reference to the Board from TetrisGUI.
     */
    public PreviewPanel(final BoardPanel theBoardPanel) {
        super();
        myBoardPanel = theBoardPanel;
        configurePanel();
    }
    
    /*
     * *********************************   PUBLIC METHODS    **********************************
     */
    
    /**
     * Paints the current Board.
     * 
     * @param theGraphics The graphics context to use for painting.
     */
    public void paintComponent(final Graphics theGraphics) {
        super.paintComponent(theGraphics);
        final Graphics2D g2d = (Graphics2D) theGraphics;
        
        //clean up the display of the object.
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);
        /*
         * Get the current piece, and the frozen blocks. Re-instantiate the list
         * of the Current piece's block shapes (myCurrentBlocks) to clear it.  Capture
         * the piece's current coordinates in board coordinates and assign it to myBlocks.
         */
        final int[][] blocks = ((AbstractPiece) myBoard.getNextPiece()).getRotation();
        myNextBlocks = new ArrayList<Shape>();
        
        //Call helper methods.        
        createNextPiece(blocks);
        
        final Icon saveIcon = new ImageIcon(".\\block icons\\x.png");
        final Image image = ((ImageIcon) saveIcon).getImage();
                
        /*
         * Paint each block in myCurrentBlocks.        
         */
        for (final Shape temp: myNextBlocks) {
            if (myShips) {
                /*
                 * Create an integer and use it to draw that many sequentially smaller filled
                 * 3dBlocks nested inside each other to provide the illusion of a 3D block
                 * using for loop. Paint an image of a X-Wing over the block.
                 */
                ((Graphics2D) theGraphics).setPaint(new Color(COLOR_VALUES, COLOR_VALUES,
                                                              COLOR_VALUES, COLOR_VALUES));
                theGraphics.fillRoundRect((int) temp.getBounds().getX(),
                                       (int) temp.getBounds().getY(),
                                       myBlockSize, 
                                       myBlockSize,
                                       ROUNDING, ROUNDING);
                theGraphics.drawImage(image, (int) temp.getBounds().getX(),
                              (int) temp.getBounds().getY(),
                              myBlockSize, myBlockSize, this);
            } else {
                /*
                 * Create an integer and use it to draw that many sequentially smaller filled
                 * 3dBlocks nested inside each other to provide the illusion of a 3D block
                 * using for loop.
                 */
                ((Graphics2D) theGraphics).setPaint(Color.cyan);
                final int line = 4;
                for (int i = 0; i < line; i++) {
                    theGraphics.fill3DRect((int) temp.getBounds().getX() + i,
                                           (int) temp.getBounds().getY() + i,
                                           myBlockSize - (2 * i), 
                                           myBlockSize - (2 * i), true); 
                }
                
            }
        }
    }
    
    /**
     * Setter for myShips, determines if blocks look like ships or blocks.
     * 
     * @param theBoolean The state of the ships checkBox in the option menu in 
     * TetrisMenu.
     */
    public void setShips(final boolean theBoolean) {
        myShips ^= true;
        repaint();
    }

    /* (non-Javadoc)
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    @Override
    public void update(final Observable theObservable,
                       final Object theObject) {
        if (theObservable instanceof Board) {
            myBlockSize = myBoardPanel.getBlockWidth();
            repaint();
        }
    }
    
    /*
     * ********************************   PRIVATE METHODS    *********************************
     */
    
    /**
     * Method to initialize the class fields and configure the JPanel.
     */
    private void configurePanel() {
        myBoard = myBoardPanel.getBoard();
        myShips = false;
        setBackground(Color.LIGHT_GRAY);
        myNextBlocks = new ArrayList<Shape>();
        
        //Provide initial block size by using initial values of block width in myBoardPanel.
        myBlockSize = myBoardPanel.getBlockWidth();
    }
    
    /**
     * Helper method to paint objects in my PaintComponent method.
     * 
     * @param theBlocks the 2D integer array of block coordinates passed to the method.
     */
    private void createNextPiece(final int[][] theBlocks) {
        /*
         * Step through myBlocks using the first index of the 2D array.  Using
         * the [0] index and [1] index of this array, create a Rectangle2D and 
         * make it the width of myBlockWidth and the height of the panel height divided
         * by myBoardHeight subtracted from the total height of the panel.  (EX: panel
         * is 800 pixels high, so 800 / the amount of tiles in the board: 20 so each row
         * is 40 pixels high. Finally subtract that from 800 to get the blocks to fall 
         * downward instead of up.)  Add each rectangle2D to myCurrentBlocks.        
         */
        for (int i = 0; i < theBlocks.length; i++) {
            final int j = 0;
            //Create the shapes 3 blockwidth's from the left.
            final int x = (myBlockSize * 3) + (theBlocks[i][j] * myBlockSize);
            //Bug where currentPeice falls one level more than they should.
            final int y = getHeight() - ((theBlocks[i][j + 1] * myBlockSize) + myBlockSize);
            
            /*
             * Since my code wants to start at bottom and fall upward, certain things have to 
             * be coded oddly to reverse this behavior.  I have to have y bigger than the 
             * negative of the block height, in order to display the piece at the top of the
             * panel.
             */
            final Shape currentPieceBlock = new RoundRectangle2D.Double(x, y,
                                                                        myBlockSize,
                                                                        myBlockSize,
                                                                        20,
                                                                        20);
            myNextBlocks.add(currentPieceBlock);
            
        }
    }
}
