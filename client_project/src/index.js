import React from 'react';
import ReactDOM from 'react-dom';
import App from './components/App';
import { createStore, applyMiddleware, compose } from 'redux';
import { Provider } from 'react-redux';
import thunk from 'redux-thunk';

import reducers from './reducers'
import Modal from 'react-modal'

// esto es para que ande una tool de chrome para redux
const composeEnhancer = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;

// este es el store de redux
// acá se guarda el estado de la app
// (si está logueado, las listas de lugares, sugestiones de lugares)
const store = createStore(
    reducers,
    composeEnhancer(applyMiddleware(thunk))
);

ReactDOM.render(
    // este provider hace algo para que
    // a los componentes les pueda llegar cosas del store
    <Provider store={store}>
        <App />
    </Provider>,
    document.getElementById('root')
);

Modal.setAppElement('#root');