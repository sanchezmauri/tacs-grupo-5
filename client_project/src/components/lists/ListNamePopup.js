import React from 'react';

import { stringifyRequestError } from '../../errors';

class ListNamePopup extends React.Component {
    constructor(props) {
        super(props);
        
        this.state = {
            newName: this.props.name,
            loading: false,
            error: null
        };

        this.onSubmit = this.onSubmit.bind(this);
    }

    showLoad() { this.setState({loading: true})};
    stopLoad() { this.setState({loading: false})};
    showError(error) {
        this.setState({loading: false, error});
    }

    onSubmit(submit) {
        submit.preventDefault();
        this.showLoad();

        this.props.nameHandler(this.state.newName, this);
    }

    renderError() {
        if (this.state.error) {
            const stringifiedError = stringifyRequestError(this.state.error);

            return (
                <div className="ui error message">
                    <div className="header">{stringifiedError.title}</div>
                    <p>{stringifiedError.message}</p>
                </div>
            )
        }
    }

    render() {
        let formStateClass = '';
        
        if (this.state.loading)
            formStateClass = 'loading';
        else if (this.state.error)
            formStateClass = 'error';

        return (
            <React.Fragment>
                <h3 className="ui medium header">{this.props.title}</h3>

                <form className={`ui ${formStateClass} form`} onSubmit={this.onSubmit}>
                    <div className="field">
                        <label>List Name</label>
                        <input
                            type="text"
                            name="listName"
                            value={this.state.newName}
                            onChange={change => this.setState({
                                newName: change.target.value,
                                error: null
                            })}
                        />        
                    </div>

                    {this.renderError()}
                    
                    <button className="ui button primary" type="submit">
                        Submit
                    </button>

                    <button className="ui button" type="button" onClick={this.props.cancelHandler}>
                        Cancel
                    </button>
                </form>
            </React.Fragment>
        );
    }
}


export default ListNamePopup;