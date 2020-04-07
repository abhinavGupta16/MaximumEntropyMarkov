import java.util.Map;
import java.util.Set;

public enum Features {
    FIRST_LETTER_CAPS("[A-Z].*"),
    ALL_CAPS("[A-Z][A-Z]*"),
//    DATE("(([0-9][0-9]*-)([0-9][0-9]*-)*)?[0-9][0-9]*([a-zA-Z]?)"),
    NUMBER_WORD_PATTERN("([a-zA-Z]*)?(\\-?)[-+]?[0-9](\\:?)(\\-?)[0-9]*(,[0-9][0-9]*)*?(\\.[0-9]*)*([a-zA-Z]?)"),
//    ED_WORD_PATTERN(".*ed");
//    AL_WORD_PATTERN(".*al");
//    IAL_WORD_PATTERN(".*ial");
//    TIAL_WORD_PATTERN(".*tial"),
//    ABLE_WORD_PATTERN(".*able"),
//    S_WORD_PATTERN(".*s"),
    ING_WORD_PATTERN(".*ing"),
//    ER_WORD_PATTERN(".*er"),
//    ION_WORD_PATTERN(".*ion"),
//    ON_WORD_PATTERN(".*on"),
    LOGY_WORD_PATTERN(".*logy");
//    EST_WORD_PATTERN(".*est"),
//    ITY_WORD_PATTERN(".*ity"),
//    ISM_WORD_PATTERN(".*ism"),
//    AGE_WORD_PATTERN(".*age"),
//    ATE_WORD_PATTERN(".*ate"),
//    ARION_WORD_PATTERN(".*arion"),
//    TION_WORD_PATTERN(".*tion");
//    IVE_WORD_PATTERN(".*ive"),
//    ISH_WORD_PATTERN(".*ish");

    private final String patternVal;
    Features(String s) {
        this.patternVal = s;
    }

    public static String generateFeatureString(String[] prevInput, String[] input, String[] nextInput, boolean printTag,
                                               String[] prevPrevInput, boolean firstWord, boolean lastWord,
                                               ExternalLists externalLists, String[] nextNextInput) throws Exception{
        String word = input[0];
        String pos = input[1];
        String chunkTag = input[2];
        String tag = "";

        StringBuffer sb = new StringBuffer(word+"\tpos="+ pos + "\tchunkTag=" + chunkTag);
//        if(firstWord){
//            sb.append("\tfirstWord=truetrue");
//        } else {
//            sb.append("\tfirstWord=falsefalse");
//        }

//        if(lastWord){
//            sb.append("\tlastWord=truetrue");
//        } else {
//            sb.append("\tlastWord=falsefalse");
//        }

        if(input.length==4){
            tag = input[3];
        }
        appendPrevTag(prevInput, sb);
        appendNextTag(nextInput, sb, externalLists);
//        appendNextNextTag(nextNextInput, sb);
        appendPrevPrevTag(prevPrevInput, sb);
//        appendNamePrefixes(prevInput, prevPrevInput, sb, externalLists);
//        addNoun(input, externalLists, sb);
//        appendForSameTag(prevInput, input, sb);
        for (Features pattern : Features.values()) {
            if(word.matches(pattern.patternVal)){
                sb.append("\t" + pattern.name() + "=truetrue");
            } else {
                sb.append("\t" + pattern.name() + "=falsefalse");
            }
        }
        addExternalListFeatures(word, sb, externalLists);
        if(printTag)
            sb.append("\t" + tag);
        return sb.toString();
    }

    private static void appendForSameTag(String[] prevInput, String[] input, StringBuffer sb){
        if(prevInput.length < 3){
            sb.append("\tbothSame=$$$");
            return;
        }
        if(prevInput[1].equals(input[1]) && input[1].equals("NNP")){
            sb.append("\tbothSame=truetrue");
        } else {
            sb.append("\tbothSame=falsefalse");
        }
    }

    private static void addNoun(String[] input, ExternalLists externalLists, StringBuffer sb){
        if(externalLists.nounPosTags.contains(input[1])){
            sb.append("\tnntag=truetrue");
        } else {
            sb.append("\tnntag=falsefalse");
        }
    }

