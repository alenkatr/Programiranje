
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


public static class PoincareThread implements Runnable{
    //array of size 10, Instance
    int intArray [10];
    private Insatance inst;
    private Thread t;
    

    public PoincareThread (Instance name){
        //definition of what will instance do -------- crossvalid??
        this.inst = name;

    }


public void run(){

    //thread starts
    System.out.println("Running " +  t);
    //read data + run crossvalidation + classifier + 
    //+ store classifier + appropriate of intArray[]
    //accept original dataset
		BufferedReader reader = new BufferedReader(new FileReader("/home/alenkat/Documents/Mag/podatki/VsiJPG/Zobrazi.arff")); // preberi iz datoteke
		ArffReader arff = new ArffReader(reader);
		Instances data = arff.getData();
		data.setClassIndex(data.numAttributes() - 1); // doda indeks zadnjemu atributu

		//filtering
		Filter[] fil = new BinaryPatternsPyramidFilter;
		// new PHOGFilter
		fil.setInputFormat(data);
		fil.setImageDirectory("/home/alenkat/Documents/Mag/podatki/VsiJPG/");
		Instances imgFilt = Filter.useFilter(data, fil);
		
        Remove r = new Remove();
		r.setAttributeIndices("1"); // odstrani prvi atribut - ime fotografije 
		r.setInputFormat(imgFilt);
        Instances newData = Filter.useFilter(imgFilt,r);
        
        //classifier
        for(int k = 1; k < 11; k++){
        IBk knn = new IBk(int k); 
		knn.setWindowSize(0);
		knn.getNearestNeighbourSearchAlgorithm().setDistanceFunction(new PoincareDistance());
        knn.buildClassifier(newData);
        System.out.println("k = " +  k );
        }

        //crossvalid.
        Evaluation eval = new Evaluation(newData);
        eval.crossValidateModel(knn, newData, 10, new Random(1));
    }
}

public class PMThread {
    public static void main (String args[]){
        //run threads + AggregateableEvaluation
        //run PoincareThread!!
        (new Thread(new PoincareThread())).start();

        //AggregateableEvaluation
        //agg = new weka.classifiers.evaluation.AggregateableEvaluation(eval);
        //agg.aggregate(eval);

        //thread ends
        System.out.println("End " +  t);

        //crate a file with results
        BufferedWriter writer = new BufferedWriter(new FileWriter("/home/alenkat/Documents/DataAnalysis/SlikeFilterRez/BPPthreads.txt"));
		writer.write("** REZULTATI **");
		writer.write(eval.toSummaryString());
		writer.write(eval.toClassDetailsString());
 		writer.newLine();
 		writer.flush();
		writer.close();
		
    }
}
