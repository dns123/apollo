import java.io.*;
import java.util.Scanner;
import java.util.Vector;

public class VerifyPatterns {
	public static void main (String[] args ) throws IOException {
		Scanner sc = new Scanner(System.in);
		System.out.println(" Type name of pattern file " );
 	    	//String patFileName = sc.next();
 	    	String patFileName = "kakhadata.txt";
 	    	File patFile = new File(patFileName);
 	    	if ( ! ( patFile.exists() && patFile.canRead() ) ) {
			System.out.println(patFileName+" Does not exist or is not readable " );
			System.exit(0) ;
		}
		System.out.println(" Type the number of patterns " );
 	    	//int patCount =  sc.nextInt();
 	    	int patCount =  84;
		System.out.println(" Type the number of inputs in each pattern " );
 	    	//int ni =  sc.nextInt();
 	    	int ni =  35;
		System.out.println(" Type the number of outputs in each pattern " );
 	    	//int no =  sc.nextInt();
 	    	int no =  7;
 	    	sc.close();
		int[][] patArray = new int[patCount][ni+no]; 	    	
		FileReader fr = new FileReader( patFileName);
		BufferedReader br = new BufferedReader( fr);
		String patternString = br.readLine() ;
		int currentPattern = 0;
		while ( patternString != null ) {		
			System.out.println(patternString);
			sc = new Scanner(patternString);
			for (int i =0; i<ni+no; i++ ) {
				if(sc.hasNextInt()) {
					patArray[currentPattern][i] = sc.nextInt();
				}
				else {
					System.out.println("Not enough values in pattern no. "+(currentPattern + 1 ));
					//System.exit(0);
				}
			}
			patternString = br.readLine() ;
			currentPattern ++;
		}
		System.out.println(currentPattern+ " patterns read from file "+  patFileName+" == "+patCount);		
		
		sc.close();
		br.close();
		fr.close();
		//for ( int p =0 ; p<patCount ; p ++ ) {
		for ( int p =0 ; p<5 ; p ++ ) {
			System.out.println(" Character representation of pattern "+ (p+1));
			//char symbol = 'O' ;
			String symbol1 = "OO" ;
			String symbol2 = "  " ;
			int element = 0;
			for ( int r = 0; r < 7 ; r++ ) {
				for ( int c = 0; c < 5 ; c++ ) {
					//System.out.print(patArray[p][element]);
					if ( patArray[p][element] == 1 ) {
						System.out.print(symbol1);
					}
					else {
						System.out.print(symbol2);
					}
					element ++ ;
				}
				System.out.println();
			}
			System.out.println();
			System.out.println();
		}
			
	}
}	

