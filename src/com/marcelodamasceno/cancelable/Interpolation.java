package com.marcelodamasceno.cancelable;

import java.io.FileNotFoundException;
import java.util.Enumeration;
import java.util.Random;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.interpolation.UnivariateInterpolator;


import com.marcelodamasceno.util.ArffConector;
import com.marcelodamasceno.util.InstancesUtils;
import com.marcelodamasceno.util.Transformations;


import weka.core.Instance;
import weka.core.Instances;

//TODO: Criar um polinomio para cada instância e não utilizar o valor da classe para interpolar.
/**
 * This class creates a cancelable dataset using the interpolation approach
 * @author marcelo
 *
 */
public class Interpolation {

	//X array
	private double[]x;	
	//Random X array
	private double[] xRandom;

	//Y array
	private double[]y;
	//Y array with the aplication of the poli object using the xRandom array. yTransformed=poli(xRandom)
	private double[]yTransformed;

	//Interpolator
	UnivariateInterpolator interpolator;
	
	//Polynomio
	UnivariateFunction poli;

	//Original dataSet
	Instances dataSet;	
	
	//Class
	String classe;
	
	public Interpolation(Instance data){	
		x=new double[data.numAttributes()-1];
		y=new double[data.numAttributes()-1];
		xRandom=new double[data.numAttributes()-1];
		yTransformed=new double[data.numAttributes()-1];
		feedArrays(data);
		interpolator = new SplineInterpolator();
	}
	
	/**
	 * Crates a Polynomial Interpolation using the data present in the dataset
	 * @param data Dataset
	 */
	public Interpolation(Instances data){
		dataSet=data;		
		x=new double[data.numAttributes()-1];
		y=new double[data.numAttributes()-1];
		xRandom=new double[data.numAttributes()-1];
		yTransformed=new double[data.numAttributes()-1];
		interpolator = new SplineInterpolator();
	}
	
	/**
	 * Generate the cancelableDataSet
	 * @return
	 */
	private Instances interpolateInstances(){
		Instances transformedDataSet=new Instances(dataSet);
		transformedDataSet.clear();
		createRandomArray();
		for (Instance instance : dataSet) {
			classe=instance.stringValue(instance.classIndex());
			feedArrays(instance);
			transformedDataSet.add(interpolate());
		}
		return transformedDataSet;
	}
	
	/**
	 * Generate the transformed instance with the interpolate function
	 * @return
	 */
	public Instance interpolate(){		
		poli = interpolator.interpolate(x, y);		
		return createTransformedInstance();		
	}
	
	/**
	 * Feed the x and y array
	 * @param data DataSet will be converted
	 */
	private void feedArrays(Instance data){
		for(int i=0;i<data.numAttributes()-1;i++){
			x[i]=i;
			if(data.attribute(i).isNominal()){
				y[i]=Double.parseDouble(data.stringValue(i));
			}else{
				y[i]=data.value(i);
			}
		}		
	}



	/**
	 * Feed the x and y array
	 * @param data DataSet will be converted
	 */
	@SuppressWarnings("unused")
	private void feedArrays(Instances data){
		for(int i=0;i<data.numInstances();i++){	
			//data.numAttributes*i is the position the data will be included. This is because the data is placed in sequential order.
			addInstance(data.get(i),data.numAttributes()*i);
		}
	}

	/**
	 * Add the instance in x and y array
	 * @param instance Instance
	 * @param position position in sequential order
	 */
	private void addInstance(Instance instance,int position){
		//feeding the arrays y and x	
		for(int i=0;i<instance.numAttributes();i++){
			y[position]=instance.value(i);
			x[position]=position;			
		}
	}


	/**
	 * Return the created polynomial
	 * @return Polynomial
	 */
	public UnivariateFunction getPolynomial(){		
		return poli;
	}

	
	public Instances createCancelableDataSetOneFunctionForAllDataSet(){
		createRandomArray();
		Transformations t = new Transformations();
		Instances cancelableDataSet=t.doubleToInstances(yTransformed,dataSet.numAttributes());
		return cancelableDataSet;
	}
	
	/**
	 * Generate the transformed instance with the interpolate function and the random array xRandom
	 * @return
	 */
	private Instance createTransformedInstance(){
		for(int i=0;i<xRandom.length;i++){
			yTransformed[i]=poli.value(xRandom[i]);
		}
		Transformations t= new Transformations();		
		return t.doubleArrayToInstanceWithClass(yTransformed,classe);
	}

	/**
	 * Generates a RandowArray with min=0 and max=num of attributes of the current instance
	 * @return
	 */
	private double[] createRandomArray(){
		Random rand= new Random();
		int min=0, max=x.length-1;

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		int randomNum = rand.nextInt(max - min + 1) + min;
		for(int i=0;i<x.length;i++){
			xRandom[i]=randomNum;			
			randomNum = rand.nextInt(max - min + 1) + min;
		}
		return xRandom;
	}

	public void printArray(double[]a){
		System.out.print("{");
		for(int i=0;i<a.length;i++)
			System.out.print(a[i]+" , ");
		System.out.print("}");
	}

	public static void main(String[] args) throws FileNotFoundException {
		ArffConector conector=new ArffConector();
		Instances dataset=null;
		String projectPath="/home/marcelo/Área de Trabalho/Documentos-Windows/Google Drive/doutorado/projeto/dataset/Base de Toque/";		
		String folderResults="IntraSession/";

		dataset = conector.openDataSet(projectPath+folderResults+"IntraSession-User_41_Day_1_Scrolling.arff");
		/*Interpolation inter=new Interpolation(dataset);
		//System.out.println("Grau: "+inter.getcoefficients());
		System.out.println(" y -original: "+inter.y[0]);
		System.out.println("y- cancelável: "+inter.poli.value(1));
		//inter.printPolynomial();
		Instances cancelableDataSetInstances=inter.createCancelableDataSetOneFunctionForAllDataSet();

		System.out.println("***Original DataSet***");
		System.out.println(dataset.toString());

		System.out.println("***Cancelable DataSet***");
		System.out.println(cancelableDataSetInstances.toString());
		*/
		/*Create the revocable database where it was used a interpolation for each instance*/
		Instances cancelableDataSet=new Instances(dataset);
		cancelableDataSet.clear();
		@SuppressWarnings("unchecked")
		Enumeration<String> en=dataset.classAttribute().enumerateValues();
		InstancesUtils iUtils=new InstancesUtils();
		
		while (en.hasMoreElements()) {
			String classe = (String) en.nextElement();
			Instances subDataSet=iUtils.subInstances(dataset, classe);
			Interpolation inter2=new Interpolation(subDataSet);
			Instances instances=inter2.interpolateInstances();
			cancelableDataSet.addAll(instances);
			
		}		
		System.out.println(dataset.instance(0).toString());
		System.out.println(cancelableDataSet.instance(0).toString());		
	}

}
