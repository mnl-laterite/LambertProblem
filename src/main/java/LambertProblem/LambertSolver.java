/*
 * Dă o aproximare numerică a problemei lui Lambert pentru corpuri 
 * sub influenta gravitationala a Pamantului. 
 * Algmoritmul de fata se limiteaza la orbite eliptice si nu ia in considerare
 * directia miscarii (calculeaza mereu solutia drumului scurt) sau revolutii 
 * multiple.
 */
package LambertProblem;

import java.lang.Math;

/**
 * @author Emanuel Titerlea
 */
class LambertSolver {
    
    private static final double epsilon = 1e-5; //precizia de aproximare dorita
    //constanta gravitationala planetara geocentrica (in km^3/s^-2)
    private static final double GM = 398600.4418;
    private boolean success = true;
    
    private double[] posA; //prima pozitie masurata (fata de baricentru)
    private double[] posB; //a doua pozitie masurata (fata de baricentru)
    private double distA; //distanta fata de baricentru la prima masuratoare
    private double distB; //distanta fata de baricentru la a doua masuratoare
    private double theta; //unghiul dintre posA si posB (0 < theta < pi)
    private double timeOfFlight; //timpul scurs intre masuratori
    private double distC; //distanta dintre prima si a doua pozitie (c)
    private double distSum; // semiperimetrul triunghiului dat de masuratori (s)
    
    private double Qx; //Q(x,rho)- Functia de aproximat
    private double dQx; // dQ/dx
    private double x; //parametrul liber al lui Q
    private double Q; //valoarea cautata a lui Q
    private double rho; //parametru constant al lui Q
    
    /**
     * variabile auxiliare folosite pentru a genera aproximari ale lui Q(x)
     */
    
        double E;
        double y;
        double z;
        double f;
        double g;
        double h;
        double l;
        
    LambertSolver (double[] r1, double[] r2, double timeOfFlight) {
        
        posA = r1;
        posB = r2;
        this.timeOfFlight = timeOfFlight; 
        
        distA = Math.sqrt( dot(posA,posA) );
        distB = Math.sqrt( dot(posB,posB) );
        
        /**
         * calculeaza valorile pentru unghiul dintre posA si posB 
         * si pentru c, distanta dintre corp la prima si respectiv a doua 
         * masuratoare
         */
        theta = Math.acos( Math.max(-1, Math.min(1, dot(posA,posB)/distA/distB)));
        distC = Math.sqrt(distA*distA + distB*distB - 2*distA*distB*Math.cos(theta));
        distSum = (distA + distB + distC)/2; 
        
        Q = Math.sqrt(8*GM/distSum)*timeOfFlight/distSum; 
        rho = Math.sqrt(distA*distB)/distSum*Math.cos(theta/2);
        
        solve();
        
    }
    
    /**
     * Caută x a.i. Qx = Q folosind metoda iterativa Newton-Raphson 
     */
    private void solve() {
        
        int iterations = 0;

        x = 0;
        getNewAprox(0); //aproximarea initiala
        
        while ( Double.compare(Math.abs(Qx - Q), epsilon) > 0) {
            
            ++iterations;
    
            x = x -  (Qx - Q)/dQx;
            getNewAprox(x);
             
            if (iterations > 100) {
                
                success = false;   //Qx converge prea incet (nu exista solutie?)
                break;
            }
        }
        
    }
    
    /**
     * Genereaza o aproximare numerica pentru Q(x) si dQ/dx in x
     */
    private void getNewAprox(double x) {
        
        //eliminam cazuri imposibile ce pot aparea din cauza erorilor numerice
        if (Double.compare(x, -1) < 0) 
            x = Math.abs(x) - 2;
        if (Double.compare(x, -1) == 0) 
            x += epsilon;
        
        /**
         * calculeaza variabilele auxiliare 
         * pentru a gasi aproximari ale lui Qx si dQx
         */
        
        E = x*x - 1;
        y = Math.sqrt(Math.abs(E));
        z = Math.sqrt(1+rho*rho*E);
        f = y*(z - rho*x);
        g = x*z - rho*E;
        h = y*(x - rho*z);
        l = Math.atan2(f,g);
        
        Qx = 2*(l - h)/y/y/y;
        dQx = (4 - 4*rho*rho*rho*x/z - 3*x*Q)/E; 
    }
    
