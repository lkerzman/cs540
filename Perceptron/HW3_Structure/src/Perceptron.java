import java.util.List;

/**
 * You should implement your Perceptron in this class. 
 * Any methods, variables, or secondary classes could be added, but will 
 * only interact with the methods or variables in this framework.
 * 
 * You must add code for at least the 3 methods specified below. Because we
 * don't provide the weights of the Perceptron, you should create your own 
 * data structure to store the weights.
 * 
 */
public class Perceptron {

	/**
	 * The initial value for ALL weights in the Perceptron.
	 * We fix it to 0, and you CANNOT change it.
	 */
	public final double INIT_WEIGHT = 0.0;

	/**
	 * Learning rate value. You should use it in your implementation.
	 * You can set the value via command line parameter.
	 */
	public final double ALPHA;

	/**
	 * Training iterations. You should use it in your implementation.
	 * You can set the value via command line parameter.
	 */
	public final int EPOCH;

	// TODO: create weights variables, input units, and output units.
	double weights [][]; //weights array
	double bias [];//holds bias weights
	double inputs [];//inputs array
	double outputs [];//outputs array
	double sums [];//for linear sum of the weights &inputs
	double truth [];//array with true output values
	double largest; //holds largetst value of
	int num;//the output value
	int count;//counter for Epochs
	double real; //holds the actual output value
	List<Double> curr;//to get inputs
	int trainNum;//counter for training
	int testNum; //counter for testing
	int correct;//number correct for testing






	/**
	 * Constructor. You should initialize the Perceptron weights in this
	 * method. Also, if necessary, you could do some operations on
	 * your own variables or objects.
	 * 
	 * @param alpha
	 * 		The value for initializing learning rate.
	 * 
	 * @param epoch
	 * 		The value for initializing training iterations.
	 * 
	 * @param featureNum
	 * 		This is the length of input feature vector. You might
	 * 		use this value to create the input units.
	 * 
	 * @param labelNum
	 * 		This is the size of label set. You might use this
	 * 		value to create the output units.
	 */
	public Perceptron(double alpha, int epoch, int featureNum, int labelNum) {
		this.ALPHA = alpha;
		this.EPOCH = epoch;
		count=0;
		weights=new double [featureNum][labelNum];
		bias= new double [labelNum];
		inputs=new double[featureNum];
		outputs = new double[labelNum];
		sums= new double[labelNum];
		truth=new double[labelNum];
		largest=0.0;  
		num=0;
		real=0.0;
		trainNum=0;
		testNum=0;
		correct=0;

	}

	/**
	 * Train your Perceptron in this method.
	 * 
	 * @param trainingData
	 */
	public void train(Dataset trainingData) {
		while(count<EPOCH){
			count++;
			trainNum=0;
			while (trainingData.instanceList.size()>trainNum){

				//get dataset info
				curr=trainingData.instanceList.get(trainNum).getFeatureValue();//get inputs
				inputs=SetInputs(inputs,curr);
				//real output
				real=Integer.parseInt(trainingData.instanceList.get(trainNum).getLabel());

				//prediction step
				for(int i=0; i<sums.length;i++){//reset sums
					sums[i]=0.0;
				}
				sums=Sums(weights, bias, inputs, outputs, sums);
				for (int i=0;i<sums.length;i++){//sig function
					outputs[i]=Sigmoid(sums[i]);
				}
				largest=Double.MIN_VALUE;
				for(int i=0;i<outputs.length;i++){//predict output
					if(largest<outputs[i]){
						largest=outputs[i];
						num=i;	
					}
				}



				//training step
				for(int i=0;i<truth.length;i++){//each output's actual value
					truth[i]=0.0;
				}
				truth[(int)real]=1;
				//reassign weights
				for (int i=0; i<inputs.length;i++){
					for (int j=0; j<outputs.length;j++){
						weights[i][j]=weights[i][j]+(ALPHA *(truth[j]-outputs[j])*outputs[j]*(1-outputs[j])*inputs[i]);
					}
				}
				for (int i=0;i<bias.length;i++){//bias weights
					bias[i]=ALPHA*(truth[i]-outputs[i])*outputs[i]*(1-outputs[i]);
				}
				trainNum++;
			}
		}

	}

	/**
	 * Test your Perceptron in this method. Refer to the homework documentation
	 * for implementation details and requirement of this method.
	 * 
	 * @param testData
	 */
	public void classify(Dataset testData) {
		//System.out.println("Testing");
		while (testData.instanceList.size()>testNum){
			
			//get dataset info
			curr=testData.instanceList.get(testNum).getFeatureValue();//get inputs
			inputs=SetInputs(inputs,curr);
			real=Integer.parseInt(testData.instanceList.get(testNum).getLabel());

			//prediction step
			for(int i=0; i<sums.length;i++){//reset sums
				sums[i]=0.0;
			}
			sums=Sums(weights, bias, inputs, outputs, sums);
			for (int i=0;i<sums.length;i++){//sig func
				outputs[i]=Sigmoid(sums[i]);
			}
			largest=Double.MIN_VALUE;
			for(int i=0;i<outputs.length;i++){//predict output
				if(largest<outputs[i]){
					largest=outputs[i];
					num=i;	
				}
			}
			
			//check if correct value
			if(real==num){
				correct++;
			}
			testNum++;
			System.out.println(num);
		}
		double percent=(double) correct/testNum;
		System.out.printf("%.4f",percent);

	}
	
	//Calculates Sigmoid function
	//
	//in=linear sum of weights*inputs
	public double Sigmoid(double in){
		double sig=1;
		sig=1.0/(1+(Math.pow(Math.E,-in)));
		//System.out.println("Sigmoid is "+ sig);
		return sig;
	}
	
	//Sets inputs 
	//
	//in=inputs
	//current= dataset list
	//
	//returns in=an array of inputs
	public double[] SetInputs(double []in, List<Double> current){//in is linear comb of w1x1+....wnxn
		for (int i=0;i<current.size();i++){
			in[i]=current.get(i);
		}
		return in;
	}
	
	//Calculates linear sum
	//
	//weight= weights double array
	//b=bias array
	//in=input array
	//out=output array
	//sum=sums array
	//
	//returns sum=sums array
	public double[] Sums(double[][] weight, double[] b, double[] in, double[] out, double[] sum){
		for (int i=0;i<in.length;i++){
			for (int j=0;j<out.length;j++){
				sum[j]=sum[j]+(in[i]*weight[i][j]);
			}
		}
		for (int i=0;i<b.length;i++){
			sum[i]=sum[i]+bias[i];
		}
		return sum;

	}

}