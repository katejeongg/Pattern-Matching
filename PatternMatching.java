import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PatternMatching {

    public static List<Integer> bruteForce(CharSequence pattern,
                                           CharSequence text,
                                           CharacterComparator comparator) {
        if (pattern == null || pattern.length() == 0) {
            throw new IllegalArgumentException("Pattern is null or of length 0.");
        }
        if (text == null || comparator == null) {
            throw new IllegalArgumentException("Text or comparator is null.");
        }
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i <= text.length() - pattern.length(); i++) {
            int j = 0;
            while (j < pattern.length() && comparator.compare(pattern.charAt(j), text.charAt(i + j)) == 0) {
                j++;
            }
            if (j == pattern.length()) {
                list.add(i);
            }
        }
        return list;
    }

    public static int[] buildFailureTable(CharSequence pattern,
                                          CharacterComparator comparator) {
        if (pattern == null || comparator == null) {
            throw new IllegalArgumentException("Pattern or comparator is null.");
        }
        int[] table = new int[pattern.length()];
        int i = 0;
        int j = 1;
        table[i] = 0;
        while (j < pattern.length()) {
            if (comparator.compare(pattern.charAt(i), pattern.charAt(j)) == 0) {
                table[j] = i + 1;
                i++;
                j++;
            } else {
                if (i == 0) {
                    table[j] = 0;
                    j++;
                } else {
                    i = table[i - 1];
                }
            }
        }
        return table;
    }

    public static List<Integer> kmp(CharSequence pattern, CharSequence text,
                                    CharacterComparator comparator) {
        if (pattern == null || pattern.length() == 0) {
            throw new IllegalArgumentException("Pattern is null or of length 0.");
        }
        if (text == null || comparator == null) {
            throw new IllegalArgumentException("Text or comparator is null.");
        }
        if (pattern.length() > text.length()) {
            return new ArrayList<Integer>();
        }
        int[] table = buildFailureTable(pattern, comparator);
        List<Integer> list = new ArrayList<Integer>();
        int i = 0;
        int j = 0;
        while (i <= text.length() - pattern.length()) {
            while (j < pattern.length() && comparator.compare(pattern.charAt(j), text.charAt(i + j)) == 0) {
                j++;
            }
            if (j == 0) {
                i++;
            } else {
                if (j == pattern.length()) {
                    list.add(i);
                }
                int shift = table[j - 1];
                i += j - shift;
                j = shift;
            }
        }
        return list;
    }

    public static Map<Character, Integer> buildLastTable(CharSequence pattern) {
        if (pattern == null) {
            throw new IllegalArgumentException("Pattern is null.");
        }
        Map<Character, Integer> table = new HashMap<Character, Integer>();
        for (int i = 0; i < pattern.length(); i++) {
            table.put(pattern.charAt(i), i);
        }
        return table;
    }

    public static List<Integer> boyerMoore(CharSequence pattern,
                                           CharSequence text,
                                           CharacterComparator comparator) {
        if (pattern == null || pattern.length() == 0) {
            throw new IllegalArgumentException("Pattern is null or of length 0.");
        }
        if (text == null || comparator == null) {
            throw new IllegalArgumentException("Text or comparator is null.");
        }
        if (pattern.length() > text.length()) {
            return new ArrayList<Integer>();
        }
        Map<Character, Integer> table = buildLastTable(pattern);
        List<Integer> list = new ArrayList<Integer>();
        int i = 0;
        while (i <= text.length() - pattern.length()) {
            int j = pattern.length() - 1;
            while (j >= 0 && comparator.compare(text.charAt(i + j), pattern.charAt(j)) == 0) {
                j--;
            }
            if (j == -1) {
                list.add(i);
                i++;
            } else {
                int shift = table.getOrDefault(text.charAt(i + j), -1);
                if (shift < j) {
                    i += j - shift;
                } else {
                    i++;
                }
            }
        }
        return list;
    }

    public static List<Integer> boyerMooreGalilRule(CharSequence pattern,
                                                    CharSequence text,
                                                    CharacterComparator comparator) {
        if (pattern == null || pattern.length() == 0) {
            throw new IllegalArgumentException("Pattern is null or has length 0.");
        }
        if (text == null || comparator == null) {
            throw new IllegalArgumentException("Text or comparator is null.");
        }
        if (pattern.length() > text.length()) {
            return new ArrayList<Integer>();
        }
        int[] failTable = buildFailureTable(pattern, comparator);
        Map<Character, Integer> lastTable = buildLastTable(pattern);
        List<Integer> list = new ArrayList<Integer>();
        int k = pattern.length() - failTable[pattern.length() - 1];
        int i = 0;
        int lowBound = 0;
        while (i <= text.length() - pattern.length()) {
            int j = pattern.length() - 1;
            while (j >= lowBound && comparator.compare(text.charAt(i + j), pattern.charAt(j)) == 0) {
                j--;
            }
            if (j < lowBound) {
                list.add(i);
                i += k;
                lowBound = pattern.length() - k;
            } else {
                int shift = lastTable.getOrDefault(text.charAt(i + j), -1);
                if (shift < j) {
                    i += j - shift;
                } else {
                    i++;
                }
                lowBound = 0;
            }
        }
        return list;
    }
}
