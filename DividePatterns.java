import java.io.*;
import java.util.Scanner;
import java.util.Vector;

public class DividePatterns {
	public static void main (String[] args ) throws IOException {
		Scanner sc = new Scanner(System.in);
		System.out.println(" Type name of patterns in file " );
 	    	String patFileName = sc.next();
 	    	File patFile = new File(patFileName);
 	    	if ( ! ( patFile.exists() && patFile.canRead() ) ) {
			System.out.println(patFileName+" Does not exist or is not readable " );
			System.exit(0) ;
		}
		System.out.println(" Type the percentage of patterns to be transferred to test file " );
 	    	int testPercent =  sc.nextInt();
 	    	if ( testPercent < 10 || testPercent > 50 ) {
 	    		testPercent = 20 ;
 	    	}
 	    	String testFileName = "test"+patFileName;
 	    	String trainFileName = "train"+patFileName;  	    	
		System.out.println(testPercent+ " % of patterns to be moved to file  "+testFileName+ " remaining to "+trainFileName) ;
		FileReader fr = new FileReader( patFileName);
		BufferedReader br = new BufferedReader( fr);
		Vector <String> patterns = new Vector <String>() ;
		String patternString = br.readLine() ;
		while ( patternString != null ) {
			patterns.add( patternString ) ;
			patternString = br.readLine() ;
		}
		int numberOfPatterns = patterns.size();
		int numberOfTestPatterns = numberOfPatterns * testPercent / 100;
		int numberOfTrainPatterns = numberOfPatterns - numberOfTestPatterns;
		System.out.println(numberOfTestPatterns+ " patterns in file "+  testFileName);
		System.out.println(numberOfTrainPatterns+ " patterns in file "+  trainFileName);
		
		FileWriter fw = new FileWriter( testFileName);
		PrintWriter testw = new PrintWriter( fw);
		fw = new FileWriter( trainFileName);
		PrintWriter trainw = new PrintWriter( fw);		
		
		Vector<Integer> moved = new Vector<Integer>() ;
		int np = 0 ;
		while (np < numberOfTestPatterns ) {
			int rp =(int) Math.round( Math.random() * numberOfPatterns );
			if ( ! ( moved.contains ( rp ) )) {
				moved.add(rp) ;
				patternString = patterns.get(rp); 
				testw.println(patternString);
				np ++ ;
			}
		}
		for( int p=0; p < numberOfPatterns; p ++ ) {
			if ( ! ( moved.contains ( p ) ) ) {
				patternString = patterns.get(p); 
				trainw.println(patternString);
			}
		}
		
		sc.close();
		br.close();
		fr.close();
		testw.close();
		trainw.close();
		fw.close();
		
	}
}	

