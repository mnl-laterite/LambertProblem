
package LambertProblem;

import java.util.Scanner;

/**
 * @author Emanuel Titerlea
 */
public class LambertProblem {
    
    private static final double day = 86400; //secunde

    private boolean isRunning;  
    private Solution solution;
    private LambertSolver solver; 
    private double[] R1; 
    private double[] R2;
    private double timeOfFlight;
    
    Scanner scan; 
    
    public LambertProblem() {
        
        solution = new Solution();
        R1 = new double[3];
        R2 = new double[3];
        scan = new Scanner(System.in);
    }
    
    private void printSolution() {
        
        solver.generateSolution(solution);
        double[] aux;
        
        System.out.println("Semiaxa majora a orbitei este "+ solution.getSemiMajorAxis() +"(km)");
        System.out.println("Excentricitatea orbitei este "+ solution.getEccentricity());
        
        aux = solution.getVelA();
        System.out.println("Componentele vitezei initiale sunt: ");
        System.out.println("VX="+aux[0] +"(km/s)");
        System.out.println("VY="+aux[1] +"(km/s)");
        System.out.println("VZ="+aux[2] +"(km/s)");
        
        aux = solution.getVelB();
        System.out.println("Componentele vitezei finale sunt: ");
        System.out.println("VX="+aux[0] +"(km/s)");
        System.out.println("VY="+aux[1] +"(km/s)");
        System.out.println("VZ="+aux[2] +"(km/s)");
         
    }

    private void start() {
        
        isRunning = true;
        int choice; 
        
        System.out.println("Programul va incerca sa rezolve numeric problema lui "
                + "Lambert in cazul eliptic.");
        System.out.println("Date de intrare: vectorii de pozitie R1 si R2, si "
                + "timpul scurs intre masuratori.");
        System.out.println("Unitatile de masura sunt km^3 pentru pozitie "
                + "si zile pentru timp.");
        
        System.out.println();
        System.out.println("Dati coordonatele lui R1 (in km)---");
        System.out.println("X=");
        R1[0] = scan.nextDouble();
        System.out.println("Y=");
        R1[1] = scan.nextDouble();
        System.out.println("Z=");
        R1[2] = scan.nextDouble();
            
        System.out.println("Dati coordonatele lui R2 (in km)---");
        System.out.println("X=");
        R2[0] = scan.nextDouble();
        System.out.println("Y=");
        R2[1] = scan.nextDouble();
        System.out.println("Z=");
        R2[2] = scan.nextDouble();
            
        System.out.println("Dati timpul scurs (in zile)");
        timeOfFlight = scan.nextDouble();
        
        solver = new LambertSolver(R1, R2, day*timeOfFlight);
        printSolution();
        
        if (solver.wasSuccessful()) 
            System.out.println("Succes!");
        else
            System.out.println("Ceva nu a mers.");
        
        System.out.println("Doriti sa dati alte valori? [1=da/0=nu]");
        choice = scan.nextInt();
        
        if (choice == 0) 
            isRunning = false;
        
        while (isRunning) {
            
            System.out.println();
            System.out.println("Dati alte coordonatele pentru R1 (in km)---");
            System.out.println("X=");
            R1[0] = scan.nextDouble();
            System.out.println("Y=");
            R1[1] = scan.nextDouble();
            System.out.println("Z=");
            R1[2] = scan.nextDouble();
            
            System.out.println("Dati alte coordonatele pentru R2 (in km)---");
            System.out.println("X=");
            R2[0] = scan.nextDouble();
            System.out.println("Y=");
            R2[1] = scan.nextDouble();
            System.out.println("Z=");
            R2[2] = scan.nextDouble();
            
            System.out.println("Dati timpul scurs (in zile)");
            timeOfFlight = scan.nextDouble();
            
            solver.newParameters(R1, R2, day*timeOfFlight);
            printSolution();
            
            if (solver.wasSuccessful()) 
                System.out.println("Succes!");
            else
                System.out.println("Ceva nu a mers.");
               
            System.out.println("Doriti sa dati alte valori? [1=da/0=nu]");
            choice =  scan.nextInt();
        
            if (choice == 0) 
                isRunning = false;
            
         }
    }
    
    public static void main(String[] args) {
        
        LambertProblem problem = new LambertProblem();
        problem.start();
        System.exit(0);
    }
}
