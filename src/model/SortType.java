package model;

/**
 * Types of sorting algorithms supported by the SortFactory and GUI.
 */
public enum SortType {
    BUBBLE("Bubble Sort"),
    SHELL("Shell Sort"),
    MERGE("Merge Sort");

    private final String myDisplayName;

    SortType(final String theName) {
        myDisplayName = theName;
    }

    @Override
    public String toString() {
        // This is what will show up in the JComboBox in the GUI
        return myDisplayName;
    }
}
