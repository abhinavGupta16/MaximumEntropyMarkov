import opennlp.maxent.GISModel;
import opennlp.maxent.io.SuffixSensitiveGISModelReader;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class Viterbi {

    public static void main(String[] args) throws Exception {
        String dataFileName = args[0];
        String modelFileName = args[1];
        String responseFileName = args[2];
        GISModel m = (GISModel) new SuffixSensitiveGISModelReader(new File(modelFileName)).getModel();
        BufferedReader dataReader = new BufferedReader (new FileReader(dataFileName));
        PrintWriter responseWriter = new PrintWriter (new FileWriter(responseFileName));
        String line;
        String[] featureLine;
        List<String[]> featureList = new LinkedList<>();
        List<String> obs = new LinkedList<>();
        int wordIndex = 0;
        while ((line = dataReader.readLine()) != null) {
            if (!line.trim().equals("")) {
                obs.add(line.split("\t")[0]);
                featureLine = line.split("\t");
                String[] featureArray = buildFeatureArray(featureLine);
                featureList.add(featureArray);
            } else {
                String[] obsArr = obs.stream().toArray(String[] ::new);
                String[][] featureArray = featureList.stream().toArray(String[][] ::new);
                String[] tags = calculateViterbi(obsArr, m, featureArray);
                for(int i = 0; i < tags.length; i++){
                    responseWriter.println(obsArr[i] + "\t" + tags[i]);
                }
                obs = new LinkedList<>();
                featureList = new LinkedList<>();
                responseWriter.println();
            }
        }
        responseWriter.close();
    }

    /**
     *
     * @param obs sentence stored in the array
     * @param m GIS Model
     * @param features Features corresponding to every word
     * @return predicted tag sequence
     */
    public static String[] calculateViterbi(String[] obs, GISModel m, String[][] features){
        double[] currentProbs = m.eval(features[0]);
        Pair[][] v = new Pair[currentProbs.length][obs.length];
        for(int i = 0; i<v.length; i++){
            for(int j = 0; j < v[0].length;j++){
                v[i][j] = new Pair(0,null);
            }
        }

        for(int i = 0; i < v.length; i++){
            v[i][0].value = currentProbs[i];
        }

        for(int wordIndex = 1; wordIndex < obs.length; wordIndex++){
            for(int stateIndex = 0; stateIndex < currentProbs.length; stateIndex++){
                double maxProb = -1;
                Integer maxStateIndex = -1;
                for(int prevStateIndex = 0; prevStateIndex < currentProbs.length; prevStateIndex++){
                    // replacing the prevTag and prevPrevTag (same as replacing @@ and &&&)
                    features[wordIndex][5] = replacePrevFeature(m, prevStateIndex);
                    features[wordIndex][7] = replacePrevPrevFeature(m, prevStateIndex-1, features[wordIndex][7]);
                    double[] currEvalProbs = m.eval(features[wordIndex]);
                    double prob = currEvalProbs[stateIndex] * v[prevStateIndex][wordIndex-1].value;
                    if(prob>maxProb){
                        maxProb = prob;
                        maxStateIndex = prevStateIndex;
                    }
                }
                //update the values with maximum probability and stateIndex
                v[stateIndex][wordIndex].value = maxProb;
                v[stateIndex][wordIndex].prev = maxStateIndex;
            }
        }

        // getting back tags
        String[] tags = new String[obs.length];
        int tagsIndex = obs.length-1;
        Integer maxStateIndex = 0;
        Pair max = v[maxStateIndex][tagsIndex];
        for(int i = 0; i < v.length; i++){
            if(max.value < v[i][tagsIndex].value){
                max = v[i][tagsIndex];
                maxStateIndex = i;
            }
        }

        if(tagsIndex<0)
            return tags;

        while(tagsIndex >= 0){
            tags[tagsIndex] = m.getOutcome(maxStateIndex);
            maxStateIndex = v[maxStateIndex][tagsIndex].prev;
            tagsIndex--;
        }
        return tags;
    }

    public static String replacePrevFeature(GISModel m, int stateIndex){
        String outcome = m.getOutcome(stateIndex);
        return "prevTag=" + outcome;
    }
    public static String replacePrevPrevFeature(GISModel m, int stateIndex, String feature){
        if(stateIndex<0){
            return feature;
        }
        String outcome = m.getOutcome(stateIndex);
        return "prevPrevTag=" + outcome;
    }

    public static String[] buildFeatureArray(String[] featureFileArray){
        String[] featureArray = new String[featureFileArray.length-1];
        for(int i = 1; i < featureFileArray.length; i++){
            featureArray[i-1] = featureFileArray[i];
        }
        return featureArray;
    }

    public static Pair getMaxValue(Pair[][] v){
        int lastColIndex = v[0].length-1;
        Pair max = v[lastColIndex][0];
        for(int i = 0; i < v.length; i++){
            if(max.value < v[lastColIndex][i].value){
                max = v[lastColIndex][i];
            }
        }
        return max;
    }
}

class Pair{
    double value;
    Integer prev;
    Pair(double value, Integer prev){
        this.value = value;
        this.prev = prev;
    }
    @Override
    public String toString() {
        return this.value + " " + this.prev;
    }
}