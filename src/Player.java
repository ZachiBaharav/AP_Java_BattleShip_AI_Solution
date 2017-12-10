
/**
 * Order of calls to player strategy functions:
 *
 * In game constructor:
 * -->    p = new Player(ROWS, COLS);  *
 * Then, for each round:  *
 * numOfTurns++ ;
 * -->            p.planNextMove();
 * -->            int r = p.getMoveR();
 * -->            int c = p.getMoveC();
 *
 * boolean hitSomething ;
 * hitSomething = (b[r][c] == EMPTY) ? false : true;
 * -->            p.hitSomething(r,c,hitSomething);
 *
 * boolean sunkAShip ;
 * Ship s = getShipById(b[r][c]);
 * sunkAShip = (s==null) ? false : s.isSunk();
 * -->            p.sunkAShip(r,c,sunkAShip);
 *
 *
 */
import java.util.Random;


public class Player {

    static Random rand = new Random();
    int rows, cols;
    int rtry, ctry;

    boolean DEBUG_MODE = false;
    
    public enum Status {EMPTY, HIT, MISS, SUNK};
    
    Status[][] gameBoard;  //0:nothing , 1:hit 2:shipSunk -1:miss

    
    private class PlayerShip{
        public int l;
        public boolean sunk;
    }

    PlayerShip[] s ;
    
    public Player(int r, int c) {
        rows = r;
        cols = c;

        // for strategy
        rtry = 0;
        ctry = 0;

        gameBoard = new Status[rows][cols];
        for (int rr = 0; rr < rows; ++rr) {
            for (int cc = 0; cc < cols; ++cc) {
                gameBoard[rr][cc] = Status.EMPTY ;
            }
        }

        /*
        ships = new Ship[5];
        // Place a Patrol
        ships[0] = placeAShip(2,"Patrol");  // Patrol: Length 2
        ships[1] = placeAShip(3,"Destroyer");  // Destroyer: Length 3
        ships[2] = placeAShip(3,"Submarine");  // Sub: Length 3
        ships[3] = placeAShip(4,"Battleship");  // Battleship: Length 4
        ships[4] = placeAShip(5,"Carrier");  // Carrier: Length 5          
         */
        s = new PlayerShip[5];
        
        s[0] = new PlayerShip(); s[0].l=2; s[0].sunk=false;
        s[1] = new PlayerShip(); s[1].l=3; s[1].sunk=false;
        s[2] = new PlayerShip(); s[2].l=3; s[2].sunk=false;
        s[3] = new PlayerShip(); s[3].l=4; s[3].sunk=false;
        s[4] = new PlayerShip(); s[4].l=5; s[4].sunk=false;


    }

    public void planNextMove() {

        if(DEBUG_MODE) System.out.println("in Plan next move");

        int[][] p = new int[rows][cols];
        buildProbabilityMap(gameBoard, p);

        printProbMap("planNextMove built probability map:",p);
        
        double maxp = 0;
        for (int ii = 0; ii < rows; ++ii) {
            for (int jj = 0; jj < cols; ++jj) {
                if (p[ii][jj] > maxp) {
                    maxp = p[ii][jj];
                    rtry = ii;
                    ctry = jj;
                }
            }
        }

        if(DEBUG_MODE) System.out.println("Guessed: (r,c)=("+rtry +","+ctry+")");
        // the ones we are guessing are rtry and ctry
        /*
        while (gameBoard[rtry][ctry] != 0) {
            rtry = (int) rand.nextInt(rows);
            ctry = (int) rand.nextInt(cols);
        }
        */
    }

    public void buildProbabilityMap(Status[][] gameBoard, int[][] p) {
        for (int ii = 0; ii < rows; ++ii) {
            for (int jj = 0; jj < cols; ++jj) {
                p[ii][jj] = 0;
            }
        }

        boolean hitMode = false;
        // Determine if we are in 'hit' mode
        for (int rr = 0; rr < rows; rr++) {
            for (int cc = 0; cc < cols; cc++) {
                if (gameBoard[rr][cc] == Status.HIT) {
                    hitMode = true;
                }
            }
        }
        if(DEBUG_MODE) System.out.println("hitMode= " + hitMode);
        // loop over ships
        for (int ss = 0; ss < 5; ++ss) { // 5 ship types
         
            if (s[ss].sunk) continue;
            int l = s[ss].l;
            
            for (int rr = 0; rr < rows; rr++) {
                for (int cc = 0; cc < cols; cc++) {
                    for (int dd = 0; dd < 2; ++dd) // 0-down, 1-right
                    {
                        if (canPlaceShip(rr, cc, l, dd)) {
                            if (!hitMode || (hitMode && containsHit(rr,cc,l,dd)))
                                updateProbMap(p, hitMode, rr, cc, l, dd);
                        }
                    }
                }
            }

        }
    }

    private boolean canPlaceShip(int r, int c, int l, int d) {
        boolean free = false;
        if (d == 0) // down
        {
            if (r + l - 1 < rows) {
                free = true;
                for (int rr = r; rr < r + l; ++rr) {
                    if (gameBoard[rr][c] == Status.MISS || gameBoard[rr][c] == Status.SUNK) {
                        free = false;
                    }
                }
            }
        }
        if (d == 1) // right
        {
            if (c + l - 1 < cols) {
                free = true;
                for (int cc = c; cc < c + l; ++cc) {
                    if (gameBoard[r][cc] == Status.MISS || gameBoard[r][cc] == Status.SUNK) {
                        free = false;
                    }
                }
            }
        }
        return free;

    }

