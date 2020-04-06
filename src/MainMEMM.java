public class MainMEMM {
    public static void main(String[] args) throws Exception{
        String trainFile = "D:\\NYU_assignment\\Spring_2020\\NLP\\Assignment6\\MaximumEntropyMarkov\\files\\CONLL_train.pos-chunk-name";
        String testFile = "D:\\NYU_assignment\\Spring_2020\\NLP\\Assignment6\\MaximumEntropyMarkov\\files\\CONLL_dev.pos-chunk";
        String featureFile = "D:\\NYU_assignment\\Spring_2020\\NLP\\Assignment6\\MaximumEntropyMarkov\\files\\CONLL_pos-chunk-name.feature";
        String modelFile = "D:\\NYU_assignment\\Spring_2020\\NLP\\Assignment6\\MaximumEntropyMarkov\\files\\CONLL.model";
        String finalOutputFile = "D:\\NYU_assignment\\Spring_2020\\NLP\\Assignment6\\MaximumEntropyMarkov\\files\\response.name";

        String featureFileForTest = "D:\\NYU_assignment\\Spring_2020\\NLP\\Assignment6\\MaximumEntropyMarkov\\files\\CONLL_pos-chunk.feature";

        FeatureBuilder featureBuilder = new FeatureBuilder(trainFile, featureFile);
        featureBuilder.generateFeatures();

        MEtrain.main(new String[]{featureFile, modelFile});

        FeatureBuilder featureBuilder2 = new FeatureBuilder(testFile, featureFileForTest);
        featureBuilder2.generateFeatures();

        MEtag.main(new String[]{featureFileForTest, modelFile, finalOutputFile});

//        Runtime.getRuntime().exec("python score.name.py");
    }
}
