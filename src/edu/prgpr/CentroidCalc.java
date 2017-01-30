/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.prgpr;

/**
 *
 * @author Saj, Joanna
 */
public class CentroidCalc {
    /**
     * the class calculated the center point of a building
     */

   /**
    * empty constructor
    */
   public CentroidCalc(){
       
   }
   /**
    *
    * @param ary the array
    * @return the area of the building
    */
    public double sum(double [] ary){
        
        int sum = 0;
        for(int i=0;i<ary.length;i++){
            sum += ary[i];
         }
        
        return sum;
     }
    /**
     *
     * @param way the MapWay there is a building
     * @return the coordinates of the center of the building
     */
    public  double[] centroid(MapWay way){
        double area = 0;
        double xCenter = 0;
        double yCenter = 0;
        int size = way.wayNodes.size();
        double [] xCoord = new double[size];
        double [] yCoord = new double[size];
        
        
        for(int i=0;i<way.wayNodes.size();i++){
            xCoord[i] = way.wayNodes.get(i).getX_Coordinate();
            yCoord[i] = way.wayNodes.get(i).getY_Coordinate();
          }

        double [] array= new double [size];
        
        for(int i=0;i<size-1;i++){
            
           array[i] = (xCoord[i]*yCoord[i+1] - xCoord[i+1]*yCoord[i]);
         }
        
        area = 0.5*this.sum(array);

        double [] array1 = new double [size];
        
        for(int i=0;i<size-1;i++){
            
           array1[i] = xCoord[i]*yCoord[i+1] - xCoord[i+1]*yCoord[i];

          }
       
       xCenter = (1/(6*area))*this.sum(array);
       
       double [] array2 = new double [size];
        
       for(int i=0;i<size-1;i++){
            
           array2[i] = xCoord[i]*yCoord[i+1] - xCoord[i+1]*yCoord[i];
           
        }
        
       yCenter = (1/(6*area))*this.sum(array);

       double [] centroidCoor = new double[2];

       centroidCoor[0] = xCenter;
       centroidCoor[1] = yCenter;
       
       return centroidCoor;

    }
    

}
