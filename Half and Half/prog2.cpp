///////////////////////////////////////////////////////////////////////////////
//
//  Half-and-Half
//
//  Author: Taylor Doell
//
//  Class: CSC 372 - Analysis of Algorithms
//
//  Course Instructor: Dr. A. Logar
//
//  Date Due: November 1, 2015
//  Date Programmed: 17 October 2015 - 31 October 2015
//
//  Description:
//		This program calculates the North / South line that would divide a farm
//      into two equal pieces given the farm is a convex polygon. It starts off
//      by having a loop that reads in the amount of points for the polygon
//      being read in. Once the count is read in, it reads in the amount of
//      points specified by that count. If three points were given for the
//      polygon, the polygon has to be convex and is able to be solved for the
//      area. If more than three points, the program verifies that the polygon
//      is convex. If it is not convex, the program can not divide the polygon
//      and find the solution to splitting the farm into two equal pieces. If
//      it is solveable, It cuts the shape into two pieces and decides where to
//      divide the shape. It does this by adding the min and max of each shape
//      and diving that answer by two. Each time it does this, the program
//      checks to see if the two areas are the same. If not, it takes the bigger
//      shape and resets the min or max appropriately. When a solution is found
//      or not, it outputs the case number and the solution if one was found,
//      or "No Solution" if it couldn't be solved.
//
//  Compilation Instructions: This program runs under the c++11 compiler.
//
//  Errors: No known errors
//
///////////////////////////////////////////////////////////////////////////////

#include <iostream>
#include <fstream>
#include <vector>
#include <string>
#include <iomanip>
#include <cmath>

using namespace std;

///////////////////////////////////////////////////////////////////////////////
//                                 Stucts
///////////////////////////////////////////////////////////////////////////////
/******************************************************************************
    This struct holds the x and y coordinates for a specific point
******************************************************************************/
struct Point
{
    double x;
    double y;
};

/******************************************************************************
    This struct holds values for the max and min of the shape
******************************************************************************/
struct Values
{
    double min;
    double max;
};

///////////////////////////////////////////////////////////////////////////////
//             			      Function Prototypes
///////////////////////////////////////////////////////////////////////////////
double CheckLeftTurn(Point point1, Point point2, Point point3);
bool ExistsSolution(vector<Point> coordinates);
double FindSolution(vector<Point> coordinates, Values &coordVals);
double GetArea(vector<Point> coordinates);
Point GetIntersectPoint(Point point1, Point point2, double middleXVal);
string GetOutputFileName(string inputFileName);
void PrintSolution(ofstream &fout, int caseNumber, bool isSolveable, double solution);
void ReadPoints(ifstream &fin, vector<Point> &coordinates, Values &coordVals, int count);
void SplitShape(vector<Point> coordinates, vector<Point> &shape1, vector<Point> &shape2, double middleXVal);

///////////////////////////////////////////////////////////////////////////////
//
//	Main
//
//	Author: Taylor Doell
//
//	Description:
//		This is the main starting point in the program and calls the functions
//		need to complete the requirements for this program. To get a full
//		description of what this program does, please refer to the program
//		description at the top of this file.
//
//	Parameters:
//		int argc     - number of command line arguments given
//		char *argv[] - arguments give from command line
//
//	Returns:
//		0 program ran successful.
///////////////////////////////////////////////////////////////////////////////
int main(int argc, char *argv[])
{
    bool isSolvable = false;
    int count = 0;
    int caseNumber = 1;
    double caseSolution;
    vector<Point> coordinates;
    Values coordVal;

    // If argc < 2, no arguments were given for a filename
    if (argc < 2)
    {
        // If not filename was given, output error and exit
        cout << "No input file was given as arguments" << endl;
        exit(1);
    }

    ifstream fin(argv[1]); // Open input file
    // Get output filename and open the file
    ofstream fout(GetOutputFileName(argv[1]));

    // Read in how many points are in the polygon
    while (fin >> count)
    {
        // Read in the points for the polygon
        ReadPoints(fin, coordinates, coordVal, count);

        // If polygon has only 3 coordinates, can only be convex
        if (coordinates.size() == 3)
        {
            isSolvable = true;
        }
        else
        {
            // Check if solution is convex and solveable
            isSolvable = ExistsSolution(coordinates);
        }

        coordinates.pop_back();

        // If polygon area is solveable, find the solution
        if (isSolvable == true)
        {
            caseSolution = FindSolution(coordinates, coordVal);
        }

        // Print the case number and solution if either solveable or not
        PrintSolution(fout, caseNumber, isSolvable, caseSolution);

        coordinates.clear(); // Clear coordinates to process next shape
        caseNumber += 1; // Increment case number for next shape
        isSolvable = false; // Reset isSolveable to false for next shape
    }

    // Close the input and output file
    fin.close();
    fout.close();

    return 0;
}

