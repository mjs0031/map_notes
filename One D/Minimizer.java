 /**
  Matthew J Swann
  Version 1.3, 2011-04-03
  
  This is code for the Minimizer class, which will serve as a logic minimization
  mechanism. This class parses the function and dont-care terms associated with 
  a Karnaugh Map and sets truth and dont-care values accordingly within each 
  location on the map. Also included is a process by which implicants are found
  based on the comparisons between bit registers. The algorithms associated with
  such are an adaptation of the Quine-McCulsky algorithm for the derivation of
  prime implicants.
 */
 
 
 /*
  NOTES - NEED TO MAKE A CHECK FOR LARGE DON'T CARE TERMS TO MIX WITH
   	    SIGNIFICANTLY SMALLER MIN TERMS (currently wrong output):
			 	Min = "00001+11011+10100"
				D/C = "000XX+11111+10101"
  NOTES - Essential Size check
  REFACTOR MinTermStorage? array cut down???
  Notes - need to refactor parse functions into seperate functions based on
             boolean values.
  NOTES - adjust search mechanism for adding implicants.
 */
 
   public class Minimizer{
   
      private static final char ONE = '1', ZERO = '0', EITHER_A = 'x', EITHER_B = 'X',
      	EITHER_C = '-';							//Static chars for comparisons.
      private static final int MIN = 0, MAX = 9;
      													//Static ints for comparisons and initialization.   
      private int variables;						//Number of variables.
      private int minterms;						//Tracking variable for total true minterms.
      private int dontCares;						//Number of don't care terms.
      private int implicants;						//Number of implicants.
      private int primeImplicants;				//Number of prime implicants.
      private int essentials;						//Number of essential implicants.
      private OneDMap OneD;                  //Map to be processed.
      private String function;					//Function to be mapped.
      private String dontCareTerms;				//Denoted to be don't care terms.
      private String[] mintermStorage;			//Data Storage for total true minterms;
      private String[] dontCareStorage;		//Data Storage for don't care terms.
      private String[] implicantStorage;		//Data Storage for implicants.
      private String[] primeImplicantStorage;//Data Storage for prime implicants.
      private String[] essentialStorage;		//Data Storage for essential implicants.
      private boolean funcInBinary;				//True if in Binary.
      private boolean dontInBinary;				//True if in Binary.
   
   
   //DELETE ME!
      String[][] mintermAssignment;
      String[] primeStorageClone;
   
   /**
    Constructor taking five parameters. The first is a one dimensional map 
    object from the OneDMap class. The second is a string representation of
    the minterm function. The third is a string representation of the dont-care
    terms. The fourth and fifth are boolean control variables that are reflective
    of the format of the string input.
    
    @param OneDMap The one dimensional map to be parsed via logic minimization.
    @param func The function of minterms to be processed in regards to the map.
    @param dontCare A string representation of don't care terms to be processed.
    @param funcInBinary A control variable used to determine which methods will 
    be called based upon the format of the function itself.
    @param dontInBinary A control variable used to determine which methods will 
    be called based upon the format of the dont care terms.
   */
   
      public Minimizer(OneDMap map, String func, String dontCare, 
      boolean funcInBinary, boolean dontInBinary){
         OneD = map;
         variables = OneD.getVariables();
         function = func+"+";
         minterms = MIN;
         mintermStorage = new String[32];
         
         dontCareTerms = dontCare+"+";
         dontCares = MIN;
         dontCareStorage = new String[32];
         
         implicants = MIN;
         implicantStorage = new String[32];
         
         primeImplicants = MIN;
         primeImplicantStorage = new String[32];
         
         essentials = MIN;
         essentialStorage = new String[10];
      	
         this.funcInBinary = funcInBinary;
         this.dontInBinary = dontInBinary;
         
      //Parse Block
         if(funcInBinary){
            parseBinaryFunction();
         }
         else{
            parseDecimalFunction();
         }
         if(!dontCareTerms.trim().equals("+")){
            if(dontInBinary){
               parseBinaryDontCare();
            }
            else{
               parseDecimalDontCare();
            }
         }
         minTermValidation();
         dontCareValidation();
         
      //Minimization Block
         findImplicants();
         reduceImplicants();
         
         System.out.println(primeImplicantToString());
         minimization();
         System.out.println(essentialsToString());
         
         
      }
      
   	
   //**
   //START PARSING BLOCK
   //**
      
   
   /**
    Parses the string representation of the function in binary and places it into
    a string array. The array is later processed and corresponding KSquares are 
    amended such that they reflect the proper truth values.
   */ 
      
      private void parseBinaryFunction(){
         String term = "";
         for(int i = 0; i < function.length(); i++){
            if(function.charAt(i) == ONE || function.charAt(i) == ZERO
            || isValidChar(function.charAt(i))){
               term += function.charAt(i);
            }
            else{
               term = expandTerm(term);
               mintermStorage[minterms] = term;
               minterms++;
               term = "";
               if(minterms == mintermStorage.length){
                  expandMintermStorage();
               }
            }
         }
      }
      
   	
   /**
    Converts the string representation of the function in decimal into binary and
    subsequently places it into a string array. The array is later processed and 
    corresponding KSquares are amended such that they reflect the proper truth values.
   */
   
      private void parseDecimalFunction(){
         String term = "";
         for(int i = 0; i < function.length(); i++){
            if(function.charAt(i) == '+'){
               int stash = Integer.parseInt(term);
               term = fullBinary(Integer.toBinaryString(stash));
               mintermStorage[minterms] = term;
               minterms++;
               term = "";
               if(minterms == mintermStorage.length){
                  expandMintermStorage();
               }
            }
            else{
               term += function.charAt(i);
            }
         }
      }	
   
   	
   /**
    Parses the string representation of dont care terms in binary and places it into
    a string array. The array is later processed and corresponding KSquares are 
    amended such that they reflect the proper truth values.
   */
   
      private void parseBinaryDontCare(){
         String term = "";
         for(int i = 0; i < dontCareTerms.length(); i++){
            if(dontCareTerms.charAt(i) == ONE || dontCareTerms.charAt(i) == ZERO ||
               isValidChar(dontCareTerms.charAt(i))){
               term += dontCareTerms.charAt(i);
            }
            else{
               term = expandTerm(term);
               dontCareStorage[dontCares] = term;
               dontCares++;
               term = "";
               if(dontCares == dontCareStorage.length){
                  expandDontCareStorage();
               }
            }
         }
      }	
   
   
   /**
    Converts the string representation of the function in decimal into binary and
    subsequently places it into a string array. The array is later processed and 
    corresponding KSquares are amended such that they reflect the proper truth values.
   */
   
      private void parseDecimalDontCare(){
         String term = "";
         for(int i = 0; i < dontCareTerms.length(); i++){
            if(dontCareTerms.charAt(i) == '+'){
               int stash = Integer.parseInt(term);
               term = fullBinary(Integer.toBinaryString(stash));  
               dontCareStorage[dontCares] = term;
               dontCares++;
               term = "";
               if(dontCares == dontCareStorage.length){
                  expandDontCareStorage();
               }
            }
            else{
               term += dontCareTerms.charAt(i);
            }
         }
      }
   
   
   /**
    Compares each minterm with each KSquare's numerical values to determine a 
    match. In the event of a match, the corresponding square is amended such
    that its truth value is set to true. 
   */
   	
      private void minTermValidation(){
         int permutations = (int) Math.pow(2, variables);
       
         for(int i = 0; i < minterms; i++){
            String currentTerm = mintermStorage[i];
            
         // Process of the first half of the map if the first bit is denoted
         // to be false.	
            if(currentTerm.charAt(0) == ZERO || isValidChar(currentTerm.charAt(0))){
               for(int j = 0; j < permutations/2; j++){
                  KSquare currentSquare = OneD.getElement(j);
                  String binary = currentSquare.getBinaryValue();
                  for(int k = 0; k < currentTerm.length(); k++){
                     if(currentTerm.charAt(k) == binary.charAt(k)
                          || isValidChar(currentTerm.charAt(k))){
                        if(k < currentTerm.length()-1){
                           continue;
                        }
                        else{
                           currentSquare.setTruthValue(true);
                           OneD.setElement(j, currentSquare);
                        }
                     }
                     else{
                        break;
                     }
                  }
               }
            }
               
         // Process of the second half of the map if the first bit is denoted
         // to be true.
            if(currentTerm.charAt(0) == ONE || isValidChar(currentTerm.charAt(0))){
               for(int j = permutations/2; j < permutations; j++){
                  KSquare currentSquare = OneD.getElement(j);
                  String binary = currentSquare.getBinaryValue();
                  for(int k = 0; k < currentTerm.length(); k++){
                     if(currentTerm.charAt(k) == binary.charAt(k)
                          || isValidChar(currentTerm.charAt(k))){
                        if(k < currentTerm.length()-1){
                           continue;
                        }
                        else{
                           currentSquare.setTruthValue(true);
                           OneD.setElement(j, currentSquare);
                        }
                     }
                     else{
                        break;
                     }
                  }
               }
            }
         }
      }
      
   	
   /**
    Compares each 'don't care'-term's numerical value to determine a match. 
    In the event of a match, the corresponding square is amended such that its 
    truth value is set to true. 
   */		
      
      private void dontCareValidation(){
      
         int permutations = (int)Math.pow(2, variables);
         
         for(int i = 0; i < dontCares; i++){
            
            String currentTerm = dontCareStorage[i];   
            for(int j = 0; j < permutations; j++){
               KSquare currentSquare = OneD.getElement(j);
               String binary = currentSquare.getBinaryValue();
               for(int k = 0; k < currentTerm.length(); k++){
                  if(currentTerm.charAt(k) == binary.charAt(k)
                     || isValidChar(currentTerm.charAt(k))){
                     if(k < currentTerm.length() - 1){
                        continue;
                     }
                     else{
                        currentSquare.setDontCare(true);
                        OneD.setElement(j, currentSquare);
                     }
                  }
                  else{
                     break;
                  }
               }
            }
         }
      }
   
   
   //**
   //END PARSING BLOCK
   //**
   
   
   
   //**
   //START MINIMIZATION BLOCK
   //**   
   
   
   /**
   
   */
   
      public boolean minimization(){
         int count = 0;
         mintermAssignment = new String[2][minterms];
         for(int i = 0; i < minterms; i ++){
            mintermAssignment[0][i] = mintermStorage[i];
         }
         for(int i = 0; i < minterms; i++){
            for(int j = 0; j < primeImplicants; j++){
               if(encompasses(primeImplicantStorage[j], mintermStorage[i])
               && mintermAssignment[1][i] == null){
                  mintermAssignment[1][i] = j+"";
                  count++;
                  for(int k = 0; k < minterms; k++){
                     if(encompasses(primeImplicantStorage[j], mintermStorage[k])
                     && mintermAssignment[1][k] == null){
                        mintermAssignment[1][k] = j+"";
                        count++;
                     }
                  }
               }
               if(count == minterms){
                  allocateEssentials();
                  return true;
               }
            }
         }  
         return false;
      }
   
   
   /**
   
   */
   
      public void allocateEssentials(){
         primeStorageClone = primeImplicantStorage.clone();
         for(int i = 0; i < minterms; i++){
            if(!primeStorageClone[Integer.parseInt(mintermAssignment[1][i])].equals("Added")){
               essentialStorage[essentials] = primeStorageClone[Integer.parseInt(mintermAssignment[1][i])];
               primeStorageClone[Integer.parseInt(mintermAssignment[1][i])] = "Added";
               essentials++;
            }
         }
      }
   
   
   /**
   
   */
   
      private void reduceImplicants(){
      	
         for(int i = implicants-1; i >= 0; i--){
            //System.out.println(implicantStorage[i]);
            for(int j = implicants-1; j >=0 ; j--){
               if(canReplaceImplicant(implicantStorage[i])){
                  replaceImplicant(implicantStorage[i]);
                  break;
               }
               else{
                  if(!encompasses(implicantStorage[i], implicantStorage[j]) 
                  && !primeImplicantContained(implicantStorage[i]) && !primeIsEncompassed(implicantStorage[i])){
                     primeImplicantStorage[primeImplicants] = implicantStorage[i];
                     primeImplicants++;
                     break;
                  }
               }
            }
         }
      }
   
   
   /**
    Parses through the minterms and finds implicants based upon a comparison
    of the characters in each minterm and don't care term.
   */
   
      private void findImplicants(){
         boolean isAdded = false;
      
         for(int i = 0; i < minterms; i++){
            isAdded = false;
            for(int j = i; j < minterms; j++){
               if(toBeCombined(mintermStorage[i], mintermStorage[j])){
                  String newImplicant = combine(mintermStorage[i], mintermStorage[j]);
                  if(!implicantContained(newImplicant)){
                     implicantStorage[implicants] = newImplicant;
                     implicants++;
                     isAdded = true;
                  }
               }
            }
           
         	/*
            if(canReplaceImplicant(mintermStorage[i])){
               replaceImplicant(mintermStorage[i]);
               isAdded = true;
            } 
         	/*
            if(!isAdded && !implicantContained(mintermStorage[i])){
               implicantStorage[implicants] = mintermStorage[i];
               implicants++;
            }
            */
         }
         
         	
         if(!dontCareTerms.trim().equals("+")){
            for(int i = 0; i < minterms; i++){
               isAdded = false;
               for(int j = i; j < dontCares; j++){
                  if(toBeCombined(mintermStorage[i], dontCareStorage[j])){
                     String newImplicant = combine(mintermStorage[i], dontCareStorage[j]);
                     if(!implicantContained(newImplicant)){
                        implicantStorage[implicants] = newImplicant;
                        implicants++;
                        isAdded = true;
                     }
                  }
               }
               
            	/*
               if(canReplaceImplicant(mintermStorage[i])){
                  replaceImplicant(mintermStorage[i]);
                  isAdded = true;
               } 
            	  
               if(!isAdded && !implicantContained(mintermStorage[i])){
                  implicantStorage[implicants] = mintermStorage[i];
                  implicants++;
               }
               */
            }  
         }
       
         
         for(int i = 0; i < minterms; i++){
            if(!isEncompassedByImplicants(mintermStorage[i])){
               implicantStorage[implicants] = mintermStorage[i];
               implicants++;
            }
         }
         
         
      //Combines the implicants where applicable.   
         boolean updated = true;
         while(updated){
            updated = false;
            for(int i = 0; i < implicants; i++){
               for(int j = i; j < implicants; j++){
                  if(toBeCombined(implicantStorage[i], implicantStorage[j])){
                     String newImplicant = combine(implicantStorage[i], implicantStorage[j]);
                     if(!implicantContained(newImplicant)){
                        implicantStorage[implicants] = newImplicant;
                        implicants++;
                        updated = true;
                     }
                  }
               }
            }
         }
      }
   
   
   /**
    Determines if a given implicant can be put in place of an implicant already
    contained within the prime implicant storage.
    
    @param source The source of the replacement comparison.
    @return Returns true if the source can replace an implicant already contained
    within the prime implicant storage.
   */  
     
      private boolean canReplaceImplicant(String source){
         if(primeImplicantStorage[0] == null){
            return true;
         }
         else{
            for(int i = 0; i < primeImplicants; i++){
               for(int j = 0; j < source.length(); j++){
                  if(source.charAt(j) == primeImplicantStorage[i].charAt(j)
                  || isValidChar(source.charAt(j))){
                     continue;
                  }
                  else{
                     return false;
                  }
               }
            }
            return true;
         }
      }
   
   
   /**
    Replaces an implicant within the prime implicant data storage with a larger
    implicant.
    
    @param source The source string to replace a smaller implicant.
   */
      
      private void replaceImplicant(String source){
         if(primeImplicantStorage[0] == null){
            primeImplicantStorage[0] = source;
            primeImplicants++;
         }
         else{
            for(int i = 0; i < primeImplicants; i++){
               for(int j = 0; j < source.length(); j++){
                  if(source.charAt(j) == primeImplicantStorage[i].charAt(j)
                  || isValidChar(source.charAt(j))){
                     if(j < source.length() - 1){
                        continue;  
                     } 
                     else{
                        primeImplicantStorage[i] = source;
                     }
                  }
                  else{
                     break;
                  }
               }
            }
         }
      }
   
   
   /**
    Returns true if the source passed as a parameter is encompassed by an
    implicant already existing within the implicant data storage.
    
    @param source The source for comparison within the prime implicant data
    storage.
    @return Returns true if the source is already encompassed by a member of
    the implicant storage.
   */
   
      private boolean isEncompassedByImplicants(String source){
         for(int i = 0; i < implicants; i++){
            if(encompasses(implicantStorage[i], source)){
               return true;
            }
         }
         return false;
      } 
      
   
   /**
    Returns true if the source passed as a parameter is encompassed by an
    implicant already existing within the prime implicant data storage.
    
    @param source The source for comparison within the prime implicant data
    storage.
    @return Returns true if the source is already encompassed by a member of
    the prime implicant storage.
   */
     
      private boolean primeIsEncompassed(String source){
         for(int i = 0; i < primeImplicants; i++){
            if(encompasses(primeImplicantStorage[i], source)){
               return true;
            }
         }
         return false;
      } 
      
   	
   /**
    Returns true if a given source encompasses a comparision string.
    
    @param source The scource for the comparision.
    @param comparision The string source is compared to.
    @return Returns true if the scource emcompasses the comparision.
   */	
   	
      public boolean encompasses(String source, String comparison){
         for(int i = 0; i < comparison.length(); i++){
            if(source.charAt(i) == comparison.charAt(i) || isValidChar(source.charAt(i))){
               continue;
            }
            else{
               return false;
            }
         }
         return true;
      }
      
   
   /**
   
   */
   
      public boolean isEncompassed(String source, String comparison){
         for(int i = 0; i < source.length(); i++){
            if(comparison.charAt(i) == source.charAt(i) || isValidChar(comparison.charAt(i))){
               continue;
            }
            else{
               return false;
            }
         }
         return true;
      }		
      
   	
   /**
    Combines two strings representing the binary value of an implicant if
    and only if the strings are one bit different.
    
    @param source One of the two string values.
    @param comparison One of the two string values
    @return Returns a string representation of the combined terms.
   */
   
      private String combine(String source, String comparison){
         if(source.length() <= comparison.length()){
            for(int i = 0; i < source.length(); i++){
               if(source.charAt(i) != comparison.charAt(i)){
                  source = comparison.substring(0, i)+EITHER_B+comparison.substring(i+1, comparison.length());
                  break;
               }
            }
         }
         else{
            for(int i = 0; i < comparison.length(); i++){
               if(source.charAt(i) != comparison.charAt(i)){
                  source = source.substring(0, i)+EITHER_B+source.substring(i+1, source.length());
                  break;
               }
            }
         }
         return source;
      }	
   
   
   /**
    Assesses whether or not two strings can be combined. If the difference in 
    bit values is one, the strings are combinable.
    
    @param source One of the two string values.
    @param comparison One of the two string values
    @return Returns true if the two strings can be combined.
   */
   
      private boolean toBeCombined(String source, String comparison){
         int differential = 0;
         int sourceDontCare = 0;
         int comparisonDontCare = 0;
         
         if(source.equals(comparison)){
            return false;
         }
         
      //Calculates dont care bits in source.	
         for(int i = 0; i < source.length(); i++){
            if(isValidChar(source.charAt(i))){
               sourceDontCare++;
            }
         }
         
      //	Calculates dont care bits in comparison.
         for(int i = 0; i < comparison.length(); i++){
            if(isValidChar(comparison.charAt(i))){
               comparisonDontCare++;
            }
         }
         
      //If don't care bits are equivalent, an evaluation is made as to the difference
      //in bits between source and comparison.	
         if(comparisonDontCare == sourceDontCare){
            for(int i = 0; i < source.length(); i++){
               if(isValidChar(source.charAt(i)) && isValidChar(comparison.charAt(i))){
                  continue;
               }
               if(source.charAt(i) != comparison.charAt(i)){
                  differential++;  
               }
            }
         }
         if(differential == 1){
            return true;
         }
         else{
            return false;
         }
      }
    
      
   /**
    Returns true if the parameter matches the 'X', 'x' or '-' characters.
    
    @param character The character value to be evaluated.
    @return Returns true if the character matches 'X', 'x' or '-'.
   */
   
      private boolean isValidChar(char character){
         if(character == EITHER_A || character == EITHER_B || 
         character == EITHER_C){
            return true;
         }
         else{
            return false;
         }
      }
      
   
   /**
    Returns true if the source is already contained within the implicant
    data storage.
    
    @param source The source of comparision in the implicant data storage.
    @return Returns true if the source is already contained within the
    implicant data storage.
   */
      
      private boolean implicantContained(String source){
         for(int i = 0; i < implicants; i++){
            if(source.equals(implicantStorage[i])){
               return true;
            }
         }
         return false;
      }
   
   
   /**
    Returns true if the scource is already contained within the prime
    implicant data storage.
    
    @para source The source of comparision in the prime implicant data storage.
    @return Returns true if the source is already contained within the prime
    implicant data storage.
   */
      
      private boolean primeImplicantContained(String source){
         for(int i = 0; i < primeImplicants; i++){
            if(source.equals(primeImplicantStorage[i])){
               return true;
            }
         }
         return false;
      }
   
   
   //**
   //END MINIMIZATION BLOCK
   //**	
   
         
   
   //**
   //START SUPPORT BLOCK
   //**
   
   /**
    Sets the truth function for truth processing and minimization.
    
    @param func The specified function for truth processing and minimization.
   */
      
      public void setFunction(String func){
         function = func;
         parseBinaryFunction();
      }
   
   
   /**
    Returns a string representation of the function.
    
    @return Returns the function.
   */
      
      public String getFunction(){
         return function;
      }
      
   
   /**
    Resizes the minterm storage.
   
    @return Returns true if the minterm storage has been resized.
   */	
   
      private void expandMintermStorage(){
         String[] stash = new String[mintermStorage.length*3/2];
         for(int i = 0; i < mintermStorage.length; i++){
            stash[i] = mintermStorage[i];
         }
         mintermStorage = stash;
      }
      
   	
   /**
    Resizes the dont care term storage.
   
    @return Returns true if the minterm storage has been resized.
   */	
   
      private void expandDontCareStorage(){
         String[] stash = new String[dontCareStorage.length*3/2];
         for(int i = 0; i < dontCareStorage.length; i++){
            stash[i] = dontCareStorage[i];
         }
         dontCareStorage = stash;
      } 
      
   	
   /**
    Resizes the implicant storage.
    
    @return Returns true if the implicant storage has been resized.
   */	  
   
      private void expandImplicantStorage(){
         String[] stash = new String[implicantStorage.length*3/2];
         for(int i = 0; i < implicantStorage.length; i++){
            stash[i] = implicantStorage[i];
         }
         implicantStorage = stash;
      }
      
   
   /**
    Resizes the prime implicant storage.
    
    @return Returns true if the prime implicant storage has been
    resized.
   */	
   
      private void expandPrimeImplicantStorage(){
         String[] stash = new String[primeImplicantStorage.length*3/2];
         for(int i = 0; i < primeImplicantStorage.length; i++){
            stash[i] = primeImplicantStorage[i];
         }
         primeImplicantStorage = stash;
      }
      
   
   /**
    Expands the source string to the right to the extent that its length
    matches the number of variables by appending don't care bits.
    
    @param source The string passed for expansion.
    @return Returns a right-sided expansion of the original string.
   */
   
      private String expandTerm(String source){
         while(source.length() < variables){
            source += EITHER_B;
         }
         return source;
      }
      
   
   /**
    Expands the source string to the left to the extent that its length
    matches the number of variables by inserting don't care bits.
    
    @param source The string passed for expansion.
    @return Returns a left-sided expansion of the original string.
   */		
   
      private String fullBinary(String source){
         if(source.length() == variables){
            return source;
         }
         else{
            int stash = variables - source.length();
            String temp = "";
            for(int i = 0; i < stash; i++){
               temp += ZERO;
            }
            temp += source;
            return temp;
         }
      }
   
   
   //**
   //END SUPPORT BLOCK
   //**
   
   
   
   //**
   //START OUPUT BLOCK
   //**
   
   /**
    Returns the amended map with proper truth values for the given function.
    
    @return Returns the amended map with proper truth values.
   */   
   
      public OneDMap returnMap(){
         return OneD;
      }
   
   
   /**
   
   */
   
      public String[] getEssentialArray(){
         return essentialStorage;
      }
      
   
   /**
   
   */
   
      public int getEssentials(){
         return essentials;
      }	
   
   /**
    Returns a string representation of the minterms contained within their
    respective data storage.
    
    @return Returns a string representation of the minterms.
   */   
   	
      public String minToString(){
         String output = "MinTerms: "+minterms+"\n";
         for(int i = 0; i < minterms; i++){
            output+= mintermStorage[i]+"\n";
         }
         return output;
      }
      
   
   /**
    Returns a string representation of the dont care terms contained within
    their respective data storage.
    
    @return Returns a string representation of the dont care terms.
   */	
      
      public String dontCareToString(){
         String output = "Dont Care Terms:"+dontCares+"\n";
         for(int i = 0; i < dontCares; i++){
            output += dontCareStorage[i]+"\n";
         }
         return output;
      }
      
   
   /**
    Returns a string representation of the implicants contained within their
    respective data storage.
    
    @return Returns a string representation of the implicants.
   */	
      
      public String implicantToString(){
         String output = "Total Implicants: "+implicants+"\n";
         for(int i = 0; i < implicants; i++){
            output += implicantStorage[i]+"\n";
         }
         return output;
      }
      
   
   /**
    Returns a string representation of the prime implicants contained within their
    respective data storage.
    
    @return Returns a string representation of the prime implicants.
   */	
      
      public String primeImplicantToString(){
         String output = "Prime Implicants: "+primeImplicants+"\n";
         for(int i = 0; i < primeImplicants; i++){
            output += primeImplicantStorage[i]+"\n";
         }
         return output;
      }
      
   
   /**
   
   */
   
      public String essentialsToString(){
         String output = "Essentials: \n";
         for(int i = 0; i < essentials; i++){
            output += essentialStorage[i]+"\n";
         }
         return output;
      }
   
   //**
   //END OUTPUT BLOCK
   //**	
   }