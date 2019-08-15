
package LambertProblem;

/**
 * @author Emanuel Titerlea
 */
public class Orbit {
    
    private double semiMajorAxis;
    private double eccentricity;
    private double focalParameter;
    
    public Orbit() {
        
    }
    
    public Orbit(double a, double e) {
        
        semiMajorAxis = a;
        eccentricity = e;
        focalParameter = a*(1 - e*e);
    }
    
    public double getSemiMajorAxis() {
        
        return semiMajorAxis; 
    }
    
    public double getEccentricity() {
        
        return eccentricity;
    } 
    
    public void setSemiMajorAxis (double a) {
        
        semiMajorAxis = a;
    }
    
    public void setEccentricity (double e) {

        eccentricity = e;
    }
 }
