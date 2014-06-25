   public class playpen{
   
      public static void main(String[] args){
      
         OneDMap OneD = new OneDMap(5);
         String func = "1+2+7+5";
         String dont = "00011+00000";
         Minimizer m = new Minimizer(OneD, func, dont, false, true);
      	
         System.out.println("\n\n");
      	
         System.out.println(m.minToString());
         System.out.println(m.dontCareToString());
         System.out.println(m.implicantToString());
         System.out.println(m.primeImplicantToString());
      
      
      }
   
   }