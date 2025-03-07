package ru.nsu.lebedev.pizzeria;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Utility class for JSON serialization and deserialization using Jackson.
 *
 * <p>
 * This class follows the <strong>Single Responsibility Principle</strong> by focusing solely on JSON conversion.
 * Moreover, by relying on the abstract marker interface {@code Serializable},
 * it observes the <strong>Dependency Inversion Principle</strong>.
 * </p>
 *
 * <p>
 * Note: The parameter type {@code Serializable} is a marker interface used to indicate that an object for JSON.
 * </p>
 */
public final class Json {
    /**
     * Private constructor to prevent instantiation, as this is a utility class.
     */
    private Json() {
    }

    /**
     * Serializes an object to its JSON string representation.
     *
     * @param object an instance of a class implementing {@code Serializable}.
     * @return the JSON string representation of the object.
     * @throws IOException if an error occurs during serialization.
     */
    public static String serialize(JsonSerializable object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

    /**
     * Serializes an object and writes its JSON representation to the specified output stream.
     *
     * @param object       an instance of a class implementing {@code Serializable}.
     * @param outputStream the output stream to which the JSON data is written.
     * @throws IOException if an error occurs during serialization.
     */
    public static void serialize(JsonSerializable object, OutputStream outputStream) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(outputStream, object);
    }

    /**
     * Deserializes a JSON string into an object of the specified type.
     *
     * @param inputString the JSON string to be deserialized
     * @param type        the target class type
     * @param <T>         the type of the returned object
     * @return an instance of type T deserialized from the JSON string
     * @throws ParsingException if an error occurs during parsing
     */
    public static <T extends JsonSerializable> T parse(String inputString, Class<T> type) throws ParsingException {
        try {
            return new ObjectMapper().readValue(inputString, type);
        } catch (IOException e) {
            throw new ParsingException(e);
        }
    }

    /**
     * Deserializes JSON data from an input stream into an object of the specified type.
     *
     * <p>
     * The method reads the input stream using UTF-8 encoding and converts it into the target object.
     * </p>
     *
     * @param inputStream the input stream containing JSON data
     * @param type        the target class type
     * @param <T>         the type of the returned object
     * @return an instance of type T deserialized from the JSON input stream
     * @throws ParsingException if an error occurs during parsing
     */
    public static <T extends JsonSerializable> T parse(InputStream inputStream, Class<T> type) throws ParsingException {
        ObjectMapper mapper = new ObjectMapper();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        try {
            var parser = mapper.createParser(reader);
            return parser.readValueAs(type);
        } catch (IOException e) {
            throw new ParsingException(e);
        }
    }
}
