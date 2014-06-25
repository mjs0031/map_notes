   public class TestingOneD{
   
      public static void main(String[] args){
      
         int n = 5;
         OneDMap x = new OneDMap(n);
         x.establishSimpleIA();
         x.fillMap();
         
         for(int i = 0; i < Math.pow(2, n); i++){
            KSquare temp = x.getNext();
            System.out.println("D:"+temp.getDecimalValue()+
               "\nB:"+temp.getBinaryValue()+"\nV:"+temp.getVariables()+
               "\n\n\n");
         }
      
      }
      
   	/**
   	* for(int i = 0; i < x.length(); i++){
   	* int j = 0;
   	* while(i % Math.pow(2, j) = 0){
   	* j++
   	* }
   	* x[i] = Math.pow(2, j-1); 	
   	*}
   	*
   	* complementary powers of two with inverted powers:
   	* 2^3 appears 2^0 number of times.
   	* 2^2 appears 2^1 number of times.
   	*
   	* Start with i = 2^(n-1) make that 2^n-1. Multiple index
   	* by two and replace that number if not replaced
   	*/
   
   }