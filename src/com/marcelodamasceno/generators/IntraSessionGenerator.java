package com.marcelodamasceno.generators;

import java.io.FileNotFoundException;

import weka.core.Instances;

public class IntraSessionGenerator extends Generator {

    /**
     * @param args
     */
    public static void main(String[] args) {
	String projectPath = "/home/marcelo/Área de Trabalho/Documentos-Windows/Google Drive/doutorado/projeto/dataset/Base de Toque/";
	String folderResults = "IntraSession-SemNominal/";

	String cancelableFunction = Generator.INTERPOLATOR;

	// Generating the cancelable dataset for each user
	for (int user = 1; user <= 41; user++) {
	    Generator generator = new IntraSessionGenerator();
	    try {
		String fileName = "IntraSession-User_" + user
			+ "_Day_1_Scrolling.arff";
		Instances dataset = generator.getConector().openDataSet(projectPath
			+ folderResults + fileName);

		generator.generateIntraSession(dataset, fileName,
			cancelableFunction);

		fileName = "IntraSession-User_" + user
			+ "_Day_1_Horizontal.arff";
		dataset = generator.getConector().openDataSet(projectPath
			+ folderResults + fileName);
		generator.generateIntraSession(dataset, fileName,
			cancelableFunction);

	    } catch (FileNotFoundException e) {
		e.printStackTrace();
	    } catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
    }

}
