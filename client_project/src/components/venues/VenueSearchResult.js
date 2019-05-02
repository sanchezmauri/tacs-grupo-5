import React from 'react'

/*
Venue JSON

"id": "4ed9866346907c1b41a0e5f6",
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
...
*/

const VenueSearchResult = (props) => {
    const venuesJSX = props.venues.map(venue => (
            <div key={venue.id}>
                <h3>{venue.name}</h3>
                <p>{venue.location.address}</p>
            </div>
        )
    );

    return (
        <div>
            {venuesJSX}
        </div>
    );
}

export default VenueSearchResult;