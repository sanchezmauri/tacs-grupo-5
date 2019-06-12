import React from 'react';
import { connect } from 'react-redux';
import Modal from 'react-modal';

import {
    fetchLists as fetchListsAction,
    createList as createListAction,
    deleteList as deleteListAction
} from '../../actions/lists';
import history from '../../routes/history';
import {requestErrorMessage} from '../../errors';

import {linkWithId, LIST} from '../../routes/paths';
import ListNamePopup from './ListNamePopup';
import ListsList from './ListsList';
import ConfirmationPopup from '../ConfirmationPopup';
import { CREATE_LIST, DELETE_LIST } from '../../actions/types';


class ListsHome extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            popupToShow: null,
        };

        this.hidePopup = this.hidePopup.bind(this);
        this.showPopup = this.showPopup.bind(this);
        this.receiveNewListName = this.receiveNewListName.bind(this);
        this.createListPopup = this.createListPopup.bind(this);
        this.deleteConfirmationPopup = this.deleteConfirmationPopup.bind(this);
        this.showDeleteListPopup = this.showDeleteListPopup.bind(this);
        this.deleteList = this.deleteList.bind(this);
    }
    
    componentDidMount() {
        this.props.fetchListsAction();
    }

    hidePopup() {
        this.setState({popupToShow: null});
    }
    
    showPopup(popupRenderFn) {
        this.setState({popupToShow: popupRenderFn});
    }

    // event handlers of children
    // handles modal create popup
    receiveNewListName(name) {
        this.props.createListAction(name);
        this.hidePopup();
    }

    // handles listslist edit callback
    editList(list) {
        history.push(linkWithId(LIST, list.id));
    }

    deleteList(listId) {
        this.props.deleteListAction(listId);
        this.hidePopup();
    }

    showDeleteListPopup(list) {
        this.setState({
            popupToShow: this.deleteConfirmationPopup,
            listToDelete: list
        });
    }

    renderCreate() {
        let buttonClass =   "ui primary " +
                            (this.props.loading[CREATE_LIST] ? "loading " : "") +
                            "button";

        let errorMessage = null;
        const createListError = this.props.errors[CREATE_LIST];
        
        if (createListError) {
            errorMessage = (
                <div className="ui error message">
                    <div className="header">Error al crear lista</div>
                    <p>{requestErrorMessage(createListError)}</p>
                </div>
            );
        }
    
        return (
            <div>
                <button
                    className={buttonClass}
                    onClick={click => this.showPopup(this.createListPopup)}>
                    <i className="icon plus"></i>
                    Nueva Lista de Lugares
                </button>

                {errorMessage}
            </div>
        );
    }

    createListPopup() {
        return (
            <ListNamePopup
                title="Crear lista"
                name="Nueva Lista"
                nameHandler={this.receiveNewListName}
                cancelHandler={this.hidePopup}
            />
        );
    }

    deleteConfirmationPopup() {
        const listToDelete = this.state.listToDelete;

        const left = {
            text: 'Borrar',
            class: 'ui negative button',
            callback: () => this.deleteList(listToDelete.id)
        };

        const right = {
            text: 'Cancelar',
            class: 'ui button',
            callback: () => 
                this.setState({
                    showPopup: null, // hide popup
                    listToDelete: null // clear list state
                })
        };

        return (
            <ConfirmationPopup
                title={`Eliminar Lista`}
                message={`Estás seguro de eliminar la lista ${listToDelete.name}?`}
                left={left}
                right={right}
            />
        );
    }

    renderModal() {
        if (this.state.popupToShow) {
            return (
                <Modal
                    isOpen={true}
                    onRequestClose={this.hidePopup}
                    shouldCloseOnOverlayClick={true}
                >
                    {this.state.popupToShow()}
                </Modal>
            );
        }
    }

    render() {
        return (
            <div>
                <h1>Tus listas de lugares</h1>
                <ListsList
                    lists={this.props.lists}
                    editList={this.editList}
                    deleteList={this.showDeleteListPopup}
                    deleteEnabled={this.props.loading[DELETE_LIST] === undefined}
                />
                {this.renderCreate()}
                {this.renderModal()}
            </div>
        );
    }
}


const mapStateToProps = (state, ownProps) => {
    return {
        lists: state.lists,
        loading: state.loading,
        errors: state.errors
    }
}

export default connect(
    mapStateToProps,
    {
        fetchListsAction,
        createListAction,
        deleteListAction
    }
)(ListsHome);