package models.venues;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FSLocation {
    public String address;
    public String crossStreet;
    public List<String> formattedAddress;

    public FSLocation(JsonNode fromJson) {

        var addressNode = fromJson.get("address");
        this.address = addressNode != null ? addressNode.asText() : "";

        var csNode = fromJson.get("crossStreet");
        this.crossStreet = csNode != null ? csNode.asText() : "";

        this.formattedAddress = new ArrayList<>();
        Iterator<JsonNode> addressesIterator = fromJson.get("formattedAddress").elements();
        addressesIterator.forEachRemaining( it -> formattedAddress.add(it.asText()));
    }

    //Required for Deserialization
    public FSLocation() {

    }
}