///////////////////////////////////////////////////////////////////////////////
//
//	CheckLeftTurn
//
//	Author: Taylor Doell
//
//	Description: This function asks for three points to be given and computes
//               the cross product by turning the three points into two vectors.
//               Once the cross product is calculated, it checks to see if the
//               cross product calculated is positive or negative. If positive,
//               the turn is a left turn, otherwise it is negative and results
//               in a right turn.
//
//	Parameters:
//		Point point1 - first point to check turn with
//      Point point2 - second point to check turn with
//      Point point3 - third point to check turn with
//
//	Returns:
//		bool retval - returns true if the points make a left turn
//
///////////////////////////////////////////////////////////////////////////////
double CheckLeftTurn(Point point1, Point point2, Point point3)
{
    double answer = 0.0;

    // Constructing vector from points 1 and 2
    Point vector1;
    vector1.x = point2.x - point1.x;
    vector1.y = point2.y - point1.y;

    // Constructing vector from points 1 and 3
    Point vector2;
    vector2.x = point3.x - point1.x;
    vector2.y = point3.y - point1.y;

    // Compute the cross product of the two vectors
    answer = (vector1.x * vector2.y) - (vector2.x * vector1.y);

    return answer;
}

///////////////////////////////////////////////////////////////////////////////
//
//	ExistsSolution
//
//	Author: Taylor Doell
//
//	Description: This function loops through the coordinates given from the
//               input file and checks to see if they form a convex polygon.
//               It does this by checking the first turn and seeing if it is
//               right or left. Depending on which way it first turned, the
//               rest of the turns have to be that direction. This function
//               loops until it hits the first non zero turn. This function
//               then sets a boolean value indiciating true if the first turn
//               was a left turn and false if the first turn was a right turn.
//
//	Parameters:
//		vector<Point> coordinates - vector that holds points from polygon
//
//	Returns:
//		bool retval - returns true if the coordinates form a polygon
//
///////////////////////////////////////////////////////////////////////////////
bool ExistsSolution(vector<Point> coordinates)
{
    int i = 0;
    int count = 0;
    double answer = 0.0;
    bool isLeftTurn = false;
    bool isSolveable = true;

    // Check if first turn is a left turn or right turn
    while (answer == 0)
    {
        // Only loops here if cross product starting out is zero
        answer = CheckLeftTurn(coordinates[count], coordinates[count + 1], coordinates[count + 2]);

        if (answer == 0)
        {
            // Increment count to skip when looping through the rest
            count++;
        }
    }

    if (answer < 0)
    {
        // Set isSolveable to false
        isLeftTurn = false;
    }
    else if (answer > 0)
    {
        // Set isSolveable to false
        isLeftTurn = true;
    }

    // Loop through the coordinates checking to see if all have the same turn
    for (i = count; i < coordinates.size() - 3; i++)
    {
        // Get cross product result to see if left or right turn
        answer = CheckLeftTurn(coordinates[i], coordinates[i+1], coordinates[i+2]);

        if (answer == 0)
        {
            // If the cross product is 0 we don't know which turn it is
            continue;
        }
        else if (answer < 0 && isLeftTurn == true)
        {
            // Set isSolveable to false
            isSolveable = false;
            break;
        }
        else if (answer > 0 && isLeftTurn != true)
        {
            // Set isSolveable to false
            isSolveable = false;
            break;
        }
    }

    // Return if the solution is solveable
    return isSolveable;
}

