package kaphein.template.cmstemplatebatis;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.StreamSupport;

/**
 *  경로 표현식 관련 유틸리티 function 모음집
 */
public final class PathUtils
{
    public static final char DEFAULT_SEPARATOR = '/';

    public static final Collection<Character> SEPARATORS = Collections.unmodifiableList(Arrays.asList('/', '\\'));

    public static String join(Iterable<String> tokens)
    {
        return join(DEFAULT_SEPARATOR, tokens);
    }

    public static String join(char separator, Iterable<String> tokens)
    {
        return join(
            separator,
            StreamSupport
                .stream(tokens.spliterator(), false)
                .toArray(String[]::new)
        );
    }

    public static String join(String... tokens)
    {
        return join(DEFAULT_SEPARATOR, tokens);
    }

    /**
     * @see <a href="https://github.com/browserify/path-browserify/blob/872fec31a8bac7b9b43be0e54ef3037e0202c5fb/index.js#L157">path-browserify</a>
     */
    public static String join(char separator, String... tokens)
    {
        if(null == tokens)
        {
            throw new IllegalArgumentException("'tokens' cannot be null");
        }

        String result = null;

        if(tokens.length < 1)
        {
            result = ".";
        }
        else
        {
            String joined = null;

            for(String token : tokens)
            {
                if(!token.isEmpty())
                {
                    if(null == joined)
                    {
                        joined = token;
                    }
                    else
                    {
                        joined += separator + token;
                    }
                }
            }

            if(null == joined)
            {
                result = ".";
            }
            else
            {
                result = normalize(separator, joined);
            }
        }

        return result;
    }

    public static String normalize(String path)
    {
        return normalize(DEFAULT_SEPARATOR, path);
    }

    /**
     * @see <a href="https://github.com/browserify/path-browserify/blob/872fec31a8bac7b9b43be0e54ef3037e0202c5fb/index.js#L157">path-browserify</a>
     */
    public static String normalize(char separator, String path)
    {
        if(path.length() < 1)
        {
            path = ".";
        }
        else
        {
            final boolean isAbsolute = isSeparator(path, 0);
            final boolean trailingSeparator = isSeparator(path, path.length() - 1);

            path = normalizeStringPosix(separator, path, !isAbsolute);

            if(path.isEmpty() && !isAbsolute)
            {
                path = ".";
            }
            if(!path.isEmpty() && trailingSeparator)
            {
                path += separator;
            }

            if(isAbsolute)
            {
                path = separator + path;
            }
        }

        return path;
    }

    /**
     * @see <a href="https://github.com/browserify/path-browserify/blob/872fec31a8bac7b9b43be0e54ef3037e0202c5fb/index.js#L157">path-browserify</a>
     */
    private static String normalizeStringPosix(char separator, String path, boolean allowAboveRoot)
    {
        String result = "";
        Character code = null;
        int lastSegmentLength = 0;
        int lastSlash = -1;
        int dots = 0;

        for(int i = 0; i <= path.length(); ++i)
        {
            if(i < path.length())
            {
                code = path.charAt(i);
            }
            else if(isSeparator(code))
            {
                break;
            }
            else
            {
                code = separator;
            }

            if(isSeparator(code))
            {
                if(lastSlash == i - 1 || dots == 1)
                {
                    // Does nothing.
                }
                else if(lastSlash != i - 1 && dots == 2)
                {
                    if(result.length() < 2 || lastSegmentLength != 2 || result.charAt(result.length() - 1) != '.' || result.charAt(result.length() - 2) != '.')
                    {
                        if(result.length() > 2)
                        {
                            int lastSlashIndex = lastSeparatorIndexOf(result);
                            if(lastSlashIndex != result.length() - 1)
                            {
                                if(lastSlashIndex == -1)
                                {
                                    result = "";
                                    lastSegmentLength = 0;
                                }
                                else
                                {
                                    result = result.substring(0, lastSlashIndex);
                                    lastSegmentLength = result.length() - 1 - lastSeparatorIndexOf(result);
                                }
                                lastSlash = i;
                                dots = 0;

                                continue;
                            }
                        }
                        else if(result.length() == 2 || result.length() == 1)
                        {
                            result = "";
                            lastSegmentLength = 0;
                            lastSlash = i;
                            dots = 0;

                            continue;
                        }
                    }
                    if(allowAboveRoot)
                    {
                        if(result.length() > 0)
                        {
                            result += separator + "..";
                        }
                        else
                        {
                            result = "..";
                        }

                        lastSegmentLength = 2;
                    }
                }
                else
                {
                    if(result.length() > 0)
                    {
                        result += separator + path.substring(lastSlash + 1, i);
                    }
                    else
                    {
                        result = path.substring(lastSlash + 1, i);
                    }

                    lastSegmentLength = i - lastSlash - 1;
                }

                lastSlash = i;
                dots = 0;
            }
            else if(code == '.' && dots != -1)
            {
                ++dots;
            }
            else
            {
                dots = -1;
            }
        }

        return result;
    }

    private static int lastSeparatorIndexOf(String text)
    {
        int index = -1;

        for(int i = text.length(); index < 0 && i >= 0; )
        {
            --i;

            if(isSeparator(text, i))
            {
                index = i;
            }
        }

        return index;
    }

    private static boolean isSeparator(String text, int index)
    {
        return index >= 0 && index < text.length() && isSeparator(text.charAt(index));
    }

    private static boolean isSeparator(Character c)
    {
        return SEPARATORS.contains(c);
    }

    private PathUtils()
    {
        // Does nothing.
    }
}
