import React from 'react';

const ConfirmationPopup = props => {
    return (
        <React.Fragment>
            <h3 className="ui medium header">{props.title}</h3>

            <p className="content">
                {props.message}
            </p>
                
            <button className={props.left.class} onClick={props.left.callback}>
                {props.left.text}
            </button>

            <button className={props.right.class} onClick={props.right.callback}>
                {props.right.text}
            </button>
        </React.Fragment>
    );

}


export default ConfirmationPopup;