# Minimum Distance Classifier
# Written by Taylor Doell and Elliott Rarden
# First programming assignment for CSC461 - Programming Languages
# for Dr. John Weiss
# Turned in on: 

import csv
import math
import operator
import random
import sys
import output

def calculateCentriods(dataset, numClasses):
    '''
    RETURN A LIST OF ALL CENTRIODS

    PARAM dataset:      The dataset to calculate the centriods for.  Note that it must
        be organized as [classID, attribute1Value, attribute2Value, ..., attributeNValue]
        with each row as a sample of data
    PARAM numClasses:   The number of classes in the dataset
    RETURN:             A list containing the centriods for each class, indexed as
        centriods[classID]
    '''
    centroids = []
    for classNumber in range(numClasses):
            # Reset list each time
            currentCentroid = []

            # Put all of the rows into current centirod list, but get rid of first column (class number)
            for row in dataset:
                if row[0] == classNumber:
                    currentCentroid.append(row[1:])

            # Get the columns with zip, sum, average, round to 1 decimal point
            currentCentroid = [(sum(x)/len(currentCentroid)) for x in zip(*currentCentroid)]

            # Store class number in first entry of list
            currentCentroid.insert(0, classNumber)

            centroids.append(currentCentroid)
    return centroids

def euclideanDistance(p, q, dim):
    '''
    RETURN THE EUCLIDAN DISTANCE BETWEEN TWO N-TUPLES

    PARAM p:   A tuple contaning a point, formatted (x-coord, y-coord, z-coord, etc.)
    PARAM q:   A tuple formatted (x-coord, y-coord, z-coord, etc.)
    PARAM dim: The dimension of both tuples
    RETURN:    The euclidian distance (vector) as a floating point value
    '''
    # Minimum distance is 0
    d = 0

    for x in range(1,dim):
        d += (p[x] - q[x]) ** 2 # Sum the Squares
    return math.sqrt(d) # Square Root of the sum of the squares

def getClosestClass(centroids, sample):
    '''
    RETURN THE CLOSEST CENTRIOD TO THE GIVEN SAMPLE

    PARAM centroids:    A list of centroids
    PARAM numClasses:   The sample to classify
    RETURN:             The centriod which sample is closest to.

    NOTE: We return the whole row, not just the classID
    '''
    distancesList = []

    # Find the distance to each centriod, then sort based on it
    for classID in range(len(centroids)):
        currentDistance = euclideanDistance(sample, centroids[classID], len(sample))
        distancesList.append((centroids[classID], currentDistance))
    distancesList.sort(key=operator.itemgetter(1))

    # Closest centriod's class
    return distancesList[0][0]

def getMinMax(dataset):
    '''
    RETURN THE MINIMUM AND MAXIMUM OF EACH ATTRIBUTE

    PARAM dataset:      The dataset to parse
    RETURN:             A list of attribute minimums and maximums

    NOTE: Return list format is [(attribute1min, attribute1max), (attribute2min, attribute2max), ...]
    '''
    maxList = []
    minList = []

    # Grab each column
    for column in zip(*dataset):
        maxList.append(column)
        minList.append(column)

    # Put maximums and minimums into list
    for i in range(len(maxList)):
        maxList[i] = max(maxList[i])
    for i in range(len(minList)):
        minList[i] = min(minList[i])

    # Assign tuples and return
    attributeMinMax = []
    for i in range(len(maxList)):
        pair = (minList[i], maxList[i])
        attributeMinMax.append(pair)
    return attributeMinMax

def normalizeFunciton(value, minimum, maximum):
    '''
    NORMALZE FUNCTION

    PARAM value:        The value to be noramlized
    PARAM minimum:      Minimum value to use in the funciton
    PARAM maximum:      Maximum value to use in the funciton
    RETURN:             (value - minimum) / (maximum - minimum)
    '''
    return (value - minimum) / (maximum - minimum)

