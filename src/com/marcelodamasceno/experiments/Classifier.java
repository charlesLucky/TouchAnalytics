package com.marcelodamasceno.experiments;

import static com.marcelodamasceno.util.Const.*;

import com.marcelodamasceno.main.InterSession;
import com.marcelodamasceno.main.InterWeek;
import com.marcelodamasceno.main.IntraSession;

/**
 * Class to train and evaluate the classifier using EER or Incorrect Percentage
 * in InterSession, InterWeek and IntraSession datasets.
 * 
 * @author marcelo
 * 
 */
public abstract class Classifier {

	private InterSession interSession;
	private InterWeek interWeek;
	private IntraSession intraSession;
	private weka.classifiers.Classifier classifier;
	private String datasetPath = "/home/marcelo/Área de Trabalho/projeto/dataset/Base de Toque/";

	/**
	 * Constructor
	 * 
	 * @param classifier
	 *            Weka Classifier
	 */
	public Classifier(weka.classifiers.Classifier classifier) {
		super();

		this.classifier = classifier;

		interSession = new InterSession();

		interWeek = new InterWeek();

		intraSession = new IntraSession();


		interSession.setFolderResults("InterSession/");
		interWeek.setFolderResults("InterWeek/");
		intraSession.setFolderResults("IntraSession/");
	}

	/**
	 * Execute the experiment in BioConvolving Database
	 * 
	 * @param metric
	 *            Could be EER or Incorrect Percentage
	 */
	public void executeBioConvolvingCancelaveis(String metric) {
		setPathBioConvolving();
		//Needs this for switch below jdk 1.7
		int metricInt=1;
		if(metric.equals(INCORRECT))
			metricInt=0;

		switch (metricInt) {
		case 0:
			System.out.println("BioConvolving-Incorrect - InterSession");
			interSession.classifyAllUsers(classifier, false, false);
			System.out.println("BioConvolving-Incorrect - InterWeek");
			interWeek.classifyAllUsers(classifier, false, false);
			System.out.println("BioConvolving-Incorrect - IntraSession");
			intraSession.classifyAllUsers(classifier, false, false);
			break;
		case 1:
			System.out.println("BioConvolving-EER - InterSession");
			interSession.classifyAllUsers(classifier, true, false);
			System.out.println("BioConvolving-EER - InterWeek");
			interWeek.classifyAllUsers(classifier, true, false);
			System.out.println("BioConvolving-EER - IntraSession");
			intraSession.classifyAllUsers(classifier, true, false);
			break;
		default:
			break;
		}

	}

