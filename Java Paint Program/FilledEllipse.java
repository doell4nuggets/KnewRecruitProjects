import java.awt.*;
/******************************************************************************
* Author: Benjamin Kaiser
* Class: CSC 461 - Programming Languages
* Instructor: Dr. Weiss
* Description:  This class is the filled ellipse class for the shape hiearchy
*	in the Paint program for CSC 461.  This is a regular class extending the
*	ellipse class The methods contained in this class include move(), draw(),
*	and toString().  move() is a function which will be used for manipulating
*	the values which need to be set when a shape is moved around the DrawPanel.
* Usage: This class is not meant to be a stand-alone class but rather to be
*	used as a base class for a shape hiearchy.
* Compilation: Use the Java Compiler on Paint.java and run Paint to use this
*	class.  This class must be in the same folder as Paint.java and all of
*	it's subsidiaries.
******************************************************************************/
public class FilledEllipse extends Ellipse
{
    // Holds background color for this object
    public Color bgShColor;
	/**************************************************************************
	* Author: Taylor Doell
	* Description: This function is the constructor for the FilledEllipse
	*	class it sets the fgShColor member variable to the fgColor which was
	*	passed into the function.  It also sets the bgShColor to bgColor. 
	*	It then calls move to set the intial coordinates.  
	* Parameters: 
	*	first: this is the coordinate of the upper left corner
	*	second: this is the coordinate of the lower right corner
	*	fgColor: this is the color of the border of the rectangle.  
	*	bgColor: this is the color of the interior of the rectangle.
	**************************************************************************/
    public FilledEllipse(Coordinate first, Coordinate second, Color bgColor, Color fgColor)
    {
        super(first, second, fgColor);

        // Set foreground and background color
        fgShColor = fgColor;
        bgShColor = bgColor;

        // Call move to initialize coordinates
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
        // Get width and height from the coordinates
        width = second.XCoord - first.XCoord;
        height = second.YCoord - first.YCoord;

        // If width and height > 0, the first point is the top left
        if (width > 0 && height > 0)
        {
            firstCoord = first;
        }
        // If width and height < 0, the second point is the top left
        else if (width < 0 && height < 0)
        {
            firstCoord = second;
        }
        // If width is < 0 and height > 0, reorder coordinates to form top left
        else if (width < 0 && height > 0)
        {
            firstCoord.XCoord = second.XCoord;
            firstCoord.YCoord = first.YCoord;
        }
        // If width is > 0 and height < 0, reorder coordinates to form top left
        else if (width > 0 && height < 0)
        {
            firstCoord.XCoord = first.XCoord;
            firstCoord.YCoord = second.YCoord;
        }

        // Compute the center of the shape for moving
        center.XCoord = (first.XCoord + second.XCoord)/2;
        center.YCoord = (first.YCoord + second.YCoord)/2;

        // Compute the vectors to get back to both x y coordiantes to help moving
        vecto1.XCoord = first.XCoord - center.XCoord;
        vecto1.YCoord = first.YCoord - center.YCoord;
        vecto2.XCoord = second.XCoord - center.XCoord;
        vecto2.YCoord = second.YCoord - center.YCoord;

        // Set width and height to their absolute value
        width = Math.abs(width);
        height = Math.abs(height);
    }
	/**************************************************************************
	* Author: Benjamin Kaiser
	* Description: This function is the actual function which calls the
	*	drawOval from the swing API.  It also sets the lines to be thicker.
	*	This function also calls the fillOval from the swing API.  
	* Parameters: 
	*	g: this is the graphics event which is used to draw the rectangle
	**************************************************************************/
    public void draw(Graphics g)
    {
        // Cast graphics to graphics2d to be able to set stroke size
        Graphics2D g2 = (Graphics2D)g;
        // Set background color and create a filled rectangle
        g2.setColor(bgShColor);
        g.fillOval(firstCoord.XCoord, firstCoord.YCoord, width, height);
        // Set the foreground color on the object
        g2.setColor(fgShColor);
        g2.setStroke(new BasicStroke(2)); // Set stroke size of 2
        // Draw the rectange with coordiantes, width and height
        g2.drawOval(firstCoord.XCoord, firstCoord.YCoord, width, height);
    }
	/**************************************************************************
	* Author: Taylor Doell
	* Description: This function is the function which converts this shape into
	*	something printable.  It ends up printing that this is a filled
	*	ellipse and the first and second coordinates.  
	* Parameters: NA
	**************************************************************************/
    public String toString()
    {
        String formattedString = "";

        // Format the string by appending the shape name and the first and second coordinates
        formattedString += "Filled Ellipse:\n";
        formattedString += "First Point (" + firstCoord.XCoord + ", " + firstCoord.YCoord + ")\n";
        formattedString += "Second Point (" + secondCoord.XCoord + ", " + secondCoord.YCoord + ")\n";

        return formattedString;
    }
}
