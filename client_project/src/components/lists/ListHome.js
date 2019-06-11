import React from 'react'
import { connect } from 'react-redux';

import { fetchLists } from '../../actions/lists';
import history from '../../routes/history';
import { LIST_VENUES_SEARCH, linkWithId } from '../../routes/paths';
import { requestErrorMessage } from '../../errors';

import VenueList from './VenueList';
import { FETCH_LISTS } from '../../actions/types';



class ListHome extends React.Component {
    componentDidMount() {
        //if (!this.props.list) {
            this.props.fetchLists();
        //}
    }

    render() {
        const { list, loading, error } = this.props;
        
        if (!list)
            return <div>Loading list...</div>;
        
        if (error)
            return <div>Error cargando lista: {requestErrorMessage(error)}</div>

        return (
            <div>
                <h1 className="ui header">{list.name}</h1>

                <VenueList list={list}/>
                
                <div>
                    <button
                        className="ui primary button"
                        onClick={click => history.push(linkWithId(LIST_VENUES_SEARCH, list.id))}>
                        <i className="icon plus"></i>
                        Agregar Lugares
                    </button>
                </div>
            </div>
        );
    }
}

const mapStateToProps = (state, ownProps) => {
    const listId = ownProps.match.params.id;

    return {
        list: state.lists[listId],
        loading: state.loading[FETCH_LISTS],
        error: state.errors[FETCH_LISTS]
    }
}

export default connect(
    mapStateToProps,
    {
        fetchLists
    }
)(ListHome);