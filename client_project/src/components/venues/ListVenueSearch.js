import React from 'react';
import { connect } from 'react-redux';

import { searchVenues, findCoords } from '../../actions/venues';
import { SEARCH_VENUES } from '../../actions/types'
import SearchBar from '../SearchBar';
import VenueSearchResult from './VenueSearchResult'


class ListVenueSearch extends React.Component {
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
    
    renderBody(searchEnabled) {
        if (searchEnabled) {
            return <VenueSearchResult
                        listId={parseInt(this.props.match.params.id, 10)}
                        venues={this.props.searchVenues}
                    />;
        } else {
            return <p>Enable geolocation when asked</p>
        }
    }
    
    render() {
        const searchEnabled = this.hasCoords();
        
        return (
            <div className="ui container">
                <SearchBar
                    title={'Buscar Lugares'}
                    enabled={searchEnabled}
                    loading={this.props.loading[SEARCH_VENUES]}
                    onSubmit={this.onSearchSubmit.bind(this)}
                />

                {this.renderBody(searchEnabled)}
            </div>
        );
    }
}

const mapStateToProps = state => ({
    coords: state.coords,
    searchVenues: state.searchVenues,
    loading: state.loading
});

export default connect(
    mapStateToProps, {
    searchVenuesAction: searchVenues,
    findCoords
})(ListVenueSearch);