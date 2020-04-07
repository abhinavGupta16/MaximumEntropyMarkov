import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

public class MainMEMM {
    public static void main(String[] args) throws Exception{
        String trainFile = "D:\\NYU_assignment\\Spring_2020\\NLP\\Assignment6\\MaximumEntropyMarkov\\files\\CONLL_train.pos-chunk-name";
        String testFile = "D:\\NYU_assignment\\Spring_2020\\NLP\\Assignment6\\MaximumEntropyMarkov\\files\\CONLL_dev.pos-chunk";
        String featureFile = "D:\\NYU_assignment\\Spring_2020\\NLP\\Assignment6\\MaximumEntropyMarkov\\files\\CONLL_pos-chunk-name.feature";
        String modelFile = "D:\\NYU_assignment\\Spring_2020\\NLP\\Assignment6\\MaximumEntropyMarkov\\files\\CONLL.model.bin.gz";
        String outputFile = "D:\\NYU_assignment\\Spring_2020\\NLP\\Assignment6\\MaximumEntropyMarkov\\files\\mid_response.name";
        String finalOutputFile = "D:\\NYU_assignment\\Spring_2020\\NLP\\Assignment6\\MaximumEntropyMarkov\\files\\response.name";
        String actualOutputFile = "D:\\NYU_assignment\\Spring_2020\\NLP\\Assignment6\\MaximumEntropyMarkov\\files\\CONLL_dev.name";

        String featureFileForTest = "D:\\NYU_assignment\\Spring_2020\\NLP\\Assignment6\\MaximumEntropyMarkov\\files\\CONLL_pos-chunk.feature";

        FeatureBuilder featureBuilder = new FeatureBuilder(trainFile, featureFile);
        featureBuilder.generateFeatures();

        MEtrain.main(new String[]{featureFile, modelFile});

        FeatureBuilder featureBuilder2 = new FeatureBuilder(testFile, featureFileForTest);
        featureBuilder2.generateFeatures();

        MEtag.main(new String[]{featureFileForTest, modelFile, finalOutputFile});
//        featureBuilder2.postProcessFile(outputFile, finalOutputFile);
        diff(actualOutputFile, finalOutputFile);
//        Runtime.getRuntime().exec("python score.name.py");
    }

    public static void diff(String actualFileName, String generatedFileName) throws Exception{
        File actualFile = new File(actualFileName);
        File generatedFile = new File(generatedFileName);
        List<String> actualFileData = Files.readAllLines(actualFile.toPath(), StandardCharsets.UTF_8);
        List<String> generatedFileData = Files.readAllLines(generatedFile.toPath(), StandardCharsets.UTF_8);
        String diffFileName = "D:\\NYU_assignment\\Spring_2020\\NLP\\Assignment6\\MaximumEntropyMarkov\\files\\diff.txt";
        BufferedWriter bWriter = new BufferedWriter(new FileWriter(diffFileName));

        for(int i = 0; i < actualFileData.size(); i++){
            if(!actualFileData.get(i).trim().equals(generatedFileData.get(i).trim())){
                bWriter.write(actualFileData.get(i).split("\t")[0] + " " + actualFileData.get(i).split("\t")[1] + " " + generatedFileData.get(i).split("\t")[1] + "\n");
            }
        }
        bWriter.close();
    }
}
