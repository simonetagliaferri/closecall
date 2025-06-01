package it.simonetagliaferri.model.dao.fs;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/* gson.toJson tournament creates the following error:
 * Unable to make field private final int java.time.LocalDate.year accessible: module java.base does not "opens java.time" to module com.google.gson
 * Caused by the dates in the tournament model, to fix that an adapter is needed
 */

public class GsonLocalDateAdapter implements JsonDeserializer<LocalDate>, JsonSerializer<LocalDate> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    @Override
    public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return LocalDate.parse(json.getAsString(), formatter);
    }

    @Override
    public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.format(formatter));
    }
}
