import org.apache.commons.io.FilenameUtils;

import java.io.*;

public class FeatureBuilder {
    private File inputFileName;
    private File outputFileName;

    public FeatureBuilder(String inputFileName, String outputFileName){
        this.inputFileName = new File(inputFileName);
        this.outputFileName = new File(outputFileName);
    }

    public void generateFeatures() throws Exception{
        BufferedReader bReader = new BufferedReader(new FileReader(inputFileName));
        BufferedWriter bWriter = new BufferedWriter(new FileWriter(outputFileName));
        String line;
        boolean printTag = true;
        if("pos-chunk".equals(FilenameUtils.getExtension(inputFileName.getAbsolutePath()))){
            printTag = false;
        }

        String[] prevInput = new String[0];
        String[] nextInput = new String[0];
        while ((line = bReader.readLine()) != null) {
            if (!line.trim().equals("")) {
                String[] input = line.split("\t");
                String featureString = Features.generateFeatureString(prevInput, input, nextInput, printTag);
                bWriter.write(featureString + "\n");
                prevInput = input;
            } else {
                bWriter.write("\n");
            }
        }
        bReader.close();
        bWriter.close();
    }
}
