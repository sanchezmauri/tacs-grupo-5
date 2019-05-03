import httpStatusCodes from 'http-status-codes';

export const stringifyRequestError = error => {
    let title = 'Error';
    let message = '';

    if (error.response) {
        // The request was made and the server responded with a status code
        // that falls out of the range of 2xx

        title = `Error (status = ${error.response.status})`;
        
        if (error.response.data) {
            message = error.response.data.message;
        }
        // error.response.headers
    } else if (error.request) {
        // The request was made but no response was received
        // `error.request` is an instance of XMLHttpRequest in the browser and an instance of
        // http.ClientRequest in node.js
        message = 'La request ni siquiera salió';
        //console.log(error.request);
    } else {
        // Something happened in setting up the request that triggered an Error
        message = error.message;
    }

    // console.log(error.config);

    return {title, message}
}