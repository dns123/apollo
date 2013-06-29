import java.io.*;
import java.util.Scanner;
public class Remap {
	private double [][] tiresData;
	private double [][] rtiresData;
	private double [][] remapData;
	private int numberofInputs;      // For testfile (terstnormtirespat2.txt)
	private int numberofOutputs;
	private int numberofPatterns;
	private int numberOfInputs;      // For original data file (tirespat.txt)
	private int numberOfOutputs;
	private int numberOfPatterns;
	private double [][] inputs;   //   inputs and outputs are for original data tirespat.txt to calculate pmax,pmin.
	private double [][] outputs;
	private double [][] rinputs;  //  rinputs and routputs are for input and output data from the reinormtirespat2.txt to remap
	private double [][] routputs;
	private double [][] remapInputs;
	private double [][] remapOutputs;
	private double[] baseCoordinates;
		
	public Remap (int ni, int no, int np) {
		numberofInputs = ni;
		numberofOutputs = no;
		numberofPatterns = np;
		tiresData = new double [numberofPatterns] [numberofInputs+numberofOutputs];
		inputs = new double [numberofPatterns] [numberofInputs];
		outputs = new double [numberofPatterns] [numberofOutputs];
		baseCoordinates = new double[numberofInputs];
	}
	
	public Remap (String dataFile) throws IOException {
		System.out.println("Reading data from "+dataFile);
		Scanner sc = new Scanner(new File(dataFile));
		numberofInputs = sc.nextInt();
		numberofOutputs = sc.nextInt();
		numberofPatterns = sc.nextInt();
		rtiresData = new double [numberofPatterns] [numberofInputs+numberofOutputs];
		rinputs = new double [numberofPatterns] [numberofInputs];
		routputs = new double [numberofPatterns] [numberofOutputs];
		/*baseCoordinates = new double[numberofInputs];
		for (int i=0; i<numberofInputs; i++) {
			baseCoordinates[i] = sc.nextDouble();
		}*/
		
		for (int i=0; i<numberofPatterns; i++) {
			for (int j=0; j<numberofInputs+numberofOutputs ; j++) {
				rtiresData[i][j] = sc.nextDouble();
				if ( j < numberofInputs)  
					rinputs [i][j] = rtiresData [i][j] ;
				else
					routputs [i][j-numberofInputs] = rtiresData [i][j];
				
			}
		}		
		
	}

	public Remap () {
		this (6,5,135);
	}

	public void readData () throws IOException  {
		String dataFile = "tirespat.txt";
		System.out.println("Reading data from "+dataFile);
		Scanner sc1 = new Scanner(new File(dataFile));
		numberOfInputs = sc1.nextInt();
		numberOfOutputs = sc1.nextInt();
		numberOfPatterns = sc1.nextInt();
		baseCoordinates = new double[numberOfInputs];
		for (int i=0; i<numberOfInputs; i++) {
			baseCoordinates[i] = sc1.nextDouble();
		}

		
		tiresData = new double [numberOfPatterns] [numberOfInputs+numberOfOutputs];
		inputs = new double [numberOfPatterns] [numberOfInputs];
		outputs = new double [numberOfPatterns] [numberOfOutputs];
				
		for (int i=0; i<numberOfPatterns; i++) {
			for (int j=0; j<numberOfInputs+numberOfOutputs ; j++) {
				tiresData[i][j] = sc1.nextDouble();
				if ( j < numberOfInputs)  
					inputs [i][j] = tiresData [i][j];
				else
					outputs [i][j-numberOfInputs] = tiresData [i][j];
				
			}
		}
		
/*		inputs = new double [numberofPatterns] [numberofInputs];
		for (int i=0; i<numberofPatterns; i++) {
			for (int j=0; j<numberofInputs; j++) {
				inputs [i][j] = tiresData [i][j];
			}
		}
		
		outputs = new double [numberofPatterns] [numberofOutputs];
		for (int i=0; i<numberofPatterns; i++) {
			for (int j=0; j<numberofOutputs; j++) {
				outputs [i][j] = tiresData [i][j+numberofInputs];
			}
		} */
	}
	
