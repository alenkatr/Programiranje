/**
   * Poincare razdalja po formuli d(p,q)=ln(1+2|p-q|²/(1-|p|²)(1-|q|²)+koren[(1+2|p-q|²/(1-|p|²)(1-|q|²))²-1])
   * Izdelano september 2018
   * Nadgrajeno april 2020
   */
package si.fis.unm.weka;


/** preberi podatke - uvozi .arff file */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.Object;  //za izbiro filtra
import java.util.Random;  // za cross validation

import weka.core.TechnicalInformation.Field;
import weka.core.TechnicalInformation.Type;
import weka.core.neighboursearch.PerformanceStats;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.EuclideanDistance;
import weka.core.NormalizableDistance;
import weka.core.TechnicalInformation;
import weka.core.DenseInstance;
import weka.core.RevisionUtils;
import weka.core.TechnicalInformationHandler;
// import weka.core.*;

/** filtri */
import weka.filters.Filter;
import weka.filters.unsupervised.instance.imagefilter;
//import java.awt.image.BufferedImage; IBk 

/** klasifikator */
import weka.classifiers.lazy.IBk;
import weka.core.WeightedInstancesHandler;
import weka.core.neighboursearch.LinearNNSearch;
import weka.core.neighboursearch.NearestNeighbourSearch;




/**
 <!-- globalinfo-start -->
 * Implementing Poincare distance (or similarity) function.<br/>
 * <br/>
 * One object defines not one distance but the data model in which the distances between objects of that data model can be computed.<br/>
 * <br/>
 * Attention: For efficiency reasons the use of consistency checks (like are the data models of the two instances exactly the same), is low.<br/>
 * <br/>
 * For more information, see:<br/>
 * <br/>
 * <p/>
 <!-- globalinfo-end -->
 *
 <!-- technical-bibtex-start -->
 * BibTeX:
 * <pre>
 * &#64;misc{missing_id,
 *    author = {Wikipedia},
 *    title = {Poincare distance},
 * }
 * </pre>
 * <p/>
 <!-- technical-bibtex-end -->
 *
 <!-- options-start -->
 * Valid options are: <p/>
 * 
 * <pre> -D
 *  Turns off the normalization of attribute 
 *  values in distance calculation.</pre>
 * 
 * <pre> -R &lt;col1,col2-col4,...&gt;
 *  Specifies list of columns to used in the calculation of the 
 *  distance. 'first' and 'last' are valid indices.
 *  (default: first-last)</pre>
 * 
 * <pre> -V
 *  Invert matching sense of column indices.</pre>
 * 
 <!-- options-end --> 
 *
 * @author 
 * @version $Revision: 1 $
 */
