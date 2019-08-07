/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package desafiojava;

import java.util.Scanner;

/**
 *
 * @author 081170007
 */
public class DesafioJava {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        // RECEBE VALORES E MONTA A SEQUENCIA
        int s = 0;        

       Scanner in = new Scanner(System.in); 
       System.out.printf("Enter the maximum value to your sequence:  ");
       int n = in.nextInt();
       System.out.printf("Enter the starter value to a(normally it's 1):  ");
       int a = in.nextInt();
       System.out.printf("Enter the starter value to b(normally it's zero): ");
       int b = in.nextInt();
       
        while(s + b < n){
         s = a + b;
         System.out.println(s);
         b = a;
         a = s;                 
        }
    }
    
}