///////////////////////////////////////////////////////////////////////////////
//
//	FindSolution
//
//	Author: Taylor Doell
//
//	Description: This function uses the coordinates read in from the file and
//               the max and min x values to split the polygon into two shapes.
//               Once the polygon is split into the two shapes, the areas of
//               the two shapes are compared and if they are not equal, the
//               middleXVal is recalculated with the new max and min values.
//               The only reasons the max or min values are changed depend on
//               which shape has the larger area.
//
//	Parameters:
//		vector<Point> coordinates - vector that the points for the polygon
//      Values &coordVals - struct that holds the max and min x values
//
//	Returns:
//		double middleXVal - the x value to divide the area exactly in half
//
///////////////////////////////////////////////////////////////////////////////
double FindSolution(vector<Point> coordinates, Values &coordVals)
{
    double shape1Area;
    double shape2Area;
    vector<Point> shape1;
    vector<Point> shape2;
    double middleXVal = fabs((coordVals.max + coordVals.min) / 2);

    // Split the shape to baseline of areas
    SplitShape(coordinates, shape1, shape2, middleXVal);
    // Get the areas for shape1 and shape2
    shape1Area = GetArea(shape1);
    shape2Area = GetArea(shape2);

    // While the two areas don't equal another when above 1e-6
    while (fabs(shape1Area - shape2Area) > 1e-6)
    {
        // If shape1Area > shape2Area, split the right side to find new max x
        if (shape1Area > shape2Area)
        {
            // Set max x value middle and recalculate the middleXVal
            coordVals.max = middleXVal;
            middleXVal = (coordVals.max + coordVals.min) / 2;

            // Split shape1 into two shapes with the new middleXVal
            SplitShape(coordinates, shape1, shape2, middleXVal);
            // Get the areas of both shapes
            shape1Area = GetArea(shape1);
            shape2Area = GetArea(shape2);
        }
        else
        {
            // Set min x value middle and recalculate the middleXVal
            coordVals.min = middleXVal;
            middleXVal = (coordVals.max + coordVals.min) / 2;

            // Split shape2 into two shapes with the new middleXVal
            SplitShape(coordinates, shape1, shape2, middleXVal);
            // Get the areas of both shapes
            shape1Area = GetArea(shape1);
            shape2Area = GetArea(shape2);
        }
    }

    // Return the final solutions middleXVal to split the two areas equally
    return middleXVal;
}

///////////////////////////////////////////////////////////////////////////////
//
//	GetArea
//
//	Author: Dr. Corwin and Dr. Logar
//
//	Description: This function computes the area of the polygon given to the
//               function. This has been slightly modified from the version
//               posted on the website.
//
//	Parameters:
//		vector<Point> coordinates - vector that holds points from polygon
//
//	Returns:
//		double retval - the area computed for the polygon given
//
///////////////////////////////////////////////////////////////////////////////
double GetArea(vector<Point> coordinates)
{
    int i, j = 0;
    double retval = 0;

    // Loop through the points calculating the area
    for (i = 0; i < coordinates.size(); i++)
    {
        // Calculate the area by adding and subtracting trapezoids
        j = (i + 1) % coordinates.size();
        retval += coordinates[i].x * coordinates[j].y;
        retval -= coordinates[i].y * coordinates[j].x;
    }

    // Return the absolute value of the area
    return fabs(retval / 2);
}

///////////////////////////////////////////////////////////////////////////////
//
//	GetIntersectPoint
//
//	Author: Taylor Doell
//
//	Description: This function computes the y value that satisfies the
//               intersection point that occurs between the two points given.
//               The x value for the intersection point is given by the
//               middleXVal that is passed into the function.
//
//	Parameters:
//		Point point1 - one end point of the line segment sent in
//      Point point2 - the second end point of the line segment
//      double middleXVal - the x value for the intersection point
//
//	Returns:
//		double retPoint - the point where the middleXVal intersects segment
//
///////////////////////////////////////////////////////////////////////////////
Point GetIntersectPoint(Point point1, Point point2, double middleXVal)
{
    Point retPoint;

    retPoint.x = middleXVal; // Set x coordinate of intersection

    // Get the y value for the intersection point
    retPoint.y = ((middleXVal - point1.x) / (point2.x - point1.x)) * (point2.y - point1.y) + point1.y;

    return retPoint; // Return the Point of intersection
}

///////////////////////////////////////////////////////////////////////////////
//
//	GetOutputFileName
//
//	Author: Taylor Doell
//
//	Description: This function takes the input file name and pulls off the
//               string before the . and extension in order to generate the
//               the same file name with a .out extension.
//
//	Parameters:
//		string inputFileName - inputFileName that was given by cmd arguments
//
//	Returns:
//		string fileName - returns the output file name
//
///////////////////////////////////////////////////////////////////////////////
string GetOutputFileName(string inputFileName)
{
    // Get the index of where the period is
    int periodIndex = inputFileName.find_last_of(".");
    // Get substring from index 0 up to the index of where the period is at
    string fileName = inputFileName.substr(0, periodIndex);

    fileName += ".out"; // Append out to the file name

    return fileName;
}

///////////////////////////////////////////////////////////////////////////////
//
//	PrintSolution
//
//	Author: Taylor Doell
//
//	Description: This function determines if a solution needs to be written to
//               the output file or to print no solution. That decisions is
//               determined by the isSolveable variable provided to the
//               function. For each case, it outputs the case number and then
//               either no solution or the solution.
//
//	Parameters:
//		ofstream &fout - file to write the output to
//      int caseNumber - the number of shape we are on
//      bool isSolveable - specifies if the shape has a solution or not
//      double solution - holds value of where to split the polygon
//
//	Returns:
//		None
//
///////////////////////////////////////////////////////////////////////////////
void PrintSolution(ofstream &fout, int caseNumber, bool isSolveable, double solution)
{
    // Construct beginning section of the output string
    string outputString = "Case " + to_string(caseNumber) + ": ";

    if (isSolveable == true)
    {
        // If solution was solveable, print the solution out to 5 decimal points
        fout << outputString << setprecision(5) << fixed << solution << endl;
    }
    else
    {
        // Print no solution if isSolveable is false
        fout << outputString << "No solution" << endl;
    }
}

