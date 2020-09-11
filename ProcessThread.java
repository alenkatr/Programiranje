/**VERZIJA:10.9.2020
 * 1. In the main program create an array of size 10 (10 folds) to hold the evaluation object processed by each thread
2. Create a Runnable class that accepts the original dataset an a fold number. 
It's task is to create the appropriate train/test fold, build the classifier, 
evaluate it on the test fold and then store it in the appropriate location of the global array from step 1.
3. Launch 10 threads to execute the 10 Runnables
4. When they've all finished, create an AggregateableEvalation to aggregate all the individual Evaluation objects
 */



import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.*;

import weka.classifiers.Evaluation;
import weka.classifiers.lazy.IBk;
import weka.core.Instances;
import weka.core.converters.ArffLoader.ArffReader;
import weka.filters.MultiFilter;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.instance.imagefilter.*;

public class ProcessThread{


    public static class PoincareThread implements Runnable{
        //Instance, Thread
        private Insatance inst;
        private Thread t;
        

        //create InstancePMThread
        @Override
        public void run(){

        //print which thread starts
        System.out.println("Running " +  t);

        //read data + run crossvalidation + classifier + 
        //+ store classifier + appropriate of intArray[]
        //accept original dataset
            BufferedReader reader = new BufferedReader(new FileReader("/home/alenkat/Documents/Mag/podatki/VsiJPG/Zobrazi.arff")); // preberi iz datoteke
            ArffReader arff = new ArffReader(reader);
            Instances data = arff.getData();
            data.setClassIndex(data.numAttributes() -1);

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

            //crossvalidation -- threads??
            Evaluation eval = new Evaluation(newData);
            eval.crossValidateModel(knn, newData, 10, new Random(1));
            //Evaluation eval = new Evaluation(newData);
            //myClassifier.buildClassifier(newData);
            //for (int i = 0; i < testData.numInstances(); i++) {
            //    eval.evaluateModelOnce(myClassifier, newData.instance(i)); OR need these metrics then call evaluateModelOnceAndRecordPrediction() 
            //}
        }

    }
    public static void main (String args[]){
        //run threads + AggregateableEvaluation
        //run PoincareThread
        (new Thread(new PoincareThread())).start();

        /**ExecutorService will take care of your threads and we will initialize the number of threads to be the number of cores that your CPU has available.
         *  List<Future> will allow us to wait for all threads to finish before we continue with the main thread. 
         * List<eval> is there to hold the results added by the threads  */

        //private static ExecutorService executor = null;
        //private static List<Future> futures = new ArrayList<>();	
        //private static List<eval> resultList = Collections.synchronizedList(new ArrayList<eval>());

        //AggregateableEvaluation
        agg = new weka.classifiers.evaluation.AggregateableEvaluation(eval);
        agg.aggregate(eval);

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