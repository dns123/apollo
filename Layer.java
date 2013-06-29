import java.io.*;
import java.util.Scanner;
public class Layer {
	private int numberOfNeurons ;
	private int numberOfInputs ;
	private int numberOfPatterns;
	private Neuron[] neurons;
	private double[] currentInputs;
	private double[] currentOutputs;
	private double[][] allOutputs;
	private double[][] patterns;
	private double[][] weights;
	private String label;
	
	public Layer(int neu, int inps, int pats, String lbl) {
		numberOfNeurons = neu;
		numberOfInputs = inps ;
		numberOfPatterns=pats;
		neurons = new Neuron[numberOfNeurons];
		for ( int n = 0; n < numberOfNeurons ; n++ ) {
			neurons[n] = new Neuron(inps,pats,lbl+(n+1));
		}
		patterns = new double[numberOfPatterns][numberOfInputs+numberOfNeurons];
		currentInputs = new double[numberOfInputs+1];
		currentOutputs = new double[numberOfNeurons];
		allOutputs = new double[numberOfPatterns][numberOfNeurons];
		weights = new double[numberOfNeurons][numberOfInputs+1];
		label = lbl;
	}

	public Layer(int neu, int inp) {
		this(neu,inp,4,"No Name");
		double[][] pats = { {-1, -1, -1, -1}, {-1, 1, 1, -1}, {1, -1, 1, -1}, {1, 1, 1, 1} };
		patterns=pats;
	}

	public Layer() {
		this(2,2);
	}

	public void readLayer() throws IOException {
		Scanner sc = new Scanner(new File("layerdata2.txt"));
		System.out.println("Reading Layer of Perceptrons data from layerdata.txt");
		numberOfNeurons = sc.nextInt();
		numberOfInputs = sc.nextInt();
		numberOfPatterns = sc.nextInt();
		patterns = new double[numberOfPatterns][numberOfInputs+numberOfNeurons];
		for ( int p = 0; p < numberOfPatterns; p++) {
			for ( int i = 0; i < numberOfInputs+numberOfNeurons ; i ++ ) {
				patterns[p][i] = sc.nextDouble();
			}
		}
		weights = new double[numberOfNeurons][numberOfInputs+1];
		for ( int n = 0; n < numberOfNeurons ; n++ ) {
			for ( int i = 0; i <= numberOfInputs ; i ++ ) {
				weights[n][i] = sc.nextDouble() ;
			}
		}
		double[][] patns = new double[numberOfPatterns][numberOfInputs+1];
		double[] wts = new double[numberOfInputs+1];
		neurons = new Neuron[numberOfNeurons];
		for ( int p = 0; p < numberOfPatterns; p++) {
			for ( int i = 0; i < numberOfInputs ; i ++ ) {
				patns[p][i] = patterns[p][i];
			}
		}
		for ( int n = 0; n < numberOfNeurons ; n++ ) {
			neurons[n] = new Neuron(numberOfInputs,numberOfPatterns,(label+(n+1)));
			for ( int p = 0; p < numberOfPatterns; p++) {
				patns[p][numberOfInputs] = patterns[p][numberOfInputs+n];
			}
			neurons[n].assignPatterns(patns);
			for ( int i = 0; i < numberOfInputs+1 ; i ++ ) {
				wts[i] = weights[n][i] ;
			}
			neurons[n].assignWeights(wts);			
		}
		currentInputs = new double[numberOfInputs+1];
		currentOutputs = new double[numberOfNeurons];
		allOutputs = new double[numberOfPatterns][numberOfNeurons];

	}

	public void assignWeights ( double[][] wts) {
		weights = wts;
		for ( int n = 0; n < numberOfNeurons ; n++ ) {
			double[] neuronWeights = new double[numberOfInputs+1];
			for ( int i = 0; i <= numberOfInputs ; i ++ ) {
				neuronWeights[i] = wts[n][i];
			}
			neurons[n].assignWeights(neuronWeights);
		}			
	}
	
	public void randomizeWeights ( double range) { // weights to be randomized in (-range,+range)
		for ( int n = 0; n < numberOfNeurons ; n++ ) {
			neurons[n].randomizeWeights ( range);
		}
	}
	
	public void saveWeights ( PrintWriter pw ) {
		for ( int n = 0; n < numberOfNeurons ; n++ ) {
			neurons[n].saveWeights ( pw);
		}
	}	

	public void printWeights() {
		for ( int n = 0; n < numberOfNeurons ; n++ ) {
			neurons[n].printWeights();			
		}
	}

	public void assignPatterns ( int n) {
		double[][] patns = new double[numberOfPatterns][numberOfInputs + 1];
		for ( int p = 0; p < numberOfPatterns; p++) {
			for ( int i = 0; i < numberOfInputs ; i ++ ) {
				patns[p][i] = patterns[p][i];
			}
			patns[p][numberOfInputs] = patterns[p][numberOfInputs+n];
		}
		neurons[n].assignPatterns(patns);
	}

	public void assignPatterns () {
		for ( int n = 0; n < numberOfNeurons ; n++ ) {
			assignPatterns(n) ;
		}
	}

