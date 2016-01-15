import math

def dist(p, q):
	'''
	RETURN THE EUCLIDAN DISTANCE BETWEEN TWO N-TUPLES

	PARAM p:   A tuple contaning a point, formatted (x-coord, y-coord, z-coord, etc.)
	PARAM q:   A tuple formatted (x-coord, y-coord, z-coord, etc.)
	PARAM dim: The dimension of both tuples
	RETURN:    If the computation cannot be performed, the following values are returned
		Dimension mismatch	: -1
		TypeError			: -2

	NOTE: Return value is a floating point value, the euclidian distance from p to q

	NOTE: This function assumes the x-coordiant is in the 0th place and y in the other

	NOTE: If you pass in a tuple with more than 2 dimensions, I will do the math
		on the first two values of the tuple and ignore the rest
	'''

	if isinstance(p, tuple):
		return -2
	if isinstance(q, tuple):
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

if __name__ == '__main__':
	x = (0)
	y = (1,2)
	print(dist(x, y))
