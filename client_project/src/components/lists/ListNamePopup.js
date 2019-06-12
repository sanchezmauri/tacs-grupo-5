import React from 'react';

class ListNamePopup extends React.Component {
    constructor(props) {
        super(props);
        
        this.state = {
            newName: this.props.name,
        };

        this.onSubmit = this.onSubmit.bind(this);
    }

    onSubmit(submit) {
        submit.preventDefault();
        this.props.nameHandler(this.state.newName);
    }

    render() {
        return (
            <React.Fragment>
                <h3 className="ui medium header">{this.props.title}</h3>

                <form className={`ui form`} onSubmit={this.onSubmit}>
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