package si.fis.unm.weka;

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


public class Genesis {

	public static final String b = "bpp";
	public static final String e = "edge";
	public static final String g = "gabor";
	public static final String j = "jpeg";
	public static final String p = "phog";

	static{
		Options options = new Options();

        Option bpp = new Option("b", "bpp", true, "BinaryPatternsPyramidFilter");
        bpp.setRequired(true);
        options.addOption(bpp);

        Option edge = new Option("e", "edge", true, "EdgeHistogramFilter");
        edge.setRequired(true);
        options.addOption(edge);

        Option gabor = new Option("g", "gabor", true, "GaborFilter");
        gabor.setRequired(true);
        options.addOption(gabor);

        Option jpeg = new Option("j", "jpeg", true, "JpegCoefficientFilter");
        jpeg.setRequired(true);
        options.addOption(jpeg);

        Option phog = new Option("p", "phog", true, "PHOGFilter");
        phog.setRequired(true);
        options.addOption(phog);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("my-parser-for-image-filters", options);

            System.exit(1);
            return;
        }

        String inputBpp = cmd.getOptionValue("bpp");
        String inputEdge = cmd.getOptionValue("edge");
        String inputGabor = cmd.getOptionValue("gabor");
        String inputJpeg = cmd.getOptionValue("jpeg");
        String inputPhog = cmd.getOptionValue("phog");
	}


	public static void main(String[] args) throws Exception {
		new Genesis().execute(args);
		
		BufferedReader reader = new BufferedReader(new FileReader("/home/alenkat/Documents/Mag/podatki/VsiJPG/Zobrazi.arff")); // preberi
																														// iz
																														// datoteke
		ArffReader arff = new ArffReader(reader);
		Instances data = arff.getData();
		data.setClassIndex(data.numAttributes() - 1); // doda indeks zadnjemu atributu

		
		BinaryPatternsPyramidFilter bpp = new BinaryPatternsPyramidFilter(); // new instance of filter
		// BPP.setOptions(options); // set options// za cross validation

		EdgeHistogramFilter edge = new EdgeHistogramFilter(); // new instance of filter
		// edge.setOptions(options); // set options// za cross validation

		MultiFilter mf = new MultiFilter();		// weka metoda za uporabo večih filtrov hkrati
		mf.setFilters(new Filter[]{bpp, edge});
		//Filter [] filters = {bpp, edge};// gabor, jpeg, phog};
		//mf.setFilter(filters);

		//Scanner s = new Scanner(System.in);
		//System.out.println("Vpiši filter");
		//String imefiltra = s.next();

		//switch(mf){
		for (Filter f : mf){
		
			if(f = bpp){
			
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

			else if (f=edge){
			
        	edge.setInputFormat(data);
        	edge.setImageDirectory("/home/alenkat/Documents/Mag/podatki/VsiJPG/");
        	Instances imgFilt = Filter.useFilter(data, edge);
        
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
			BufferedWriter writer = new BufferedWriter(new FileWriter("/home/alenkat/Documents/DataAnalysis/SlikeFilterRez/Mpeg.txt"));
			writer.write("** REZULTATI **");
			writer.write(eval.toSummaryString());
			writer.write(eval.toClassDetailsString());
 			writer.newLine();
 			writer.flush();
			writer.close();
			}

			//default:
			//throw new IllegalArgumentException(imefiltra + " ni definiran.");
		

		}


		

	}

}
