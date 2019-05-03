import React from 'react'
import { connect } from 'react-redux';


class VenueList extends React.Component {
    render() {
        if (!this.props.list) {
            console.log("got no list");
            return <div>Loading...</div>;
        }

        const { list } = this.props;
        return (
            <div>
                {list.name}
            </div>
        );
    }
}

const mapStateToProps = (state, ownProps) => {
    const listId = ownProps.match.params.id;
    
    return {
        list: state.lists[listId]
    }
}

export default connect(mapStateToProps)(VenueList);