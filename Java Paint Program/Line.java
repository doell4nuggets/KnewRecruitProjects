import java.awt.*;
/******************************************************************************
* Author: Benjamin Kaiser
* Class: CSC 461 - Programming Languages
* Instructor: Dr. Weiss
* Description:  This class is the line class for the shape hiearchy in the
*	Paint program for CSC 461.  This is a regular class extending the shape
*	class so necessarily it overwrites the methods from Shape.java.  The
*	methods contained in this class include move(), draw(), and toString();
*	move() is a function which will be used for manipulating the values
*	which need to be set when a shape is moved around the DrawPanel.
* Usage: This class is not meant to be a stand-alone class but rather to be
*	used as a base class for a shape hiearchy.
* Compilation: Use the Java Compiler on Paint.java and run Paint to use this
*	class.  This class must be in the same folder as Paint.java and all of
*	it's subsidiaries.
******************************************************************************/
public class Line extends Shape
{
	/**************************************************************************
	* Author: Taylor Doell
	* Description: This function is the constructor for the Line class
	*	it sets the fghShColor member variable to the fgColor which was passed
	*	into the function.  It then calls move to set the intial coordinates
	* Parameters: 
	*	first: this is the coordinate of the upper left corner
	*	second: this is the coordinate of the lower right corner
	*	fgColor: this is the color of the border of the rectangle.  
	**************************************************************************/
	public Line(Coordinate first, Coordinate second, Color fgColor)
    {
        // Set the first and second coordinate for the line
        firstCoord = first;
        secondCoord = second;

        // Set the foreground color of the line
        fgShColor = fgColor;

        // Call move to set initialize coordinates
        move(first, second);
    }

	/**************************************************************************
	* Author: Benjamin Kaiser
	* Description: This function takes two coordinates and sets the member
	*	variables of the upper left and lower right corners.  It also sets the
	*	center coordinates.  
	* Parameters: 
	*	first: this is the coordinate of the upper left corner
	*	second: this is the coordinate of the lower right corner
	**************************************************************************/
    public void move(Coordinate first, Coordinate second)
    {
        // Set the first and second coordinate for the line
        firstCoord = first;
        secondCoord = second;

        // Set the center of the line for moving
        center.XCoord = (first.XCoord + second.XCoord)/2;
        center.YCoord = (first.YCoord + second.YCoord)/2;

        // Make two vectors to help keep shape after moving
        vecto1.XCoord = first.XCoord - center.XCoord;
        vecto1.YCoord = first.YCoord - center.YCoord;
        vecto2.XCoord = second.XCoord - center.XCoord;
        vecto2.YCoord = second.YCoord - center.YCoord;
    }
	/**************************************************************************
	* Author: Taylor Doell
	* Description: This function is the actual function which calls the
	*	drawLine from the swing API.  It also sets the lines to be thicker.
	* Parameters: 
	*	g: this is the graphics event which is used to draw the rectangle
	**************************************************************************/
    public void draw(Graphics g)
    {
        // Cast graphics to graphics2d to be able to set stroke size
        Graphics2D g2 = (Graphics2D)g;
        // Set foreground color on the object
        g2.setColor(fgShColor);
        g2.setStroke(new BasicStroke(2)); // Set stroke size of 2
        // Draw the line
        g2.drawLine(firstCoord.XCoord, firstCoord.YCoord, secondCoord.XCoord, secondCoord.YCoord);
    }
	/**************************************************************************
	* Author: Taylor Doell
	* Description: This function is the function which converts this shape into
	*	something printable.  It ends up printing that this is a line and
	*	the first and second coordinates.  
	* Parameters: NA
	**************************************************************************/
    public String toString()
    {
        String formattedString = "";

		// Format the string by appending the shape name and the first and second coordinates
        formattedString += "Line:\n";
        formattedString += "First Point (" + firstCoord.XCoord + ", " + firstCoord.YCoord + ")\n";
        formattedString += "Second Point (" + secondCoord.XCoord + ", " + secondCoord.YCoord + ")\n";

        return formattedString;
    }
}
