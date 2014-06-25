   import javax.swing.*;
   import javax.swing.border.*;
   import java.awt.*;
   import java.awt.event.*;
   import java.io.*;

/**
 Matthew J Swann
 Version 1.1, 2011-02-27
 
 This is code for a GUI that assembles and transposes a one-dimensional
 version of a Karnaugh Map onto a two dimensional plane reserving the
 proper Hamming Distance between grid locations. This class relies heavily
 upon the OneDMap class and the corresponding KSquare class. This GUI has 
 been tailored specifically to work with the aforementioned classes in
 order to provide a visual represenation of a K-Map variant. 
 
 This GUI will accept user input for both the number of variables and the
 function of minterms in order to provide a graphical represenation that
 is maliable.
*/

   public class OneDGui extends JFrame{
   
   
   // Gui related variables.
      private JLabel FunctionBinary, VariableLabel, VariableTotal, PermutationTotal,
      	DontCareLabel, FunctionDecimal, essentialLabel;
      private JTextField FunctionBinaryInput, DontCareInput, VariableInput,
      	FunctionDecimalInput;
      private JTextArea essentialOutput;
      private JButton Update;
      private JPanel grid, interactions, updateArea, essentialGrid, primary;
      private String functionBinary, functionDecimal, dontCareTerms, function;
      private boolean funcInBinary, dontInBinary;
      private int variables, permutations;
      private int[] incrementalArray;
      private String[] essentialArray;
   
   // Mapping object.
      private OneDMap OneD;										//One dimesional map.
      private Minimizer minimizer;								//Minimization mechanism.
   
   
   /**
    Constructor for GUI.
   
    @param variables The numer of variables contained within
    the map.
   */
   
      public OneDGui(int numOfVariables) throws IOException{
      
      // Variable initialization.
         variables = numOfVariables;
         function = "000+001+011+111+110+100";
         dontCareTerms = "";
         permutations = (int)Math.pow(2, variables);
         funcInBinary = true;
         dontInBinary = true;
         
      // Lables.
         FunctionBinary = new JLabel("Function(Binary):");
         FunctionDecimal = new JLabel("Function(Decimal):");
         VariableLabel = new JLabel("Variables:");
         VariableTotal = new JLabel("Variable Total: "+variables);
         PermutationTotal = new JLabel("Permutation Total: "+permutations);
         DontCareLabel = new JLabel("'Don't Care' Terms(Binary): "); 
         essentialLabel = new JLabel("Essential Implicants:");
         
      // Fields.
         FunctionBinaryInput = new JTextField(20);
         FunctionDecimalInput = new JTextField(20);
         DontCareInput = new JTextField(20);
         VariableInput = new JTextField(5);
       
      // Buttons.
         Update = new JButton("Update");
         UpdateListener updateListener = new UpdateListener();
         Update.addActionListener(updateListener);
         
      // Panels.
         interactions = new JPanel();
         updateArea = new JPanel();
         primary = new JPanel();
         essentialGrid = new JPanel();	
         grid = new JPanel();	
      	
         establishGrid();
         establishEssentialGrid();
         compileComponents();
      }
   
   
   /**
    Establishes the grid within the gui serving as the visual
    representation of the map. Adds the current decimal and binary
    values for each grid sqaure as they are processed.
   */
      
      private void establishGrid(){
      
      // One Dimensional Map construction. 	
         OneD = new OneDMap(variables);
         incrementalArray = OneD.getModifiedIA();
      
         minimizer = new Minimizer(OneD, function, dontCareTerms, funcInBinary,
            dontInBinary);
         OneD = minimizer.returnMap();
         essentialArray = minimizer.getEssentialArray();
         essentialOutput = new JTextArea(minimizer.essentialsToString());
         
      
      // Grid designation with row and column assignment.   
         int rows, columns;
         if(variables%2.0 == 0){
            rows = (int)Math.pow(2, variables/2);
            columns = (int)Math.pow(2, variables/2);
         }
         else{
            rows = (int)Math.pow(2, variables/2);
            columns = (int)Math.pow(2, (variables/2)+1);
         }
         grid.removeAll();
         grid.setLayout(new GridLayout(rows, columns));
          
       // Tracking variables from one-to-two dimensional
       // translation.  
         int highStart = (2*rows)-1;
         int lowStart = 1;
         int tracker; 
       
       // Unpacking algorithm.
         for(int i = 0; i < rows; i++){
            tracker = i;
            KSquare currentSquare = OneD.getElement(tracker);
            JTextArea currentData = new JTextArea(" "+currentSquare.getDecimalValue()+
                  "\n "+currentSquare.getBinaryValue()+" ");
            if(currentSquare.getTruthValue() == true){
               currentData.setBorder(new MatteBorder(3, 3, 3, 3, Color.red));
               currentData.setBackground(new Color(255, 240, 240));
            }
            else if(currentSquare.getTruthValue() == false && currentSquare.getDontCareValue() == true){
               currentData.setBorder(new MatteBorder(3, 3, 3, 3, Color.blue));
               currentData.setBackground(new Color(240, 240, 255));
            }
            else{
               currentData.setBorder(new MatteBorder(3, 3, 3, 3, Color.black));
            }
            grid.add(currentData);      	
            
            for(int j = 0; j < (columns/2)-1; j++){
               currentSquare = OneD.getElement(tracker+=highStart);
               currentData = new JTextArea(" "+currentSquare.getDecimalValue()+
                  "\n "+currentSquare.getBinaryValue()+" ");
               if(currentSquare.getTruthValue() == true){
                  currentData.setBorder(new MatteBorder(3, 3, 3, 3, Color.red));
                  currentData.setBackground(new Color(255, 240, 240));
               }
               else if(currentSquare.getTruthValue() == false && currentSquare.getDontCareValue() == true){
                  currentData.setBorder(new MatteBorder(3, 3, 3, 3, Color.blue));
                  currentData.setBackground(new Color(240, 240, 255));
               }
               else{
                  currentData.setBorder(new MatteBorder(3, 3, 3, 3, Color.black));
               }
               grid.add(currentData);
               
               currentSquare = OneD.getElement(tracker+=lowStart);
               currentData = new JTextArea(" "+currentSquare.getDecimalValue()+
                  "\n "+currentSquare.getBinaryValue()+" ");
               if(currentSquare.getTruthValue() == true){
                  currentData.setBorder(new MatteBorder(3, 3, 3, 3, Color.red));
                  currentData.setBackground(new Color(255, 240, 240));
               }
               else if(currentSquare.getTruthValue() == false && currentSquare.getDontCareValue() == true){
                  currentData.setBorder(new MatteBorder(3, 3, 3, 3, Color.blue));
                  currentData.setBackground(new Color(240, 240, 255));
               }
               else{
                  currentData.setBorder(new MatteBorder(3, 3, 3, 3, Color.black));
               }
               grid.add(currentData);      
            
            }
            
            currentSquare = OneD.getElement(tracker+=highStart);
            currentData = new JTextArea(" "+currentSquare.getDecimalValue()+
                  "\n "+currentSquare.getBinaryValue()+" ");
            if(currentSquare.getTruthValue() == true){
               currentData.setBorder(new MatteBorder(3, 3, 3, 3, Color.red));
               currentData.setBackground(new Color(255, 240, 240));
            }
            else if(currentSquare.getTruthValue() == false && currentSquare.getDontCareValue() == true){
               currentData.setBorder(new MatteBorder(3, 3, 3, 3, Color.blue));
               currentData.setBackground(new Color(240, 240, 255));
            }
            else{
               currentData.setBorder(new MatteBorder(3, 3, 3, 3, Color.black));
            }
            grid.add(currentData);  
            
         // Tracking variable upkeep.	
            highStart -= 2;
            lowStart += 2;
         }   
      }
      
   
   /**
   
   */	
   
      public void establishEssentialGrid(){
      
            
      
      // Grid designation with row and column assignment.   
         int rows, columns;
         if(variables%2.0 == 0){
            rows = (int)Math.pow(2, variables/2);
            columns = (int)Math.pow(2, variables/2);
         }
         else{
            rows = (int)Math.pow(2, variables/2);
            columns = (int)Math.pow(2, (variables/2)+1);
         }
         essentialGrid.removeAll();
         essentialGrid.setLayout(new GridLayout(rows, columns));
          
       // Tracking variables from one-to-two dimensional
       // translation.  
         int highStart = (2*rows)-1;
         int lowStart = 1;
         int tracker; 
       
       // Unpacking algorithm.
         for(int i = 0; i < rows; i++){
            tracker = i;
            KSquare currentSquare = OneD.getElement(tracker);
            JTextArea currentData = new JTextArea(" "+currentSquare.getDecimalValue()+
                  "\n "+currentSquare.getBinaryValue()+" ");
               	
            int colorValue = findEssentialNumber(currentSquare);
            System.out.println(colorValue);		
            if(colorValue%5 == 0){
               currentData.setBorder(new MatteBorder(3, 3, 3, 3, Color.cyan));
               //currentData.setBackground(new Color(255, 240, 240));
            }
            else if(colorValue%5 == 1){
               currentData.setBorder(new MatteBorder(3, 3, 3, 3, Color.green));
               //currentData.setBackground(new Color(240, 240, 255));
            }
            else if(colorValue%5 == 2){
               currentData.setBorder(new MatteBorder(3, 3, 3, 3, Color.magenta));
            }
            else if(colorValue%5 == 3){
               currentData.setBorder(new MatteBorder(3, 3, 3, 3, Color.yellow));
            }
            else if(colorValue%5 == 4){
               currentData.setBorder(new MatteBorder(3, 3, 3, 3, Color.orange));
            }
            else{
               currentData.setBorder(new MatteBorder(3, 3, 3, 3, Color.black));
            }
            essentialGrid.add(currentData);      	
            
            for(int j = 0; j < (columns/2)-1; j++){
               currentSquare = OneD.getElement(tracker+=highStart);
               currentData = new JTextArea(" "+currentSquare.getDecimalValue()+
                  "\n "+currentSquare.getBinaryValue()+" ");
               colorValue = findEssentialNumber(currentSquare);
               System.out.println(colorValue);    
            		
               if(colorValue%5 == 0){
                  currentData.setBorder(new MatteBorder(3, 3, 3, 3, Color.cyan));
               //currentData.setBackground(new Color(255, 240, 240));
               }
               else if(colorValue%5 == 1){
                  currentData.setBorder(new MatteBorder(3, 3, 3, 3, Color.green));
               //currentData.setBackground(new Color(240, 240, 255));
               }
               else if(colorValue%5 == 2){
                  currentData.setBorder(new MatteBorder(3, 3, 3, 3, Color.magenta));
               }
               else if(colorValue%5 == 3){
                  currentData.setBorder(new MatteBorder(3, 3, 3, 3, Color.yellow));
               }
               else if(colorValue%5 == 4){
                  currentData.setBorder(new MatteBorder(3, 3, 3, 3, Color.orange));
               }
               else{
                  currentData.setBorder(new MatteBorder(3, 3, 3, 3, Color.black));
               }
               essentialGrid.add(currentData);
               
               currentSquare = OneD.getElement(tracker+=lowStart);
               currentData = new JTextArea(" "+currentSquare.getDecimalValue()+
                  "\n "+currentSquare.getBinaryValue()+" ");
               colorValue = findEssentialNumber(currentSquare);
               System.out.println(colorValue);     
               if(colorValue%5 == 0){
                  currentData.setBorder(new MatteBorder(3, 3, 3, 3, Color.cyan));
               //currentData.setBackground(new Color(255, 240, 240));
               }
               else if(colorValue%5 == 1){
                  currentData.setBorder(new MatteBorder(3, 3, 3, 3, Color.green));
               //currentData.setBackground(new Color(240, 240, 255));
               }
               else if(colorValue%5 == 2){
                  currentData.setBorder(new MatteBorder(3, 3, 3, 3, Color.magenta));
               }
               else if(colorValue%5 == 3){
                  currentData.setBorder(new MatteBorder(3, 3, 3, 3, Color.yellow));
               }
               else if(colorValue%5 == 4){
                  currentData.setBorder(new MatteBorder(3, 3, 3, 3, Color.orange));
               }
               else{
                  currentData.setBorder(new MatteBorder(3, 3, 3, 3, Color.black));
               }
               essentialGrid.add(currentData);      
            
            }
            
            currentSquare = OneD.getElement(tracker+=highStart);
            currentData = new JTextArea(" "+currentSquare.getDecimalValue()+
                  "\n "+currentSquare.getBinaryValue()+" ");
            colorValue = findEssentialNumber(currentSquare);
            System.out.println(colorValue);   	
         			
            if(colorValue%5 == 0){
               currentData.setBorder(new MatteBorder(3, 3, 3, 3, Color.cyan));
               //currentData.setBackground(new Color(255, 240, 240));
            }
            else if(colorValue%5 == 1){
               currentData.setBorder(new MatteBorder(3, 3, 3, 3, Color.green));
               //currentData.setBackground(new Color(240, 240, 255));
            }
            else if(colorValue%5 == 2){
               currentData.setBorder(new MatteBorder(3, 3, 3, 3, Color.magenta));
            }
            else if(colorValue%5 == 3){
               currentData.setBorder(new MatteBorder(3, 3, 3, 3, Color.yellow));
            }
            else if(colorValue%5 == 4){
               currentData.setBorder(new MatteBorder(3, 3, 3, 3, Color.orange));
            }
            else{
               currentData.setBorder(new MatteBorder(3, 3, 3, 3, Color.black));
            }
            essentialGrid.add(currentData);  
            
         // Tracking variable upkeep.	
            highStart -= 2;
            lowStart += 2;
         }  
      }
      
   
   /**
   
   */
   
      public int findEssentialNumber(KSquare currentSquare){
         for(int i = 0; i < minimizer.getEssentials(); i++){
            if(minimizer.encompasses(essentialArray[i], currentSquare.getBinaryValue())){
               return i;
            }
         }
         return -1;
      }	
   
   
   /**
    Places the components of the GUI within their respective
    panels. Also, sets size of GUI.
   */  
    
      private void compileComponents(){
         this.getContentPane().add(primary);
         
      // Primary panel.   
         primary.add(grid);
         primary.add(interactions);
         primary.add(updateArea);
         
      	//REDO THIS
         primary.add(essentialGrid);
         primary.add(essentialLabel);
         primary.add(essentialOutput);
         
      	
         primary.setPreferredSize(new Dimension(600, 600));
         
      // Data panel.	
         interactions.setLayout(new GridLayout(5, 2));
         interactions.add(FunctionBinary);
         interactions.add(FunctionBinaryInput);
         interactions.add(FunctionDecimal);
         interactions.add(FunctionDecimalInput);
         interactions.add(DontCareLabel);
         interactions.add(DontCareInput);
         interactions.add(VariableLabel);
         interactions.add(VariableInput);
         interactions.add(VariableTotal);
         interactions.add(PermutationTotal);
         
      // Update panel.	
         updateArea.setLayout(new GridLayout(1,1));
         updateArea.add(Update);
      }
   
   
   /**
    Update Listener for changing the map, function, 'don't care' terms,
    and number of variables.
   */
   
      private class UpdateListener implements ActionListener{
      
         public void actionPerformed(ActionEvent event){
         
            functionBinary = FunctionBinaryInput.getText().trim();
            functionDecimal = FunctionDecimalInput.getText().trim();
            if(!functionBinary.equals("")){
               function = functionBinary;
               funcInBinary = true;
            }
            else{
               function = functionDecimal;
               funcInBinary = false;
            }
            dontCareTerms = DontCareInput.getText().trim();
            variables = Integer.parseInt(VariableInput.getText());
            permutations = (int)Math.pow(2, variables);
            
            establishGrid();
            establishEssentialGrid();
            VariableTotal.setText("Variable Total: "+variables);
            PermutationTotal.setText("Permutation Total: "+permutations);
            grid.updateUI();
            essentialGrid.updateUI();
            essentialOutput.updateUI();
            interactions.updateUI();
            /*
            primary.remove(essentialOutput);
            primary.updateUI();
            essentialOutput = new JTextArea(minimizer.essentialsToString());
            primary.add(essentialOutput);
            */
            essentialOutput.setText(minimizer.essentialsToString());
            primary.removeAll();
            primary.add(grid);
            primary.add(interactions);
            primary.add(updateArea);
         
         //REDO THIS
            primary.add(essentialGrid);
            primary.add(essentialLabel);
            primary.add(essentialOutput);
         	
            primary.updateUI(); 
         }
      
      }
   
   
   /**
    Ad-hoc driver.
   */
      
      public static void main(String[] args) throws IOException{
         
         OneDGui Map = new OneDGui(3);
         
         Map.setTitle("Mappus Karnaughus");
         Map.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       
         Map.pack();
         Map.setVisible(true);
      }
   }