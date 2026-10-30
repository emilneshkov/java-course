//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
/// **
///  * Extracts all hashtags from a social-media post.
///  *
///  * A hashtag begins at a '#', has a body of one or more letters/digits, and ends
///  * at the first character that is not a letter or digit. The returned tags are
///  * lowercased, stripped of the leading '#', and de-duplicated in first-seen
///  * order. A lone '#' is ignored.
///  *
///  * @param post the text of the post;
///  * @return the extracted tags (lowercased, without '#', de-duplicated, in
///  *         first-seen order), or an empty array if the post contains no hashtags
///  */

public  class HashtagExtractor{
    static boolean isHashtag(char ch){

        return ch == '#';
    }

    static boolean isLetterOrNumber(char ch){
        return (ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z') || (ch >= '0' && ch <= '9');
    }

    static int getPotentialHashtagsCount(String post){
        if(post.isEmpty()){
            return 0;
        }

        int potentialHashtagsCount=0;

        for (int i = 0; i < post.length(); i++) {
            if(isHashtag(post.charAt(i))){
                potentialHashtagsCount++;
            }
        }

        return potentialHashtagsCount;
    }

     static String getHashtagValue(int pos, String post){
        int nextIndex = pos + 1;
        String result = "";

        while (nextIndex < post.length() && isLetterOrNumber(post.charAt(nextIndex))) {

            result += post.charAt(nextIndex);
            nextIndex++;
        }

        return result;
    }
    static boolean hashtagAlreadyExists(String[] hashtagArr, String candidate){
        for (String s : hashtagArr) {
            if (candidate.equals(s)) {
                return true;
            }
        }

        return false;
    }
    static String[] trimResultArr (int resultSize, String[] result){
        String[] finalResult = new String[resultSize];

        for (int i = 0; i < resultSize; i++) {
            finalResult[i] = result[i];
        }

        return finalResult;
    }

    public static String[] extractHashtags(String post) {
        if (post == null || post.isEmpty()) {
            return new String[0];
        }

        int potentialHashtagsCount = getPotentialHashtagsCount(post);
        if(potentialHashtagsCount == 0){
            return new String[0];
        }

        int resultSize=0;
        String[] result = new String[potentialHashtagsCount];
        String lowerPost = post.toLowerCase();

        for (int i = 0; i < lowerPost.length(); i++) {
            if(isHashtag(lowerPost.charAt(i))){
                int nextIndex = i + 1;

                if (nextIndex < lowerPost.length() && isLetterOrNumber(lowerPost.charAt(nextIndex))) {

                    String newHashtagCandidate = getHashtagValue(i,lowerPost);
                    if(!hashtagAlreadyExists(result,newHashtagCandidate)){
                        result[resultSize]=newHashtagCandidate;
                        resultSize++;
                    }
                }
            }
        }

        return trimResultArr(resultSize,result);
    }
}
