package com.neeter.preeter;

public class LiteralHelper
{
    public static String processStringLiteral(String content)
    {
        StringBuilder literalBuilder = new StringBuilder();

        int contentLen = content.length() - 1;
        for (int i = 1; i < contentLen; ++i)
        {
            char character = content.charAt(i);

            if (character == '\\' && i < contentLen - 1)
            {
                char controlChar = content.charAt(++i);
                switch (controlChar)
                {
                    case 'n':
                        literalBuilder.append('\n');
                        break;
                    case '\\':
                        literalBuilder.append('\\');
                        break;
                    default:
                        break; // Do nothing
                }
            }
            else
            {
                literalBuilder.append(character);
            }
        }

        return literalBuilder.toString();
    }
}
