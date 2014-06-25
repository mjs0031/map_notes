/**
 Matthew J Swann
 Version 1.0, 2011-02-07
*/

   public class ZeroDMap{
   
      private int n;                           //Number of variables for the map.
      private KSquare ZeroD;                   //Organic data variable.
      private int incrementalArray[];          //Array containing incremental factors.
   
    
   /**
    Constructor to be used with parameters from user input. Cretes
    organic data variable and frames incremental array.
   
    @param n The number of variables as specified by user input.
   */
      
      public ZeroDMap(int n){
         this.n = n;
         ZeroD = new KSquare(0,"", "");
         incrementalArray = new int[(int)(Math.pow(2, n) - 1)];
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
      
      public void establishSimpleIA(){
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
    Processes each square of the 'map' in dynamic fashion. Also,
    prints current values for decimal and binary along with
    matching variable representations.
   */   
   	
      public void processMap(){
      //DELETE THIS NONSENSE!!!
         String binary = "";
         for(int k = 0; k < n; k++){
            binary += "0";
         }
         System.out.println("Decimal:\t0"+"\nBinary:\t\t"+
            binary+"\nVariables: (none)\n\n");
      
         int temp = 0;
         
         for(int i = 0; i < incrementalArray.length; i++){
            temp+= incrementalArray[i];
            binary = Integer.toBinaryString(temp);
         
            if(binary.length() == n){
               ZeroD = new KSquare(temp, binary, "");
               
            	//DELETE THIS NONSENSE!!!
               System.out.println("Decimal:\t"+temp+"\nBinary:\t\t"+
                  binary);
            }
            else{
               int numOfZero = n - binary.length();
               String newBinary = "";
               for(int j = 0; j < numOfZero; j++){
                  newBinary += "0";
               }
               newBinary += binary;
               binary = newBinary;
               ZeroD = new KSquare(temp, binary, "");
               
              //DELETE THIS NONSENSE!!!
               System.out.println("Decimal:\t"+temp+"\nBinary:\t\t"+
                  newBinary);
            }
          
          
            String variableRep = "";
            char[] chars = new char[n];
            chars = binary.toCharArray();
            
         	
            for(char q = 'A'; q < 'Z' && q - 'A' < chars.length; q++){
               variableRep += chars[q-'A'] == '1' ? q : " ";
            }  
            
            int stash = incrementalArray[i];
         	
            System.out.println("Variables:\t"+variableRep+
               "\nIncremental:\t"+stash+"\nDirect Value:\t"+temp+
               "\nDirect Binary:\t"+Integer.toBinaryString(temp)+"\n\n");  
         	
         }
      }
   	
   }
   
