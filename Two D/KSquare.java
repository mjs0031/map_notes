   /**
	 Matthew J Swann
	 Version 1.0, 2010-10-27
	
	 Source code for the representation of data represented by a single
	 sqaure in a Karnaugh Map. The current three pieces of data are the
	 decimal index for the map, the binary representation of the decimal
	 which will have a Hamming Distance of one (1) with adjecent squares
	 and finally the variables denoted to be true by that binary 
	 representation.
	
	 The KSquare class has been designed to work with a KMap Class which
	 will populate a map made of the squares. This class contains methods
	 for setting and returning the values stored within a KSqaure for both
	 information retrieval and later for display.
	*/
   
   public class KSquare{
   
      private String kData[] = new String[3];      //Data storage.
      private String decimalValue;
      private boolean truthValue = false;
      
   	
   /**
    Primary constructor to be implemented in the KMap class. Constructor
    takes parameters matching decimal and binary values along with a
    variable representation only when designated true by the binary value.
   
    @param decimalValue The decimal index of the current square.
    @param binaryValue The binar equivalent of the decimal value attached 
    to the current square.
    @param variableRepresentation Those variable appearing as true, denoted
    by the binary representation of the current square.
   */
   
      public KSquare(int decimalValue, String binaryValue, 
      String variableRepresentation){
         kData[0] = decimalValue + "";
         kData[1] = binaryValue;
         kData[2] = variableRepresentation;
      }
      
   	
   /**
    Default constructor setting the kData to empyt string values.
    To be used to initialize the complete map once the grid has
    been established.
   */
   	
      public KSquare(){
         kData[0] = 0+"";
         kData[1] = "";
         kData[2] = "";
         decimalValue = 0 + "";
      }
      
   
   /**
    Sets the decimal value of the current square.
   
    @param value Specified decimal value.
   */
      
      public void setDecimalValue(int value){
         kData[0] = value + "";
      }
   
   
   /**
    Returns the decimal vlaue as a String.
   
    @return Returns a String representation of the decimal value
    set in kData.
   */
      
      public String getDecimalValue(){
         return kData[0];
      }
    
    
   /**
    Sets the binary value of the current square.
   
    @param value String representation of the decimal value.
   */
   	
      public void setBinaryValue(String binary){
         kData[1] = binary; 
      }
      
   
   /**
    Returns a String representation of the binary value of the
    current square.
   
    @return Returns a String representation of the binary value.
   */
   
      public String getBinaryValue(){
         return kData[1];
      }
   
   
   /**
    Sets the variable value for the square. Does not convert from
    binary to variable due non-access to the number of mapping
    variables.
    
    @param variables The variables denoted to be true.
   */
   
      public void setVariables(String variables){
         kData[2] = variables;
      }
   
   
   /**
    Appends the current variable representation with a denoted
    variable only if it is true according to this square's
    binary value.
   
    @param variable The variable appearing as true based on the
    binary value.
   */	
      
      public void addVariable(String variable){
         String temp = kData[2];
         temp += variable;
         kData[2] = temp;
      }
      
   
   /**
    Returns a String representation of the positive variables
    based upon the binary representation.
   
    @return Returns a String representation of the positive
    variables based upon the binary representation.
   */	
   
      public String getVariables(){
         return kData[2];
      }
   
   
   /**
    Sets the truth value. 
    
    @param newTruthValue The new truth value.
   */
   
      public void setTruthValue(boolean newTruthValue){
         truthValue = newTruthValue;
      }
      
   
   /**
    Returns the truth value.
    
    @return Returns the truth value.
   */	
   
      public boolean getTruthValue(){
         return truthValue;
      }
   
   }