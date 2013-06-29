import java.io.*;
import java.util.Scanner;
public class Remap1 {
	private double [][] tiresData;
	private double [][] rtiresData;
	private double [][] remapData;
	private int numberofInputs;
	private int numberofOutputs;
	private int numberofPatterns;
	private int renumberofInputs;
	private int renumberofOutputs;
	private int renumberofPatterns;
	private double inMin, inMax, outMin, outMax;
	private double [][] inputs;   //   inputs and outputs are for original data tirespat.txt to calculate pmax,pmin.
	private double [][] outputs;
	private double [][] rinputs;  //  rinputs and routputs are for input and output data from the reinormtirespat2.txt to remap
	private double [][] routputs;
	private double [][] remapInputs;
	private double [][] remapOutputs;
	private double[] baseCoordinates;
		
	public Remap1 (int ni, int no, int np) {
		numberofInputs = ni;
		numberofOutputs = no;
		numberofPatterns = np;
		tiresData = new double [numberofPatterns] [numberofInputs+numberofOutputs];
		inputs = new double [numberofPatterns] [numberofInputs];
		outputs = new double [numberofPatterns] [numberofOutputs];
		baseCoordinates = new double[numberofInputs];
	}
	
	public Remap1 (String dataFile) throws IOException {
		System.out.println("Reading data from "+dataFile);
		Scanner sc = new Scanner(new File(dataFile));
		renumberofInputs = sc.nextInt();
		renumberofOutputs = sc.nextInt();
		renumberofPatterns = sc.nextInt();
		//rtiresData = new double [numberofPatterns] [numberofInputs+numberofOutputs];
		//rinputs = new double [numberofPatterns] [numberofInputs];
		routputs = new double [renumberofPatterns] [renumberofOutputs];
		/*baseCoordinates = new double[numberofInputs];
		for (int i=0; i<numberofInputs; i++) {
			baseCoordinates[i] = sc.nextDouble();
		}*/
		
		for (int i=0; i<renumberofPatterns; i++) {
			for (int j=0; j<renumberofOutputs ; j++) {
				routputs[i][j] = sc.nextDouble();
			}
		}		
	}

	public Remap1 () {
		this (6,5,135);
	}

	public void readData () throws IOException  {
		String dataFile = "tirespat2.txt";
		System.out.println("Reading data from "+dataFile);
		Scanner sc1 = new Scanner(new File(dataFile));
		numberofInputs = sc1.nextInt();
		numberofOutputs = sc1.nextInt();
		numberofPatterns = sc1.nextInt();
		//System.out.println(numberofInputs +"   " +numberofOutputs+"  "+numberofPatterns);
		
		tiresData = new double [numberofPatterns] [numberofInputs+numberofOutputs];
		inputs = new double [numberofPatterns] [numberofInputs];
		outputs = new double [numberofPatterns] [numberofOutputs];
				
		for (int i=0; i<numberofPatterns; i++) {
			for (int j=0; j<numberofInputs+numberofOutputs ; j++) {
				tiresData[i][j] = sc1.nextDouble();
				if ( j < numberofInputs)  
					inputs [i][j] = tiresData [i][j];
				else
					outputs [i][j-numberofInputs] = tiresData [i][j];
				
			}
		}
		
	}
	
	public void minmax() {
		double inmin,inmax,outmax,outmin;
		inmin=inmax = rinputs [0][0];
		outmin=outmax= routputs [0][0];
		for (int i=0; i < renumberofPatterns; i++) {
			for (int j=0; j < renumberofInputs; j++) {
				if (inmax < rinputs [i][j])
					inmax = rinputs [i][j];
				if (inmin > rinputs [i][j])
					inmin = rinputs [i][j];
			}
			for (int j=0; j < renumberofOutputs; j++) {
				if (outmin > routputs [i][j])
					outmin = routputs [i][j];
				if (outmax < routputs [i][j])
					outmax = routputs [i][j];
			}
		}
		inMin = inmin;
		inMax = inmax;
		outMin = outmin;
		outMax = outmax;	
	}
	
	public void rmap() {
		minmax();
		
		/*remapInputs = new double [numberofPatterns] [numberofInputs];
		for (int i=0; i<numberofPatterns; i++) {
			for (int j=0; j<numberofInputs; j++) {
				remapInputs[i][j] = (pmax-pmin) * ((rinputs[i][j]+1)/2) + pmin;
			}
		}*/
		
		remapOutputs = new double [renumberofPatterns] [renumberofOutputs];
		for (int i=0; i<renumberofPatterns; i++) {
			for (int j=0; j<renumberofOutputs; j++) {
				remapOutputs[i][j] = (outMax-outMin) * ((routputs[i][j]+1)/2) + outMin;
			}
		}
		
		/*remapData = new double [numberofPatterns] [numberofInputs+numberofOutputs];
		for (int i=0; i<numberofPatterns; i++) {
			for (int j=0; j<numberofInputs+numberofOutputs; j++) {
				if (j<numberofInputs)
					remapData [i][j] = remapInputs [i][j];
				else 
					remapData [i][j] = remapOutputs [i][j-numberofInputs];
			}
		}*/
		
	}
	
	
	public void print() throws IOException {
		String fileName = "remapoutdata.txt";
		FileWriter fw = new FileWriter( fileName);
		PrintWriter pw = new PrintWriter( fw);
		for (int i=0; i<renumberofPatterns; i++) {
			for (int j=0; j<renumberofOutputs; j++) {
				pw.print(remapOutputs[i][j]+"  ");
			}
			pw.println();	
		}
		pw.close();
		fw.close();
		
	}
	
		
	public static void main (String [] args) throws IOException  {
		Remap1 n = new Remap1 ("output.txt");
		n.readData();
		n.rmap();		
		n.print();
	}
}
