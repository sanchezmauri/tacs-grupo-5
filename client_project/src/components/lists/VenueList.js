import React from 'react';
import { connect } from 'react-redux';
import { isEmpty } from 'lodash';

import { visitVenue } from '../../actions/lists';

class UserVenueItem extends React.Component {
    render() {
        const { listId, venue, visitVenue } = this.props;

        const visitedElement =  venue.visited ?
                                <div className="ui label">Visited</div> :
                                (<button
                                    className="ui right floated primary button"
                                    onClick={click => visitVenue(listId, venue.id)}>
                                    Visitar
                                    <i className="right chevron icon"></i>
                                </button>);

        return (<div key={venue.id} className="item">
            <div className="content">
                <div className="header">
                    {venue.name}
                </div>

                <div className="description">
                    <p>{venue.address}</p>
                </div>

                <div className="extra">
                    {visitedElement}
                </div>
            </div>
        </div>);
    }
}

UserVenueItem = connect(null, { visitVenue })(UserVenueItem);

const VenuesList = props => {
    const { list } = props;

    if (isEmpty(list.venues))
        return <div>No tenés venues</div>;

    const venuesElements = list.venues.map(venue =>
        <UserVenueItem key={venue.id} venue={venue} listId={list.id} />
    );

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