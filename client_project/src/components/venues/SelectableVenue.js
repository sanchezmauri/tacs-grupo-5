import React from 'react';
import { connect } from 'react-redux';
import { isVenueSelected } from '../../reducers';
import { selectVenue, deselectVenue } from '../../actions/venues';

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

class SelectableVenue extends React.Component {
    constructor(props) {
        super(props);

        this.toggleSelected = this.toggleSelected.bind(this);
    }

    toggleSelected(event) {
        let actionCreator = this.props.isSelected ?
                        this.props.deselectVenue :
                        this.props.selectVenue;
        
        actionCreator(this.props.venue);
    }

    render() {
        let cardClass = `ui ${this.props.isSelected ? 'blue' : ''} card`;

        return (
            <div className={cardClass} onClick={this.toggleSelected}>
                <div className="content">
                    <h4 className="header">
                        {this.props.venue.name}
                    </h4>

                    <div className="description">
                        <p>
                            {this.props.venue.location.address}
                        </p>
                    </div>
                </div>

            </div>
        );
    }
}

const mapStateToProps = (state, ownProps) => ({
    isSelected: isVenueSelected(ownProps.venue.id, state)
})

export default connect(
    mapStateToProps,
    { selectVenue, deselectVenue }
)(SelectableVenue);