  /**
  * Matthew J Swann
  * Dev. Notes
  * Don't steal my shit... I will find you.
  */
  
   public class Dev{
   
      public static void main (String[] args){
         int n = 4;
         int a = 0;
         int b = 1;
         int c = n;
         /**
         System.out.println(Math.pow(2, a));
         System.out.println(Math.pow(2, b));
         b++;
         System.out.println(Math.pow(2, a));
         System.out.println(Math.pow(2, b));
         b++;
         System.out.println(Math.pow(2, a));
         System.out.println(Math.pow(2, b));
         b++;
         System.out.println(Math.pow(2, a));
         System.out.println(Math.pow(2, b));
         c--;
         System.out.println(Math.pow(2, a));
         System.out.println(Math.pow(2, c));
         c--;
         System.out.println(Math.pow(2, a));
         System.out.println(Math.pow(2, c));
         c--;
         System.out.println(Math.pow(2, a));
         System.out.println(Math.pow(2, c));
         */
      	
         int x[] = new int[15];
         x[0] = 1;
         x[1] = 2;
         x[2] = 1;
         x[3] = 4;
         x[4] = 1;
         x[5] = 2;
         x[6] = 1;
         x[7] = 8;
         x[8] = 1;
         x[9] = 2;
         x[10] = 1;
         x[11] = 4;
         x[12] = 1;
         x[13] = 2;
         x[14] = 1;
      
      	
         int count = 0;
         for(int gamma = 1; gamma <= Math.pow(2, 3); gamma*=2){
            for(int i = 0; i<= x.length - 1; i++){
            
               if(x[i] == gamma){
                  count++;
                  if(count%2 == 0){
                     int temp = x[i];
                     x[i] = 0 - temp;
                  }
               }
            }
            count = 0;
         }
        
         for(int i = 0; i<= x.length - 1; i++){
            System.out.print(x[i]+", ");
         }
         System.out.println("\n\n");
         
         int z[] = new int[15];
         for(int i = 0; i <= z.length -1; i++){
            z[i] = -1;
         }
      
         int zeta = 8;
         //for(double delta = alpha/2; delta >= 1; delta/=2.0){
         for(int epsilon = zeta; epsilon >= 1; epsilon /=2){
            for(int beta = 0; beta <= z.length-1; beta++){
               
               if(z[beta] == -1 && (beta+1)%epsilon == 0){
                  z[beta] = epsilon;
               }
            
            }
         }
         
         for(int i = 0; i<= z.length-1; i++){
            System.out.print(z[i]+", ");
         }
         
         for(int gamma = 1; gamma <= Math.pow(2, 3); gamma*=2){
            for(int i = 0; i<= z.length - 1; i++){
            
               if(z[i] == gamma){
                  count++;
                  if(count%2 == 0){
                     int temp = z[i];
                     z[i] = 0 - temp;
                  }
               }
            }
            count = 0;
         }
         
         System.out.println("\n\n");
         
         for(int i = 0; i<= z.length-1; i++){
            System.out.print(z[i]+", ");
         }
         
         
      
         
      	
      }
   
   }