def SampleClassifier(dataset, filename):
    '''
    CLASSIFY EACH ROW IN THE DATASET BY MINIMUM DISTANCE TO ANY CLASS' CENTRIOD

    PARAM dataset:      The dataset to classify
    RETURN:             none
    '''

    # Organize our class list and attribute names into their own lists
    classList = dataset.pop(0)
    attributeNames = dataset.pop(0)

    # We remove sample number from the first column
    for row in dataset:
        row.pop(0)

    # At the point, the dataset only contains values in the following format
    # [ classID, attribute1Value, attribute2Value, ..., attributeNValue ]

    # We go ahead and count the total number of members in each class right now
    classMembershipCount = countClassMembershpip(dataset)
    for row in dataset:
        classMembershipCount[row[0]] += 1
    numberCorrect = classMembershipCount[:]
    guesses = []

    # Now we can start looking at one sample at a time
    i = 0 # Index for current sample
    while i < len(dataset): #len(dataset):
        unknownSample = dataset.pop(i)

        # Get the min and max for each attribute, and save it to this list
        minMaxSet = getMinMax(dataset)

        # Take our training data, normalize it, and save it for use
        normalizedData = []
        for row in dataset:
            temp = []
            # Call the normalization function
            for column in range(1,len(row)):
                temp.append(normalizeFunciton(row[column], minMaxSet[column][0], minMaxSet[column][1]))
            temp.insert(0, row[0])
            normalizedData.append(temp)

        # Normalize our test sample
        normalizedSample = [unknownSample[0]]
        for column in range(1,len(unknownSample)):
            normalizedSample.append(normalizeFunciton(unknownSample[column], minMaxSet[column][0], minMaxSet[column][1]))

        # Compute the centriod for each class.  Move this into another function
        centriods = calculateCentriods(normalizedData, len(classMembershipCount))

        # Find the closest neighbor
        closestNeighbor = getClosestClass(centriods, normalizedSample)
        guessClass = closestNeighbor[0]
        guesses.append(guessClass)
        if unknownSample[0] != guessClass:
            numberCorrect[unknownSample[0]] -= 1

        # Increment the number of items we've processed, and insert it back into the list
        dataset.insert(i, unknownSample)
        i+=1

    output.outputToConsole(classList, dataset, classMembershipCount, numberCorrect)
    output.outputToFile(filename, classList, dataset, classMembershipCount, numberCorrect, guesses)

def countClassMembershpip(dataset):
    '''
        PREPARE THE LIST THAT WILL CONTAIN THE NUMBER OF MEMBERS OF EACH CLASS

        PARAM dataset:      The dataset to parse
        RETURN:             A list with indexed as follows
            numberOfMembersInClass = classMembers[classID]
    '''
    classMembers = []
    for row in dataset:
        # row[0] is the classID.  If we have
        if row[0] >= len(classMembers):
            classMembers.append(0)

    return classMembers

def read_csv(filename):
    '''
        RETURN A LIST OF ROWS WHERE EACH ROW IS A ROW IN THE CSV FILE

        PARAM filename: A .csv file to parse
        RETURN:         A list of rows where each row is a row in the csv file

        NOTE: All numerical values in the returned dataset are floating point values
        NOTE: Ignores strings
    '''
    # Open and read the file with the built in csv file
    with open(filename) as f:
        obj = csv.reader(f)
        dataset = list(obj)

    # Cast each entry into a float, ignore non-numerical values
    for i in range(0, len(dataset)):
        for j in range(0, len(dataset[i])):
            try:
                dataset[i][j] = float(dataset[i][j])
            except:
                pass

    # Sample Number and Class Number are integers
    for row in dataset[2:]:
        row[0] = int(row[0])
        row[1] = int(row[1])

    return dataset

if __name__ == "__main__":
    filename = sys.argv[1]
    dataset = read_csv(sys.argv[1])

    SampleClassifier(dataset, sys.argv[1])
