/**
 Matthew J Swann
 Version 1.1, 2011-02-27
 
 This is the source code for a one-dimensional Karnaugh Map
 variant. The map is created using an Incremental Array and a 
 tacking variable that serves as the cummulativly updating
 decimal value of each sqaure within the map.
 
 This code has been appended to allow for a translation 
 from one dimensional storage to two dimensional visual
 represenation.
*/

   public class OneDMap{
   
      private int n;                          //Number of variables for the map.
      private int iterator = 0;			       //Iterative variable.
      private int permutations;				    //Iterative ceiling.
      private KSquare OneD[];                 //Single dimension mapping array.
      private int incrementalArray[];         //Incremental storage.
      
   
   /**
    Constructor to be used with parameters from user input. Also,
    frames the single dimension logic map and incremental array.
   
    @param n The number of variables as specified by user input.
   */
   
      public OneDMap(int n){
         this.n = n;
         permutations = (int)Math.pow(2, n);
         OneD = new KSquare[(int)Math.pow(2, n)];
         incrementalArray = new int[(int)(Math.pow(2, n) - 1)];
         
         establishSimpleIA();
         fillMap();
      }
   
      
   /**
    Drafts a temporary array of integer values that will be
    used to increment the cumulative value of each sqaure within
    the one dimesional array/map. This method utilizes two
    looping mechanisms.
   
    The first loop forces the upper limit on the power of two
    which can be divided into the currently specified index.
    When that ceiling is found, the index is assigned a value
    equal to the value of two raised to the j. The next index is
    simply assigned a '1' as each second index is odd and 
    not divisible by any power of two higher than zero. Two to 
    the zero yeilding '1'.
       
    The third loop inverts the sign of each integer around
    each power of two above zero. The most simplistic way
    of working this problem is to count the occurance of 
    each variable and to make the cummulatively even occurances
    a negative.
   */
   	
      private void establishSimpleIA(){
         incrementalArray[0] = 1;
      
      /*
       Utilizes rising powers of two to determine maximum value
       for current index. Uses a zero modulus to determine
       divisibility of index value.
      */
         for(int i = 1; i < incrementalArray.length-1; i+=2){
            int j = 1;
            while((i+1)%Math.pow(2, j) == 0){
               j++;
            }
            incrementalArray[i] = (int)Math.pow(2, j-1);
            incrementalArray[i+1] = 1;
         }
         
      /*
       Spicing of negative integer swap around powers of two
       using an even division modulus to control positive and
       negative signature.
      */
         int count = 0;
         for(int i = 1; i <= (int)Math.pow(2, n-1); i*=2){
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
    Fills one D map with K-Sqaures containing both decimal
    and binary representations.
   */
   
      private void fillMap(){
         int temp = 0;
         String binary = "";
         for(int i = 0; i < n; i++){
            binary += "0";
         }
         KSquare currentSquare = new KSquare(temp, binary, "");
         OneD[0] = currentSquare;
         
      /*
       Defining the binary representation from cummulatively
       updated temp variable.	
      */	
         for(int i = 0; i < incrementalArray.length; i++){
            temp += incrementalArray[i];
            binary = Integer.toBinaryString(temp);
         	
            if(binary.length() != n){
               int numOfZero = n - binary.length();
               String newBinary = "";
               for(int j = 0; j < numOfZero; j++){
                  newBinary += "0";
               }
               newBinary += binary;
               binary = newBinary;
            }
         
         /*
          Defining the variable representations from the
          updated binary values.
         */   
            String variableRep = "";
            char[] chars = binary.toCharArray(); 	
            for(char q = 'A'; q < 'Z' && q - 'A' < chars.length; q++){
               variableRep += chars[q-'A'] == '1' ? q : " ";
            }
         
         /*
          Square value assignment for the currently indexed
          grid location.
         */   
            currentSquare = new KSquare(temp, binary, variableRep);
            OneD[i+1] = currentSquare;  	
         }
      }
   
   
   /**
    Returns an abridged form of the Incremental Array, which
    will serve as a the IA for a two-dimensional representation.
    
    @return Returns a refined form of the IA for visual
    assimilation.
   */
      
      public int[] getModifiedIA(){
         int[] counter = new int[(int)Math.pow(2.0, (n/2)+1)];
         counter[0] = 0;
         for (int i = 1; i < counter.length; i ++){
            counter[i] = incrementalArray[i-1];
         }
         return counter;
      }
   
   
   /**
    Returns the KSqaure from the map at a given index.
    
    @return Returns the KSquare at a given index.
   */
   
      public KSquare getElement(int index){
         return OneD[index];
      }
      
   
   /**
    Sets the index of the one dimensional array to a given
    KSquare.
    
    @param index The index to which the KSquare is assigned.
    @param square The assigned KSqaure.
   */	
   
      public void setElement(int index, KSquare square){
         OneD[index] = square;
      }
      
   
   /**
    Iterates through the KSquare array and returns the next
    available KSqaure.
    
    @return Return the next available KSqaure.
   */
   
      public KSquare getNext(){
         if(iterator < permutations){
            iterator ++;
            return OneD[iterator-1];
         }
         else{
            return OneD[iterator-1];
         }
      }
   
   
   /**
    Returns the total number of minterms.
    
    @return Returns number of minterms.
   */
   
      public int getPermutations(){
         return permutations;
      }
      
   	
   /**
    Returns the number of variables.
    
    @return Number of variables.
   */
   
      public int getVariables(){
         return n;
      }
   }