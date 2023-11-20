package persistence;

import org.json.JSONObject;

// Classes that implement this interface contain a promise
// that it can be converted to and from a JSON file
// Borrows code/concept from the example provided in the course
public interface Persistable {
    // EFFECTS: Save the current object into a JSONObject
    JSONObject save();
}
