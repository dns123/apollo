/* This program is used to normalize the data as per the requirement of the activation function used in Neural.java   Here in
this case we have used bipolar logistic function as the activation function which requires the data within the range
[-1,1].  So for any real life problem if we wish to use MLP, we require the data to be within the range [-1,1].  For
this purpose, we have used linear map (a line joining two points given by equation (y-y1)/(y2-y1)=(x-x1)/(x2-x1)=l)
*/




import java.io.*;
import java.util.Scanner;
import java.util.Vector;
public class Normalizer {
	private double [][] tiresData;
	private double [][] normalData;
	private double [][] remapData;
	private int numberofInputs;
	private int numberofOutputs;
	private int numberofPatterns;
	private int numberOfTestPatterns;
	private int numberOfTrainPatterns;	
	private double inMin, inMax, outMin, outMax;
	private double [][] inputs;
	private double [][] outputs;
	private double [][] normInputs;
	private double [][] normOutputs;
	private double[] baseCoordinates;
	
	public Normalizer (int ni, int no, int np) {
		numberofInputs = ni;
		numberofOutputs = no;
		numberofPatterns = np;
		tiresData = new double [numberofPatterns] [numberofInputs+numberofOutputs];
		inputs = new double [numberofPatterns] [numberofInputs];
		outputs = new double [numberofPatterns] [numberofOutputs];
		baseCoordinates = new double[numberofInputs];
	}
	
	public Normalizer (String dataFile) throws IOException {
		System.out.println("Reading data from "+dataFile);
		Scanner sc = new Scanner(new File(dataFile));
		numberofInputs = sc.nextInt();
		numberofOutputs = sc.nextInt();
		numberofPatterns = sc.nextInt();
		tiresData = new double [numberofPatterns] [numberofInputs+numberofOutputs];
		inputs = new double [numberofPatterns] [numberofInputs];
		outputs = new double [numberofPatterns] [numberofOutputs];
		baseCoordinates = new double[numberofInputs];
		for (int i=0; i<numberofInputs; i++) {
			baseCoordinates[i] = sc.nextDouble();
		}
		
		for (int i=0; i<numberofPatterns; i++) {
			for (int j=0; j<numberofInputs+numberofOutputs ; j++) {
				tiresData[i][j] = sc.nextDouble();
				if ( j < numberofInputs)  
					inputs [i][j] = tiresData [i][j] - baseCoordinates[j];
				else
					outputs [i][j-numberofInputs] = tiresData [i][j];
				
			}
		}		
	}

	public Normalizer () {
		this (6,5,135);
	}

	public void readData () throws IOException  {
		String dataFile = "tirespat.txt";
		System.out.println("Reading data from "+dataFile);
		Scanner sc = new Scanner(new File(dataFile));
		numberofInputs = sc.nextInt();
		numberofOutputs = sc.nextInt();
		numberofPatterns = sc.nextInt();
		for (int i=0; i<numberofPatterns; i++) {
			for (int j=0; j<numberofInputs+numberofOutputs ; j++) {
				tiresData[i][j] = sc.nextDouble();
				if ( j < numberofInputs)  
					inputs [i][j] = tiresData [i][j];
				else
					outputs [i][j-numberofInputs] = tiresData [i][j];				
			}
		}
	}
	
	public void dividePatterns(String patFileName, int testPercent ) throws IOException {
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
		numberOfTestPatterns = numberOfPatterns * testPercent / 100;
		numberOfTrainPatterns = numberOfPatterns - numberOfTestPatterns;
		System.out.println(numberOfTestPatterns+ " patterns in file "+  testFileName);
		System.out.println(numberOfTrainPatterns+ " patterns in file "+  trainFileName);
		
		FileWriter fw = new FileWriter( testFileName);
		PrintWriter testw = new PrintWriter( fw);
		fw = new FileWriter( trainFileName);
		PrintWriter trainw = new PrintWriter( fw);		
		
		Vector<Integer> moved = new Vector<Integer>() ;
		int np = 0 ;
		String s = numberofInputs+" "+ numberofOutputs+" "+ numberOfTestPatterns;
		testw.println(s);
		while (np < numberOfTestPatterns ) {
			int rp =(int) Math.round( Math.random() * numberOfPatterns );
			if( rp >= numberOfPatterns) 
			continue;
			//System.out.println("*&^%***************** "+rp);
			if ( ! ( moved.contains ( rp ) )) {
				moved.add(rp) ;
				patternString = patterns.get(rp); 
				testw.println(patternString);
				np ++ ;
			}
		}

		s = numberofInputs+" "+ numberofOutputs+" "+ numberOfTrainPatterns;
		trainw.println(s);
		for( int p=0; p < numberOfPatterns; p ++ ) {
			if ( ! ( moved.contains ( p ) ) ) {
				patternString = patterns.get(p); 
				trainw.println(patternString);
			}
		}
		br.close();
		fr.close();
		testw.close();
		trainw.close();
		fw.close();
		
	}
	
