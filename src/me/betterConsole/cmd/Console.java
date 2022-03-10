package me.betterConsole.cmd;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class Console {

    private static BufferedWriter   writeBufferedWriter;
    private static BufferedWriter   errorBufferedWriter;
    private static SimpleDateFormat simpleDateFormat;
    private static Charset          charsets;
    private static int              outputBufferSize;
    private static boolean          shutdownHookAdded;
    private static long             timer;
    private static String           pattern;
    private static Locale           locale;

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
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeError(final Object errorMessage) {
        try {
            getErrorBufferedWriter().write(errorMessage.toString());
            check();
        } catch(IOException e) {
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
        } catch(IOException exception) {
            exception.printStackTrace();
        }
    }

    public static void flushError() {
        try {
            getErrorBufferedWriter().flush();
        } catch(IOException exception) {
            exception.printStackTrace();
        }
    }

    private static String getTimeFormatted() {
        return "[" + getSimpleDateFormat().format(getDate()) + "]";
    }

    private static Date getDate() {
        return new Date();
    }

    private static SimpleDateFormat getSimpleDateFormat() {
        if(Objects.isNull(Console.simpleDateFormat)) Console.simpleDateFormat = createSimpleDateFormat();
        return Console.simpleDateFormat;
    }

    private static SimpleDateFormat createSimpleDateFormat() {
        return new SimpleDateFormat(getPattern(), getLocale());
    }

    private static String getPattern() {
        if(Objects.isNull(Console.pattern)) Console.pattern = "hh:mm:ss";
        return Console.pattern;
    }

    private static Locale getLocale() {
        if(Objects.isNull(Console.locale)) Console.locale = Locale.ENGLISH;
        return Console.locale;
    }

    private static BufferedWriter getWriteBufferedWriter() {
        if(Objects.isNull(Console.writeBufferedWriter)) Console.writeBufferedWriter = createWriteBufferedWriter();
        return Console.writeBufferedWriter;
    }

    private static BufferedWriter createWriteBufferedWriter() {
        return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(java.io.FileDescriptor.out), getCharsets()), getOutputBufferSize());
    }

    private static BufferedWriter getErrorBufferedWriter() {
        if(Objects.isNull(Console.errorBufferedWriter)) Console.errorBufferedWriter = createErrorBufferedWriter();
        return Console.errorBufferedWriter;
    }

    private static BufferedWriter createErrorBufferedWriter() {
        return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(java.io.FileDescriptor.err), getCharsets()), getOutputBufferSize());
    }

    private static Charset getCharsets() {
        if(Objects.isNull(Console.charsets)) Console.charsets = StandardCharsets.US_ASCII;
        return Console.charsets;
    }

    private static int getOutputBufferSize() {
        if(Console.outputBufferSize <= 0) Console.outputBufferSize = 512;
        return Console.outputBufferSize;
    }


}
