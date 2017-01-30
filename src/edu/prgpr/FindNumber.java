/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.prgpr;

/**
 *
 * @author Saj, Joanna *
 */

public class FindNumber {

     /**
      * The class parsed a string to two strings - streetname and housenumber
      */

     /**
      * the the whole string
      */
      String string;
     /**
      * the part of the string with the housenumber
      */
     String numberseq;
      /**
      * the part of the string with the streetname
      */
     String streetseq;

     /**
      * constructor
      *
      * @param str the whole string to parse
      */
     FindNumber(String str){
        this.string = str;
        this.findNumber(string);
     }

    /**
     *  @param str the string to parse
     */
    private void findNumber(String str){

        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        char charr;
        char charStreet;
        char charNumber;
        int index = 0;

        for(int i=0;i<str.length();i++){ 
             charr = str.charAt(i);
             if(charr == ';'){
                index = i;
             }
        }

        for(int j=0;j<index;j++){
            charStreet = str.charAt(j);
            sb1.append(charStreet);

        }

        for(int k=index+1;k<str.length();k++){
            charNumber = str.charAt(k);
            sb2.append(charNumber);

        }

         streetseq = sb1.toString();
         numberseq = sb2.toString();
        }

    /**
     *
     * @return the streetname
     */
    public String returnStreet(){

        return streetseq;
    }
    /**
     *
     * @return the number as a string
     */
    public String returnNumber(){

        return numberseq;
    }
    }


