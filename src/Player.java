/**
 * Order of calls to player strategy functions:

* In game constructor:
-->    p = new Player(ROWS, COLS);        
    
Then, for each round:    
    
            numOfTurns++ ;
-->            p.planNextMove();
-->            int r = p.getMoveR();
-->            int c = p.getMoveC();

            boolean hitSomething ;
            hitSomething = (b[r][c] == EMPTY) ? false : true;                     
-->            p.hitSomething(r,c,hitSomething);

            boolean sunkAShip ;
            Ship s = getShipById(b[r][c]);
            sunkAShip = (s==null) ? false : s.isSunk();
-->            p.sunkAShip(r,c,sunkAShip);

   
 */


import java.util.Random;

public class Player {

    static Random rand = new Random();
    int rows, cols;
    int rtry, ctry;

    public Player(int r, int c) {
        rows = r;
        cols = c;
        
        // for strategy
        rtry = 0;
        ctry = 0; 
    }

    public void planNextMove() {

    }

    public int getMoveR() {
        // Random strategy
        // return (int) rand.nextInt(rows);
        
        // Sequence strategy
        return rtry;
    }

    public int getMoveC() {
        // Random strategy
        // return (int) rand.nextInt(cols);
        
        // Sequence strategy
        int c = ctry;
        ctry++;
        if (ctry>=cols)
        {
            ctry =0;
            rtry++;
        }
        return c;
    }

    public void hitSomething (int r, int c, boolean h)
    {
        
    }

    public void sunkAShip (int r, int c, boolean s)
    {
        
    }

}