///////////////////////////////////////////////////////////////////////////////
//
//	ReadPoints
//
//	Author: Taylor Doell
//
//	Description: This function reads the amount of points told by the count
//               that was previously read in. It inserts those points into a
//               vector of coordinates and also sets the max and min x values
//               for that particular polygon.
//
//	Parameters:
//      ifstream &fin - input file to read coordinates from
//		vector<Point> &coordinates - vector that holds strings from file
//      Values &coordVals - struct that holds the max and min x values
//      int count - represents the number of points to be read in
//
//	Returns:
//		None
//
///////////////////////////////////////////////////////////////////////////////
void ReadPoints(ifstream &fin, vector<Point> &coordinates, Values &coordVals, int count)
{
    int i = 0;
    Point newPoint;
    Point firstPoint;

    // Read the first point in and add it to the coordinates
    fin >> firstPoint.x;
    fin >> firstPoint.y;
    coordinates.push_back(firstPoint);

    // Set the base line values for min and max
    coordVals.min = firstPoint.x;
    coordVals.max = firstPoint.x;

    // Loop through reading in the points from the file
    for (i = 0; i < count - 1; i++)
    {
        // Get x and y coordinate for the Point
        fin >> newPoint.x;
        fin >> newPoint.y;

        // Look for new min and max x values
        if (newPoint.x < coordVals.min)
        {
            // If the x coordinate is less than min, reset the min value
            coordVals.min = newPoint.x;
        }
        else if (newPoint.x > coordVals.max)
        {
            // If the x coordinate is greate than max, reset the max value
            coordVals.max = newPoint.x;
        }

        // Push the Point onto the vector of coordinates
        coordinates.push_back(newPoint);
    }

    // Push the first point onto the coordinates to wrap last and first point
    coordinates.push_back(firstPoint);
    // Push the second point on again to check the last turn wrapping back
    coordinates.push_back(coordinates[1]);
}

///////////////////////////////////////////////////////////////////////////////
//
//	SplitShape
//
//	Author: Taylor Doell
//
//	Description: This function takes in a polygon and splits the coordinates up
//               into smaller shapes by cutting it into two pieces at the x
//               value provided by middleXVal. It loops through the points and
//               checks the current point and the next point seeing if the x
//               values for those points are around the middleXValue. As the
//               points are found, it calls a function to calculate the
//               intersection point and then adds it to the correct shape. The
//               boolean isShape1 determines if we are adding points to the 1st
//               or 2nd shape.
//
//	Parameters:
//		vector<Point> coordinates - vector that holds points from polygon
//      vector<Point> &shape1 - vector to hold points for the first shape
//      vector<Point> &shape2 - vector to hold points for the second shape
//      double middleXVal - the x value to split the polygon at
//
//	Returns:
//		None
//
///////////////////////////////////////////////////////////////////////////////
void SplitShape(vector<Point> coordinates, vector<Point> &shape1, vector<Point> &shape2, double middleXVal)
{
    Point intersectPoint;
    int i;

    // Clear Points from both shapes
    shape1.clear();
    shape2.clear();

    // Loop through the coordinates splitting them into two shapes
    for (i = 0; i < coordinates.size(); i++)
    {
        // If inserting on first shape, push the Point onto shape 1
        if (coordinates[i].x < middleXVal)
        {
            shape1.push_back(coordinates[i]);
        }
        else if (coordinates[i].x == middleXVal)
        {
            // If the coordinates[i].x == middleXVal it is a point in the list
            shape1.push_back(coordinates[i]);
            shape2.push_back(coordinates[i]);
        }
        // If inserting on second shape, push the Point onto shape 2
        else
        {
            shape2.push_back(coordinates[i]);
        }

        // If the middleXVal occurs between two points enter if statement
        if ((coordinates[i].x < middleXVal && coordinates[i + 1].x > middleXVal) ||
            (coordinates[i].x > middleXVal && coordinates[i + 1].x < middleXVal))
        {
            // Get the point of intersection
            intersectPoint = GetIntersectPoint(coordinates[i], coordinates[i + 1], middleXVal);

            // The intersection point needs to be inserted into both shapes
            shape1.push_back(intersectPoint);
            shape2.push_back(intersectPoint);
        }
    }
}
