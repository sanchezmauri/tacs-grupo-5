import React from 'react'
import { Link } from 'react-router-dom'

import * as urls from '../routes/urls';

const Header = () => {
    return (
        <nav>
            <ul>
            <li>
                <Link to={urls.HOME}>Home</Link>
            </li>
            </ul>
        </nav>
    );
}

export default Header;