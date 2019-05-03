import React from 'react'


const ListsList = props => {
        return (
            <div className="ui divided link items">
                {Object.values(props.lists).map(
                    list =>
                        <div className="item" key={list.id}>
                            <div className="content">
                                <div className="header">{list.name}</div>
                                <div className="description">
                                    <p>list.location.formattedAddress iria aca, otra cosa que habria que guardar en server</p>
                                </div>
                            </div>
                        </div>
                )}
            </div>
        );
}

export default ListsList;