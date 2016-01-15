import javax.swing.*;
/******************************************************************************
* Author: Taylor Doell
* Class: CSC 461 - Programming Languages
* Instructor: Dr. Weiss
* Description:  This class is the main class for the Paint program for CSC 461.
*  	This class contains our main method.
* Usage: This class requires the MainWindow class and all it's subsidiaries.
*	These should be all contained in the same folder.
* Compilation: Use the Java Compiler on this file (Paint.java) and run Paint to
*   use this program  This class must be in the same folder as all of it's
*	subsidiaries.
******************************************************************************/
public class Paint
{
	/**************************************************************************
	* Author: Taylor Doell
	* Description: This is the main function of the CSC461 Paint program.  It
	*	instantiates an instance of the MainWindow.  The app is set to be
	*	maximized to start.
	* Parameters:
	*	args: these are the command line arguments.  This program doesn't use
	*	them.
	**************************************************************************/
    public static void main( String args[] )
    {
        JFrame app = new MainWindow(); // Create main window
        // Set close app to close when x is clicked
        app.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        app.setVisible( true ); // Show main window
    }
}
