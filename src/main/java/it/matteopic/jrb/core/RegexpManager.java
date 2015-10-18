/**
 * RegexpManager.java
 *
 * Creato il 27-lug-2005 18.10.49
 */
package it.matteopic.jrb.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.regexp.RECompiler;
import org.apache.regexp.REProgram;
import org.apache.regexp.RESyntaxException;

/**
 * @author Matteo Piccinini
 */
public class RegexpManager {
	
	public RegexpManager(){
		useAnchoringBounds = true;
	}

    private boolean caseInsensitive, multiline, dotall, unicodeCase, canonEq,
            unixLine, comments, useAnchoringBounds;
    private String regexp;

    public void setUseAnchoringBounds(boolean useAnchoringBounds) {
        this.useAnchoringBounds = useAnchoringBounds;
    }

    public void setUnixLine(boolean unixLine) {
        this.unixLine = unixLine;
    }

    public void setAllowComments(boolean comments) {
        this.comments = comments;
    }

    public void setCanonEq(boolean canonEq) {
        this.canonEq = canonEq;
    }

    public void setCaseInsensitive(boolean caseInsensitive) {
        this.caseInsensitive = caseInsensitive;
    }

    public void setDotall(boolean dotall) {
        this.dotall = dotall;
    }

    public void setMultiline(boolean multiline) {
        this.multiline = multiline;
    }

    public void setUnicodeCase(boolean unicodeCase) {
        this.unicodeCase = unicodeCase;
    }

    /**
     * @param regexp
     *            The regexp to set.
     */
    public void setRegexp(String regexp) {
        this.regexp = regexp;
        int flags = (canonEq ? Pattern.CANON_EQ : 0)
                | (caseInsensitive ? Pattern.CASE_INSENSITIVE : 0)
                | (dotall ? Pattern.DOTALL : 0)
                | (multiline ? Pattern.MULTILINE : 0)
                | (unixLine ? Pattern.UNIX_LINES : 0)
                | (comments ? Pattern.COMMENTS : 0)
                | (unicodeCase ? Pattern.UNICODE_CASE : 0);

        pattern = Pattern.compile(regexp, flags);
    }

    public List<Match> process(String textToTest) {
        // char[]chars = textToTest.toCharArray();
        // StringBuffer sb = new StringBuffer();
        // for (int i = 0; i < chars.length; i++) {
        // sb.append((int)(chars[i]));
        // }
        // System.out.println(sb);

        if (pattern == null)
            throw new NullPointerException("Regexp mancante");

        matcher = pattern.matcher(textToTest);
        if (matcher == null)
            return Collections.emptyList();
        matcher.useAnchoringBounds(useAnchoringBounds);
        // matcher.useTransparentBounds(useAnchoringBounds)

        List<Match> list = new ArrayList<Match>();
        while (matcher.find()) {
            MatchResult result = matcher.toMatchResult();
            process(result, list, textToTest);
        }
        return list;
    }

    private void process(MatchResult result, List<Match> resultList,
            String originalText) {
        // System.out.println("---------------------");
        // System.out.println(originalText);
        // System.out.println("Start: " + result.start());
        // System.out.println("End: " + result.end());
        // System.out.println(originalText.substring(result.start(),
        // result.end()));

        int groups = result.groupCount();
        System.out.println("Groups count " + groups);
        if (groups == 0) {
            resultList.add(new Match(result.start(), result.end()));
        } else {
            int start, end;
            for (int i = 1; i <= groups; i++) {
                start = result.start();// start(i);
                end = result.end();// end(i);
                resultList.add(new Match(start, end));
                System.out.println("Group " + i);
                System.out.println(" Start: " + result.start());
                System.out.println(" End " + result.end());
                System.out.println(" Start "+i+": " + result.start(i));
                System.out.println(" End "+i+": " + result.end(i));
                //
                // System.out.print(" start: " + start+" end: "+end);
                System.out.println(" value: " + originalText.substring(result.start(i),result.end(i)));
            }
        }
    }

    public String jakartaRegexpPrecompile(String name) {
        // Create a compiler object
        RECompiler r = new RECompiler();
        StringBuilder sb = new StringBuilder();
        try {
            // Compile regular expression
            String instructions = name + "Instructions";

            // Output program as a nice, formatted character array
            sb.append("\n    // Pre-compiled regular expression '" + pattern
                    + "'\n" + "    private static final char[] " + instructions
                    + " = \n    {");

            // Compile program for pattern
            REProgram program = r.compile(regexp);

            // Number of columns in output
            int numColumns = 7;

            // Loop through program
            char[] p = program.getInstructions();
            for (int j = 0; j < p.length; j++) {
                // End of column?
                if ((j % numColumns) == 0) {
                    sb.append("\n        ");
                }

                // Print character as padded hex number
                String hex = Integer.toHexString(p[j]);
                while (hex.length() < 4) {
                    hex = "0" + hex;
                }
                sb.append("0x" + hex + ", ");
            }

            // End of program block
            sb.append("\n    };");
            sb.append("\n    private static final REProgram " + name
                    + " = new REProgram(" + instructions + ");");
//            sb.append("\n    RE re = new RE(").append(name).append(");");
//            sb.append("\n    re");
            return sb.toString();
        } catch (RESyntaxException e) {
            return("Syntax error in expression \"" + name + "\": "
                    + e.toString());
        } catch (Exception e) {
            return("Unexpected exception: " + e.toString());
        } catch (Error e) {
            return("Internal error: " + e.toString());
        }
    }

    public String replace(String replacement, String textToReplace){
        Matcher matcher = pattern.matcher(textToReplace);
        return matcher.replaceAll(replacement);
    }


    private Pattern pattern;

    private Matcher matcher;
}
