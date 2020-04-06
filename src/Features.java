public enum Features {
    FIRST_LETTER_CAPS("[A-Z].*");


    private final String patternVal;
    Features(String s) {
        this.patternVal = s;
    }

    public static String generateFeatureString(String[] prevInput, String[] input, String[] nextInput, boolean printTag) {
        String word = input[0];
        String pos = input[1];
        String chunkTag = input[2];
        String tag = "";

        StringBuffer sb = new StringBuffer(word+"\tpos="+ pos + "\tchunkTag=" + chunkTag);
        if(input.length==4){
            tag = input[3];
        }
        appendPrevTag(prevInput, sb);
        for (Features pattern : Features.values()) {
            if(word.matches(pattern.patternVal)){
                sb.append("\t" + pattern.name() + "=TRUE");
            } else {
                sb.append("\t" + pattern.name() + "=FALSE");
            }
        }
        if(printTag)
            sb.append("\t" + tag);
        return sb.toString();
    }

    private static void appendPrevTag(String[] prevInput, StringBuffer sb){
        if(prevInput.length<3)
            return;
        String word = prevInput[0];
        String pos = prevInput[1];
        String chunkTag = prevInput[2];
        String tag = "@@";
        if(prevInput.length==4){
            tag = prevInput[3];
        }
        sb.append("\tprevWord="+ word + "\tprevPos="+ pos + "\tprevChunkTag=" + chunkTag + "\tprevTag=" + tag);
    }
}