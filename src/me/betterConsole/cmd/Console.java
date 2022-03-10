package me.betterConsole.cmd;

public class Console {

    private static java.io.BufferedWriter     writeBufferedWriter;
    private static java.io.BufferedWriter     errorBufferedWriter;
    private static java.text.SimpleDateFormat simpleDateFormat;
    private static java.nio.charset.Charset   charsets;
    private static int                        outputBufferSize;
    private static boolean                    shutdownHookAdded;
    private static long                       timer;
    private static String                     pattern;
    private static java.util.Locale           locale;

    public static void print(final Object message) {
        write(getTimeFormatted() + ": " + message + "\n");
    }

    public static void error(final Object errorMessage) {
        writeError(getTimeFormatted() + ": " + errorMessage + "\n");
    }

    public static void write(final Object message) {
        try {
            getWriteBufferedWriter().write(message.toString());
            check();
        } catch(java.io.IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeError(final Object errorMessage) {
        try {
            getErrorBufferedWriter().write(errorMessage.toString());
            check();
        } catch(java.io.IOException e) {
            e.printStackTrace();
        }
    }

    private static void check() {
        if(! shutdownHookAdded) createShutdownHook();
        checkFlush();
    }

    private static void createShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(Console::flushAll));
        shutdownHookAdded = true;
    }

    private static void checkFlush() {
        if(System.currentTimeMillis() - timer < 1000L) return;
        timer = System.currentTimeMillis();
        flushAll();
    }

    public static void flushAll() {
        flushWrite();
        flushError();
    }

    public static void flushWrite() {
        try {
            getWriteBufferedWriter().flush();
        } catch(java.io.IOException exception) {
            exception.printStackTrace();
        }
    }

    public static void flushError() {
        try {
            getErrorBufferedWriter().flush();
        } catch(java.io.IOException exception) {
            exception.printStackTrace();
        }
    }

    private static String getTimeFormatted() {
        return "[" + getSimpleDateFormat().format(getDate()) + "]";
    }

    private static java.util.Date getDate() {
        return new java.util.Date();
    }

    private static java.text.SimpleDateFormat getSimpleDateFormat() {
        if(Console.simpleDateFormat == null) Console.simpleDateFormat = createSimpleDateFormat();
        return Console.simpleDateFormat;
    }

    private static java.text.SimpleDateFormat createSimpleDateFormat() {
        return new java.text.SimpleDateFormat(getPattern(), getLocale());
    }

    private static String getPattern() {
        if(Console.pattern == null) Console.pattern = "hh:mm:ss";
        return Console.pattern;
    }

    private static java.util.Locale getLocale() {
        if(Console.locale == null) Console.locale = java.util.Locale.ENGLISH;
        return Console.locale;
    }

    private static java.io.BufferedWriter getWriteBufferedWriter() {
        if(Console.writeBufferedWriter == null) Console.writeBufferedWriter = createWriteBufferedWriter();
        return Console.writeBufferedWriter;
    }

    private static java.io.BufferedWriter createWriteBufferedWriter() {
        return new java.io.BufferedWriter(new java.io.OutputStreamWriter(new java.io.FileOutputStream(java.io.FileDescriptor.out), getCharsets()), getOutputBufferSize());
    }

    private static java.io.BufferedWriter getErrorBufferedWriter() {
        if(Console.errorBufferedWriter == null) Console.errorBufferedWriter = createErrorBufferedWriter();
        return Console.errorBufferedWriter;
    }

    private static java.io.BufferedWriter createErrorBufferedWriter() {
        return new java.io.BufferedWriter(new java.io.OutputStreamWriter(new java.io.FileOutputStream(java.io.FileDescriptor.err), getCharsets()), getOutputBufferSize());
    }

    private static java.nio.charset.Charset getCharsets() {
        if(Console.charsets == null) Console.charsets = java.nio.charset.StandardCharsets.US_ASCII;
        return Console.charsets;
    }

    private static int getOutputBufferSize() {
        if(Console.outputBufferSize <= 0) Console.outputBufferSize = 512;
        return Console.outputBufferSize;
    }


}
