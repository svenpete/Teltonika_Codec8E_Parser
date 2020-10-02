package DataParser;

public class BitConverter {

    /**
     * This method turns a binary into a signed two complement binary string.
     * @param str should be a hex string as binary representation.
     * @return signed two complement binary string
     */
    public static String findTwoscomplement(StringBuffer str) {
        int strLength = str.length();

        // Traverse the string to get first '1' from
        // the last of string
        int bitPosition;
        for (bitPosition = strLength - 1; bitPosition >= 0; bitPosition--)
            if ( str.charAt(bitPosition) == '1' )
                break;

        // If there exists no '1' concat 1 at the
        // starting of string
        if ( bitPosition == -1 )
            return "1" + str;

        // Continue traversal after the position of
        // first '1'
        for (int k = bitPosition - 1; k >= 0; k--) {
            //Just flip the values
            if ( str.charAt(k) == '1' )
                str.replace(k, k + 1, "0");
            else
                str.replace(k, k + 1, "1");
        }

        // return the modified string
        return str.toString();
    }
}
