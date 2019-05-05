import React from 'react'


const ListsList = props => {
        return (
            <div className="ui divided items">
                {Object.values(props.lists).map(
                    list =>
                        <div className="item" key={list.id}>
                            <div className="content">
                            <div className="ui right floated icon buttons">
                                    <button
                                        className="ui mini yellow button"
                                        onClick={click => props.editList(list)}>
                                        <i className="edit icon"></i>
                                    </button>
                                    <button className={`ui mini negative ${!props.deleteEnabled ? "loading" : ""} button`}
                                            onClick={click => props.deleteList(list)}>
                                        <i className="trash alternate icon"></i>
                                    </button>
                                </div>  
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