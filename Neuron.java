import java.util.Scanner;
import java.io.*;
public class Neuron {
	private int numberOfInputs ;
	private int numberOfPatterns;
	private double[][] patterns;
	private double[] weights;
	private double[] currentInputs;
	private double currentOutput;
	private double[] currentOutputs;
	private double learningRate;
	private String label;

	public Neuron(int ni, int np, double lr, String lbl) {
		numberOfInputs = ni;
		numberOfPatterns = np;
		patterns = new double[np][ni+1];
		weights = new double[ni+1];
		currentInputs = new double[ni + 1 ];
		currentOutputs = new double[np ];
		learningRate = lr;
		label = lbl;
	}

	public Neuron(int ni, int np, double lr) {
		this(ni,np,lr,"No Label");
	}

	public Neuron(int ni, int np, String lbl) {
		this(ni,np,0.5,lbl);
	}

	public Neuron() {
		this(2,4,"No Name");
	}
	
	public void readNeuron() throws IOException {
		Scanner sc = new Scanner(new File("netdatabpor.txt"));
		System.out.println("Reading Perceptron data from netdatabpor.txt");
		numberOfInputs = sc.nextInt();
		numberOfPatterns = sc.nextInt();
		learningRate = sc.nextDouble();
		patterns = new double[numberOfPatterns][numberOfInputs+1];
		for ( int p = 0; p < numberOfPatterns; p++) {
			for ( int i = 0; i <= numberOfInputs ; i ++ ) {
				patterns[p][i] = sc.nextDouble();
			}
		}
		currentInputs = new double[numberOfInputs+1];
		weights = new double[numberOfInputs+1];
		for ( int i = 0; i <= numberOfInputs ; i ++ ) {
			weights[i] = sc.nextDouble() ;
		}
	}

	public void assignPatterns ( double[][] patns) {
		for ( int p = 0; p < numberOfPatterns; p++) {
			for ( int i = 0; i <= numberOfInputs ; i ++ ) {
				patterns[p][i] = patns[p][i];
			}
		}
	}

	public void assignWeights ( double[] wts) {
		for ( int i = 0; i <= numberOfInputs ; i ++ ) {
			weights[i] = wts[i] ;
		}
	}

	public void randomizeWeights ( double range) { // weights to be randomized in (-range,+range)
		for ( int i = 0; i <= numberOfInputs ; i ++ ) {
			double rnd = (Math.random()*2.0 -1.0)*range;
			weights[i] = rnd ;
		}
	}

	public void saveWeights ( PrintWriter pw ) {
		for ( int i = 0; i <= numberOfInputs ; i ++ ) {
			pw.print(weights[i] +"  ") ;
		}
		pw.println();
	}

	public void setLearningRate( double lr ) {
		learningRate = lr;
	}

	public double getLearningRate( ) {
		return learningRate ;
	}

	public double bipolarLogistic( double x ) {
		double bpl = (1.0 - Math.exp(-x) ) / (1.0 + Math.exp(-x) );
		return bpl;
	}

	public double bipolarStep( double x ) {
		if ( x <= 0.0 ) 
			return -1.0;
		else
			return 1.0;
	}

	public double binaryStep( double x ) {
		if ( x <= 0.0 ) 
			return 0.0;
		else
			return 1.0;
	}

	public double bplGradient( double x ) {
		double bpl = bipolarLogistic(x);
		double bplg = ( 1.0 - bpl * bpl ) / 2.0;
		return bplg;
	}

	public double weightedSum (int patn) {
		currentInputs[0] = -1.0;
		for ( int i = 0; i < numberOfInputs ; i ++ ) {
			currentInputs[i+1] = patterns[patn][i];
		}
		double sum =0.0;
		for ( int i = 0; i <= numberOfInputs ; i ++ ) {
			sum += weights[i] * currentInputs[i] ;
		}
		return sum ;
	}

	public double weightedSum (double[] inps) {
		double sum =0.0;
		for ( int i = 0; i <= numberOfInputs ; i ++ ) {
			sum += weights[i] * inps[i] ;
		}
		return sum ;
	}

	public double output( double[] inps, int pat) {
		double sum = weightedSum(inps) ;
		//double opt = bipolarLogistic( sum );
		currentOutput = bipolarLogistic( sum );
		currentOutputs[pat] = currentOutput;
		//System.out.println(" Output pattern "+pat+" is "+currentOutput);				
		return currentOutput;
	}

	public double output( double[] inps) {
		double sum = weightedSum(inps) ;
		currentOutput = bipolarLogistic( sum );
		return currentOutput;
	}


