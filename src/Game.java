
import java.util.Random;
import java.util.Arrays;

public class Game {
    
    static Random rand = new Random() ;
        
    // See comment in Display
    static final int ROWS = 10;
    static final int COLS = 10;
    
    static final int EMPTY = -1;
    static final boolean HIT = true;
    
    
    // board can be a class by itself etc.
    // BUT, we'll keep it VERY simple with two 2D arrays.    
    // one for the board itself, and one to record hits/tries
    private int[][]  b;
    private boolean[][]  h;
    
    private Ship[] ships;
    private Player p;
    private int numOfTurns;
    
    public Game()
    {
        numOfTurns = 0;
        b = new int[ROWS][COLS];
        h = new boolean[ROWS][COLS];
        
        for (int rr=0; rr<ROWS; ++rr)                     // fill it with empty
            for (int cc=0; cc<COLS; ++cc)
            {
                b[rr][cc]=EMPTY;
                h[rr][cc]=!HIT;
            }   
        p = new Player(ROWS, COLS);        
        
        populateBoard(b);
        
        Ship.resetId();
    }
    
    
    
    public void play(boolean loopMode)
    {
        
        while(!done() ){
            numOfTurns++ ;
            p.planNextMove();
            int r = p.getMoveR();
            int c = p.getMoveC();

            h[r][c] = HIT;
            
            
            boolean hitSomething ;
            hitSomething = (b[r][c] == EMPTY) ? false : true;                     
            p.hitSomething(r,c,hitSomething);

            Ship s = getShipById(b[r][c]);
            if (s!=null)
                s.updateHit(r, c);
            
            boolean sunkAShip ;
            sunkAShip = (s==null) ? false : s.isSunk();
            p.sunkAShip(r,c,sunkAShip);

            
            //printBoard();
            if (!loopMode)
                break;
        }
    }
    
    private boolean done()
    {
        for (int rr=0; rr<ROWS; ++rr)                     // fill it with empty
            for (int cc=0; cc<COLS; ++cc)
                if (b[rr][cc] !=EMPTY && h[rr][cc] !=HIT)
                    return false;

        return true;
    }
    public int getTurns()
    {
        return numOfTurns;
    }
    
    private void populateBoard(int[][] b)
    {
        ships = new Ship[5];
        // Place a Patrol
        ships[0] = placeAShip(2,"Patrol");  // Patrol: Length 2
        ships[1] = placeAShip(3,"Destroyer");  // Destroyer: Length 3
        ships[2] = placeAShip(3,"Submarine");  // Sub: Length 3
        ships[3] = placeAShip(4,"Battleship");  // Battleship: Length 4
        ships[4] = placeAShip(5,"Carrier");  // Carrier: Length 5        
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

    private Ship getShipById(int id)
    {
        for (int ii=0; ii<ships.length ; ++ii)
            if (ships[ii].getId() == id)
                return ships[ii];
        
        return null;
    }
    
    public boolean isCellShip(int r, int c)
    {
        return (b[r][c] != EMPTY );
    }
    
    public boolean isShipSunk(int r, int c)
    {
        Ship s = getShipById(b[r][c]);
        if (s==null)
            return false;
        else
            return s.isSunk();
    }

    public boolean isCellShipTop(int r, int c)
    {
        Ship s = getShipById(b[r][c]);
        if (s==null)
            return false;
        else
            return s.isShipTop(r, c);
    }
    public boolean isCellShipBottom(int r, int c)
    {
        Ship s = getShipById(b[r][c]);
        if (s==null)
            return false;
        else
            return s.isShipBottom(r, c);
    }
    public boolean isCellShipLeft(int r, int c)
    {
        Ship s = getShipById(b[r][c]);
        if (s==null)
            return false;
        else
            return s.isShipLeft(r, c);
    }
    public boolean isCellShipRight(int r, int c)
    {
        Ship s = getShipById(b[r][c]);
        if (s==null)
            return false;
        else
            return s.isShipRight(r, c);
    }
    public boolean isCellHit(int r, int c)
    {
        return (h[r][c] == HIT);
    }
    
    

    
    public void printBoard()
    {
        System.out.println("Printing board!!");
        for (int rr=0; rr<ROWS; ++rr)
        {
            for (int cc=0; cc<COLS; ++cc)
            {
                System.out.printf("%2d ", b[rr][cc]);
            }
            System.out.println();
        }
        
    }
    
}