	public void assignPatterns (double[][] ptns) {
		for ( int p = 0; p < numberOfPatterns; p++) {
			for ( int i = 0; i < numberOfInputs+numberOfNeurons ; i ++ ) {
				patterns[p][i] = ptns[p][i];
			}
		}
		assignPatterns ();
	}

	public void printPatterns() { 
		for ( int n = 0; n < numberOfNeurons ; n++ ) {
			System.out.println(" Patterns for neuron "+(n+1));
			neurons[n].printPatterns();
		}
	}

	public double test ( ) {		
		double layerError = 0.0;
		for ( int n = 0; n < numberOfNeurons ; n ++ ) {
			neurons[n].printPatterns();
			layerError += neurons[n].test( ) ;
		}
		System.out.println(" sum of squared errors for all neurons in layer = "+ layerError);
		return layerError;
	}
	
	public double[] feedForward ( double[] inps,int pat) {
		for ( int n = 0; n < numberOfNeurons ; n ++ ) {
			currentOutputs[n] = neurons[n].output(inps,pat);
			allOutputs[pat][n]=currentOutputs[n];
		}
		return currentOutputs;
	}

	public double[] feedForward ( double[] inps) {
		for ( int n = 0; n < numberOfNeurons ; n ++ ) {
			currentOutputs[n] = neurons[n].output(inps);
		}
		return currentOutputs;
	}


	public double[] feedForward ( int pat ) {
		for ( int n = 0; n < numberOfNeurons ; n ++ ) {
			currentOutputs[n] = neurons[n].output(pat);
			allOutputs[pat][n]=currentOutputs[n];
		}
		return currentOutputs;
	}

	public double[][] feedForward ( ) {
		for ( int p = 0; p < numberOfPatterns; p++) {
			feedForward (p);
		}
		return allOutputs;
	}

	public double[][] errorBackPropagate(double[] errors, double[] inps){
		double[][] updatedWeights = new double[numberOfNeurons][numberOfInputs+1] ;
		for ( int n = 0; n < numberOfNeurons ; n ++ ) {
			double error = errors[n];
			double[] newWeights = new double[numberOfInputs+1] ;
			newWeights = neurons[n].errorBackPropagate(error,inps);
			for ( int i = 0; i <= numberOfInputs; i++ ) {
				updatedWeights[n][i] = newWeights[i];
			}
		}
		weights = updatedWeights ;
		return updatedWeights;
	}

	public double[][] errorBackPropagate(double[] errors, double[][] inps, int pat){
		double[][] updatedWeights = new double[numberOfNeurons][numberOfInputs+1] ;
		for ( int i = 0; i <= numberOfInputs; i++ ) {
			currentInputs[i] = inps[pat][i] ;
		}		
		for ( int n = 0; n < numberOfNeurons ; n ++ ) {
			double error = errors[n];
			double[] newWeights = new double[numberOfInputs+1] ;
			newWeights = neurons[n].errorBackPropagate(error,currentInputs);
			for ( int i = 0; i <= numberOfInputs; i++ ) {
				updatedWeights[n][i] = newWeights[i];
			}
		}
		weights = updatedWeights ;
		return updatedWeights;
	}



	public void printOutputs ( ) {
		System.out.print(" layer "+label+" outputs ");
		for ( int n = 0; n < numberOfNeurons ; n ++ ) {
			System.out.print(" neuron "+(n+1)+" : ");
			for ( int p = 0; p < numberOfPatterns; p++) {
				System.out.print(allOutputs[p][n]+" ; ");	
			}
		}
		System.out.println();
	}		

	public double test ( double tolerance ) {		
		double layerError = 0.0, error = 0.0;
		for ( int n = 0; n < numberOfNeurons ; n ++ ) {
			neurons[n].printPatterns();
			error = neurons[n].test();
			layerError += error;
			if ( error < tolerance ) {
				System.out.println((n+1)+" neuron has converged "+ error);
			}
			else {
				System.out.println((n+1)+" neuron has not converged "+ error);
			}
		}
		System.out.println(" sum of squared errors for all neurons in layer = "+ layerError);
		return layerError;
	}

	public void train ( ) {
		for ( int n = 0; n < numberOfNeurons ; n ++ ) {
			neurons[n].train();
		}
	} 

	public void train1 ( ) {
		for ( int p = 0; p < numberOfPatterns; p++) {
			currentInputs[0] = -1.0 ;
			for ( int i = 0; i < numberOfInputs; i++ ) {
				currentInputs[i+1] = patterns[p][i] ;
			}
			feedForward(currentInputs, p);
			double[] errs = new double[numberOfNeurons] ;
			for ( int n = 0; n < numberOfNeurons ; n ++ ) {
				errs[n] = patterns[p][numberOfInputs+n] -  currentOutputs[n];
			}
			errorBackPropagate( errs, currentInputs) ;
		}
	} 

	public static void main (String[] args ) throws IOException {
		//System.out.println(" Is everything OK ?");
		Layer l1 = new Layer();	
		l1.readLayer()	;
		l1.printPatterns();
		int cycles = 10;
		if ( args.length >0 ) {
			cycles = Integer.parseInt(args[0]);
		}
		for ( int c = 0 ; c < cycles ; c++ ) {
			System.out.println(" **** Training cycle "+(c+1)+" **********************");
			l1.train();
		}
		l1.test();
	}
}


	


