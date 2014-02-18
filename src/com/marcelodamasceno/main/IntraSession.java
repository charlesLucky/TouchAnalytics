package com.marcelodamasceno.main;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import weka.classifiers.Classifier;
import weka.classifiers.lazy.IBk;
import weka.core.Instances;


import com.marcelodamasceno.util.ArffConector;
import static com.marcelodamasceno.util.Const.*;

public class IntraSession extends Experiment {

    public IntraSession() {
	setFolderResults("IntraSession/");
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
	IntraSession main = new IntraSession();
	Classifier ibk = new IBk(5);
	main.classifyAllUsers(ibk, false, false);

    }

    public void classifyAllUsers(Classifier classifier, boolean eerBool,
	    boolean correctStatistics) {
	folderResults="IntraSession/";
	Instances scrolling = null;
	Instances horizontal = null;

	ArffConector conector = new ArffConector();

	ArrayList<Double> scrollingResults = new ArrayList<Double>();
	ArrayList<Double> horizontalResults = new ArrayList<Double>();

	// It will save the temporary result classifiers
	double eer = 0.0;
	double correctPercentage = 0.0;
	double incorrectPercentage = 0.0;
	
	

	for (int user = 1; user <= 41; user++) {
	    try {
		scrolling = conector
			.openDataSet(PROJECTPATH + folderResults
				+ "IntraSession-User_" + user
				+ "_Day_1_Scrolling.arff");
		if (eerBool) {
		    eer = classifyEER(scrolling, null, classifier);
		    scrollingResults.add(eer);
		    // System.out.println("IntraSession-Scrolling-User "+user+" - EER: "+eer);
		} else {
		    if (correctStatistics) {
			correctPercentage = classify(scrolling, null,
				classifier, true, false);
			scrollingResults.add(correctPercentage);
			// System.out.println("IntraSession-Scrolling-User "+user+" - Correct: "+correctPercentage+"%");
		    } else {
			incorrectPercentage = classify(scrolling, null,
				classifier, false, false);
			scrollingResults.add(incorrectPercentage);
			// System.out.println("IntraSession-Scrolling-User "+user+" - Incorrect: "+incorrectPercentage+"%");
		    }
		}

		horizontal = conector.openDataSet(PROJECTPATH + folderResults
			+ "IntraSession-User_" + user
			+ "_Day_1_Horizontal.arff");

		if (eerBool) {
		    eer = classifyEER(horizontal, null, classifier);
		    horizontalResults.add(eer);
		    // System.out.println("IntraSession-Horizontal-User "+user+" - EER: "+eer);
		} else {
		    if (correctStatistics) {
			correctPercentage = classify(horizontal, null,
				classifier, true, false);
			horizontalResults.add(correctPercentage);
			// System.out.println("IntraSession-Horizontal-User "+user+" - Correct: "+correctPercentage+"%");
		    } else {
			incorrectPercentage = classify(horizontal, null,
				classifier, false, false);
			horizontalResults.add(incorrectPercentage);
			// System.out.println("IntraSession-Horizontal-User "+user+" - Incorrect: "+incorrectPercentage+"%");
		    }
		}
	    } catch (FileNotFoundException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
	printResults(scrollingResults, horizontalResults, eerBool,
		correctStatistics);
    }

}
