import React from 'react';
import { connect } from 'react-redux';
import Modal from 'react-modal';

import {createList as createListRequest} from '../../api';
import { fetchListsRequest, createList as createListAction } from '../../actions/lists';
import ListNamePopup from './ListNamePopup';
import ListsList from './ListsList';

class ListsHome extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            showModal: false,
            creatingList: false
        };

        this.hideModal = this.hideModal.bind(this);
        this.showModal = this.showModal.bind(this);
        this.receiveNewListName = this.receiveNewListName.bind(this);
    }

    hideModal() {
        this.setState({showModal: false});
    }
    
    showModal() {
        this.setState({showModal: true});
    }

    receiveNewListName(name, listNamePopup) {        
        createListRequest(name).then(response => {
            this.props.createListAction(response.data);
            this.hideModal();
        }).catch(error => {
            listNamePopup.showError(error);
        })
    }

    componentDidMount() {
        this.props.fetchListsRequest();
    }

    renderCreate() {
        return (
            <button
                className="ui basic button"
                onClick={this.showModal}>
                <i className="icon plus"></i>
                Nueva Lista de Lugares
            </button>
        );
    }

    renderModal() {
        return (
            <Modal
                isOpen={this.state.showModal}
                onRequestClose={this.hideModal}
                shouldCloseOnOverlayClick={true}
            >
                <ListNamePopup
                    title="Crear lista"
                    name="Nueva Lista"
                    nameHandler={this.receiveNewListName}
                    cancelHandler={this.hideModal}
                />
            </Modal>
        );
    }

    render() {
        return (
            <div>
                <h1>Tus listas de lugares</h1>
                <ListsList lists={this.props.lists} />
                {this.renderCreate()}
                {this.renderModal()}
            </div>
        );
    }
}

const mapStateToProps = state => ({
    lists: state.lists
})

export default connect(
    mapStateToProps,
    {
        fetchListsRequest,
        createListAction
    }
)(ListsHome);