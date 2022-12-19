import {COUNT_CITIES, EDIT_CITY, LOAD_CITIES, UPDATE_CITY} from "./CitiesActionTypes";
import {get, put} from "../util/callBackendUtils";

const getCitiesUrl = (nameFilter, pageSize, page) => (
    nameFilter ?
        `/cities/filter/${nameFilter}?sortBy=name&sortOrder=ascending&pageSize=${pageSize}&startPage=${page}` :
        `/cities?sortBy=name&sortOrder=ascending&pageSize=${pageSize}&startPage=${page}`);

export const loadCityList = (dispatch, pageSize, page, nameFilter) => get(dispatch,
    {
        url: getCitiesUrl(nameFilter, pageSize, page),
        action: {type: LOAD_CITIES},
    }
);
export const countCities = (dispatch, nameFilter) => get(dispatch,
    {
        url: nameFilter ? `/cities/count/${nameFilter}` : '/cities/count',
        action: {type: COUNT_CITIES}
    }
);
export const updateCity = (dispatch, city) => put(dispatch,
    {url: `/cities/${city.id}`, action: {type: UPDATE_CITY}, payload: city}
);

export const editCity = (dispatch, city) => {
    dispatch({type: EDIT_CITY, city});
};

