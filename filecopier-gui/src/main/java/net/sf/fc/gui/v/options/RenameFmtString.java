package net.sf.fc.gui.v.options;

import java.util.TimeZone;
import java.util.Calendar;
import java.util.Formatter;
import java.util.IllegalFormatException;

public final class RenameFmtString {
    private final String renameFmtString;
    private final String internalRenameFmtString;
    private final String previewRenameFmtString;
    private final Calendar cal;

    /** Creates a new instance of RenameFmtString */
    public RenameFmtString(String renameFmtString, Calendar cal) throws IllegalFormatException {
        this.renameFmtString = renameFmtString;
        this.cal = cal;
        internalRenameFmtString = buildRenameInternalFmtString();
        previewRenameFmtString = buildPreviewRenameFmtString();
    }

    public RenameFmtString setCalendar(Calendar cal) {
        return new RenameFmtString(renameFmtString, cal);
    }

    public RenameFmtString setTimeZone(TimeZone timeZone) {
        Calendar copy = (Calendar)cal.clone();
        copy.setTimeZone(timeZone);
        return new RenameFmtString(renameFmtString, copy);
    }

    public RenameFmtString setRenameFmtString(String renameFmtString) throws IllegalFormatException {
        return new RenameFmtString(renameFmtString, cal);
    }

    public RenameFmtString setRenameFmtString(String renameFmtString, Calendar cal) throws IllegalFormatException {
        return new RenameFmtString(renameFmtString, cal);
    }

    public Calendar getCalendar() {
        return cal;
    }

    public String getPreviewRenameFmtString() {
        return previewRenameFmtString;
    }

    public String getRenameFmtString() {
        return renameFmtString;
    }

    private String buildRenameInternalFmtString() {
        StringBuilder sb = new StringBuilder(32);
        int percentCt = 0;
        char prevChar = ' ';
        boolean fmtExpected = false;
        for (int i = 0; i < renameFmtString.length(); i++) {
            char curChar = renameFmtString.charAt(i);
            sb.append(curChar);

            if (curChar == '%') {
                // Set fmtExpected to the opposite of what it was.
                fmtExpected = !fmtExpected;
                if (fmtExpected) {
                    // The next char should be a format character.
                    if (percentCt++ > 0) {
                        // The percent count is greater than 0. This means this is not the first format character, so
                        // append a < so that we will not have to add another arg to the list when we call fmt.format()
                        sb.append('<');
                        // Set the curChar to < so when I set the prevChar it will be set to <
                        curChar = '<';
                    }
                } else if (prevChar == '<') {
                    // The prevChar was a <, so I need to get rid of the < because the second % means to insert a literal %.
                    // If I leave the < in, this will not work.
                    if (percentCt > 1) {
                        sb.deleteCharAt(sb.length() - 2);
                    }
                    // Decrement the percentCt because the previous time I incremented it, that % was just to insert a literal %.
                    percentCt--;
                } else if (percentCt == 1 && prevChar == '%') {
                    // After the first %, I do not add a <. This % follows the first %, so I must decrement percentCt since that
                    // % was just to insert a literal %.
                    percentCt--;
                }
            } else if ((fmtExpected) && (curChar == '<') && (prevChar == '<')) {
                // The user inserted a '<', so delete the one that I put in there.
                sb.deleteCharAt(sb.length() - 2);
            } else if ((curChar == 'n') && (fmtExpected)) {
                if (prevChar == '<') {
                    // Delete the < I put in there because that means the char before that is a % and %n equals new line. A <
                    // in between would mess that up.
                    sb.deleteCharAt(sb.length() - 2);
                } else if (percentCt == 1 && prevChar == '%') {
                    // Decrement the percentCt because the previous % was just to insert a newline character.
                    percentCt--;
                }
                fmtExpected = false;
            } else if (curChar != 't' && (Character.isLetter(curChar)) && (fmtExpected)) {
                // If a character in a format sequence is a letter, that is the end of the format sequence, so set fmtExpected
                // to false.
                fmtExpected = false;
            }
            prevChar = curChar;
        }

        return sb.toString();
    }

    private String buildPreviewRenameFmtString() throws IllegalFormatException {
        Formatter fmt = new Formatter();

        fmt.format(internalRenameFmtString, cal);
        return fmt.toString();
    }

    public static void main(String[] args) {
        RenameFmtString renameFmtString = new RenameFmtString("_%tY%tm%td_%tH%tM%tS%tL", Calendar.getInstance());
        System.out.println("Rename Format String  = " + renameFmtString.getRenameFmtString());
        System.out.println("Rename Format Preview = " + renameFmtString.getPreviewRenameFmtString());
    }

}
