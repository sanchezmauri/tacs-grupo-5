import React from 'react';
import { isEmpty } from 'lodash';

const VenuesList = props => {
    if (isEmpty(props.venues))
        return <div>No tenés venues</div>;

    const venuesElements = props.venues.map(venue => {
        const visitedElement =  venue.visited ?
                                <div class="ui label">Visited</div> :
                                (<div class="ui right floated primary button">
                                    Visitar
                                    <i class="right chevron icon"></i>
                                </div>);

        return (<div key={venue.id} className="item">

            <div className="content">
                <div className="header">
                    {venue.name}
                </div>

                <div className="description">
                    <p>{venue.address}</p>
                </div>

                <div class="extra">
                    {visitedElement}
                </div>
            </div>
        </div>);
    });

    return (
        <React.Fragment>
            <h2 className="ui header">Lugares de la lista</h2>
            <div className="ui divided items">
                {venuesElements}
            </div>
        </React.Fragment>
    );
}

export default VenuesList;