	public double inmax(double [][] inp) {
		double inmax = inp [0][0];
		for (int i=0; i<numberofPatterns; i++) {
			for (int j=0; j<numberofInputs; j++) {
				if (inmax < inp [i][j])
					inmax = inp [i][j];
			}
		}
		return inmax;
	}
	
	public double inmin(double [][] inp) {
		double inmin = inp [0][0];
		for (int i=0; i<numberofPatterns; i++) {
			for (int j=0; j<numberofInputs; j++) {
				if (inmin > inp [i][j])
					inmin = inp [i][j];
			}
		}
		return inmin;
	}
	
	public double outmax(double [][] out) {
		double outmax = out [0][0];
		for (int i=0; i<numberofPatterns; i++) {
			for (int j=0; j<numberofOutputs; j++) {
				if (outmax < out [i][j])
					outmax = out [i][j];
			}
		}
		return outmax;
	}
	
	public double outmin(double [][] out) {
		double outmin = out [0][0];
		for (int i=0; i<numberofPatterns; i++) {
			for (int j=0; j<numberofOutputs; j++) {
				if (outmin > out [i][j])
					outmin = out [i][j];
			}
		}
		return outmin;
	}
	
	public void rmap() {
		double pmax = inmax (inputs);
		double pmin = inmin (inputs);
		double xmax = outmax (outputs);
		double xmin = outmin (outputs);
		
		remapInputs = new double [numberofPatterns] [numberofInputs];
		for (int i=0; i<numberofPatterns ; i++) {
			for (int j=0; j<numberofInputs; j++) {
				remapInputs[i][j] = (pmax-pmin) * ((rinputs[i][j]+1)/2) + pmin;
			}
		}
		
		remapOutputs = new double [numberofPatterns] [numberofOutputs];
		for (int i=0; i<numberofPatterns; i++) {
			for (int j=0; j<numberofOutputs; j++) {
				remapOutputs[i][j] = (xmax-xmin) * ((routputs[i][j]+1)/2) + xmin;
			}
		}
		
		remapData = new double [numberofPatterns] [numberofInputs+numberofOutputs];
		for (int i=0; i<numberofPatterns; i++) {
			for (int j=0; j<numberofInputs+numberofOutputs; j++) {
				if (j<numberofInputs)
					remapData [i][j] = remapInputs [i][j];
				else 
					remapData [i][j] = remapOutputs [i][j-numberofInputs];
			}
		}
		
	}
	
	
	public void rprint() throws IOException {
		String fileName = "remaptestfile.txt";
		FileWriter fw = new FileWriter( fileName);
		PrintWriter pw = new PrintWriter( fw);
		for (int i=0; i<numberofPatterns; i++) {
			for (int j=0; j<numberofInputs+numberofOutputs; j++) {
				pw.print(remapData[i][j]+"  ");
			}
			pw.println();	
		}
		pw.close();
		fw.close();
		
		/*for (int i=0; i<numberofPatterns; i++) {
			for (int j=0; j<numberofInputs+numberofOutputs; j++) {
				System.out.print(normalData[i][j]);
			}
			System.out.println();	
		}
		for (int i=0; i<numberofPatterns; i++) {
			for (int j=0; j<numberofInputs; j++) {
				System.out.print(normInputs [i][j]) ;
			}
			System.out.println();
		}
		for (int i=0; i<numberofPatterns; i++) {
			for (int j=0; j<numberofOutputs; j++) {
				System.out.print(normOutputs [i][j]);
			}
			System.out.println();
		}
		for (int i=0; i<numberofPatterns; i++) {
			for (int j=0; j<numberofInputs+numberofOutputs; j++) {
				System.out.print(tiresData[i][j]);
			}	
		}
		for (int i=0; i<numberofPatterns; i++) {
			for (int j=0; j<numberofInputs; j++) {
				System.out.print(inputs [i][j]) ;
			}
			System.out.println();
		}
		for (int i=0; i<numberofPatterns; i++) {
			for (int j=0; j<numberofOutputs; j++) {
				System.out.print(outputs [i][j]);
			}
			System.out.println();
		}*/
	}
	
		
	public static void main (String [] args) throws IOException  {
		Remap n = new Remap ("testnormtirespat2.txt");
		n.readData();
		n.rmap();		
		n.rprint();
	}
}