    private static void appendPrevTag(String[] prevInput, StringBuffer sb){
        if(prevInput.length<3) {
            sb.append("\tprevWord=$$$\tprevPos=$$$\tprevChunkTag=$$$\tprevTag=@@");
            return;
        }
        String word = prevInput[0];
        String pos = prevInput[1];
        String chunkTag = prevInput[2];
        String tag = "@@";
        if(prevInput.length==4){
            tag = prevInput[3];
        }
//        sb.append("\tprevWord="+ word + "\tprevPos="+ pos + "\tprevChunkTag=" + chunkTag + "\tprevTag=" + tag);
        sb.append("\tprevWord="+ word + "\tprevPos="+ pos + "\tprevTag=" + tag);
//        sb.append("\tprevWord="+ word + "\tprevPos="+ pos + "\tprevTag=" + tag);
    }

    private static void appendNamePrefixes(String[] prevInput, String[] prevPrevInput, StringBuffer sb, ExternalLists externalLists){
        if(prevInput.length<3) {
            sb.append("\tprevPrefix=$$$\tprevPrevPrefix=$$$");
        } else {
            String word = prevInput[0];
            if(externalLists.namePrefixes.contains(word.toUpperCase())){
                sb.append("\tprevPrefix=truetrue");
            } else {
                sb.append("\tprevPrefix=falsefalse");
            }
        }
        if(prevPrevInput.length<3){
            sb.append("\tprevPrevPrefix=$$$");
        } else {
            String word = prevPrevInput[0];
            if(externalLists.namePrefixes.contains(word.toUpperCase())){
                sb.append("\tprevPrevPrefix=truetrue");
            } else {
                sb.append("\tprevPrevPrefix=falsefalse");
            }
        }
    }

    private static void appendPrevPrevTag(String[] prevPrevInput, StringBuffer sb){
        if(prevPrevInput.length<3) {
            sb.append("\tprevPrevWord=((()");
            return;
        }
        String word = prevPrevInput[0];
        String pos = prevPrevInput[1];
        String chunkTag = prevPrevInput[2];
        String tag = "&&&";
        if(prevPrevInput.length==4){
            tag = prevPrevInput[3];
        }
//        sb.append("\tprevPrevWord="+ word + "\tprevPrevPos="+ pos + "\tprevPrevChunkTag=" + chunkTag + "\tprevTag=" + tag);
        sb.append("\tprevPrevPos="+ pos + "\tprevPrevTag="+ tag);
//        sb.append("\tprevWord="+ word + "\tprevPrevPos="+ pos + "\tprevPrevTag=" + tag);
    }

    private static void appendNextTag(String[] nextInput, StringBuffer sb, ExternalLists externalLists){
        if(nextInput.length<3) {
            sb.append("\tnextWord=****\tnextPos=****\tnextChunkTag=****");
            return;
        }
        String word = nextInput[0];
        String pos = nextInput[1];
        String chunkTag = nextInput[2];
        String tag = "@@";
        if(nextInput.length==4){
            tag = nextInput[3];
        }

//        if(externalLists.getCompanySuffixes().contains(word.toUpperCase())){
//           sb.append("\tcompanySuffix=truetrue");
//        } else {
//            sb.append("\tcompanySuffix=falsefalse");
//        }
        sb.append("\tnextWord="+ word + "\tnextPos="+ pos + "\tnextChunkTag=" + chunkTag);
//        sb.append("\tnextWord="+ word + "\tnextPos="+ pos + "\tnextChunkTag=" + chunkTag);
    }

    private static void appendNextNextTag(String[] nextNextInput, StringBuffer sb){
        if(nextNextInput.length<3) {
            sb.append("\tnextNextWord=****\tnextNextChunkTag=****");
            return;
        }
        String word = nextNextInput[0];
        String pos = nextNextInput[1];
        String chunkTag = nextNextInput[2];
        String tag = "@@";
        if(nextNextInput.length==4){
            tag = nextNextInput[3];
        }

        sb.append("\tnextChunkTag=" + chunkTag);
//        sb.append("\tnextWord="+ word + "\tnextPos="+ pos + "\tnextChunkTag=" + chunkTag);
    }

    private static void addExternalListFeatures(String word, StringBuffer sb, ExternalLists externalLists) {
        for(Map.Entry<String, Set<String>> entry : externalLists.getExternalListsMap().entrySet()){
            String featureName = entry.getKey();
            Set<String> set = entry.getValue();
            if(set.contains(word.toUpperCase())){
                sb.append("\t" + featureName + "=truetrue");
            } else {
                sb.append("\t" + featureName + "=falsefalse");
            }
        }
//        System.out.println(word);
    }
}