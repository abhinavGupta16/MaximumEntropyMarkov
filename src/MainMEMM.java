import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

public class MainMEMM {
    public static final String BASE_DIR = "C:\\Datadrive\\NYU_assignment\\Spring_2020\\NLP\\Assignment6\\MaximumEntropyMarkov\\files\\";
    public static void main(String[] args) throws Exception{
        String trainFile = BASE_DIR + "CONLL_train.pos-chunk-name";
        String testFile = BASE_DIR + "CONLL_dev.pos-chunk";
        String featureFile = BASE_DIR + "CONLL_pos-chunk-name.feature";
        String modelFile = BASE_DIR + "CONLL.model.bin.gz";
        String outputFile = BASE_DIR + "mid_response.name";
        String finalOutputFile = BASE_DIR + "response.name";
        String actualOutputFile = BASE_DIR + "CONLL_dev.name";

        String featureFileForTest = BASE_DIR + "CONLL_pos-chunk.feature";

        FeatureBuilder featureBuilder = new FeatureBuilder(trainFile, featureFile);
        featureBuilder.generateFeatures();

        MEtrain.main(new String[]{featureFile, modelFile});

        FeatureBuilder featureBuilder2 = new FeatureBuilder(testFile, featureFileForTest);
        featureBuilder2.generateFeatures();

        MEtag.main(new String[]{featureFileForTest, modelFile, finalOutputFile});
//        Viterbi.main(new String[]{featureFileForTest, modelFile, finalOutputFile});
        diff(actualOutputFile, finalOutputFile);
    }

    public static void diff(String actualFileName, String generatedFileName) throws Exception{
        File actualFile = new File(actualFileName);
        File generatedFile = new File(generatedFileName);
        List<String> actualFileData = Files.readAllLines(actualFile.toPath(), StandardCharsets.UTF_8);
        List<String> generatedFileData = Files.readAllLines(generatedFile.toPath(), StandardCharsets.UTF_8);
        String diffFileName = BASE_DIR + "diff.txt";
        BufferedWriter bWriter = new BufferedWriter(new FileWriter(diffFileName));

        for(int i = 0; i < actualFileData.size(); i++){
            if(!actualFileData.get(i).trim().equals(generatedFileData.get(i).trim())){
                bWriter.write(actualFileData.get(i).split("\t")[0] + " " + actualFileData.get(i).split("\t")[1] + " " + generatedFileData.get(i).split("\t")[1] + "\n");
            }
        }
        bWriter.close();
    }
}
