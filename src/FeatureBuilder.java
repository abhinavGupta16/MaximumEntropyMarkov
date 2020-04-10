import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

public class FeatureBuilder {
    private File inputFileName;
    private File outputFileName;

    public FeatureBuilder(String inputFileName, String outputFileName){
        this.inputFileName = new File(inputFileName);
        this.outputFileName = new File(outputFileName);
    }

    public void generateFeatures() throws Exception{
        List<String> inputFileData = Files.readAllLines(inputFileName.toPath());
        BufferedWriter bWriter = new BufferedWriter(new FileWriter(outputFileName));
        boolean printTag = true;
        if("pos-chunk".equals(FilenameUtils.getExtension(inputFileName.getAbsolutePath()))){
            printTag = false;
        }

        boolean firstWord = true;
        boolean lastWord = false;

        String[] prevInput = new String[0];
        String[] prevPrevInput = new String[0];
        String[] nextInput = new String[0];
        String[] nextNextInput = new String[0];
        ExternalLists externalLists = new ExternalLists();
        for(int i = 0; i < inputFileData.size(); i++){
            String line = inputFileData.get(i);
            if(i+1 < inputFileData.size()){
                nextInput = inputFileData.get(i+1).split("\t");
                if(".".equals(nextInput[0]))
                    lastWord = true;
            } else {
                nextInput = new String[0];
            }
            if(i+2 < inputFileData.size() && !"".equals(inputFileData.get(i+2).trim()) && !".".equals(inputFileData.get(i+2).charAt(0))){
                nextNextInput = inputFileData.get(i+2).split("\t");
            } else {
                nextNextInput = new String[0];
            }
            if (!line.trim().equals("")) {
                String[] input = line.split("\t");
                String featureString = Features.generateFeatureString(prevInput, input, nextInput, printTag,
                        prevPrevInput, firstWord, lastWord, externalLists, nextNextInput);
                bWriter.write(featureString + "\n");
                if(prevInput.length>0){
                    prevPrevInput = prevInput;
                }

                prevInput = input;
                firstWord = false;
                lastWord = false;
            } else {
                bWriter.write("\n");
                prevInput = new String[0];
                prevPrevInput = new String[0];
                firstWord = true;
            }
        }
        bWriter.close();
    }

    // Not used as did not improve results
    public void postProcessFile(String outputFileName, String processedOutputFileName) throws Exception{
        BufferedWriter bWriter = new BufferedWriter(new FileWriter(processedOutputFileName));
        BufferedReader bReader = new BufferedReader(new FileReader(outputFileName));

        String line;
        String[] input;
        while((line = bReader.readLine())!=null){
            input = line.split("\t");
            if(input[0].matches("\".*")){
                bWriter.write(input[0] + "\tO");
            } else if(input[0].matches("\\..*")){
                bWriter.write(input[0] + "\tO");
            } else {
                bWriter.write(line);
            }
            bWriter.write("\n");
        }
        bWriter.close();
    }
}
