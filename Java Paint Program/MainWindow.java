import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.File;
import java.util.List;
import java.util.LinkedList;
/******************************************************************************
* Author: Taylor Doell
* Class: CSC 461 - Programming Languages
* Instructor: Dr. Weiss
* Description:  This class is the JFrame for the CSC461 Paint program.  It
*	creates the menus and also a JPanel for drawing which we call DrawPanel.
*	This class also implements the KeyListener interface to listen for key
*	presses for deletion, clearing, and closing.
* Usage: This class requires the Paint class all it's subsidiaries.	These
*	should be all contained in the same folder.
* Compilation: Use the Java Compiler on the Paint.java file and run Paint to
*	use this program  This class must be in the same folder as all of it's
*	subsidiaries.
******************************************************************************/
public class MainWindow extends JFrame implements KeyListener
{
    // String array contains the names for the menus
    String[] menuNames = {"File", "Shape", "Foreground Color", "Background Color", "Help"};

    JMenuBar menubar; // Holds reference of the menubar that was created
    DrawPanel drawPanel; // Holds reference to the drawPanel

	/**************************************************************************
	* Author: Taylor Doell
	* Description: This function is the constructor for the MainWindow class
	*	it creates a new instance of the drawPanel class and also creates the
	*	menu bar
	* Parameters: NA
	**************************************************************************/
    public MainWindow()
    {
        // Set the title and size of the JFrame
        setTitle("Paint");
        setSize( 640, 480 );

        // Create JPanel to draw shapes on
        drawPanel = new DrawPanel();

        // Set menu bar to select shape, colors, file, and help
        setJMenuBar(CreateMenuBar());
        // Add drawPanel to the content pane
        getContentPane().add(drawPanel);
        // Point the key listeners to this class
        addKeyListener(this);
    }
	/**************************************************************************
	* Author: Taylor Doell
	* Description: This function is empty because it is not used.  It needs to
	* 	exist though because the KeyListener interfaces requires it to be
	*	overwritten.
	* Parameters:
	*	e: this is a key event
	**************************************************************************/
    public void keyTyped(KeyEvent e){ }
	/**************************************************************************
	* Author: Taylor Doell
	* Description: This function is empty because it is not used.  It needs to
	* 	exist though because the KeyListener interfaces requires it to be
	*	overwritten.
	* Parameters:
	*	e: this is a key event
	**************************************************************************/
    public void keyReleased(KeyEvent e){ }
	/**************************************************************************
	* Author: Taylor Doell
	* Description: This function checks to see if a certain key has been
	*	pressed.  If the key is 'ESC' or 'Q', the program closes.  If the key
	*	is D, the top item in the list gets deleted.  If the key is C,
	*	the list is dumped and no items are shown on the DrawPanel.
	* Parameters:
	*	e: this is a key event
	**************************************************************************/
    public void keyPressed(KeyEvent e)
    {
        int keycode = e.getKeyCode(); // Get the code from key clicked

        if (keycode == KeyEvent.VK_ESCAPE || keycode == KeyEvent.VK_Q)
        {
            System.exit(0); // Exit if escape or Q is clicked
        }
        else if (keycode == KeyEvent.VK_D)
        {
            // Call drawPanel to remove the last object created
            drawPanel.RemoveLastObject();
        }
        else if (keycode == KeyEvent.VK_C)
        {
            // Clear screen if C is pressed
            drawPanel.ClearScreen();
        }
        // Call repaint to refresh the JPanel
        repaint();
    }

	/**************************************************************************
	* Author: Taylor Doell
	* Description: This function sets up a list of menu items for the menuBar
	*	It then returns that menubar.  It starts out by loading into a list
	*	of menus and then running through that list and adding them to the
	*	menubar.
	* Parameters: NA
	**************************************************************************/
    private JMenuBar CreateMenuBar()
    {
      	menubar = new JMenuBar();

        // Get list of menus to add to the menuBar
        List<JMenu> menus = GetMenus();

        // For each menu in Menus, add the menu to the menuBar
        for (JMenu menu : menus)
        {
            menubar.add(menu);
        }

        // Return created menuBar
        return menubar;
    }

	/**************************************************************************
	* Author: Taylor Doell
	* Description: This function gets all of the menu items and throws them
	*	into a list.
	* Parameters: NA
	**************************************************************************/
    private List<JMenu> GetMenus()
    {
        // Create list to hold menus
        List<JMenu> menus = new LinkedList<JMenu>();

        // Create a menu for each string in the array menuNames
        for (String menuName : menuNames)
        {
            menus.add(new JMenu(menuName));
        }

        // Add menu items to the file menu
        AddFileMenuItems(menus.get(0));
        // Add menu items to the Shape menu
        AddShapeMenuItems(menus.get(1));
        // Add menu items to the Foreground Color menu
        AddForegroundColorMenuItems(menus.get(2));
        // Add menu items to the Background Color menu
        AddBackgroundMenuItems(menus.get(3));
        // Add menu items to the Help menu
        AddHelpMenuItems(menus.get(4));

        return menus; // Return list of menus
    }

