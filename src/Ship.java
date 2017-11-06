

public class Ship {

    private String name;
    private int[] rows, cols;
    int l ;
    private boolean[] hit;
    private boolean sunk=false;
    private int id;  // unique ID
    private static int nextId = 0;
    
    public Ship(String str, int l)
    {
        name = str;
        this.l = l;
        id = nextId++;
    }
    
    public static void resetId()
    {
        nextId=0;
    }
    /**
     * Assumption: Coordinates are ordered left to right and top to bottom 
     * @param r
     * @param c 
     */
    public void setCoord(int[] r, int[] c)
    {
        rows = new int[l];
        cols = new int[l];
        hit  = new boolean[l];
        for (int ii=0; ii<l ; ++ii)
        {
            rows[ii]=r[ii];
            cols[ii]=c[ii];
            hit[ii] = false;
        }
    }
    
    public void updateHit(int r, int c)
    {
        for (int ii=0; ii<l ; ++ii)
        {
            if (rows[ii]==r && cols[ii]==c)
            {
                hit[ii]=true;
                break;
            }
        }
        
        sunk = true;
        for (int ii=0; ii<l ; ++ii)
        {
          if (!hit[ii])
          {
              sunk = false;
              break;
          }
        }
    }
    
    
    public boolean isSunk()
    {
            return sunk; 
    }
    
    public int getId()
    {
        return id;
    }
    
    
    public int getL()
    {
        return l;
    }    
    
    public boolean isShipTop(int r, int c)
    {
        if (cols[0] == cols[1] && cols[0]==c && rows[0]==r)
            return true;
        else
            return false;
    }

    public boolean isShipBottom(int r, int c)
    {
        if (cols[0] == cols[1] && cols[0]==c && rows[l-1]==r)
            return true;
        else
            return false;
    }
    
    public boolean isShipLeft(int r, int c)
    {
        if (rows[0] == rows[1] && rows[0]==r && cols[0]==c)
            return true;
        else
            return false;
    }

    public boolean isShipRight(int r, int c)
    {
        if (rows[0] == rows[1] && rows[0]==r && cols[l-1]==c)
            return true;
        else
            return false;
    }

}
