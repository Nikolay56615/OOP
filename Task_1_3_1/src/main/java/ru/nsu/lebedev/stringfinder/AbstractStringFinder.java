package ru.nsu.lebedev.stringfinder;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

/**
 * Abstract class for string search.
 * Provides basic methods for opening a file, reading segments,
 * and searching for a substring.
 */
public abstract class AbstractStringFinder {
    static final int CAPACITY = 1048576; // 1 MB
    protected StringBuilder buffer = new StringBuilder();
    protected BufferedReader reader = null;
    protected String searchTarget = "";
    protected LinkedList<Long> targetsPositions = new LinkedList<>();

    /**
     * Default constructor.
     */
    public AbstractStringFinder() {
    }

    /**
     * Opens a file for reading from the specified path and saves a BufferedReader
     * with UTF-8 encoding to read the file by using inputStreamReader.
     *
     * @param filename path to the file for searching
     * @return true if the file was opened successfully
     */
    private boolean openFile(String filename) {
        try {
            FileInputStream fileInputStream = new FileInputStream(filename);
            InputStreamReader inputStreamReader = new InputStreamReader(
                    fileInputStream, StandardCharsets.UTF_8
            );
            reader = new BufferedReader(inputStreamReader, CAPACITY);
            return true;
        } catch (IOException e) {
            System.err.println("Failes to open file" + e);
            return false;
        }
    }

    /**
     * Closes the BufferedReader if it was opened.
     */
    private void closeFile() {
        try {
            if (reader != null) {
                reader.close();
                reader = null;
            }
        } catch (IOException e) {
            throw new RuntimeException("Error closing file", e);
        }
    }

    /**
     * Reads a segment of text from the file.
     * Reads characters from the file up to the CAPACITY value.
     *
     * @return the number of characters read or -1 if the end of
     * the file is reached
     */
    protected int readSegment() {
        char[] charBuffer = new char[CAPACITY];
        int readCharsCount;
        try {
            readCharsCount = reader.read(charBuffer);
            if (readCharsCount == -1) {
                return -1;
            }
        } catch (IOException e) {
            System.err.println("Error reading segment" + e);
            return -1;
        }
        buffer.setLength(0);
        buffer.append(charBuffer, 0, readCharsCount);
        return readCharsCount;
    }

    /**
     * Starts searching for a substring in the file.
     * Finds all starting indices of `target` occurrences in the file.
     * If the file was not opened, the result will be empty.
     *
     * @param filename path to the file for searching
     * @param target substring to search for
     */
    public void find(String filename, String target) {
        setSearchTarget(target);
        targetsPositions.clear();
        boolean isOpened = openFile(filename);
        if (!isOpened) {
            return;
        }
        findingSubstring();
        closeFile();
    }

    /**
     * Main search method to be implemented in a subclass.
     * This method defines the logic for searching for the `searchTarget` substring.
     */
    protected abstract void findingSubstring();

    /**
     * Returns a list of starting indices of each occurrence of the substring
     * found after the last call to `find()`.
     *
     * @return LinkedList with starting indices of each occurrence of `searchTarget`
     */
    public LinkedList<Long> getTargetsPositions() {
        return targetsPositions;
    }

    /**
     * Sets the substring to search for.
     *
     * @param target substring to search for
     */
    private void setSearchTarget(String target) {
        this.searchTarget = target;
    }
}
