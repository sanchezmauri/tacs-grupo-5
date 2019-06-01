import React from 'react';
import _ from 'lodash';
import {Link} from 'react-router-dom';

class UserForm extends React.Component {
    constructor(props) {
        super(props);

        this.state = _.zipObject(
            props.fields,
            props.fields.map(field => '')
        );

        console.log(`state from fields: ${this.state}`);

        this.clickSubmit = this.clickSubmit.bind(this);
    }

    clickSubmit(click) {
        this.props.submit(this.state);
    }

    renderHeader() {
        return (
            <h2 className="ui teal image header">
                <div className="content">
                    {this.props.title}
                </div>
            </h2>
        );
    }

    renderField(name, type, icon) {
        return (
            <div key={name} className="field">
                <div className="ui left icon input">
                    <i className={`${icon} icon`}></i>
                    <input
                        type={type}
                        value={this.state[name]}
                        onChange={change => this.setState({[name]: change.target.value})}
                    />
                </div>
            </div>
        );
    }

    renderFields() {
        return this.props.fields.map(field => {
            const props = UserForm.fieldProps[field];
            return this.renderField(field, props.type, props.icon);
        })
    }

    renderForm() {
        return (
            <form className="ui large form" onSubmit={e => e.preventDefault()}>
                <div className="ui stacked segment">
                    
                    {this.renderFields()}
                    
                    <div className="ui fluid large teal submit button" onClick={this.clickSubmit}>
                        {this.props.buttonText}
                    </div>
                </div>

                <div className="ui error message">

                </div>
            </form>
        );
    }

    renderAlternativeAction() {
        return (
            <div className="ui message">
                {this.props.alternativeLink.pre} <Link to={this.props.alternativeLink.link}
                    className="item">
                    {this.props.alternativeLink.message}
                </Link>
            </div>
        );
    }

    render() {
        return (
            <div className="ui middle aligned center aligned grid">
                <div className="column">
                    {this.renderHeader()}
                    {this.renderForm()}
                    {this.renderAlternativeAction()}
                </div>
            </div>
        );
    }
}

UserForm.fieldProps = {
    name: {
        type: 'text',
        icon: 'user'
    },
    email: {
        type: 'text',
        icon: 'at'
    },
    password: {
        type: 'password',
        icon: 'lock'
    }
}

export default UserForm;

/*
<div className="field">
            <div className="ui left icon input">
                <i className="user icon"></i>
                <input
                    type="text"
                    value={this.state.email}
                    onChange={change => this.setState({email: change.target.value})}
                />
            </div>
        </div>
        
        <div className="field">
            <div className="ui left icon input">
                <i className="lock icon"></i>
                <input
                    type="password"
                    value={this.state.password}
                    onChange={change => this.setState({password: change.target.value})}
                />
            </div>
        </div>
*/