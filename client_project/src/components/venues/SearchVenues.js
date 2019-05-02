import React from 'react';
import { connect } from 'react-redux';

import { searchVenues, findCoords } from '../../actions';
import SearchBar from './SearchBar';
import VenueSearchResult from './VenueSearchResult'


class SearchVenues extends React.Component {
    componentDidMount() {
        this.props.findCoords();
    }

    onSearchSubmit(searchTerm) {
        this.props.searchVenuesAction(
            this.props.coords.latitude,
            this.props.coords.longitude,
            searchTerm
        );
    }

    hasCoords() {
        return  this.props.coords &&
                this.props.coords.latitude &&
                this.props.coords.longitude;
    }
    
    render() {
        const searchBarEnabled = this.hasCoords();
        let bodyJSX;

        if (searchBarEnabled) {
            bodyJSX = <VenueSearchResult venues={this.props.searchVenues} />;
        } else {
            bodyJSX = <p>Enable geolocation when asked</p>
        }

        return (
            <div className="ui container">
                <SearchBar
                    enabled={searchBarEnabled}
                    onSubmit={this.onSearchSubmit.bind(this)}
                />

                {bodyJSX}
            </div>
        );
    }
}

const mapStateToProps = state => ({
    coords: state.coords,
    searchVenues: state.searchVenues
});

export default connect(
    mapStateToProps, {
    searchVenuesAction: searchVenues,
    findCoords
})(SearchVenues);