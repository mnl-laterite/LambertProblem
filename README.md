# LambertProblem
A short console application that calculates the elements of an orbit of a body in Earth's gravity well from two position measurements of the orbiting body (in baricentric coordinates). The program instructions are in Romanian.

# How to build and run

- Clone the repository.
- Run `mvn package`.
- Run `java -jar .\target\LambertProblem-1.0-SNAPSHOT.jar`
- Input the coordinates of the measurements (from `https://ssd.jpl.nasa.gov/horizons.cgi`) when prompted 
- On `https://ssd.jpl.nasa.gov/horizons.cgi` selected ephemeris type `vector table`, Observer Location `Geocentric`, and as target body the moon or some other artificial satelite of interest
- The program will then approximate the elements of the orbit of the body and display them in the console
