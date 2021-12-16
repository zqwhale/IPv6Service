package mqeye.service.tools;
import java.io.UnsupportedEncodingException;

public class UnicodeConverter
{
  private static final char[] hexDigit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 
    'b', 'c', 'd', 'e', 'f' };

  public static void main(String[] args)
    throws UnsupportedEncodingException
  {
    String s = "ÎÒ¿¿";

    System.out.println("Original:\t\t" + s);

    s = toEncodedUnicode(s, true);

    System.out.println("to unicode:\t\t" + s);

    s = fromEncodedUnicode(s.toCharArray(), 0, s.length());

    System.out.println("from unicode:\t" + s);
  }

  private static char toHex(int nibble)
  {
    return hexDigit[(nibble & 0xF)];
  }

  public static String toEncodedUnicode(String theString, boolean escapeSpace)
  {
    char aChar;
    int len = theString.length();

    int bufLen = len * 2;

    if (bufLen < 0)
    {
      bufLen = 2147483647;
    }

    StringBuffer outBuffer = new StringBuffer(bufLen);

    for (int x = 0; x < len; ++x)
    {
      aChar = theString.charAt(x);

      if ((aChar > '=') && (aChar < ''))
      {
        if (aChar == '\\')
        {
          outBuffer.append('\\');

          outBuffer.append('\\');
        }
        else
        {
          outBuffer.append(aChar);
        }

      }
      else
      {
        switch (aChar)
        {
        case ' ':
          if ((x == 0) || (escapeSpace)) outBuffer.append('\\');

          outBuffer.append(' ');

          break;
        case '\t':
          outBuffer.append('\\');

          outBuffer.append('t');

          break;
        case '\n':
          outBuffer.append('\\');

          outBuffer.append('n');

          break;
        case '\r':
          outBuffer.append('\\');

          outBuffer.append('r');

          break;
        case '\f':
          outBuffer.append('\\');

          outBuffer.append('f');

          break;
        case '!':
        case '#':
        case ':':
        case '=':
          outBuffer.append('\\');

          outBuffer.append(aChar);

          break;
        default:
          if ((aChar < ' ') || (aChar > '~'))
          {
            outBuffer.append('\\');

            outBuffer.append('u');

            outBuffer.append(toHex(aChar >> '\f' & 0xF));

            outBuffer.append(toHex(aChar >> '\b' & 0xF));

            outBuffer.append(toHex(aChar >> '\4' & 0xF));

            outBuffer.append(toHex(aChar & 0xF));
          }
          else
          {
            outBuffer.append(aChar);
          }
        }

      }

    }

    return outBuffer.toString();
  }

  public static String fromEncodedUnicode(char[] in, int off, int len)
  {
    char[] out = new char[len];

    int outLen = 0;

    int end = off + len;

    while (off < end)
    {
      char aChar = in[(off++)];

      if (aChar == '\\')
      {
        aChar = in[(off++)];

        if (aChar == 'u')
        {
          int value = 0;

          for (int i = 0; i < 4; ++i)
          {
            aChar = in[(off++)];

            switch (aChar)
            {
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
              value = (value << 4) + aChar - 48;

              break;
            case 'a':
            case 'b':
            case 'c':
            case 'd':
            case 'e':
            case 'f':
              value = (value << 4) + 10 + aChar - 97;

              break;
            case 'A':
            case 'B':
            case 'C':
            case 'D':
            case 'E':
            case 'F':
              value = (value << 4) + 10 + aChar - 65;

              break;
            case ':':
            case ';':
            case '<':
            case '=':
            case '>':
            case '?':
            case '@':
            case 'G':
            case 'H':
            case 'I':
            case 'J':
            case 'K':
            case 'L':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'S':
            case 'T':
            case 'U':
            case 'V':
            case 'W':
            case 'X':
            case 'Y':
            case 'Z':
            case '[':
            case '\\':
            case ']':
            case '^':
            case '_':
            case '`':
            default:
              throw new IllegalArgumentException("Malformed \\uxxxx encoding.");
            }

          }

          out[(outLen++)] = (char)value;
        }
        else
        {
          if (aChar == 't')
          {
            aChar = '\t';
          }
          else if (aChar == 'r')
          {
            aChar = '\r';
          }
          else if (aChar == 'n')
          {
            aChar = '\n';
          }
          else if (aChar == 'f')
          {
            aChar = '\f';
          }

          out[(outLen++)] = aChar;
        }

      }
      else
      {
        out[(outLen++)] = aChar;
      }

    }

    return new String(out, 0, outLen);
  }
}