    private boolean containsHit(int r, int c, int l, int d){
        if (d == 0) // down
        {
            for (int rr = r; rr < r + l; ++rr) {
                if (gameBoard[rr][c] == Status.HIT ) {
                    return true;
                }
            }
        }
        if (d == 1) // right
        {
            for (int cc = c; cc < c + l; ++cc) {
                if (gameBoard[r][cc] == Status.HIT ) {
                    return true;
                }
            }
        }
        return false;

    }
    
    private void updateProbMap(int[][] p, boolean hitMode, int r, int c, int l, int d) {

        if(DEBUG_MODE) System.out.print("In UpdateProbMap:");
        if(DEBUG_MODE) System.out.println("r,c=" +r +" ," + c + " ; l=" + l + "  , d=" + d + "  ; hitMode=" + hitMode);

        if (hitMode) {

            
            if (d == 0) // down
            {
                int w=0; // Weight of support for a ship            
                for (int rr = r; rr < r + l; ++rr) 
                    if (gameBoard[rr][c] == Status.HIT)
                        w++;
                for (int rr = r; rr < r + l; ++rr)
                    if (gameBoard[rr][c] == Status.EMPTY)
                            p[rr][c]+=w;
            }
            if (d == 1) // right
            {
                int w=0; // Weight of support for a ship            
                for (int cc = c; cc < c + l; ++cc) 
                    if (gameBoard[r][cc] == Status.HIT)
                        w++;
                for (int cc = c; cc < c + l; ++cc) {
                    if (gameBoard[r][cc] == Status.EMPTY)
                            p[r][cc]+=w;
                }
            }
        }
        if (!hitMode) {

            if (d == 0) // down
            {
                for (int rr = r; rr < r + l; ++rr) {
                    if (gameBoard[rr][c] == Status.EMPTY) {
                        p[rr][c]++;
                    }
                }
            }
            if (d == 1) // right
            {
                for (int cc = c; cc < c + l; ++cc) {
                    if (gameBoard[r][cc] == Status.EMPTY) {
                        p[r][cc]++;
                    }
                }
            }
            
        }
    }

    public void printProbMap(String s, int[][] p) {
        if(!DEBUG_MODE) return; 
        System.out.println(s);
        for (int rr=0; rr<rows; ++rr) {
            for (int cc=0; cc<cols; ++cc) {
                System.out.format( "%2d | ",p[rr][cc]);
            }
            System.out.println();
        }
    }

    //************************
    /*      
    private void populateBoard(int[][] b)
    {
        
    }

    private Ship placeAShip(int l, String str)
    {
        int r,c,d;
        do{
            r = (int) rand.nextInt(ROWS);   // nextInt gets integer values between [0,n)
            c = (int) rand.nextInt(COLS);
            d = (int) rand.nextInt(2);      // 0-down, 1-right
        } while (!isFreeSpace(r,c,l,d));
        
        Ship s = new Ship(str,l);
        putOnBoard(s,r,c,d);
        return s;
    }
    
    private void putOnBoard(Ship s, int r, int c, int d)
    {
        int id = s.getId();
        int l = s.getL();
        int[] x,y;
        
        x=new int[l];
        y=new int[l];
        
        if (d==0)  // down
        {
            for (int rr=r; rr<r+l; ++rr)
            {
                b[rr][c] = id ;
                y[rr-r]=rr;
                x[rr-r]=c;
            }
        }        
        if (d==1) // right
        {
            for (int cc=c; cc<c+l; ++cc)
            {
                b[r][cc] =id ;
                x[cc-c]=cc;
                y[cc-c]=r;
            }
        }   
        
        s.setCoord(y,x);
        
    }
        
    private boolean isFreeSpace(int r, int c, int l, int d)
    {
        boolean free=false;
        if (d==0)  // down
        {
            if (r+l-1<ROWS)
            {
                free=true;
                for (int rr=r; rr<r+l; ++rr)
                    if (b[rr][c] != EMPTY )
                        free=false;
            }
        }        
        if (d==1) // right
        {
            if (c+l-1<COLS)
            {
                free=true;
                for (int cc=c; cc<c+l; ++cc)
                    if (b[r][cc] != EMPTY )
                        free=false;
            }
        }        
        return free;
                
    }
     */
    //************************
    public int getMoveR() {
        // Random strategy
        //return (int) rand.nextInt(rows);
        return rtry;

        // Sequence strategy
        //return rtry;
    }

    public int getMoveC() {
        // Random strategy
        // return (int) rand.nextInt(cols);
        return ctry;

        /*
        // Sequence strategy
        int c = ctry;
        ctry++;
        if (ctry>=cols)
        {
            ctry =0;
            rtry++;
        }
        return c;
         */
    }

    public void hitSomething(int r, int c, boolean h) {
        if (h) {
            gameBoard[r][c] = Status.HIT;
        } else {
            gameBoard[r][c] = Status.MISS;
        }
    }

    public void sunkAShip(int r, int c, boolean s, int[][] coords) {
        if (s) {
            for (int ii = 0; ii < coords.length; ++ii) {
                int rr = coords[ii][0];
                int cc = coords[ii][1];

                gameBoard[rr][cc] = Status.SUNK;
            }
            // Make ship unactive!
            
            
        }
    }

}
