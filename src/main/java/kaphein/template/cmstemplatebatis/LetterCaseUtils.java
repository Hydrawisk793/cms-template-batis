package kaphein.template.cmstemplatebatis;

import java.util.ArrayList;
import java.util.List;

public final class LetterCaseUtils
{
    public static String toDelimiterSeparatedCase(String text, String delimiter)
    {
        boolean shouldIncrementE = false;
        boolean shouldExtractToken = false;
        char initial;
        List<String> tokens = new ArrayList<>();
        int s = 0, e = 1;

        while(s < text.length())
        {
            if(e < text.length())
            {
                char c = text.charAt(e);

                if(Character.isAlphabetic(c))
                {
                    shouldExtractToken = c == Character.toUpperCase(c);
                }
                else
                {
                    shouldExtractToken = (e > 0 && Character.isAlphabetic(text.charAt(e - 1)));
                }

                shouldIncrementE = true;
            }
            else
            {
                shouldExtractToken = true;
            }

            if(shouldExtractToken)
            {
                shouldExtractToken = false;

                initial = text.charAt(s);
                tokens.add(Character.toLowerCase(initial) + text.substring(s + 1, e).toLowerCase());

                s = e;
            }

            if(shouldIncrementE)
            {
                shouldIncrementE = false;
                ++e;
            }
        }

        return String.join(delimiter, tokens);
    }

    public static String toSnakeCase(String text)
    {
        return toDelimiterSeparatedCase(text, "_");
    }
}
