///////////////////////////////////////////////////////////////////////////////
//
//  FFT
//
//  Author: Taylor Doell
//
//  Class: CSC 372 - Analysis of Algorithms
//
//  Course Instructor: Dr. A. Logar
//
//  Date Due: December 1, 2015
//  Date Programmed: 19 November 2015 - 1 December 2015
//
//  Description:
//		This program first reads in the values needed for this program to run
//      correctly. Once the values are read in, it calculates the FFT for the
//      points read in from the file. Next, it gets the absolute values of the
//      FFT and saves them to a vector. It then uses a k value read in to know
//      how many shifts to compute on the FFT values. Each iteration of the k
//      value, it gets the absolute values of the shift and adds them to running
//      total for each index. To get an average for the computed values, a
//      function is called that divides each total by k. Next, up to 5 peaks are
//      found and then printed out to the output file.
//
//  Compilation Instructions: This program runs under the c++11 compiler.
//
//  Errors: No known errors
//
///////////////////////////////////////////////////////////////////////////////

#include <iostream>
#include <fstream>
#include <complex>
#include <vector>
#include <cmath>
#include <iomanip>

using namespace std;

///////////////////////////////////////////////////////////////////////////////
//                            Constants and Enum
//  Origin: Constants and enum were acquired from Dr. Logarn and Dr. Corwin
///////////////////////////////////////////////////////////////////////////////
const int MAX = 14096;
const complex<double> I (0, 1);
enum  direction {FORWARD, INVERSE};

///////////////////////////////////////////////////////////////////////////////
//    This struct holds values for the index and number for the peak found
///////////////////////////////////////////////////////////////////////////////
struct indexComp
{
    int index; // Holds index of peak
    double peakNum; // Holds peak value
};

///////////////////////////////////////////////////////////////////////////////
//             			      Function Prototypes
///////////////////////////////////////////////////////////////////////////////
string GetOutputFileName(string inputFileName);
vector<indexComp> FindPeaks(vector<double> &total, int n);
void FFTShift(vector<complex<double> > &a, vector< complex<double> > &y, int n, int k);
void SetTotals(vector<double> &total, vector< complex<double> > y, int n);
void DivideTotals(vector<double> &total, int n, int k);
void fft(vector<complex <double> > &a, int n, vector<complex <double> > &y);

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
    int n, k; // Values needed for the FFT
    int numpoints = 0; // Later gets set to n + k - 1
    vector< complex<double> > a(MAX); // Holds values read into the file
    vector< complex<double> > y(MAX); // Holds FFT values
    vector<double> total(MAX); // Holds value after all numbers and shifts are done
    vector<indexComp> results; // Holds peak values
    string inputfilename; // Input filename
    string outputfilename; // Ooutput filename

    // Check to make sure filename is specified in command line arguments
    if (argc < 1)
    {
        // If no filename was given, output error and exit
        cout << "ERROR: Filename argument not supplied" << endl;
        exit(1);
    }

    // Set filename to command line argument
    inputfilename = argv[1];
    // Format outputfilename from inputfilename
    outputfilename = GetOutputFileName(inputfilename);

    // Declare and open the files
    ifstream fin(inputfilename);
    ofstream fout(outputfilename);

    // Check to see if files opened correctly
    if (!fin)
    {
        // If input file could not be opened, output error and exit
        cout << "Could not open the input file" << endl;
        exit(1);
    }
    if (!fout)
    {
        // If output file could not be opened, output error and exit
        cout << "Could not open the output file" << endl;
        exit(1);
    }

    // Read in n and k values and compute the number of points to read in
    fin >> n >> k;
    numpoints = n + k - 1;

    // Read in all the points from the file
    for (int i = 0; i < numpoints; i++)
    {
        // Read in points to the vector that holds all the points
        fin >> a[i];
    }

    // Calculate FFT and add the absolute values to the totals vector
    fft(a, n, y);
    SetTotals(total, y, n);

    for (int i = 1; i < k; i++)
    {
        // Calculate shift in FFT
        FFTShift(a, y, n, i);
        // Add the absolute values to the totals vector
        SetTotals(total, y, n);
    }

    // Divide the total values by k
    DivideTotals(total, n, k);

    // Find up to 5 peaks of the FFT
    results = FindPeaks(total, n);

    // Output the results to the output file
    for (int i = 0; i < results.size(); i++)
    {
        // Output the index and the peakNum to the file
        fout << results[i].index << " " << fixed << setprecision(2) << results[i].peakNum << endl;
    }

    // Close the input and output files
    fin.close();
    fout.close();

    // Program ran successful
    return 0;
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
//	FindPeaks
//
//	Author: Taylor Doell
//
//	Description: This function loops through the absolute values of the FFT and
//               finds up to 5 local max peaks in the total values. Once the
//               peaks are found, they are returned out of this function.
//
//	Parameters:
//		vector<double> &total - absolute values of FFT
//      int n - number of points in FFT
//
//	Returns:
//		vector<indexComp> results - Holds the peak values of the FFT
//
///////////////////////////////////////////////////////////////////////////////
vector<indexComp> FindPeaks(vector<double> &total, int n)
{
    int i, j; // Variables for the loops
    int indexFound = -1; // Holds the index where the next max was found
    int n2 = n / 2; // Loop through n / 2 values
    double maxVal = 0; // Holds max value for each iteration
    vector<indexComp> results; // Holds the peak values

    // Loop 5 times to find up to 5 peak values
    for (i = 0; i < 5; i++)
    {
        // Set indexFound to -1 for invalid index
        indexFound = -1;
        // Set maxVal to 0 for initial max values
        maxVal = 0;

        // Loop through n / 2 values to find the peaks
        for (j = 2; j < n2; j++)
        {
            // If the value is a peak enter if statement
            if (total[j - 1] < total[j] && total[j] > total[j + 1])
            {
                // If next value is greater than already found maxVal, enter
                if (maxVal < total[j])
                {
                    // Set the index the new max was found at
                    indexFound = j;
                    // Set the maxVal of the new max
                    maxVal = total[j + 2] + total[j + 1] + total[j] + total[j - 1] + total[j - 2];
                }
            }
        }

        // If a peak was found and index was set, enter
        if (indexFound != -1)
        {
            // Create new struct for index and peakNum
            indexComp newMax;
            // Set the index and peakNum for the new peak
            newMax.index = indexFound;
            newMax.peakNum = maxVal;
            // Add newMax to the results vector
            results.push_back(newMax);
            // Set total[indexFound] = 0 to prevent counting same max again
            total[indexFound] = 0;
        }
    }

    // Return results with up to 5 peak values
    return results;
}

