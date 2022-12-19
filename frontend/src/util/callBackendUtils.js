import {fulfilledFor, pendingFor, rejectedFor} from "./actionPromiseUtil";

export const get = (dispatch, params) => {
    Object.assign(params, {method: Method.GET});
    return call(dispatch, params);
}

export const put = (dispatch, params) => {
    Object.assign(params, {method: Method.PUT});
    return call(dispatch, params);
}

const CONTENT_TYPE_JSON = 'application/json'

const Method = Object.freeze({
    KEY: 'method',
    GET: 'GET',
    PUT: 'PUT',
    POST: 'POST',
    DELETE: 'DELETE',
});

const checkStatus = (response) => {
    if (response && response.status >= 200 && response.status < 300) {
        return response;
    }
    const error = new Error(response.statusText);
    error.response = response;
    throw error;
}


export const call = (dispatch, params) => {
    const {url, method, action, payload} = params;
    const requestBody = payload ? JSON.stringify(payload) : undefined;
    const headers = {Accept: CONTENT_TYPE_JSON, 'Content-Type': CONTENT_TYPE_JSON};
    const actionType = (typeof action === 'string') ? {type: action} : action;
    dispatch({type: pendingFor(actionType.type)});

    const handleResponse = (response) => {
        dispatch({type: fulfilledFor(actionType.type), payload: response});
    }

    const handleRejection = (error) => {
        dispatch({type: rejectedFor(actionType.type), error});
    }
    fetch(url, {
        method,
        body: requestBody,
        headers: headers,
    }).then(checkStatus).then(response => response.json()).then(handleResponse).catch(handleRejection);
}
