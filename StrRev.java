   public class StrRev{
   
      private char[] original;
      private int size;
      private String reversal;
   
      public StrRev(String str){
         original = str.toCharArray();
         size = original.length;
         reversal = "";
         RevStr(original);
      }
    
      public String RevStr(char[] tempArray){
         char[] array = tempArray;
         if(size == 1){
            return reversal += array[0];
         }
         else{
            return reversal += RevStr(array[array.length-1]);
         }
      }  
     
   	
   }