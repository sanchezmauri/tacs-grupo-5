import React from 'react';
import { connect } from 'react-redux';
import Modal from 'react-modal';

import {createList as createListRequest, deleteList as deleteListRequest} from '../../api';
import { fetchListsRequest, createList as createListAction, deleteList as deleteListAction } from '../../actions/lists';
import history from '../../routes/history';
import {linkToList} from '../../routes/paths';
import ListNamePopup from './ListNamePopup';
import ListsList from './ListsList';
import ConfirmationPopup from '../ConfirmationPopup';

class ListsHome extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            popupToShow: null,
            creatingList: false
        };

        this.hidePopup = this.hidePopup.bind(this);
        this.showPopup = this.showPopup.bind(this);
        this.receiveNewListName = this.receiveNewListName.bind(this);
        this.showDeleteListPopup = this.showDeleteListPopup.bind(this);
        this.deleteList = this.deleteList.bind(this);
    }
    
    componentDidMount() {
        this.props.fetchListsRequest();
    }

    hidePopup() {
        this.setState({popupToShow: null});
    }
    
    showPopup(popup) {
        this.setState({popupToShow: popup});
    }

    // event handlers of children
    // handles modal create popup
    receiveNewListName(name, listNamePopup) {        
        createListRequest(name).then(response => {
            this.props.createListAction(response.data);
            this.hidePopup();
        }).catch(error => {
            listNamePopup.showError(error);
        })
    }

    // handles listslist edit callback
    editList(list) {
        history.push(linkToList(list.id));
    }

    deleteList(listId) {
        deleteListRequest(listId).then(response => {
            this.props.deleteListAction(listId);
        }).catch(error => {
            this.state.setState({error});
        }).then(
            this.hidePopup
        );
    }

    showDeleteListPopup(list) {
        const left = {
            text: 'Borrar',
            class: 'ui negative button',
            callback: () => this.deleteList(list.id)
        };

        const right = {
            text: 'Cancelar',
            class: 'ui button',
            callback: () => {
                this.hidePopup();
            }
        };

        const deleteConfirmation = <ConfirmationPopup
            title={`Eliminar Lista`}
            message={`Estás seguro de eliminar la lista ${list.name}?`}
            left={left}
            right={right}
        />;

        this.showPopup(deleteConfirmation);
    }

    renderCreate() {
        const createListPopup = <ListNamePopup
            title="Crear lista"
            name="Nueva Lista"
            nameHandler={this.receiveNewListName}
            cancelHandler={this.hidePopup}
        />;

        return (
            <button
                className="ui basic button"
                onClick={click => this.showPopup(createListPopup)}>
                <i className="icon plus"></i>
                Nueva Lista de Lugares
            </button>
        );
    }

    renderModal() {
        return (
            <Modal
                isOpen={this.state.popupToShow !== null}
                onRequestClose={this.hidePopup}
                shouldCloseOnOverlayClick={true}
            >
                {this.state.popupToShow}
            </Modal>
        );
    }

    render() {
        return (
            <div>
                <h1>Tus listas de lugares</h1>
                <ListsList
                    lists={this.props.lists}
                    editList={this.editList}
                    deleteList={this.showDeleteListPopup}
                />
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
        createListAction,
        deleteListAction
    }
)(ListsHome);