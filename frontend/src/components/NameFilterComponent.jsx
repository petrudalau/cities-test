import {TextInputControl} from "./controls/TextInputControl";
import {ButtonControl} from "./controls/ButtonControl";
import React, {useState} from "react";
import PropTypes from "prop-types";
import {useTranslation} from "react-i18next";

export const NameFilterComponent = ({currentFilter, filterByName}) => {
    const { t } = useTranslation();
    const [nameFilter, setNameFilter] = useState('');

    return (<div className="name-filter">
        <TextInputControl value={nameFilter || currentFilter} onChange={setNameFilter}/>
        {/*// TODO handle enter*/}
        <div className="button-wrapper filter-button">
            <ButtonControl label={t('action.filter')} onClick={() => filterByName(nameFilter)}/>
        </div>
        <div className="button-wrapper reset-filter-button">
            <ButtonControl label={t('action.filter.reset')} onClick={() => filterByName('')} disabled={!currentFilter}/>
        </div>
    </div>);
}

NameFilterComponent.propTypes = {
    filterByName: PropTypes.func,
    currentFilter: PropTypes.string,
}
