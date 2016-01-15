import java.awt.*;
import javax.swing.*;
/******************************************************************************
* Author: Benjamin Kaiser
* Class: CSC 461 - Programming Languages
* Instructor: Dr. Weiss
* Description:  This class is the base class for the shape hiearchy in the
*	Paint program for CSC 461.  This is an abstract class so necessarily it
*	needs to be extended and the methods overwritten.  The methods contained
*	in this class include move(), draw(), and toString();
*	move() is a function which will be used for manipulating the values
*	which need to be set when a shape is moved around the DrawPanel.
* Usage: This class is not meant to be a stand-alone class but rather to be
*	used as a base class for a shape hiearchy.
* Compilation: Use the Java Compiler on Paint.java and run Paint to use this
*	class.  This class must be in the same folder as Paint.java and all of
*	it's subsidiaries.
******************************************************************************/
public abstract class Shape extends JComponent
{
    // fgShColor and bgShColor hold the foreground and background colors
    public Color fgShColor;
    public Color bgShColor;

    // firstCoord and secondCoord hold the values for the coordinates
    public Coordinate firstCoord = new Coordinate();
    public Coordinate secondCoord = new Coordinate();

    // Holds the point for the center of the object
    public Coordinate center = new Coordinate();

    // Holds vectors to recreate points from a moved center
    public Coordinate vecto1 = new Coordinate();
    public Coordinate vecto2 = new Coordinate();

    // Abstract to methods to implement for move(), draw(), and toString()
    public abstract void move(Coordinate first, Coordinate second);
    public abstract void draw(Graphics g);
    public abstract String toString();
}
