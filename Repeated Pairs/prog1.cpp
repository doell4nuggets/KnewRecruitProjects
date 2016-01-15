///////////////////////////////////////////////////////////////////////////////
//
//  Repeated Pairs
//
//  Author: Taylor Doell
//
//  Class: CSC 372 - Analysis of Algorithms
//
//  Course Instructor: Dr. A. Logar
//
//  Date Due: 4 October 2015
//  Date Programmed: 20 September 2015 - 4 October 2015
//
//  Description:
//		This program starts out by reading in all the strings from the file.
//	 	The program then loops through the vector containing the strings
//		that were read in from the file and looking to see if they contain
//		an even number of characters. If the string doesn't contain an even
//		number of characters, the string is not searched for a sequence of
//		pairs. Next, if the string does contain an even amount of characters,
//		the program uses recursion and dynamic programming to search the
//		string and determine if the string contains a sequence of pairs.
//		After all strings undergo this process, the program opens an output
//		file and outputs the results from the program to that specific file.
//		The program then exits with a return value of 0 indicating that the
//		program ran successfully.
//
//  Compilation Instructions: This program runs under the c++11 compiler.
//
//  Errors: No known errors
//
///////////////////////////////////////////////////////////////////////////////

#include <iostream>
#include <string>
#include <fstream>
#include <vector>

using namespace std;

///////////////////////////////////////////////////////////////////////////////
//             					Global Variable
///////////////////////////////////////////////////////////////////////////////
/* @brief  This table is used to make this program use dynamic programming   */
int DynamicTable[5000][5000];

///////////////////////////////////////////////////////////////////////////////
//             			      Function Prototypes
///////////////////////////////////////////////////////////////////////////////
void InitDynamicTable(int size);
int ReadInFile(vector<string> &testCaseStrings);
void OutputToFile(int numOfStrings, vector<bool> results);
bool FindPairsInString(string testString, int start, int end);

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
	int index = 0;
	int numOfStrings = 0;
	int sizeofstring = 0;
	vector<bool> results; //vector to hold results from each string processed
	vector<string> testCaseStrings; //vector to hold strings read in from file

	// Read in the strings and return the count of how many there are
	numOfStrings = ReadInFile(testCaseStrings);

	// Loop through each string the testCaseStrings vector
	for (index = 0; index < numOfStrings; index++)
	{
		sizeofstring = testCaseStrings[index].size(); // Get size of string

		InitDynamicTable(sizeofstring); // Initialize table to size of string

		// If string size isn't divisible by 2, it obviously contains no pairs
		if (sizeofstring % 2 != 0 )
		{
			results.push_back(false);
		}
		else
		{
			// Call FindPairsInString and add the result to the vector
			results.push_back(FindPairsInString(testCaseStrings[index], 0, sizeofstring));
		}
	}

	// Output results to file
	OutputToFile(numOfStrings, results);

	return 0;
}

///////////////////////////////////////////////////////////////////////////////
//
//	GetArea
//
//	Author: Taylor Doell
//
//	Description: This function reads in all the strings from the file that will
//				 be used in finding if the string contains a sequence of pairs.
//
//	Parameters:
//		vector<string> &testCaseStrings - vector that holds strings from file
//
//	Returns:
//		int size - the amount of strings that were read in from file
//
///////////////////////////////////////////////////////////////////////////////
int ReadInFile(vector<string> &testCaseStrings)
{
	string lineFromFile;
	ifstream fin("pairs.in");

	// If file didn't open, ouput error and exit
	if (!fin)
	{
		cout << "File couldn't be opened" << endl;
		exit(1);
	}

	// Read in each string from file and add to the testCaseStrings vector
	while (fin >> lineFromFile)
	{
		testCaseStrings.push_back(lineFromFile);
	}

	// Close File when done
	fin.close();

	return testCaseStrings.size(); // Return count of strings
}

