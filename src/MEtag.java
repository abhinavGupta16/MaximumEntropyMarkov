// Wrapper for maximum-entropy tagging

// NYU - Natural Language Processing - Prof. Grishman

// invoke by:  java  MEtag dataFile  model  responseFile

import java.io.*;
import java.util.regex.Matcher;
import opennlp.maxent.*;
import opennlp.maxent.io.*;

// reads line with tab separated features
//  writes feature[0] (token) and predicted tag

public class MEtag {

    public static void main (String[] args) {
	if (args.length != 3) {
	    System.err.println ("MEtag requires 3 arguments:  dataFile model responseFile");
	    System.exit(1);
	}
	String dataFileName = args[0];
	String modelFileName = args[1];
	String responseFileName = args[2];
	try {
	    GISModel m = (GISModel) new SuffixSensitiveGISModelReader(new File(modelFileName)).getModel();
	    BufferedReader dataReader = new BufferedReader (new FileReader (dataFileName));
	    PrintWriter responseWriter = new PrintWriter (new FileWriter (responseFileName));
	    String priorTag = "#";
	    String prevPriorTag = "^^^";
	    String line;
	    while ((line = dataReader.readLine()) != null) {
			if (line.equals("")) {
				responseWriter.println();
				priorTag = "#";
				prevPriorTag = "^^^";
			} else {
				line = line.replaceAll("@@", Matcher.quoteReplacement(priorTag));
				line = line.replaceAll("&&&", Matcher.quoteReplacement(prevPriorTag));
				String[] features = line.split("\t");
				String tag = m.getBestOutcome(m.eval(features));
				responseWriter.println(features[0] + "\t" + tag);
				if(!"#".equals(priorTag))
					prevPriorTag = priorTag;
				priorTag = tag;
			}
	    }
	    responseWriter.close();
	} catch (Exception e) {
	    System.out.print("Error in data tagging: ");
	    e.printStackTrace();
	}
    }

}