	/**
	 * Execute the experiment in BioConvolving Database
	 * 
	 * @param metric
	 *            Could be EER or Incorrect Percentage
	 * @param dataset
	 *            Coul be InterSession, InterWeek, IntraSession
	 */
	public void executeBioConvolvingCancelaveis(String metric, String dataset) {

		setPathBioConvolving();

		//Needs this for switch below jdk 1.7
		int metricInt=1;
		if(metric.equals(INCORRECT))
			metricInt=0;

		switch (metricInt) {
		case 0:
			if (dataset.toLowerCase().equals(INTERSESSION)) {
				System.out.println("BioConvolving-Incorrect - InterSession");
				interSession.classifyAllUsers(classifier, false, false);
			}
			if (dataset.toLowerCase().equals(INTERWEEK)) {
				System.out.println("BioConvolving-Incorrect - InterWeek");
				interWeek.classifyAllUsers(classifier, false, false);
			} else {
				System.out.println("BioConvolving-Incorrect - IntraSession");
				intraSession.classifyAllUsers(classifier, false, false);
			}
			break;
		case 1:
			if (dataset.toLowerCase().equals(INTERSESSION)) {
				System.out.println("BioConvolving-EER - InterSession");
				interSession.classifyAllUsers(classifier, true, false);
			}
			if (dataset.toLowerCase().equals(INTERWEEK)) {
				System.out.println("BioConvolving-EER - InterWeek");
				interWeek.classifyAllUsers(classifier, true, false);

			} else {
				System.out.println("BioConvolving-EER - IntraSession");
				intraSession.classifyAllUsers(classifier, true, false);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * Execute the experiment in BioHashing Database
	 * 
	 * @param metric
	 *            Could be EER or Incorrect Percentage
	 */
	public void executeBioHashingCancelaveis(String metric) {
		setPathBioHashing();

		//Needs this for switch below jdk 1.7
		int metricInt=1;
		if(metric.equals(INCORRECT))
			metricInt=0;

		switch (metricInt) {
		case 0:
			System.out.println("BioHashing-Incorrect - InterSession");
			interSession.classifyAllUsers(classifier, false, false);
			System.out.println("BioHashing-Incorrect - InterWeek");
			interWeek.classifyAllUsers(classifier, false, false);
			System.out.println("BioHashing-Incorrect - IntraSession");
			intraSession.classifyAllUsers(classifier, false, false);
			break;
		case 1:
			System.out.println("BioHashing-EER - InterSession");
			interSession.classifyAllUsers(classifier, true, false);
			System.out.println("BioHashing-EER - InterWeek");
			interWeek.classifyAllUsers(classifier, true, false);
			System.out.println("BioHashing-EER - IntraSession");
			intraSession.classifyAllUsers(classifier, true, false);
			break;
		default:
			break;
		}

	}

	/**
	 * Execute the experiment in BioHashing Database
	 * 
	 * @param metric
	 *            Could be EER or Incorrect Percentage
	 * @param dataset
	 *            Coul be InterSession, InterWeek, IntraSession
	 */
	public void executeBioHashingCancelaveis(String metric, String dataset) {
		setPathBioHashing();
		//Needs this for switch below jdk 1.7
		int metricInt=1;
		if(metric.equals(INCORRECT))
			metricInt=0;

		switch (metricInt) {
		case 0:
			if (dataset.toLowerCase().equals(INTERSESSION)) {
				System.out.println("BioHashing-Incorrect - InterSession");
				interSession.classifyAllUsers(classifier, false, false);
			}
			if (dataset.toLowerCase().equals(INTERWEEK)) {
				System.out.println("BioHashing-Incorrect - InterWeek");
				interWeek.classifyAllUsers(classifier, false, false);
			} else {
				System.out.println("BioHashing-Incorrect - IntraSession");
				intraSession.classifyAllUsers(classifier, false, false);
			}
			break;
		case 1:
			if (dataset.toLowerCase().equals(INTERSESSION)) {
				System.out.println("BioHashing-EER - InterSession");
				interSession.classifyAllUsers(classifier, true, false);
			}
			if (dataset.toLowerCase().equals(INTERWEEK)) {
				System.out.println("BioHashing-EER - InterWeek");
				interWeek.classifyAllUsers(classifier, true, false);

			} else {
				System.out.println("BioHashing-EER - IntraSession");
				intraSession.classifyAllUsers(classifier, true, false);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * Execute the experiment in DoubleSum Database
	 * 
	 * @param metric
	 *            Could be EER or Incorrect Percentage
	 */
	void executeDoubleSumCancelaveis(String metric) {
		setPathDoubleSum();
		//Needs this for switch below jdk 1.7
		int metricInt=1;
		if(metric.equals(INCORRECT))
			metricInt=0;

		switch (metricInt) {
		case 0:
			System.out.println("DoubleSum-Incorrect - InterSession");
			interSession.classifyAllUsers(classifier, false, false);
			System.out.println("DoubleSum-Incorrect - InterWeek");
			interWeek.classifyAllUsers(classifier, false, false);
			System.out.println("DoubleSum-Incorrect - IntraSession");
			intraSession.classifyAllUsers(classifier, false, false);
			break;
		case 1:
			System.out.println("DoubleSum-EER - InterSession");
			interSession.classifyAllUsers(classifier, true, false);
			System.out.println("DoubleSum-EER - InterWeek");
			interWeek.classifyAllUsers(classifier, true, false);
			System.out.println("DoubleSum-EER - IntraSession");
			intraSession.classifyAllUsers(classifier, true, false);
			break;
		default:
			break;
		}
	}

	/**
	 * Execute the experiment in Double Sum Database
	 * 
	 * @param metric
	 *            Could be EER or Incorrect Percentage
	 * @param dataset
	 *            Coul be InterSession, InterWeek, IntraSession
	 */

	void executeDoubleSumCancelaveis(String metric, String dataset) {
		setPathDoubleSum();
		//Needs this for switch below jdk 1.7
		int metricInt=1;
		if(metric.equals(INCORRECT))
			metricInt=0;
		switch (metricInt) {
		case 0:
			if (dataset.toLowerCase().equals(INTERSESSION)) {
				System.out.println("DoubleSum-Incorrect - InterSession");
				interSession.classifyAllUsers(classifier, false, false);
			}
			if (dataset.toLowerCase().equals(INTERWEEK)) {
				System.out.println("DoubleSum-Incorrect - InterWeek");
				interWeek.classifyAllUsers(classifier, false, false);
			} else {
				System.out.println("DoubleSum-Incorrect - IntraSession");
				intraSession.classifyAllUsers(classifier, false, false);
			}
			break;
		case 1:
			if (dataset.toLowerCase().equals(INTERSESSION)) {
				System.out.println("DoubleSum-EER - InterSession");
				interSession.classifyAllUsers(classifier, true, false);
			}
			if (dataset.toLowerCase().equals(INTERWEEK)) {
				System.out.println("DoubleSum-EER - InterWeek");
				interWeek.classifyAllUsers(classifier, true, false);

			} else {
				System.out.println("DoubleSum-EER - IntraSession");
				intraSession.classifyAllUsers(classifier, true, false);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * Returns the name of the Classifier
	 * @return the name of the classifier
	 */
	public String getName(){
		String name=this.classifier.getClass().getName();	
		String namesSplit[]=name.split("\\.");
		return namesSplit[namesSplit.length-1];	
	}

	/**
	 * Execute the experiment in Interpolation Database
	 * 
	 * @param metric
	 *            Could be EER or Incorrect Percentage
	 */
	void executeInterpolationCancelaveis(String metric) {

		System.out.println(getName());
		setPathInterpolation();


		//Needs this for switch below jdk 1.7
		int metricInt=1;
		if(metric.equals(INCORRECT))
			metricInt=0;
		switch (metricInt) {
		case 0:
			System.out.println();
			System.out.println("Interpolation-Incorrect - InterSession");
			interSession.classifyAllUsers(classifier, false, false);
			System.out.println("Interpolation-Incorrect - InterWeek");
			interWeek.classifyAllUsers(classifier, false, false);
			System.out.println("Interpolation-Incorrect - IntraSession");
			intraSession.classifyAllUsers(classifier, false, false);
			break;
		case 1:
			System.out.println("Interpolation-EER - InterSession");
			interSession.classifyAllUsers(classifier, true, false);
			System.out.println("Interpolation-EER - InterWeek");
			interWeek.classifyAllUsers(classifier, true, false);
			System.out.println("Interpolation-EER - IntraSession");
			intraSession.classifyAllUsers(classifier, true, false);
			break;
		default:
			break;
		}
	}

	/**
	 * Execute the experiment in Interpolation Database
	 * 
	 * @param metric
	 *            Could be EER or Incorrect Percentage
	 * @param dataset
	 *            Coul be InterSession, InterWeek, IntraSession
	 */
	void executeInterpolationCancelaveis(String metric, String dataset) {
		setPathInterpolation();
		//Needs this for switch below jdk 1.7
		int metricInt=1;
		if(metric.equals(INCORRECT))
			metricInt=0;
		switch (metricInt) {
		case 0:
			if (dataset.toLowerCase().equals(INTERSESSION)) {
				System.out.println("Interpolation-Incorrect - InterSession");
				interSession.classifyAllUsers(classifier, false, false);
			}
			if (dataset.toLowerCase().equals(INTERWEEK)) {
				System.out.println("Interpolation-Incorrect - InterWeek");
				interWeek.classifyAllUsers(classifier, false, false);
			} else {
				System.out.println("Interpolation-Incorrect - IntraSession");
				intraSession.classifyAllUsers(classifier, false, false);
			}
			break;
		case 1:
			if (dataset.toLowerCase().equals(INTERSESSION)) {
				System.out.println("Interpolation-EER - InterSession");
				interSession.classifyAllUsers(classifier, true, false);
			}
			if (dataset.toLowerCase().equals(INTERWEEK)) {
				System.out.println("Interpolation-EER - InterWeek");
				interWeek.classifyAllUsers(classifier, true, false);

			} else {
				System.out.println("Interpolation-EER - IntraSession");
				intraSession.classifyAllUsers(classifier, true, false);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * Set the experiment configuration to Original settings
	 */
	private void setPathOriginal() {
		interSession.setProjectPath(datasetPath + "InterSession-SemNominal/");

		interWeek.setProjectPath(datasetPath + "/InterWeek-SemNominal/");

		intraSession.setProjectPath(datasetPath + "IntraSession-SemNominal/");

		interSession.setFileName("InterSession-User_");
		interWeek.setFileName("InterWeek-User_");
		intraSession.setFileName("IntraSession-User_");

		interSession.setFolderResults("");
		interWeek.setFolderResults("");
		intraSession.setFolderResults("");
	}

	/**
	 * Execute the experiment in Original Database
	 * 
	 * @param metric
	 *            Could be EER or Incorrect Percentage
	 * @param dataset
	 *            Coul be InterSession, InterWeek, IntraSession
	 */
	void executeOriginal(String metric, String dataset) {
		setPathOriginal();
		//Needs this for switch below jdk 1.7
		int metricInt=1;
		if(metric.equals(INCORRECT))
			metricInt=0;
		switch (metricInt) {
		case 0:
			if (dataset.toLowerCase().equals(INTERSESSION)) {
				System.out.println("Original-Incorrect - InterSession");
				interSession.classifyAllUsers(classifier, false, false);
			}
			if (dataset.toLowerCase().equals(INTERWEEK)) {
				System.out.println("Original-Incorrect - InterWeek");
				interWeek.classifyAllUsers(classifier, false, false);
			} else {
				System.out.println("Original-Incorrect - IntraSession");
				intraSession.classifyAllUsers(classifier, false, false);
			}
			break;
		case 1:
			if (dataset.toLowerCase().equals(INTERSESSION)) {
				System.out.println("Original-EER - InterSession");
				interSession.classifyAllUsers(classifier, true, false);
			}
			if (dataset.toLowerCase().equals(INTERWEEK)) {
				System.out.println("Original-EER - InterWeek");
				interWeek.classifyAllUsers(classifier, true, false);

			} else {
				System.out.println("Original-EER - IntraSession");
				intraSession.classifyAllUsers(classifier, true, false);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * Execute the experiment in Original Database
	 * 
	 * @param metric
	 *            Could be EER or Incorrect Percentage
	 */
	void executeOriginal(String metric) {

		setPathOriginal();
		//Needs this for switch below jdk 1.7
		int metricInt=1;
		if(metric.equals(INCORRECT))
			metricInt=0;
		switch (metricInt) {
		case 0:
			System.out.println("Original-Incorrect - InterSession");
			interSession.classifyAllUsers(classifier, false, false);
			System.out.println("\nOriginal-Incorrect - InterWeek");
			interWeek.classifyAllUsers(classifier, false, false);
			System.out.println("\nOriginal-Incorrect - IntraSession");
			intraSession.classifyAllUsers(classifier, false, false);
			break;
		case 1:
			System.out.println("Original-EER - InterSession");
			interSession.classifyAllUsers(classifier, true, false);
			System.out.println("\nOriginal-EER - InterWeek");
			interWeek.classifyAllUsers(classifier, true, false);
			System.out.println("\nOriginal-EER - IntraSession");
			intraSession.classifyAllUsers(classifier, true, false);
			break;
		default:
			break;
		}
	}

	/**
	 * Set the experiment configuration to BioConvolving settings
	 */
	private void setPathBioConvolving() {
		String path = datasetPath + "/Cancelaveis/BioConvolving/";
		interSession.setProjectPath(path);

		interWeek.setProjectPath(path);

		intraSession.setProjectPath(path);

		interSession.setFileName("Bioconvolving-InterSession-User_");
		interWeek.setFileName("BioConvolving-InterWeek-User_");
		intraSession.setFileName("BioConvolving-IntraSession-User_");
	}

	/**
	 * Set the experiment configuration to BioHashing settings
	 */
	private void setPathBioHashing() {
		String path = datasetPath + "/Cancelaveis/BioHashing/";
		interSession.setProjectPath(path);

		interWeek.setProjectPath(path);

		intraSession.setProjectPath(path);

		interSession.setFileName("InterSession-User_");
		interWeek.setFileName("InterWeek-User_");
		intraSession.setFileName("IntraSession-User_");

	}

	/**
	 * Set the experiment configuration to DoubleSum settings
	 */
	private void setPathDoubleSum() {
		String path = datasetPath + "Cancelaveis/DoubleSum/";
		interSession.setProjectPath(path);

		interWeek.setProjectPath(path);

		intraSession.setProjectPath(path);

		interSession.setFileName("InterSession-User_");
		interWeek.setFileName("InterWeek-User_");
		intraSession.setFileName("IntraSession-User_");
	}

	/**
	 * Set the experiment configuration to Interpolation settings
	 */
	private void setPathInterpolation() {
		interSession
		.setProjectPath(datasetPath + "Cancelaveis/Interpolation/");

		interWeek.setProjectPath(datasetPath + "Cancelaveis/Interpolation/");

		intraSession
		.setProjectPath(datasetPath + "Cancelaveis/Interpolation/");

		interSession.setFileName("Interpolation-InterSession-User_");
		interWeek.setFileName("Interpolation-InterWeek-User_");
	}

}
