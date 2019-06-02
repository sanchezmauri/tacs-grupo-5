import httpStatusCodes from 'http-status-codes';

export const requestErrorMessage = error => {
    if (error.response) {
        // The request was made and the server responded with a status code
        // that falls out of the range of 2xx
        if (error.response.data)
            return error.response.data.message;

        return `Error (status = ${error.response.status}).`;
        // error.response.headers
    } else if (error.request) {
        // The request was made but no response was received
        // `error.request` is an instance of XMLHttpRequest in the browser and an instance of
        // http.ClientRequest in node.js
        return 'La request ni siquiera salió';
        //console.log(error.request);
    } else {
        // Something happened in setting up the request that triggered an Error
        return error.message;
    }

    // console.log(error.config);
}

export const errorStatusCode = error => {
    if (error.response === undefined) return undefined;
    
    return error.response.status;
}