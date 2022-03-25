package utility;

/**
 * This interface should be implemented by all classes that read from a file.
 */
public interface Reader {
    /**
     * This method can be used to read from a given direction and return the found object.
     * @param path the location to read from.
     * @return the found object.
     */
    Object read(String path);
}
