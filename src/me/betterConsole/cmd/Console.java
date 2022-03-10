package me.betterConsole.cmd;

/**
 * @author Anders B. Hansen
 * @version 1.0
 * @description This Console is about 30-40 times faster than normal System.out.
 * @see Console#print(Object)
 * @see Console#error(Object)
 */
public class Console {

    //<editor-fold desc="Variables">
    private static java.io.BufferedWriter     writeBufferedWriter; // BufferedWriter for printing basic information to console.
    private static java.io.BufferedWriter     errorBufferedWriter; // BufferedWriter for printing errors to console.
    private static java.text.SimpleDateFormat simpleDateFormat;    // SimpleDateFormat for formatting time to console.
    private static java.nio.charset.Charset   charsets;            // Charset for BufferedWriter 'Write' & 'Error'.
    private static int                        outputBufferSize;    // Output buffer size for BufferedWriter 'Write' & 'Error'.
    private static boolean                    shutdownHookAdded;   // Check if shutdown hook is added to runtime.
    private static long                       timer;               // Timer for interval BufferedWriter 'Write' & 'Error' flush.
    private static String                     pattern;             // Pattern for SimpleDateFormat.
    private static java.util.Locale           locale;              // Locale for SimpleDateFormat.
    //</editor-fold>

    //<editor-fold desc="Printing to console">
    // Printing message to console, with time formatted at start, and new line at end.
    public static void print(final Object message) {
        write(getTimeFormatted() + ": " + message + "\n");
    }

    // Printing error message to console, with time formatted at start, and new line at end.
    public static void error(final Object errorMessage) {
        writeError(getTimeFormatted() + ": " + errorMessage + "\n");
    }

    // Writing message to console.
    public static void write(final Object message) {
        try {
            getWriteBufferedWriter().write(message.toString());
            check();
        } catch(java.io.IOException e) {
            e.printStackTrace();
        }
    }

    // Writing error message to console.
    public static void writeError(final Object errorMessage) {
        try {
            getErrorBufferedWriter().write(errorMessage.toString());
            check();
        } catch(java.io.IOException e) {
            e.printStackTrace();
        }
    }
    //</editor-fold>

    //<editor-fold desc="Utility methods">
    // Check ShutdownHook & Flush.
    private static void check() {
        if(! shutdownHookAdded) createShutdownHook();
        checkFlush();
    }

    // Create shutdown-hook for flushing both BufferedWriter at program shutdown.
    private static void createShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(Console::flushAll));
        shutdownHookAdded = true;
    }
    //</editor-fold>

    //<editor-fold desc="Buffered Writer Flush">
    // Check if one second has passed, if so flush both BufferedWriter.
    private static void checkFlush() {
        if(System.currentTimeMillis() - timer < 1000L) return;
        timer = System.currentTimeMillis();
        flushAll();
    }

    // Flush both BufferedWriter.
    public static void flushAll() {
        flushWrite();
        flushError();
    }

    // Flush 'Write' BufferedWriter.
    public static void flushWrite() {
        try {
            getWriteBufferedWriter().flush();
        } catch(java.io.IOException exception) {
            exception.printStackTrace();
        }
    }

    // Flush 'Error' BufferedWriter.
    public static void flushError() {
        try {
            getErrorBufferedWriter().flush();
        } catch(java.io.IOException exception) {
            exception.printStackTrace();
        }
    }
    //</editor-fold>

    //<editor-fold desc="Buffered Writer">
    //<editor-fold desc="'Write' Buffered Writer">
    // Get 'Write' BufferedWriter, if BufferedWriter is null create new.
    private static java.io.BufferedWriter getWriteBufferedWriter() {
        if(Console.writeBufferedWriter == null) Console.writeBufferedWriter = createWriteBufferedWriter();
        return Console.writeBufferedWriter;
    }

    // Create new 'Write' BufferedWriter.
    private static java.io.BufferedWriter createWriteBufferedWriter() {
        return new java.io.BufferedWriter(new java.io.OutputStreamWriter(new java.io.FileOutputStream(java.io.FileDescriptor.out), getCharsets()), getOutputBufferSize());
    }
    //</editor-fold>

    //<editor-fold desc="'Error' Buffered Writer">
    // Get 'Error' BufferedWriter, if BufferedWriter is null create new.
    private static java.io.BufferedWriter getErrorBufferedWriter() {
        if(Console.errorBufferedWriter == null) Console.errorBufferedWriter = createErrorBufferedWriter();
        return Console.errorBufferedWriter;
    }

    // Create new 'Error' BufferedWriter.
    private static java.io.BufferedWriter createErrorBufferedWriter() {
        return new java.io.BufferedWriter(new java.io.OutputStreamWriter(new java.io.FileOutputStream(java.io.FileDescriptor.err), getCharsets()), getOutputBufferSize());
    }
    //</editor-fold>
    //</editor-fold>

    //<editor-fold desc="Get Handlers">
    // Get Charsets for both BufferedWriter, if Charsets is null, create default US_ASCII.
    private static java.nio.charset.Charset getCharsets() {
        if(Console.charsets == null) setCharsets(java.nio.charset.StandardCharsets.US_ASCII);
        return Console.charsets;
    }

    // Get OutputBufferSize if equal or under zero, set default to 512.
    private static int getOutputBufferSize() {
        if(Console.outputBufferSize <= 0) setOutputBufferSize(512);
        return Console.outputBufferSize;
    }

    // Get Pattern for SimpleDateFormat, if null create new with pattern hour:minute:second.
    private static String getPattern() {
        if(Console.pattern == null) setPattern("hh:mm:ss");
        return Console.pattern;
    }

    // Get Locale for SimpleDateFormat, if null create default English.
    private static java.util.Locale getLocale() {
        if(Console.locale == null) setLocale(java.util.Locale.ENGLISH);
        return Console.locale;
    }

    // Get time formatted for printing to console.
    private static String getTimeFormatted() {
        return "[" + getSimpleDateFormat().format(getDate()) + "]";
    }

    // Get new Date
    private static java.util.Date getDate() {
        return new java.util.Date();
    }

    // Get SimpleDateFormat, if SimpleDateFormat is null, create new with Console Pattern and Console Locale.
    private static java.text.SimpleDateFormat getSimpleDateFormat() {
        if(Console.simpleDateFormat == null) Console.simpleDateFormat = new java.text.SimpleDateFormat(getPattern(), getLocale());
        return Console.simpleDateFormat;
    }
    //</editor-fold>

    //<editor-fold desc="Set Handlers">
    // Set Charsets for both BufferedWriter.
    public static void setCharsets(final java.nio.charset.Charset standardCharsets) {
        Console.charsets = standardCharsets;
    }

    // Set Output buffer size for both BufferedWriter.
    public static void setOutputBufferSize(final int outputBufferSize) {
        Console.outputBufferSize = outputBufferSize;
    }

    // Set Pattern for SimpleDateFormat.
    public static void setPattern(final String pattern) {
        Console.pattern = pattern;
    }

    // Set Locale for SimpleDateFormat.
    public static void setLocale(final java.util.Locale locale) {
        Console.locale = locale;
    }
    //</editor-fold>
}