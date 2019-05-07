import React from 'react'
import { connect } from 'react-redux'
import _ from 'lodash';

import { clearVenueSelection } from '../../actions/venues';
import { addVenuesToList } from '../../actions/lists';

import SelectableVenue from './SelectableVenue';
import { ADD_VENUES_TO_LIST } from '../../actions/types';


class VenueSearchResult extends React.Component {
    constructor(props) {
        super(props);

        this.VENUES_PER_PAGE = 16;
        this.state = { page: 0 };

        this.hasPrevPage = this.hasPrevPage.bind(this);
        this.prevPage = this.prevPage.bind(this);
        this.hasNextPage = this.hasNextPage.bind(this);
        this.nextPage = this.nextPage.bind(this);
        this.addVenuesToList = this.addVenuesToList.bind(this);
    }

    componentDidMount() {
        this.props.clearVenueSelection();
    }
    
    componentWillUnmount() {
        this.props.clearVenueSelection();
    }

    paginatedVenues() {
        const length = this.props.venues.length;
        const begin = this.state.page * this.VENUES_PER_PAGE;
        const end = Math.min(begin + this.VENUES_PER_PAGE, length);

        return this.props.venues.slice(begin, end);
    }

    hasPrevPage() {
        return this.state.page > 0;
    }

    prevPage() {
        this.setState({ page: this.state.page - 1 });
    }

    hasNextPage() {
        const maxPage = Math.floor(this.props.venues.length / this.VENUES_PER_PAGE);
        return maxPage > 0 && this.state.page < maxPage;
    }

    nextPage() {
        this.setState({ page: this.state.page + 1 });
    }

    renderPaginationButton(condition, callback, buttonClass, icon, label) {
        if (condition())
            return (
                <button className={buttonClass} onClick={callback}>
                    <i className={`${icon} arrow icon`}></i>
                    {label}
                </button>
            );
        
        return null
    }

    renderPrevButton() {
        return this.renderPaginationButton(
            this.hasPrevPage,
            this.prevPage,
            'ui labeled icon button',
            'left',
            'Anterior'
        )
    }

    renderNextButton() {
        return this.renderPaginationButton(
            this.hasNextPage,
            this.nextPage,
            'ui right labeled icon button',
            'right',
            'Siguiente'
        )
    }

    addVenuesToList() {
        const venueIds = _.keys(this.props.venueSelection);

        const selectedVenues = _.map(
            venueIds,
            venueId => _.find(this.props.venues, venue => venue.id === venueId)
        );

        this.props.addVenuesToList(this.props.listId, selectedVenues, null);
    }

    renderAddVenuesButton() {
        if (_.some(_.values(this.props.venueSelection))) {
            return (
                <button className="ui button" onClick={this.addVenuesToList}>
                    Agregar lugares
                </button>
            );
        }
    }

    render() {
        return (
            <React.Fragment>
                <div className="ui link four cards">
                    {this.paginatedVenues().map(venue =>
                        <SelectableVenue key={venue.id} venue={venue} />
                    )}
                </div>


                <div className="ui center aligned buttons">
                    {this.renderPrevButton()}
                    {this.renderAddVenuesButton()}
                    {this.renderNextButton()}
                </div>
            </React.Fragment>
        );
    }
}

const mapStateToProps = (state, ownProps) => {
    return {
        venueSelection: state.venueSelection,
        loading: state.loading[ADD_VENUES_TO_LIST],
        errors: state.errors[ADD_VENUES_TO_LIST]
    };
}

export default connect(
    mapStateToProps,
    { addVenuesToList, clearVenueSelection }
)(VenueSearchResult);