	public double[] errorBackPropagate(double error,double[] inps) {
		double net = weightedSum(inps);
		double gradient = bplGradient(net);
		//printWeights(" before train ") ;
		double deltaW=0.0;
		for ( int i = 0; i <= numberOfInputs ; i ++ ) {
			deltaW = learningRate * error * gradient * inps[i];
			weights[i] += deltaW ;
		}
		//printWeights(" after train "+deltaW+" ") ;
		return weights;
	}

	public void printStatus() {
		System.out.print("Neuron "+label+" has inputs ");
		for ( int i = 0; i <= numberOfInputs ; i ++ ) {
			System.out.print(currentInputs[i]+" ; ");
		}
		System.out.print(" and output "+currentOutput);
		System.out.println();		
	}

	public double output( int patn) {
		double sum = weightedSum(patn) ;
		double opt = bipolarLogistic( sum );
		//double opt = binaryStep( sum );
		return opt;
	}

	public double test ( int patn ) {
		double otpt = output ( patn ) ;
		double desired = patterns[patn][numberOfInputs];
		double difference = otpt-desired;
		double errorSquare = difference * difference;
		System.out.println(" for pattern "+patn+ " output is "+otpt+" desired = "+desired+" difference = "+difference);
		return errorSquare;
	}

	public double test ( ) {
		double sumSquareError = 0.0;
		for ( int p = 0; p < numberOfPatterns ; p ++ ) {
			sumSquareError += test( p ) ;
		}
		System.out.println(" sum of squared errors = "+ sumSquareError);
		return sumSquareError;
	}

	public int trainDelta ( int patn ) {
		double otpt = output ( patn ) ;
		double desired = patterns[patn][numberOfInputs];
		double difference = desired-otpt;
		int changed = 0;
		if ( difference != 0.0 ) {
			changed ++;
			System.out.println(" Weights before train : " );
			printWeights();
			double gradient = (1.0 - otpt * otpt )/2.0;
			for ( int i = 0; i <= numberOfInputs ; i ++ ) {
				weights[i] = weights[i]+ learningRate * difference *gradient* currentInputs[i];
			}
			System.out.println(" Weights after train : " );
			printWeights();
		}
		return changed;	
	}

	public void trainDelta ( double error, int patn ) {
		double otpt = output ( patn ) ;
		double gradient = (1.0 - otpt * otpt )/2.0;
		for ( int i = 0; i <= numberOfInputs ; i ++ ) {
			weights[i] = weights[i]+ learningRate * error *gradient* currentInputs[i];
		}
	}

	public int train ( ) {
		int changed = 0;
		for ( int p = 0; p < numberOfPatterns ; p ++ ) {
			changed += trainDelta( p ) ;
		}
		System.out.println(" Number of Weight updations during epoch : "+changed );
		return changed;
	}

	public int train1 ( ) {
		int changed = 0;
		currentInputs[0] = -1.0 ;
		for ( int p = 0; p < numberOfPatterns ; p ++ ) {
			for ( int i = 0; i < numberOfInputs ; i ++ ) {
				currentInputs[i+1] = patterns[p][i];
			}
			double opt = output(currentInputs,p);
			double err = patterns[p][numberOfInputs] ;
			weights = errorBackPropagate(err, currentInputs);

			
			changed += 1 ;
		}
		System.out.println(" Number of Weight updations during epoch : "+changed );
		return changed;
	}

	public void printPatterns() {
		for ( int p = 0; p < numberOfPatterns ; p ++ ) {
			System.out.print("Pattern "+p+" inputs: ");
			for ( int i = 0; i < numberOfInputs ; i ++ ) {
				System.out.print(" "+patterns[p][i]);
			}
			System.out.println(" output "+patterns[p][numberOfInputs]);
		}
	}

	public void printWeights(String preface) {
		System.out.print(preface+" Weights and output are : ");
		for ( int i = 0; i <= numberOfInputs ; i ++ ) {
			System.out.print(" "+weights[i]);
		}
		System.out.print(" out: "+currentOutput);
		System.out.println();
	}

	public void printWeights() {
		printWeights(" ");
	}

	public static void main (String[] args ) throws IOException {
		Neuron pb = new Neuron();
		pb.readNeuron();
		int cycles = 5;
		if ( args.length >0 ) {
			cycles = Integer.parseInt(args[0]);
		}

		System.out.println("Patterns and weights read from input file are");
		pb.printPatterns();
		pb.printWeights();
		double error = pb.test();
		int count = 0;
		while ( error > 0.0 ) {
			System.out.println(" Cycle Number ***********************"+ (count+1));

			pb.train1();
			error = pb.test();
			count ++ ;
			if ( count > cycles ) {
				System.out.println(" No convergence in "+cycles+" cycles ");
				break;
			}
		}
		if ( count < cycles) {
			System.out.println("Weights converged in "+(count + 1 )+" cycles ");
			pb.printWeights();
		}

	}
}

