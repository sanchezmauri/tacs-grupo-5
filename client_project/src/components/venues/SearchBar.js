import React from 'react';

class SearchBar extends React.Component {
    constructor(props) {
        super(props);
        
        this.state = { query: '' };

        this.onFormSubmit = this.onFormSubmit.bind(this);
    }

    onFormSubmit(submit) {
        submit.preventDefault();

        this.props.onSubmit(this.state.query);
    }

    render() {

        return (
            <div className="ui segment">
                <form className="ui form" onSubmit={this.onFormSubmit}>
                    <div className="field">
                        <label>Buscar lugares</label>
                        <div className="ui icon input">
                            <input
                                type="text"
                                value={this.state.query}
                                onChange={ e => this.setState({query: e.target.value}) }
                                disabled={!this.props.enabled}
                            />
                            <i className="search icon"></i>
                        </div>
                    </div>
                </form>
            </div>
        );
    }
}

export default SearchBar;