    /**
     * reinitializeaza problema
     */
    
    void newParameters(double[] r1, double[] r2, double timeOfFlight) {
        
        success = true;
        
        posA = r1;
        posB = r2;
        this.timeOfFlight = timeOfFlight; 
        
        distA = Math.sqrt(dot(posA,posA));
        distB = Math.sqrt(dot(posB,posB));
        theta = Math.acos(Math.max(-1, Math.min(1, dot(posA,posB)/distA/distB)));
        distC = Math.sqrt(distA*distA + distB*distB - 2*distA*distB*Math.cos(theta));
        distSum = (distA + distB + distC)/2;
        
        Q = Math.sqrt(8*GM/distSum/distSum/distSum)*timeOfFlight; 
        rho = Math.sqrt(distA*distB)/distSum*Math.cos(theta/2);
        
        solve();
    }
    
    boolean wasSuccessful() {
        
        return success;
    }
    
    /**
     * In functie de ultima aprox. a lui x 
     * genereaza semiaxa majora (a), excentricitatea (e) 
     * si vitezele de la cele doua momente de timp. Vitezele sunt calculate
     * mai intai in componente (paralela si perpendiculara cu 
     * vectorul de pozitie). 
     */
    
    void generateSolution(Solution solution) {
        
        double[] vA = new double[3]; //viteza corpului la prima pozitie
        double[] vB = new double[3]; //viteza corpului la a doua pozitie
        
        /**
         * vector auxiliar folosit pentru a determina componentele perpendicula-
         * (cu pozitiile respective) ale vitezelor
         */
        double[] aux = new double[3]; 
        double m_aux; 
        
        double[] vAtang = new double[3]; //componenta perpendiculara pe posA
        double[] vBtang = new double[3]; // ...idem... posB
        double m_vAtang; //modulul componentei perpendiculare
        double m_vBtang; //...
        
        double m_vAradial; //viteza (scalara) de-alungul vectorului de pozitie
        double m_vBradial;
        
        
        double[] posAnorm = new double[3]; //vectorii de pozitie normalizati
        double[] posBnorm = new double[3]; 
        
        cross(posA, posB, aux); //vrem aux perpendicular pe planul orbital
        m_aux = Math.sqrt(dot(aux,aux));
        
        //normalizam vectorii
        for (int i=0;i<3;++i) {
            posAnorm[i] = posA[i]/distA;
            posBnorm[i] = posB[i]/distB;
            aux[i] /= m_aux;
        }
        
  
        cross(aux, posAnorm, vAtang);
        cross(aux, posBnorm, vBtang);   
        
        m_vAradial = Math.sqrt(2*GM*distSum)*(rho*z*(distSum - distA) - 
                                               x*(distSum - distB))/distC/distA;
        m_vBradial = Math.sqrt(2*GM*distSum)*(x*(distSum - distA) 
                                           - rho*z*(distSum - distB))/distC/distB;
        
        double a = distSum/2/(1 - x*x); 
        double e = (1 - distA/a)*(1 - distA/a) + (distA*m_vAradial)*(distA*m_vAradial)/GM/a;
        double p = a*(1-e); 
        
        m_vAtang = Math.sqrt(p*GM)/distA;
        m_vBtang = Math.sqrt(p*GM)/distB;
        
        for (int i=0;i<3;++i) {
            
            vA[i] = m_vAtang*vAtang[i] + m_vAradial*posAnorm[i];
            vB[i] = m_vBtang*vBtang[i] + m_vBradial*posBnorm[i];
        }
        
        e = Math.sqrt(e);
        solution.setSolution(a, e, vA, vB);
                
    }
    
    /**
     * metoda pentru calcularea produsului vectorial w = u x v
     */
    private void cross (double[] u, double[] v, double[] w) {
        
        w[0] = u[1]*v[2] - u[2]*v[1];
        w[1] = u[2]*v[0] - u[0]*v[2];
        w[2] = u[0]*v[1] - u[1]*v[0];
    }
    
    /**
     * metoda pentru calcularea produsului scalar u.v
     */
    private double dot(double[] u, double[] v) {
        
        return u[0]*v[0] + u[1]*v[1] + u[2]*v[2];
    }
    
}