	/**************************************************************************
	* Author: Taylor Doell
	* Description: This function takes the fileMenu and adds the exit menu
	* 	item to it.
	* Parameters:
	*	fileMenu:	this is the fileMenu object
	**************************************************************************/
    private void AddFileMenuItems(JMenu fileMenu)
    {
        JMenuItem menuItem = new JMenuItem("Exit");

        // Add action listener to exit program
        menuItem.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent ae )
            {
                // Call exit on the program
                System.exit( 0 );
            }
        });

        // Add menuItem to the fileMenu
        fileMenu.add(menuItem);
    }

	/**************************************************************************
	* Author: Taylor Doell
	* Description: This function take the shapeMenu and adds the buttons to
	*	choose a shape.  These include Line, Rectangle, Ellipse, Filled
	*	Rectangle, and Filled Ellipse.
	* Parameters:
	*	shapeMenu:	this is the shapeMenu item
	**************************************************************************/
    private void AddShapeMenuItems(JMenu shapeMenu)
    {
        JMenuItem menuItem = new JMenuItem("Line");

        // Add ShapeActionListener to the Line menuItem
        menuItem.addActionListener(new ShapeActionListener());

        // Add menuItem to the shapeMenu
        shapeMenu.add(menuItem);

        menuItem = new JMenuItem("Rectangle");

        // Add ShapeActionListener to the Rectangle menuItem
        menuItem.addActionListener(new ShapeActionListener());

        // Add menuItem to the shapeMenu
        shapeMenu.add(menuItem);

        menuItem = new JMenuItem("Ellipse");

        // Add ShapeActionListener to the Ellipse menuItem
        menuItem.addActionListener(new ShapeActionListener());

        // Add menuItem to the shapeMenu
        shapeMenu.add(menuItem);

        menuItem = new JMenuItem("Filled Rectangle");

        // Add ShapeActionListener to the Filled Rectangle menuItem
        menuItem.addActionListener(new ShapeActionListener());

        // Add menuItem to the shapeMenu
        shapeMenu.add(menuItem);

        menuItem = new JMenuItem("Filled Ellipse");

        // Add ShapeActionListener to the Filled Ellipse menuItem
        menuItem.addActionListener(new ShapeActionListener());

        // Add menuItem to the shapeMenu
        shapeMenu.add(menuItem);
    }
	/**************************************************************************
	* Author: Taylor Doell
	* Description: This function take the foreground color menu and adds the
	*	buttons to choose a color.  These include Red, Blue, Green, Yellow,
	*	Orange, Magenta, Pink, and Black
	* Parameters:
	*	foregroundColorMenu:	this is the foreground color item
	**************************************************************************/
    private void AddForegroundColorMenuItems(JMenu foregroudColorMenu)
    {
        JMenuItem menuItem = new JMenuItem("Red");

        // Add ForegroundColorActionListener to the Red menuItem
        menuItem.addActionListener(new ForegroundColorActionListener());

        // Add menuItem to the foregroudColorMenu
        foregroudColorMenu.add(menuItem);

        menuItem = new JMenuItem("Blue");

        // Add ForegroundColorActionListener to the Blue menuItem
        menuItem.addActionListener(new ForegroundColorActionListener());

        // Add menuItem to the foregroudColorMenu
        foregroudColorMenu.add(menuItem);

        menuItem = new JMenuItem("Green");

        // Add ForegroundColorActionListener to the Green menuItem
        menuItem.addActionListener(new ForegroundColorActionListener());

        // Add menuItem to the foregroudColorMenu
        foregroudColorMenu.add(menuItem);

        menuItem = new JMenuItem("Yellow");

        // Add ForegroundColorActionListener to the Yellow menuItem
        menuItem.addActionListener(new ForegroundColorActionListener());

        // Add menuItem to the foregroudColorMenu
        foregroudColorMenu.add(menuItem);

        menuItem = new JMenuItem("Orange");

        // Add ForegroundColorActionListener to the Orange menuItem
        menuItem.addActionListener(new ForegroundColorActionListener());

        // Add menuItem to the foregroudColorMenu
        foregroudColorMenu.add(menuItem);

        menuItem = new JMenuItem("Magenta");

        // Add ForegroundColorActionListener to the Magenta menuItem
        menuItem.addActionListener(new ForegroundColorActionListener());

        // Add menuItem to the foregroudColorMenu
        foregroudColorMenu.add(menuItem);

        menuItem = new JMenuItem("Pink");

        // Add ForegroundColorActionListener to the Pink menuItem
        menuItem.addActionListener(new ForegroundColorActionListener());

        // Add menuItem to the foregroudColorMenu
        foregroudColorMenu.add(menuItem);

        menuItem = new JMenuItem("Black");

        // Add ForegroundColorActionListener to the Black menuItem
        menuItem.addActionListener(new ForegroundColorActionListener());

        // Add menuItem to the foregroudColorMenu
        foregroudColorMenu.add(menuItem);
    }
	/**************************************************************************
	* Author: Taylor Doell
	* Description: This function take the backgrounbd color menu and adds the
	*	buttons to choose a color.  These include Red, Blue, Green, Yellow,
	*	Orange, Magenta, Pink, and Black
	* Parameters:
	*	backgroundColorMenu:	this is the background color item
	**************************************************************************/
    private void AddBackgroundMenuItems(JMenu backgroundColorMenu)
    {
        JMenuItem menuItem = new JMenuItem("Red");

        // Add ForegroundColorActionListener to the Red menuItem
        menuItem.addActionListener(new BackgroundColorActionListener());

        // Add menuItem to the backgroundColorMenu
        backgroundColorMenu.add(menuItem);

        menuItem = new JMenuItem("Blue");

        // Add ForegroundColorActionListener to the Blue menuItem
        menuItem.addActionListener(new BackgroundColorActionListener());

        // Add menuItem to the backgroundColorMenu
        backgroundColorMenu.add(menuItem);

        menuItem = new JMenuItem("Green");

        // Add ForegroundColorActionListener to the Green menuItem
        menuItem.addActionListener(new BackgroundColorActionListener());

        // Add menuItem to the backgroundColorMenu
        backgroundColorMenu.add(menuItem);

        menuItem = new JMenuItem("Yellow");

        // Add ForegroundColorActionListener to the Yellow menuItem
        menuItem.addActionListener(new BackgroundColorActionListener());

        // Add menuItem to the backgroundColorMenu
        backgroundColorMenu.add(menuItem);

        menuItem = new JMenuItem("Orange");

        // Add ForegroundColorActionListener to the Orange menuItem
        menuItem.addActionListener(new BackgroundColorActionListener());

        // Add menuItem to the backgroundColorMenu
        backgroundColorMenu.add(menuItem);

        menuItem = new JMenuItem("Magenta");

        // Add ForegroundColorActionListener to the Magenta menuItem
        menuItem.addActionListener(new BackgroundColorActionListener());

        // Add menuItem to the backgroundColorMenu
        backgroundColorMenu.add(menuItem);

        menuItem = new JMenuItem("Pink");

        // Add ForegroundColorActionListener to the Pink menuItem
        menuItem.addActionListener(new BackgroundColorActionListener());

        // Add menuItem to the backgroundColorMenu
        backgroundColorMenu.add(menuItem);

        menuItem = new JMenuItem("Black");

        // Add ForegroundColorActionListener to the Black menuItem
        menuItem.addActionListener(new BackgroundColorActionListener());

        // Add menuItem to the backgroundColorMenu
        backgroundColorMenu.add(menuItem);
    }
	/**************************************************************************
	* Author: Taylor Doell
	* Description: This function takes the help menu and adds the
	*	menu items for help and about
	* Parameters:
	*	helpMenu:	this is the help menu
	**************************************************************************/
    private void AddHelpMenuItems(JMenu helpMenu)
    {
        JMenuItem menuItem = new JMenuItem("Help");

        // Add ActionListener to the Help menuItem
        menuItem.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent ae )
            {
                // Create string that contains all the content for the help window
                String windowString = "Drawing:\n";
                windowString += "To draw a shape on the screen, select a shape from the menu bar.\n";
                windowString += "Next, click the mouse and drag to the preferred size and then\n";
                windowString += "release the mouse.\n\nMoving:\n";
                windowString += "To move a shape, right click the mouse towards the center of\n";
                windowString += "the shape you want to move and drag to the new location.\n\n";
                windowString += "Delete:\nTo delete the last object created, press D on the ";
                windowString += "keyboard.\n\nClearing the Screen:\nTo delete all of the objects ";
                windowString += "on the screen, press C on the keyboard.\n\nQuit:\nTo quit ";
                windowString += "the program, press Q or the escape key on the keyboard.";

                // Show help window with the formatted string as content
                JOptionPane.showMessageDialog(drawPanel, windowString, "Help", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Add help menuItem to the helpMenu
        helpMenu.add(menuItem);

        menuItem = new JMenuItem("About");

        // Add ActionListener to the About menuItem
        menuItem.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent ae )
            {
                // Create string that contains all the content for the About window
                String windowString = "Version 0.0\n\n";
                windowString += "Authors and Contributors: Taylor Doell and Ben Kaiser\n\n";
                windowString += "This program implements a basic version of Paint. ";
                windowString += "You are able\nto draw 5 different shapes including ";
                windowString += "line, rectangle, ellipse, filled\nrectangle, and filled ";
                windowString += "ellipse. Before drawing a shape, you can pick a\n";
                windowString += "foreground color and also a background color if it's a filled ";
                windowString += "shape.\nOnce a shape is drawn on the screen, you can move ";
                windowString += "that shape by\nright clicking near the center of the shape ";
                windowString += "and dragging it to the\nnew location.";

                // Show about window with the formatted string as content
                JOptionPane.showMessageDialog(drawPanel, windowString, "About", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Add about menuItem to the helpMenu
        helpMenu.add(menuItem);
    }
	/**************************************************************************
	* Author: Taylor Doell
	* Description: This class is the action listener for the shapeMenu.  The
	*	method inside gets the menu item, gets the text from this item and
	*	then assigns that text to drawPanel's current shape.
	**************************************************************************/
    class ShapeActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            // Cast source to JMenuItem
            JMenuItem menuItem = (JMenuItem)e.getSource();
            // Get the menu text off of the menuItem
            String menuText = menuItem.getText();

            // Set currentShape String to menuText
            drawPanel.currentShape = menuText;
        }
    }
	/**************************************************************************
	* Author: Taylor Doell
	* Description: This class is the action listener for the foregroundMenu.
	*	The	method inside gets the menu item, gets the text from this item and
	*	then assigns that text to drawPanel's foreGroundColor.
	**************************************************************************/
    class ForegroundColorActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            // Cast source to JMenuItem
            JMenuItem menuItem = (JMenuItem)e.getSource();
            // Get the menu text off of the menuItem
            String menuText = menuItem.getText();

            if (menuText == "Red")
            {
                // Set foregroundColor to Red
                drawPanel.foregroundColor = Color.red;
            }
            if (menuText == "Blue")
            {
                // Set foregroundColor to Blue
                drawPanel.foregroundColor = Color.blue;
            }
            if (menuText == "Green")
            {
                // Set foregroundColor to Green
                drawPanel.foregroundColor = Color.green;
            }
            if (menuText == "Yellow")
            {
                // Set foregroundColor to Yellow
                drawPanel.foregroundColor = Color.yellow;
            }
            if (menuText == "Orange")
            {
                // Set foregroundColor to Orange
                drawPanel.foregroundColor = Color.orange;
            }
            if (menuText == "Magenta")
            {
                // Set foregroundColor to Magenta
                drawPanel.foregroundColor = Color.magenta;
            }
            if (menuText == "Pink")
            {
                // Set foregroundColor to Pink
                drawPanel.foregroundColor = Color.pink;
            }
            if (menuText == "Black")
            {
                // Set foregroundColor to Black
                drawPanel.foregroundColor = Color.black;
            }
        }
    }
	/**************************************************************************
	* Author: Taylor Doell
	* Description: This class is the action listener for the backgroundMenu.
	*	The	method inside gets the menu item, gets the text from this item and
	*	then assigns that text to drawPanel's backgroundColor.
	**************************************************************************/
    class BackgroundColorActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            // Cast source to JMenuItem
            JMenuItem menuItem = (JMenuItem)e.getSource();
            // Get the menu text off of the menuItem
            String menuText = menuItem.getText();

            if (menuText == "Red")
            {
                // Set backgroundColor to Red
                drawPanel.backgroundColor = Color.red;
            }
            if (menuText == "Blue")
            {
                // Set backgroundColor to Blue
                drawPanel.backgroundColor = Color.blue;
            }
            if (menuText == "Green")
            {
                // Set backgroundColor to Green
                drawPanel.backgroundColor = Color.green;
            }
            if (menuText == "Yellow")
            {
                // Set backgroundColor to Yellow
                drawPanel.backgroundColor = Color.yellow;
            }
            if (menuText == "Orange")
            {
                // Set backgroundColor to Orange
                drawPanel.backgroundColor = Color.orange;
            }
            if (menuText == "Magenta")
            {
                // Set backgroundColor to Magenta
                drawPanel.backgroundColor = Color.magenta;
            }
            if (menuText == "Pink")
            {
                // Set backgroundColor to Pink
                drawPanel.backgroundColor = Color.pink;
            }
            if (menuText == "Black")
            {
                // Set backgroundColor to Black
                drawPanel.backgroundColor = Color.black;
            }
        }
    }
}