	public double[] remap( double[] pat ) {					// for remapping of input and output both....

		double[] repat = new double[numberofInputs + numberofOutputs];
		for ( int i =0; i< numberofInputs; i++ ) {
			repat[i] =  (inMax - inMin) * ((pat[i]+1)/2) + inMin + baseCoordinates[i];
		}
		for ( int i =numberofInputs; i< numberofInputs+numberofOutputs; i++ ) {
			repat[i] =  (outMax - outMin) * ((pat[i]+1)/2) + outMin;
		}
		
		/*for ( int i =0; i< numberofInputs+numberofOutputs; i++ ) {
				System.out.println("the remapped data is "+ repat[i] );
		}*/
		return repat;
	}
	public double[] remap1( double[] pat ) {				// for remapping of output only..
		double[] repat = new double[numberofOutputs];
		for ( int i =0; i< numberofOutputs; i++ ) {
			repat[i] =  (outMax - outMin) * ((pat[i]+1)/2) + outMin;
		}
		
		/*for ( int i =0; i< numberofOutputs; i++ ) {
				System.out.println("the remapped data for output only is "+ repat[i] );
		}*/
		return repat;
	}
	
	
	public void minmax() {
		double inmin,inmax,outmin,outmax;
		inmin = inmax = inputs [0][0];
		outmin= outmax= outputs[0][0];
		for (int i=0; i<numberofPatterns; i++) {
			for (int j=0; j<numberofInputs; j++) {
				if (inmin > inputs [i][j])
					inmin = inputs [i][j];
				if (inmax < inputs [i][j])
					inmax = inputs [i][j];
			}
			
			for (int j=0; j<numberofOutputs; j++) {
				if (outmin > outputs [i][j])
					outmin = outputs [i][j];
				if (outmax < outputs [i][j])
					outmax = outputs [i][j];
			}
		}
		inMin = inmin;
		inMax = inmax;
		outMin = outmin;
		outMax = outmax;
				
	}
	
	public double[] getNormalizationParameters() {
		double [] npm = new double[4];
		npm[0]=inMin;
		npm[1]=inMax;
		npm[2]=outMin;
		npm[3]=outMax;
		return npm;
	}
	

	
	public void normalize() {
		minmax();
		double pmax = inMax ;
		double pmin = inMin ;
		double xmax = outMax ;
		double xmin = outMin ;
		
		normInputs = new double [numberofPatterns] [numberofInputs];
		for (int i=0; i<numberofPatterns; i++) {
			for (int j=0; j<numberofInputs; j++) {
				normInputs[i][j] = ((2*(inputs[i][j] - pmin)) / (pmax - pmin)) - 1.0;
			}
		}
		
		normOutputs = new double [numberofPatterns] [numberofOutputs];
		for (int i=0; i<numberofPatterns; i++) {
			for (int j=0; j<numberofOutputs; j++) {
				normOutputs[i][j] = ((2*(outputs[i][j] - xmin)) / (xmax - xmin)) - 1.0;
			}
		}
		
		normalData = new double [numberofPatterns] [numberofInputs+numberofOutputs];
		for (int i=0; i<numberofPatterns; i++) {
			for (int j=0; j<numberofInputs+numberofOutputs; j++) {
				if (j<numberofInputs)
					normalData [i][j] = normInputs [i][j];
				else 
					normalData [i][j] = normOutputs [i][j-numberofInputs];
			}
		}
		
	}
	
	
	public void print() throws IOException {
		String fileName = "normtirespat2.txt";  // input for MLP is normalised diff between original and inflated coordinates.
		FileWriter fw = new FileWriter( fileName);
		PrintWriter pw = new PrintWriter( fw);
	
		for (int i=0; i<numberofPatterns; i++) {
			for (int j=0; j<numberofInputs+numberofOutputs; j++) {
				pw.print(normalData[i][j]+"  ");
			}
			pw.println();	
		}
		pw.close();
		fw.close();
		
	}
	
		
	public static void main (String [] args) throws IOException  {
		Normalizer n = new Normalizer ("tirespat.txt");
		//n.readData();
		n.normalize();	
		n.dividePatterns("normtirespat2.txt",20);	
		n.print();
	}
}
