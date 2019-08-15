/**
 * clasa pentru stocarea si manipularea solutiei problemei
 */
package LambertProblem;

/**
 * @author Emanuel Titerlea
 */
public class Solution extends Orbit {
    
    private double[] vel1;
    private double[] vel2; 
    
    public Solution() {
        
        vel1 = new double[3];
        vel2 = new double[3];
    }
    
    public void setSolution(double a, double e, double[] v1, double[] v2) {
        
        super.setSemiMajorAxis(a);
        super.setEccentricity(e);
        
        for (int i=0;i<3;++i) {
            vel1[i] = v1[i];
            vel2[i] = v2[i];
        }
    }
     
    public double[] getVelA() {
        
        return vel1;
    }
    
    public double[] getVelB() {
        
        return vel2;
    }
    
}