///////////////////////////////////////////////////////////////////////////////
//
//	OutputToFile
//
//	Author: Taylor Doell
//
//	Description:
//		This function outputs the case number and if that string contained a
//		sequence of pairs or not. If not, the function outputs a "no" to the
//		output file after writing the case number and a space. If it did
//		contain a sequence of pairs, the same process is followed, but a "yes"
//		is written to the file.
//
//	Parameters:
//		int numOfStrings		- int that holds number of strings from file
//		vector<bool> results	- vector containing results from the program
//
//	Returns:
//		None
//
///////////////////////////////////////////////////////////////////////////////
void OutputToFile(int numOfStrings, vector<bool> results)
{
	ofstream fout("pairs.out");
	int index = 0;

	// If output file did not open, output error message and exit
	if (!fout)
	{
		cout << "Output File couldn't be opened" << endl;
		exit(1);
	}

	// Loop through results vector and outputs the result from each string
	for (index = 0; index < numOfStrings; index++)
	{
		string outputString;

		// If the string did not contain a sequence of pairs, output no
		if (results[index] == false)
		{
			outputString = "no";
		}
		else // If the string did contain a sequence of pairs, ouput yes
		{
			outputString = "yes";
		}

		// Output case number and if the string contained a sequence of pairs
		fout << index + 1 << " " << outputString << endl;
	}

	fout.close(); // Close File when done
}

///////////////////////////////////////////////////////////////////////////////
//
//	InitDynamicTable
//
//	Author: Taylor Doell
//
//	Description:
//		This function will be called before each string is
//		searched for a sequence of pairs to intitialize the global table
//		to -1. The table is an essential part is getting this program to
//		use the concept of dynamic programming. The size parameter cuts the
//		time to intialize the table down a lot. Why try to initialize the
//		whole table when you only need a smaller section of it?
//
//	Parameters:
//		int size 	- size of string that will be searched next
//
//	Returns:
//		None
//
///////////////////////////////////////////////////////////////////////////////
void InitDynamicTable(int size)
{
	int i, j;

	// Initialize table only with the size needed for the individual string
	for ( i = 0; i <= size; i++)
	{
		for (j = 0; j <= size; j++)
		{
			// Set spot in table to -1 for ease of dynamic programming
			DynamicTable[i][j] = -1;
		}
	}
}

///////////////////////////////////////////////////////////////////////////////
//
//	FindPairsInString
//
//	Author: Taylor Doell
//
//	Description:
//		This function is a recursive function that used a
//		dynamic programming technique to find a sequence of pairs in a given
//		string. If the starting and ending index of the string is not in the
//		dynamic table, the function looks to see if the start and end equal
//		each other and returns true if so. If it gets passed both of those
//		statements, it then goes to splitting the string into two parts and
//		compairing those. By the condition of those two strings, that
//		funiton will then be recursively called and go through the same
//		process. When the string is done processing, the function returns
//		whether the string contained a sequence of pairs or not.
//
//	Parameters:
//		string testString - string that will be searched for pairs
//		int start - the starting index for searching the string
//		int end - the ending index for searching the string
//
//	Returns:
//		true  - the string contained a sequence of pairs
//		false - the string did not contain a sequence of pairs
//
///////////////////////////////////////////////////////////////////////////////
bool FindPairsInString(string testString, int start, int end)
{
	bool result = false;
	string str1 = "";
	string str2 = "";
	int count = 1;

	// If result of particular string is in table, return that value
	if (DynamicTable[start][end] != -1)
	{
		return DynamicTable[start][end];
	}

	// If the start and end value equal each other, return true
	if (start == end)
	{
		return true;
	}

	// For the length of the string, split and search for pairs
	for (int i = end - 2; i >= 0; i-=2)
	{
		str1 = testString.substr(i, count);
		str2 = testString.substr(count + i, count);

		// If strings equal each other, calculate result of remaining string
		if (str1 == str2)
		{
			// Calculate result of the remaining part of the string
			result = FindPairsInString(testString.substr(start, end - (count + 1)), start, end - (count * 2));
		}

		// Increment the count of the current index
		count++;
	}

	// Add result to the table
	DynamicTable[start][end] = result;

	return result;
}
