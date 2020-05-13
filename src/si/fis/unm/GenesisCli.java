/**

*/

package si.fis.unm.weka;

import org.apache.commons.cli.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.FileWriter;

import weka.classifiers.Evaluation;
import weka.classifiers.lazy.IBk;
import weka.core.Instances;
import weka.core.converters.ArffLoader.ArffReader;
import weka.filters.MultiFilter;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.instance.imagefilter.*;


public class GenesisCli {
 
	public static void main(String[] args) throws Exception {
		
		Options options = new Options();

        Option bp = new Option("b", false, "BinaryPatternsPyramidFilter");
        //bpp.setRequired(true);
        //options.addOption(bpp);

        Option ed = new Option("e", false, "EdgeHistogramFilter");
        //edge.setRequired(true);
		//options.addOption(edge);

		CommandLineParser parser = new DefaultParser();
        //HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = parser.parse(options,args);
	
		
		
		
		BufferedReader reader = new BufferedReader(new FileReader("/home/alenkat/Documents/Mag/podatki/VsiJPG/Zobrazi.arff")); // preberi
																														// iz
																														// datoteke
		ArffReader arff = new ArffReader(reader);
		Instances data = arff.getData();
		data.setClassIndex(data.numAttributes() - 1); // doda indeks zadnjemu atributu

		//Scanner s = new Scanner(System.in);
		//System.out.println("Vpiši filter");
		//String ime = s.next(); switch(ime){}...
        //String ime;

        if(cmd.hasOption("b")){
		
		      
		    BinaryPatternsPyramidFilter bpp = new BinaryPatternsPyramidFilter();	
        	bpp.setInputFormat(data);
        	bpp.setImageDirectory("/home/alenkat/Documents/Mag/podatki/VsiJPG/");
        	Instances imgFilt = Filter.useFilter(data, bpp);
        
        	Remove r = new Remove();
			r.setAttributeIndices("1"); // odstrani prvi atribut - ime fotografije 
        	r.setInputFormat(imgFilt);
        	Instances newData = Filter.useFilter(imgFilt,r);

       
			/** klasifikacija - uporaba klasifikatorja IBk */

			// for (int k = 0; k < 11; k++)
			IBk knn = new IBk(); // (k=1) zaenkrat
			knn.setWindowSize(0);
			knn.getNearestNeighbourSearchAlgorithm().setDistanceFunction(new PoincareDistance());
        	knn.buildClassifier(newData);

			/**
		 	* razdeli podatke na testno in učno množico - uporaba 10 cross fold validation
		 	*/

			Evaluation eval = new Evaluation(newData);
			eval.crossValidateModel(knn, newData, 10, new Random(1));

			/**
			 * izpiši rezultate - Stratified cross-validation (tu so formalni rezultati:
			 * število pravilno in napačno razvrščenih primerkov, kappa statistika,
			 * povprečna napaka, relativna napaka, število vseh primerkov) - Detailed
			 * Accuracy By Class (tu so natančnejši rezultati: Recall, Precision, MCC, ROC
			 * Area, PRC Area)
			 *
			 * System.out.println("** Rezultati  **");
			*	System.out.println(eval.toSummaryString());
			*	System.out.println(eval.toClassDetailsString());
			*/
			BufferedWriter writer = new BufferedWriter(new FileWriter("/home/alenkat/Documents/DataAnalysis/SlikeFilterRez/BPP.txt"));
			writer.write("** REZULTATI **");
			writer.write(eval.toSummaryString());
			writer.write(eval.toClassDetailsString());
 			writer.newLine();
 			writer.flush();
			writer.close();
        }
        

        else if(cmd.hasOption("e")){

		    EdgeHistogramFilter edge = new EdgeHistogramFilter();	
        	edge.setInputFormat(data);
        	edge.setImageDirectory("/home/alenkat/Documents/Mag/podatki/VsiJPG/");
        	Instances imgFilte = Filter.useFilter(data, edge);
        
        	Remove re = new Remove();
			re.setAttributeIndices("1"); // odstrani prvi atribut - ime fotografije
        	re.setInputFormat(imgFilte);
        	Instances newDatae = Filter.useFilter(imgFilte,re);

       
			/** klasifikacija - uporaba klasifikatorja IBk */

			// for (int k = 1; k < 11; k++)
			IBk knne = new IBk(); // (k=1) zaenkrat
			knne.setWindowSize(0);
			knne.getNearestNeighbourSearchAlgorithm().setDistanceFunction(new PoincareDistance());
        	knne.buildClassifier(newDatae);

			/**
		 	* razdeli podatke na testno in učno množico - uporaba 10 cross fold validation
		 	*/

			Evaluation evale = new Evaluation(newDatae);
			evale.crossValidateModel(knne, newDatae, 10, new Random(1));

			/**
			 * izpiši rezultate - Stratified cross-validation (tu so formalni rezultati:
			 * število pravilno in napačno razvrščenih primerkov, kappa statistika,
			 * povprečna napaka, relativna napaka, število vseh primerkov) - Detailed
			 * Accuracy By Class (tu so natančnejši rezultati: Recall, Precision, MCC, ROC
			 * Area, PRC Area)
			 *
			 * System.out.println("** Rezultati  **");
			*	System.out.println(eval.toSummaryString());
			*	System.out.println(eval.toClassDetailsString());
			*/
			BufferedWriter writere = new BufferedWriter(new FileWriter("/home/alenkat/Documents/DataAnalysis/SlikeFilterRez/Mpeg.txt"));
			writere.write("** REZULTATI **");
			writere.write(evale.toSummaryString());
			writere.write(evale.toClassDetailsString());
 			writere.newLine();
 			writere.flush();
			writere.close();
            
        }

			

		


		

	}

}
