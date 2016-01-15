import operator

class DataClassifier:
	def __init__( self ):
		self.__normalizedDictionary__ = {}
		self.__minMaxData__ = {}
		self.__count__ = 0

	def SetMinMax(self, dataset, columns):
		listOfAttributes = []
		for column in range(2, columns):
			listOfAttributes = []
			listOfAttributes.append(self.colToList(dataset, column))
			self.MinMax(listOfAttributes, column)

	def colToList(self, dataset):
		listToReturn = []
		for row in dataset[2:]:
			listToReturn.append(row[column])

		return listToReturn

	def MinMax(self, listOfAttributes):
		minValue = listOfAttributes[0]
		maxValue = listOfAttributes[0]

		for value in listOfAttributes:
			if value < minValue:
				minValue = value
			if value > maxValue:
				maxValue = value

		return (minValue, maxValue)

	def dist(self, p, q):
	    '''
	    RETURN THE EUCLIDAN DISTANCE BETWEEN TWO N-TUPLES

		PARAM self: reference to self object
	    PARAM p:   	A tuple contaning a point, formatted (x-coord, y-coord, z-coord, etc.)
	    PARAM q:   	A tuple formatted (x-coord, y-coord, z-coord, etc.)
	    PARAM dim: 	The dimension of both tuples
	    RETURN:    	If the computation cannot be performed, the following values are returned
	        Dimension mismatch  : -1
	        TypeError           : -2

	    NOTE: Return value is a floating point value, the euclidian distance from p to q

	    NOTE: This function assumes the x-coordiant is in the 0th place and y in the other

	    NOTE: If you pass in a tuple with more than 2 dimensions, I will do the math
	        on the first two values of the tuple and ignore the rest
	    '''

	    if not isinstance(p, tuple):
	        return -2
	    if not isinstance(q, tuple):
	        return -2

	    if len(p) != len(q):
	        return -1

	    d = 0
	    for i in range(0,len(y)):
	        try:
	            d = ((p[i] - q[i]) ** 2) + d
	        except TypeError:
	            return -2

	    d = math.sqrt(d)

	    return d

	def FindClosestClass(self, samplePoint, centriodDictionary):
		listOfDistances = []
		centriodList = list(centriodDictionary.items())
		closestClass = centriodList[0][0]
		for i in centriodList:
			if self.dist(i[1], samplePoint) < self.dist(centriodDictionary[closestClass], samplePoint):
				closestClass = i[0]

		return closestClass

	def PrintDictionary(self):
		for value in self.__minMaxData__:
			print("{1}".format(value, self.__minMaxData__[value]))

		# for value in self.__normalizedDictionary__:
		# 	print("{0}: {1}".format(value, self.__normalizedDictionary__[value]))

	def ResetClassifier(self):
		self.__normalizedDictionary__ = {}
		self.__minMaxData__ = {}
