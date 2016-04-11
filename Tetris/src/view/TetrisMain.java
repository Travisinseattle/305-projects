/*
 * TCSS 305 Fall 2015
 * Assignment 5 - PowerPaint.
 */

package view;
import java.awt.EventQueue;

import javax.media.Codec;
import javax.media.PlugInManager;

import com.ibm.media.codec.audio.dvi.JavaDecoder;

/**
 * @author Travis Holloway
 * @version Assignment 6: Tetris, December 1 2015
 */
public final class TetrisMain {

    /**
     * Main Class of the program, over write default constructor to inhibit 
     * instantiation.
     */
    private TetrisMain() {
        throw new IllegalStateException();
    }

    /**
     * Main method for Tetris, instantiates the GUI and runs start() method.
     * 
     * @param theArgs Any arguments passed to the main method.
     */
    public static void main(final String[] theArgs) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TetrisGUI().start();
            }
        });
        
        final Codec c = new JavaDecoder();
        PlugInManager.addPlugIn("com.sun.media.codec.audio.mp3.JavaDecoder",
                                c.getSupportedInputFormats(),
                                c.getSupportedOutputFormats(null),
                                PlugInManager.CODEC);
    }
}