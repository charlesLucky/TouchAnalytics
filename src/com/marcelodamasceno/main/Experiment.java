package com.marcelodamasceno.main;

import java.util.ArrayList;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.EER;
import weka.core.Instances;

public abstract class Experiment {
	
	protected abstract void classifyAllUsers(boolean eerBool, boolean correctStatistics);
	
	public double classify(Instances train, Instances test,Classifier classifier,boolean correctStatistics,boolean eerBool){
		Evaluation eval;
		EER eer=null;
		if (test==null) {			
			try {
				eval = new Evaluation(train);
				eval.crossValidateModel(classifier, train, 10, new Random(1));
				if(eerBool){
					eer = (EER) eval.getPluginMetric("EER");
					return eer.getStatistic("");
				}if(correctStatistics){
					return eval.pctCorrect();
				}else{
					return eval.pctIncorrect();
				}
			} catch (Exception e) {
				e.printStackTrace();
				return -1;
			}				
		}else {					
			try {
				classifier.buildClassifier(train);
				eval=new Evaluation(train);
				eval.evaluateModel(classifier,test);
				if(eerBool){
					eer = (EER) eval.getPluginMetric("EER");
					return eer.getStatistic("");
				}if(correctStatistics){
					return eval.pctCorrect();
				}else{
					return eval.pctIncorrect();
				}				
			} catch (Exception e) {			
				e.printStackTrace();
				return -1;
			}

		}	
	}
	
	/**
	 * Evaluates a classifier using 10 cross-validation and returns the Equal Error Rate
	 * @param train
	 * @param test
	 * @param classifier
	 * @return 
	 */
	public double classifyEER(Instances train, Instances test,Classifier classifier){
		return classify(train, test, classifier, true, true);
	}
	
	public void printResults(ArrayList<Double>scrollingResults,ArrayList<Double> horizontalResults,boolean eerBool, boolean correctStatistics){
		if(eerBool){
			System.out.println("EERScrooling: ");
		}else{
			if(correctStatistics){
				System.out.println("Scrooling-Correct Statistics: ");
			}else{
				System.out.println("Scrooling-Incorrect Statistics: ");
			}
		}
		for (int i = 0; i < scrollingResults.size(); i++) {
			System.out.print(scrollingResults.get(i)+" , ");
		}

		if(eerBool){
			System.out.println("\nEERHorizontal: ");
		}else{
			if(correctStatistics){
				System.out.println("\nHorizontal-Correct Statistics: ");
			}else{
				System.out.println("\nHorizontal-Incorrect Statistics: ");
			}
		}			
		for (int i = 0; i < horizontalResults.size(); i++) {
			System.out.print(horizontalResults.get(i)+" , ");
		}
	}

}
