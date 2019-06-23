package models.venues;

import com.fasterxml.jackson.databind.JsonNode;


public class FSVenueSearch {
    public String id;
    public String name;
    public FSLocation location;

    public FSVenueSearch(JsonNode jn) {
        this.id = jn.get("id").asText();
        this.location = new FSLocation(jn.get("location"));
        this.name = jn.get("name").asText();
    }

    public FSVenueSearch() {

    }
}
/*
* "id": "4ed9866346907c1b41a0e5f6",
"name": "Taco's House",
"location": {
    "address": "Pueyrredon 1462",
    "crossStreet": "esq. Santa Fe",
    ...
    "formattedAddress": [
        "Pueyrredon 1462 (esq. Santa Fe)",
        "Argentina"
    ]
},
"categories": [
    ...
],
*
* */