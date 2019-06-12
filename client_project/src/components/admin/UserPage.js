import React from 'react';
import { connect } from 'react-redux';

import SearchBar from '../SearchBar';
import { SEARCH_USER } from '../../actions/types';


class UserPage extends React.Component {
    onSearchSubmit(userToSearch) {
        console.log(`vamos a buscar a ${userToSearch}`);
    }
    
    render() {
        return (
            <div className="ui container">
                <SearchBar
                    title={'Buscar Usuario'}
                    enabled={true}
                    loading={false/*this.props.loading[SEARCH_USER]*/}
                    onSubmit={this.onSearchSubmit.bind(this)}
                />

                <p>busca un usuario blah</p>
            </div>
        );
    }
}

const mapStateToProps = state => ({
    // user: state.admin.user,
    // loading: state.loading
});

export default connect(
    mapStateToProps, {
    // searchUser: searchUserAction
})(UserPage);