public class PoincareDistance
  extends NormalizableDistance
  implements Cloneable, TechnicalInformationHandler {

  
      /** preberi podatke iz .arff datoteke */

    public static void main(String[] args) throws Exception {
      BufferedReader reader = new BufferedReader(new FileReader("/Documents/Slike/FRDuciMachine/VsiJPG/Zobrazi.arff"));  //preberi iz datoteke
      ArffReader arff = new ArffReader(reader);
      Instances data = arff.getData();
      data.setClassIndex(data.numAttributes() - 1);   // doda indeks zadnjemu atributu
    


/** uporabi filter na podatkih 
 * java weka.Run imageFilters // za izbiro filtra
*/


BinaryPatternsPyramidFilter BPP = new BinaryPatternsPyramidFilter();                         // new instance of filter
BPP.setOptions(options);                           // set options// za cross validation


Remove newData = new Remove();
newData.setAttributeIndices("1");  // odstrani prvi atribut - ime fotografije (?)


    
/** klasifikacija - uporaba klasifikatorja IBk */

// for (int k = 0; k < 11; k++)
IBk knn = new IBk(1);  // (k=1) zaenkrat 
knn.buildClassifier(newData);

/** razdeli podatke na testno in učno množico - uporaba 10 cross fold validation */

Evaluation eval = new Evaluation(newData);
eval.crossValidateModel(knn, newData, 10 , new Random(1));


/** izpiši rezultate 
 * - Stratified cross-validation (tu so formalni rezultati: število pravilno in napačno razvrščenih primerkov, kappa statistika, povprečna napaka, relativna napaka, število vseh primerkov) 
 * - Detailed Accuracy By Class (tu so natančnejši rezultati: Recall, Precision, MCC, ROC Area, PRC Area)*/

System.out.println("** Rezultati  **");
System.out.println(eval.toSummaryString());
System.out.println(eval.toClassDetailsString());



  
    
  
    
  
    /** for serialization. */// za cross validation
  /**
   * Constructs an Poincare Distance object, Instances must be still set.
   */
  public PoincareDistance()s {
    super();
  }

  /**
   * Constructs an Poincare Distance object and automatically initializes the
   * ranges.
   * 
   * @param data 	the instances the distance function should work on
   */
  public PoincareDistance(Instances data) {
    super(data);
  }

  /**
   * Returns a string describing this object.
   * 
   * @return 		a description of the evaluator suitable for
   * 			displaying in the explorer/experimenter gui
   */
  public String globalInfo() {
    return 
        "Implementing Poincare distance (or similarity) function.\n\n"
      + "One object defines not one distance but the data model in which "
      + "the distances between objects of that data model can be computed.\n\n"
      + "Attention: For efficiency reasons the use of consistency checks "
      + "(like are the data models of the two instances exactly the same), "
      + "is low.\n\n"
      + "For more information, see:\n\n"
      + getTechnicalInformation().toString();
  }

  /**
   * Returns an instance of a TechnicalInformation object, containing 
   * detailed information about the technical background of this class,
   * e.g., paper reference or book this class is based on.
   * 
   * @return 		the technical information about this class
   */
  public TechnicalInformation getTechnicalInformation() {
    TechnicalInformation 	result;
    
    result = new TechnicalInformation(Type.MISC);
    result.setValue(Field.AUTHOR, "Wikipedia");
    result.setValue(Field.TITLE, "Poincare distance");

    return result;
  }



public double dot(Instance inst){
 	int n = 0;
	double sum=0;
	for (int i = 0; i < n; i++){
	sum += inst.value(i)*inst.value(i);
 	 //return sum; DODANO
	}
	return sum; //DODANO
  }

public double distance(Instance first, Instance second, double cutOffValue,
	    PerformanceStats stats) {
	EuclideanDistance ed = new EuclideanDistance();
	ed.setInstances(m_Data);
	ed.setDontNormalize(true);
	
	
	
	double pn, qn, pnq;
	
	pn = ed.distance(first, new DenseInstance(0));
	qn = ed.distance(second, new DenseInstance(0));
	pnq = ed.distance(first, second);
	
	System.out.println("Pn=" + pn);
	System.out.println("Qn=" + qn);
	System.out.println("PnQ=" + pnq);
	
	double x;
	
	x = 1 + (2*Math.pow(pnq, 2))/( (1-Math.pow(pn, 2)) * (1-Math.pow(qn, 2)));
	
	return Math.log( x + Math.sqrt( Math.pow(x, 2)-1) );
}


  /**
DODAJ ŠE NORMIRANJE: 

public double Norma(Instance instance)
{
	
	double iNorm = 0;
	int i;
	Instance inst1;
	for(i = 0; i < instance.numValues(); i++){
		if(instance.classIndex() == i) continue;		
		iNorm += Math.pow(Math.abs(instance.value(i)),2);
	}
	return iNorm = Math.pow(iNorm, 0.5);

}



  /**
   * Calculates the distance between two instances.
   * 
   * @param first 	the first instance
   * @param second 	the second instance
   * @return 		the distance between the two given instances
   */
  
 /*  DODANO
  public double distance(Instance first, Instance second) {
	/*
	 * Instance je interface in posledicno ni moznosti, da bi ustvarila kar nov objekt. Ga moras dobiti na drug nacin. V tvojem primeru sploh ni potreben.
	 * Pri izracunu Poincareove razdalje (na podlagi tvoje diplomske) ne potrebujemo tretje tocke oz. izhodisca.
	 * d(P,Q) = 1/2 log( (1 + norm(P-Q/(1-PtQ) )) / (1 - norm(P-Q/(1-PtQ) )))
	 * Pt je P transponirano (delamo le z relanimi koordinati)
	 * norm je funkcija, ki izracuna Evklidovo razdaljo ||(P-Q)/(1-PtQ)|| 
	 */

	/*
	*  V primeru, da imata instanci razlicno stevilo dimenzij, potem vrnemo nezkoncnost
	* Namrec uradna metoda, ki dela distance ima ene vija-vaje, ki izracunajo razdaljo med vektorjema, ki imata razlicne dimenzije.
	
        if (first.numValues() != second.numValues()) {
		return Double.POSITIVE_INFINITY;
	}
	double normPart;
	 
	normPart = norm(first,second); //funkcija izracuna ||P-Q/(1-PtQ)||
	return 0.5 * Math.log( (1+normPart) / (1-normPart) );
	
 
  }
  */ // DODANO
  /**
   * Calculates the distance (or similarity) between two instances. Need to
   * pass this returned distance later on to postprocess method to set it on
   * correct scale. <br/>
   * P.S.: Please don't mix the use of this function with
   * distance(Instance first, Instance second), as that already does post
   * processing. Please consider passing Double.POSITIVE_INFINITY as the cutOffValue to
   * this function and then later on do the post processing on all the
   * distances.
   *
   * @param first 	the first instance
   * @param second 	the second instance
   * @param stats 	the structure for storing performance statistics.
   * @return 		the distance between the two given instances or 
   * 			Double.POSITIVE_INFINITY.
   */
/*  
  public double distance(Instance first, Instance second, PerformanceStats stats) { //debug method pls remove after use
	return distance(first, second);
   }
  
  @Override
  public double distance(Instance first, Instance second, double cutOffValue) {
	  return distance(first, second);
  }
  
  @Override
  public double distance(Instance first, Instance second, double cutOffValue,
		    PerformanceStats stats) {
	  return distance(first, second);
  }
*/  
  /**
   * Updates the current distance calculated so far with the new difference
   * between two attributes. The difference between the attributes was 
   * calculated with the difference(int,double,double) method.
   * 
   * @param currDist	the current distance calculated so far
   * @param diff	the difference between two new attributes
   * @return		the update distance
   * @see		#difference(int, double, double)
   *
   */
  protected double updateDistance(double currDist, double diff) {
    return Double.NaN;
  }
  
  /**
   * Does post processing of the distances (if necessary) returned by
   * distance(distance(Instance first, Instance second, double cutOffValue). It
   * is necessary to do so to get the correct distances if
   * distance(distance(Instance first, Instance second, double cutOffValue) is
   * used. This is because that function actually returns the squared distance
   * to avoid inaccuracies arising from floating point comparison.
   * 
   * @param distances	the distances to post-process
   *
   */
  public void postProcessDistances(double distances[]) {
    for(int i = 0; i < distances.length; i++) {
      distances[i] = Math.log(distances[i] + Math.sqrt(distances[i]*distances[i]-1));
    }
  }
  
  /**
   * Returns the squared difference of two values of an attribute.
   * 
   * @param index	the attribute index
   * @param val1	the first value
   * @param val2	the second value
   * @return		the squared difference
   */
  public double sqDifference(int index, double val1, double val2) {
    double val = difference(index, val1, val2);
    return val*val;
  }
  
  /**
   * Returns value in the middle of the two parameter values.
   * 
   * @param ranges 	the ranges to this dimension
   * @return 		the middle value
   */
  public double getMiddle(double[] ranges) {

    double middle = ranges[R_MIN] + ranges[R_WIDTH] * 0.5;
    return middle;
  }
  
  /**
   * Returns the index of the closest point to the current instance.
   * Index is index in Instances object that is the second parameter.
   *
   * @param instance 	the instance to assign a cluster to
   * @param allPoints 	all points
   * @param pointList 	the list of points
   * @return 		the index of the closest point
   * @throws Exception	if something goes wrong
   */
  public int closestPoint(Instance instance, Instances allPoints,
      			  int[] pointList) throws Exception {
    double minDist = Integer.MAX_VALUE;
    int bestPoint = 0;
    for (int i = 0; i < pointList.length; i++) {
      double dist = distance(instance, allPoints.instance(pointList[i]), Double.POSITIVE_INFINITY);
      if (dist < minDist) {
        minDist = dist;
        bestPoint = i;
      }
    }
    return pointList[bestPoint];
  }
  
  /**
   * Returns true if the value of the given dimension is smaller or equal the
   * value to be compared with.
   * 
   * @param instance 	the instance where the value should be taken of
   * @param dim 	the dimension of the value
   * @param value 	the value to compare with
   * @return 		true if value of instance is smaller or equal value
   */
  public boolean valueIsSmallerEqual(Instance instance, int dim,
      				     double value) {  //This stays
    return instance.value(dim) <= value;
  }
  
  /**
   * Returns the revision string.
   * 
   * @return		the revision
   */
  public String getRevision() {
    return RevisionUtils.extract("$Revision: 0 $");
  }








  }
}
