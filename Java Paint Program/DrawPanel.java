import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.List;
import java.util.LinkedList;
import java.lang.Math;

/******************************************************************************
* Author: Taylor Doell
* Class: CSC 461 - Programming Languages
* Instructor: Dr. Weiss
* Description:  This class is the JPanel for the CSC461 Paint program.  It
*	creates the shapes and puts them in a LinkedList.  It also implements the
*	MouseListener interface to tell if something should be drawn or moved.
* Usage: This class requires the Paint class all it's subsidiaries.	These 
*	should be all contained in the same folder.  
* Compilation: Use the Java Compiler on the Paint.java file and run Paint to
*	use this program  This class must be in the same folder as all of it's 
*	subsidiaries.  
******************************************************************************/

// ButtonPanel changes color in response to button presses
public class DrawPanel extends JPanel implements MouseListener
{
    private List<Shape> shapeList = new LinkedList<Shape>();
    private Coordinate firstCoord;
    private Coordinate secondCoord;

    public String currentShape; // Holds name of current shape
    public Color foregroundColor;
    public Color backgroundColor;
    public Shape mobileShape; // Holds shape that is being moved
	/**************************************************************************
	* Author: Taylor Doell
	* Description: This function is the constructor for the DrawPanel class.
	*	It default creates a line and sets it to black.  It also adds the mouse
	*	listener.  
	* Parameters: NA
	**************************************************************************/
    public DrawPanel()
    {
        addMouseListener(this);

        currentShape = "Line"; // Set default shape of line

        foregroundColor = Color.black;
        backgroundColor = Color.black;
    }
	/**************************************************************************
	* Author: Taylor Doell
	* Description: This function is empty because it is not used.  It needs to
	* 	exist though because the MouseListener interfaces requires it to be
	*	overwritten.  
	* Parameters: 
	*	e: this is a mouse event
	**************************************************************************/
    public void mouseClicked(MouseEvent e){ }
	/**************************************************************************
	* Author: Taylor Doell
	* Description: This function is empty because it is not used.  It needs to
	* 	exist though because the MouseListener interfaces requires it to be
	*	overwritten.  
	* Parameters: 
	*	e: this is a mouse event
	**************************************************************************/
    public void mouseEntered(MouseEvent e){ }
	/**************************************************************************
	* Author: Taylor Doell
	* Description: This function is empty because it is not used.  It needs to
	* 	exist though because the MouseListener interfaces requires it to be
	*	overwritten.  
	* Parameters: 
	*	e: this is a mouse event
	**************************************************************************/
    public void mouseExited(MouseEvent e){ }
	/**************************************************************************
	* Author: Taylor Doell
	* Description: This function checks mouse presses.  It has two options.
	*	if there is a left button press, we simply get the coordinates.  If
	*	it's been a right button press, then we find the closest shape and put
	*	it into our temporary shape called mobileShape
	* Parameters: 
	*	e: this is a mouse event
	**************************************************************************/
    public void mousePressed(MouseEvent e)
    {
        //I believe I need to if these so if button1, then paint new
        //if button3, then call a distance and move
        if (SwingUtilities.isLeftMouseButton(e) == true)
        {
			// Create new first coordinate
            firstCoord = new Coordinate();
			// Set x and y coordinates
            firstCoord.XCoord = e.getX();
            firstCoord.YCoord = e.getY();
        }
        else if (SwingUtilities.isRightMouseButton(e) == true)
        {
            if (shapeList.size() > 0)
            {
                //find closest shape in the list
                firstCoord = new Coordinate();
				// Set x and y coordinates
                firstCoord.XCoord = e.getX();
                firstCoord.YCoord = e.getY();
				// Set mobile to the closest point
                mobileShape = getClosest(firstCoord);
            }
        }
    }
	/**************************************************************************
	* Author: Taylor Doell
	* Description: This function checks mouse releases.  It has two options.
	*	if there is a left button press, we simply get the coordinates and draw
	*	the shape.  If it's been a right button press, then we get the second
	*	coordinates, and reset the values in the mobileShape, pop it out of the
	*	list and put it back in on top. 
	* Parameters: 
	*	e: this is a mouse event
	**************************************************************************/
    public void mouseReleased(MouseEvent e)
    {
        //same as in mousePressed
        //if button1, then paint new
        //if button3, then move
        if (SwingUtilities.isLeftMouseButton(e) == true)
        {
			// Create new second coordinate
            secondCoord = new Coordinate();
			// Set x and y coordinates
            secondCoord.XCoord = e.getX();
            secondCoord.YCoord = e.getY();
            
			// Call method to create and add current shape to list
			SetCurrentShape();
			
			// Call repaint to redraw the window
			repaint();
        }
        else if (SwingUtilities.isRightMouseButton(e) == true)
        {
            //move the shape
            if (mobileShape != null)
            {
				// Create new second coordinate
                secondCoord = new Coordinate();
				// Set x and y coordinates
                secondCoord.XCoord = e.getX();
                secondCoord.YCoord = e.getY();

				// Set the first coordinate x and y value based new center and shape vector
                firstCoord.XCoord = secondCoord.XCoord + mobileShape.vecto1.XCoord;
                firstCoord.YCoord = secondCoord.YCoord + mobileShape.vecto1.YCoord;

				// // Set the second coordinate x and y value based new center and shape vector
                secondCoord.XCoord = secondCoord.XCoord + mobileShape.vecto2.XCoord;
                secondCoord.YCoord = secondCoord.YCoord + mobileShape.vecto2.YCoord;
				
				// Call move with the new coordinates to place
                mobileShape.move(firstCoord, secondCoord);

				// Re add the shape to put it on top
                shapeList.add(mobileShape);
                mobileShape = null;

				// Redraw the window to show the new shape
				repaint();
            }
        }
    }
	/**************************************************************************
	* Author: Taylor Doell
	* Description: This function is the overloaded paintComponent method.
	*	It calls paintComponent on the JPanel and then draws each shape again.  
	* Parameters: 
	*	g: this is a Graphics event which is passed into the shapes to draw.  
	**************************************************************************/
    public void paintComponent(Graphics g)
    {
		super.paintComponent(g); // Tell JFrame to repaint

		// Foreach shape in the list, paint draw the shapes
        for (Shape shape : shapeList)
        {
            shape.draw(g);
        }
    }
	/**************************************************************************
	* Author: Taylor Doell
	* Description: This function removes an item from the linked list and
	*	repaints.  
	* Parameters: NA 
	**************************************************************************/
    public void RemoveLastObject()
    {
		// Remove last shape from the shapeList
        shapeList.remove(shapeList.size() - 1);

		// Redraw JPanel
        repaint();
    }
	/**************************************************************************
	* Author: Taylor Doell
	* Description: This function removes all things from the linked list and
	*	repaints.  
	* Parameters: NA 
	**************************************************************************/
    public void ClearScreen()
    {
		// Remove all shapes from the shapeList
        shapeList.clear();

		// Redraw JPanel
        repaint();
    }
	/**************************************************************************
	* Author: Taylor Doell
	* Description: This function sets our temporary currentShape variable
	*	to the type of shape that we need to draw.  It is based on the text
	*	received from the menu choice that we picked.  
	* Parameters: NA 
	**************************************************************************/
    private void SetCurrentShape()
    {
        if (currentShape == "Line")
        {
			// Add new line to list based off of the coordinates
            shapeList.add(new Line(firstCoord, secondCoord, foregroundColor));
        }
        else if (currentShape == "Filled Rectangle")
        {
			// Add new Filled Rectangle to list based off of the coordinates
            shapeList.add(new FilledRectangle(firstCoord, secondCoord, backgroundColor, foregroundColor));
        }
        else if (currentShape == "Filled Ellipse")
        {
			// Add new Filled Ellipse to list based off of the coordinates
            shapeList.add(new FilledEllipse(firstCoord, secondCoord, backgroundColor, foregroundColor));
        }
        else if (currentShape == "Rectangle")
        {
			// Add new Rectangle to list based off of the coordinates
            shapeList.add(new Rectangle(firstCoord, secondCoord, foregroundColor));
        }
        else if (currentShape == "Ellipse")
        {
			// Add new Ellipse to list based off of the coordinates
            shapeList.add(new Ellipse(firstCoord, secondCoord, foregroundColor));
        }
    }
	/**************************************************************************
	* Author: Taylor Doell
	* Description: This function uses Euclidean distance to go through the
	*	linked list and check to see if the shape is closest to the centerCoord
	*	It then returns the shape that is closest.  
	* Parameters: NA 
	**************************************************************************/
    private Shape getClosest(Coordinate centerCoord)
    {
        int min = 999999;
        Shape minShape = null;
		//temp for finding min
        int tempMin;
        for (Shape shape : shapeList)
        {
			//euclidean distance
            tempMin = (int)Math.sqrt(Math.pow(shape.center.XCoord - centerCoord.XCoord, 2.0) + Math.pow(shape.center.YCoord - centerCoord.YCoord, 2.0));
            //square root of ((x2-x1)^2 + (y2-y1)^2
            if (tempMin < min && tempMin <= 30)
            {
                min = tempMin;
                minShape = shape;
            }
        }

        // Remove shape to put on top after moving
        shapeList.remove(minShape);

        // Returns shape closest to coordinate given
        return minShape;
    }
}
