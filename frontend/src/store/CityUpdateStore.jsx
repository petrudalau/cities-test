import React, { createContext, useReducer } from 'react';
import { Map } from 'immutable';
import {EDIT_CITY, UPDATE_CITY} from "./CitiesActionTypes";
import {fulfilledFor, pendingFor, rejectedFor} from "../util/actionPromiseUtil";
import PropTypes from "prop-types";

const initialState = new Map({ loading: false});
const store = createContext(initialState);
const { Provider, Consumer } = store;
const CityUpdateStateProvider = ({children}) => {
    const [state, dispatch] = useReducer((state, action) => {
        switch (action.type) {
            case fulfilledFor(UPDATE_CITY):
                return state.set('loading', false).set('saveStatus', 'stored');
            case rejectedFor(UPDATE_CITY):
                return state.set('loading', false).set('saveStatus', 'error');
            case pendingFor(UPDATE_CITY):
                return state.set('loading', true);
            case EDIT_CITY:
                return state.set('cityToEdit', action.city);
            default:
                return state;
        }
    }, initialState);

    return (<Provider value={{state, dispatch}}>
        {children}
    </Provider>);
};

CityUpdateStateProvider.propTypes = {
    children: PropTypes.object,
}
export { store, CityUpdateStateProvider, Provider, Consumer}