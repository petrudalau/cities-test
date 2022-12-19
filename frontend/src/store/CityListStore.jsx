import React, { createContext, useReducer } from 'react';
import { Map } from 'immutable';
import {COUNT_CITIES, LOAD_CITIES} from "./CitiesActionTypes";
import {fulfilledFor, pendingFor, rejectedFor} from "../util/actionPromiseUtil";
import PropTypes from "prop-types";

const initialState = new Map({ loading: false});
const store = createContext(initialState);
const { Provider, Consumer } = store;
const CityListStateProvider = ({children}) => {
    const [state, dispatch] = useReducer((state, action) => {
        switch (action.type) {
            case rejectedFor(COUNT_CITIES):
            case rejectedFor(LOAD_CITIES):
                return state.set('error', action.error).set('loading', false);
            case fulfilledFor(COUNT_CITIES):
                return state.set('totalCount', action.payload).delete('error');
            case pendingFor(COUNT_CITIES):
                return state.delete('totalCount');
            case fulfilledFor(LOAD_CITIES):
                return state.set('loading', false).set('cities', action.payload);
            case pendingFor(LOAD_CITIES):
                return state.set('loading', true).delete('cities');
            default:
                return state;
        }
    }, initialState);

    return (<Provider value={{state, dispatch}}>
        {children}
    </Provider>);
};

CityListStateProvider.propTypes = {
    children: PropTypes.object,
}
export { store, CityListStateProvider, Provider, Consumer}