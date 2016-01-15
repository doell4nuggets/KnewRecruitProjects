def outputToConsole(classList, dataset, classMembershipCount, numberCorrect):
    '''
    This function outputs the database name, the list of classes and number of samples we classified correctly,
    the percentage, and also the list of samples indicating whether or not we classified the
    sample correctly to the console.

    PARAM classList:            List containing the the database name and class names.
    PARAM dataset:              List containing class number and attributes for each sample.
    PARAM classMembershipCount: Holds number of items per class.
    PARAM numberCorrect:        Holds the number of samples we classified correctly.
    RETURN:             none
    '''
    # Print the database name
    print(classList[0])

    classNames = []
    numClasses = 0

    # Go through each class printing the classification accuracy
    for i in classList[1:]:
        accuracy = round((100 * numberCorrect[int(i[0])]) / classMembershipCount[int(i[0])], 1)
        print("Class {0} ({1}): {2} samples, {3}% accuracy".format(
            i[0],
            i[(i.index('=')+1):],
            classMembershipCount[numClasses],
            accuracy), end='')
        print()
        numClasses += 1

    # Calculate total accuracy and output it to the console
    totalAccuracy = round((100 * sum(numberCorrect)) / len(dataset), 1)
    print("overall: {0} samples, {1}% accuracy".format(len(dataset), totalAccuracy))

def outputToFile(filename, classList, dataset, classMembershipCount, numberCorrect, guesses):
    '''
    This function outputs the database name, the list of classes and number of samples we
    classified correctly, the percentage, and also the list of samples indicating whether
    or not we classified the sample correctly to the output file.

    PARAM filename:             Initial filename to split up and create the output file name.
    PARAM classList:            List containing the the database name and class names.
    PARAM dataset:              List containing class number and attributes for each sample.
    PARAM classMembershipCount: Holds number of items per class.
    PARAM numberCorrect:        Holds the number of samples we classified correctly.
    PARAM guesses:              The guess class for each sample.
    RETURN:             none
    '''
    nameOfFile = filename.split(".")[0]
    nameOfFile += ".cv"

    cvFile = open(nameOfFile, "w+")

    # Print the database name out to the file
    cvFile.write(classList[0] + "\n")

    classNames = []
    numClasses = 0

    # Go through each class printing the classification accuracy
    for i in classList[1:]:
        accuracy = round((100 * numberCorrect[int(i[0])]) / classMembershipCount[int(i[0])], 1)
        outputString = "Class {0} ({1}): {2} samples, {3}% accuracy".format(
            i[0],
            i[(i.index('=')+1):],
            classMembershipCount[numClasses],
            accuracy)
        cvFile.write(outputString)
        cvFile.write("\n")
        numClasses += 1

    # Calculate total accuracy and output it to the file
    totalAccuracy = round((100 * sum(numberCorrect)) / len(dataset), 1)
    cvFile.write("overall: {0} samples, {1}% accuracy\n".format(len(dataset), totalAccuracy))

    # Go through each sample printing whether or not it was classified correctly
    cvFile.write("\nSample, Class, Prediction\n")
    for i in range(len(dataset)):
        outputString = "{0}, {1}, {2}".format(i+1, dataset[i][0], guesses[i])
        if dataset[i][0] != guesses[i]:
            outputString += "*"
        cvFile.write(outputString + "\n")
        outputString = ""

    cvFile.close()