///////////////////////////////////////////////////////////////////////////////
//
//	FFTShift
//
//	Author: Taylor Doell
//
//	Description: This function computes the omega value, omega_power, and the
//               next value for the shift. It then loops through the values and
//               computes the y[i] point with equation given. Each loop it
//               recomputes the omega_power value for the next iteration.
//
//	Parameters:
//		vector<double> &a - holds points from file
//      vector<double> &y - holds points from FFT
//      int n - number of points in FFT
//      int k - FFT iteration currently on
//
//	Returns:
//		NONE
//
///////////////////////////////////////////////////////////////////////////////
void FFTShift(vector<complex<double> > &a, vector< complex<double> > &y, int n, int k)
{
    // Compute the omega value
    complex<double> omega = cos(-2.0 * M_PI / n) + I * sin(-2.0 * M_PI / n);
    complex<double> omega_power = 1; // Set omega_power to 1
    complex<double> next_val = a[n + k - 1]; // Get next value in the shift

    // Loop through the vector y and compute the shift
    for (int i = 0; i < n; i++)
    {
        // Compute shift of the y[i] spot in the vector
        y[i] = (y[i] - a[k-1] + next_val) / omega_power;
        omega_power = omega_power * omega; // Compute the new omega_power
    }
}

///////////////////////////////////////////////////////////////////////////////
//
//	SetTotals
//
//	Author: Taylor Doell
//
//	Description: This function loops through n / 2 values and adds the absolute
//               values of y[i] into the total vector.
//
//	Parameters:
//		vector<double> &total - holds total values of absolute values of FFT
//      vector<double> y - holds points from FFT
//      int n - number of points in FFT
//
//	Returns:
//		NONE
//
///////////////////////////////////////////////////////////////////////////////
void SetTotals(vector<double> &total, vector< complex<double> > y, int n)
{
    int i;
    int n2 = n / 2; // Set variable for n / 2 values

    // Loop through n / 2 values to calculate totals
    for (i = 0; i < n2; i++)
    {
        // Add the absolute value of y[i] to the total[i] index
        total[i] += abs(y[i]);
    }
}

///////////////////////////////////////////////////////////////////////////////
//
//	DivideTotals
//
//	Author: Taylor Doell
//
//	Description: This function divides the total values by k once all the total
//               values have been computed to average out the total values.
//
//	Parameters:
//		vector<double> &total - holds total values of absolute values of FFT
//      int n - number of points in FFT
//      int k - number of iterations in FFT
//
//	Returns:
//		NONE
//
///////////////////////////////////////////////////////////////////////////////
void DivideTotals(vector<double> &total, int n, int k)
{
    int i;

    // Loop through total vector calculating the average total
    for (i = 0; i < n / 2; i++)
    {
        // Calculate the averaged total by dividing by k
        total[i] = total[i] / k;
    }
}

///////////////////////////////////////////////////////////////////////////////
//
//	fft
//
//	Author: Dr. Corwin and Dr. Logar
//
//	Description: This function calculates the FFT from the points given. This
//               code was taken from the class website and slightly modified to
//               compile on my machine. The changes made to the function was
//               taking out the direction, and using vectors instead of arrays.
//
//	Parameters:
//		vector<double> &a - holds the points for the FFT
//      int n - number of points in FFT
//      vector<double> &y - holds the values computed in FFT
//
//	Returns:
//		NONE
//
///////////////////////////////////////////////////////////////////////////////
void fft(vector<complex <double> > &a, int n, vector<complex <double> > &y)
  {
  vector<complex <double> > even(n);
  vector<complex <double> > even_fft(n);
  int      i;
  int      j;
  int      n2;
  vector<complex <double> > odd(n);
  vector<complex <double> > odd_fft(n);
  complex  <double> omega;
  complex  <double> omega_power;

  if (n == 1)
    {
    y[0] = a[0];
    return;
    }

  n2 = n / 2;
  j = 0;
  for (i = 0; i < n; i += 2)
    {
    even[j] = a[i];
    odd[j] = a[i + 1];
    j ++;
    }

  fft(even, n2, even_fft);
  fft(odd, n2, odd_fft);

  omega = cos(-2.0 * M_PI / n) + I * sin(-2.0 * M_PI / n);
  omega_power = 1;
  for (i = 0; i < n2; i ++)
    {
    y[i] = even_fft[i] + omega_power * odd_fft[i];
    y[i + n2] = even_fft[i] - omega_power * odd_fft[i];
    omega_power = omega * omega_power;
    }
  }
