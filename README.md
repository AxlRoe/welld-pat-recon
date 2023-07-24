In order to understand which point lay on the same line, I consider the slope between points
calculated as a tuple in this way: (y2-y1, x2-x1) and not as the ratio (y2-y1)/(x2-x1), in order to
avoid potential precision problems that could come up from the ratio.

I keep track of the inserted points in a list, that will represent the space. Every time user inserts a point,
I will calculate the slope between the inserted point and the already inserted ones and I will keep track of these
slopes in a map with multiple values like this: <slope, <list of points having the same slope>>
Each entry in the map represent a line that pass through N points

The codebase is organized in the following way:
- domain: this package contains the logic to compute the lines between points and a value object: the point that contains the logic to calculate the slope between points
- dto: is the point inserted by user
- infra: contains the rest controller

I have made the following assumptions:
- rest call /lines/{n}: N must be greater than one, since for one point pass a infinite number of lines
- coordinates of points must integer, in order to avoid to handle numeric precision problems
- point are inserted one at a time, not in bulk mode

requirements:
- java: 11
- maven: 3

Build application
run command mvn install

Run the application
run command java -jar welld-1.0.jar
The application listen on port 8888



