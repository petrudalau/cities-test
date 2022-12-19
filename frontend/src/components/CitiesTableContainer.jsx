import React, {useCallback, useEffect, useState} from 'react';
import PropTypes from 'prop-types';
import {countCities, editCity, loadCityList, updateCity} from "../store/citiesActionDispatchers";
import {CitiesTableComponent} from "./CitiesTableComponent";
import {EditCityComponent} from "./EditCityComponent";
import {AjaxLoaderControl} from "./controls/AjaxLoaderControl";
import {PaginationComponent} from "./PaginationComponent";
import {NameFilterComponent} from "./NameFilterComponent";
import {useTranslation} from "react-i18next";

const columnToDisplay = [
    { name : 'name'},
    { name:'photo'},
    { name:'action'},
];



export const CitiesTableContainer = ({cityListDispatch, updateDispatch, cityToEdit, loading, error, cities, totalCount, saveStatus}) => {
    const { t } = useTranslation();
    // TODO store vs states
    const [pageSize, setPageSize] = useState(10);
    const [nameFilter, setNameFilter] = useState('');
    const [page, setPage] = useState(0);

    const pageSizeChanged = useCallback((newPageSize) => {
        const newPageSizeInt = parseInt(newPageSize);
        const calculatePage = Math.floor(page*pageSize / newPageSizeInt);
        setPage(calculatePage);
        setPageSize(newPageSizeInt);
    }, [setPage, setPageSize]);
    const nameFilterChanged = useCallback((newFilterValue) => {
        setPage(0);
        setNameFilter(newFilterValue);
    }, [setPage, setNameFilter]);
    useEffect( () => {
        loadCityList(cityListDispatch, pageSize, page, nameFilter);
    }, [pageSize, page, nameFilter]);
    useEffect( () => {
        countCities(cityListDispatch, nameFilter);
    }, [nameFilter]);
    const editorClosed = useCallback(() => {
        editCity(updateDispatch,null);
        loadCityList(cityListDispatch, pageSize, page, nameFilter);
    }, [updateDispatch, pageSize, page, nameFilter]);
    if (!loading && error) {
        return (<span className="load-error">{t('load.result.error')}</span>);
    }
    return (<React.Fragment>
        {cityToEdit && <EditCityComponent city={cityToEdit} closeEditor={editorClosed}
                                          updateCityHandler={(city) => {updateCity(updateDispatch, city)}} saveStatus={saveStatus}/>}

        {!cityToEdit &&<table className="cities-table-container">
            <tbody>
                <tr><td>
                    <NameFilterComponent currentFilter={nameFilter} filterByName={nameFilterChanged} />
                </td></tr>
                <tr><td>
                    {loading && <AjaxLoaderControl/>}
                    {(cities && !cityToEdit)&& <CitiesTableComponent columns={columnToDisplay} rows={cities}
                                             editCity={(city) => {editCity(updateDispatch,city)}} />}
                </td></tr>
                <tr className="pagination-row"><td>
                    <PaginationComponent pageSizeChanged={pageSizeChanged} rowsPerPage={pageSize}
                                 pageChanged={setPage} currentPageIndex={page} totalCount={totalCount}/>
                </td></tr>
            </tbody>
        </table>}
    </React.Fragment>);
}

CitiesTableContainer.propTypes = {
    cityListDispatch: PropTypes.func,
    updateDispatch: PropTypes.func,
    cityToEdit: PropTypes.object,
    loading: PropTypes.bool,
    error: PropTypes.string,
    saveStatus: PropTypes.string,
    cities: PropTypes.array,
    totalCount: PropTypes.number,
}
