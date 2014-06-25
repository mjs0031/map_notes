  /**
   Matthew J Swann
   Version 1.0, 2010-10-27
  */
  
  
   public class KMap{
   
      int n;                //Number of variables for the map.
      int rows, columns;
      KSquare KGrid[][];    //Grid of KSquares serving as the map.
      int incrementalArray[];
   
   
   /**
    Constructor to be used with parameters from user input.
   
    @param n The number of variables as specified by user input.
   */
      public KMap(int n){
         this.n = n;
         rows = 0;
         columns = 0;
      }
      
   	
   /**
    Plots a grid for the map of KSquares based upon the 
    number of variables passed through the constructor. 
    If the number of variables is even, the rows and columns
    are exponential factors of two (2) to the half of the
    total number of variables. In the event that the number
    of variables is odd, the rows are equivalent to two (2)
    raised to the rounded down power of half the variables,
    while the columns are equivalent to two (2) raised to the
    rounded up power of half the variables.
   */
   
      public void establishGrid(){
         if(n%2.0 == 0){
            rows = (int)Math.pow(2, n/2);
            columns = (int)Math.pow(2, n/2);
         }
         else{
            rows = (int)Math.pow(2, (n/2));
            columns = (int)Math.pow(2, (n/2)+1);
         }
         KGrid = new KSquare[rows][columns];
         
      	//Population with blank KSqaures.
         KSquare newSquare = new KSquare();
         for(int i = 0; i <= rows - 1; i++){
            for(int j = 0; j <= columns - 1; j++){
               KGrid[i][j] = newSquare;
            }
         }
      }
   
   
   /**
    Drafts a temporary array of integer values that will be
    used to increment the left-most column of the grid and 
    the top-most row of the grid. This method utilizes three
    looping mechanisms.
   
    This incremental array is essential to the organic nature
    of the KMap as it holds the pattern for the incremental
    growth of factors that serve as the base of the grid.
   
    The first loops fills the array with negative integeres
    that are used as a marker for unchanged index values.
    The later loops will use division to evaluate proper
    placement of the powers of two. A negative value protects
    against possible null reference errors, exceptions when
    dividing by zero, and allows for easy transferance of the
    algorithm to other programming languages.
   
    The second loop divides each value at each index
    by an incremental variable, j, only when the value
    matches that of a previously unchanged value, -1.
    The replacement of each value occurs only when the
    value is both unchange and divides with a remainder
    or zero by the variable i, which decrements by powers
    of two. This protects against conflicts where a value
    is divisible by more than one power of two.
   
    The third loop inverts the sign of each integer around
    each power of two above zero. The most simplistic way
    of working this problem is to count the occurance of 
    each variable and to make the cummulatively even occurances
    a negative.
   */
   
      public void establishIncrementalArray(){
         incrementalArray = new int[columns-1];
         
      	//Base filling loop.
      	// CAN BE REMOVED! USE NULL REFERENCE FOR REMAINING SEGMENTS!
         for(int i = 0; i <= incrementalArray.length-1; i++){
            incrementalArray[i] = -1;
         }
         
      	//Overlay of base integar incremental factors.
         for(int i = (int)Math.pow(2, n/2); i >= 1; i /=2){
            for(int j = 0; j <= incrementalArray.length-1; j++){
            
               if(incrementalArray[j] == -1 && (j+1)%i == 0){
                  incrementalArray[j] = i;
               }
            }
         }
         
      	//Spicing of negative integer swap around powers of two.
         int count = 0;
         for(int i = 1; i <= (int)Math.pow(2, (n/2)+1); i*=2){
            for(int j = 0; j<= incrementalArray.length - 1; j++){
            
               if(incrementalArray[j] == i){
                  count++;
                  if(count%2 == 0){
                     int temp = incrementalArray[j];
                     incrementalArray[j] = 0 - temp;
                  }
               }
            }
            count = 0;
         }
      }
      
   	
   /**
    Fills the first column using the pattern from the 
    incremental array as a cummulative generator of
    integers to be added to a temporary variable that
    serves as a marker for index values.
   */
   
      public void fillInitialColumn(){
         int temp = 0;
         for(int i = 1; i <= rows-1; i++){
            temp += incrementalArray[i-1];
            KSquare currentSquare = new KSquare(temp, "", "");
            KGrid[i][0] = currentSquare;
         }
      }	
      
   
   /**
    Fills the first row using the pattern from the 
    incremental array as a cummulative generator of
    integers to be added to a temporary variable that
    is multiplied by the number of variables divided by 
    two and serves as a marker for index values.
   */	
   	
      public void fillInitialRow(){
         int temp = 0;
         for(int i = 1; i <= columns-1; i++){
            temp += rows*(incrementalArray[i-1]);
            KSquare currentSquare = new KSquare(temp, "", "");
            KGrid[0][i] = currentSquare;
            System.out.print(KGrid[0][i].getDecimalValue());
            System.out.println("\n");
         }   
      }
      
   	
   /**
    Fills the remaining portions of the grid with their
    respective decimal values based upon the values of the
    initial column and the initial row. These string values
    are parsed out into integers and added together, then
    set as the decimal representation for the matching 
    square on the grid.
   */		
   
      public void completeDecimalGrid(){
         int tempRow = 0, tempColumn = 0;
         for(int i = 1; i <= rows - 1; i++){
            for(int j = 1; j <= columns - 1; j++){
               tempRow = Integer.parseInt(KGrid[i][0].getDecimalValue());
               tempColumn = Integer.parseInt(KGrid[0][j].getDecimalValue());
               int temp = tempRow + tempColumn;
               KSquare currentSquare = new KSquare(temp, "", "");
               KGrid[i][j] = currentSquare;
               System.out.println(KGrid[i][j].getDecimalValue());
            }
         }
      }
      
   
   /**
    Completly fills the grid with binary representations of
    the decimal values assigned to each square. Leading zeros
    allow for variable appending only where applicable.
   */	
   	
      public void completeBinaryGrid(){
         for(int i = 0; i <= rows - 1; i++){
            for(int j = 0; j <= columns - 1; j++){
               int temp = Integer.parseInt(KGrid[i][j].getDecimalValue());
               String binary = Integer.toBinaryString(temp);
               
               if(binary.length() == n){
                  KGrid[i][j].setBinaryValue(binary);
               }
               else{
                  int numOfZero = n - binary.length();
                  String newBinary = "";
                  for(int k = 0; k < numOfZero; k++){
                     newBinary += "0";
                  }
                  newBinary += binary;
                  binary = newBinary;
                  KGrid[i][j].setBinaryValue(binary);
               }
            }
         }
      }
   	
   }