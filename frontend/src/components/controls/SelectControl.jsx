import React from 'react';
import PropTypes from 'prop-types';
import {TextLabelControl} from "./TextLabelControl";

export const SelectControl = ({ label, options, handleChange, value }) => {
    const renderOptions = () => options.map((option) => {
        const optionValue = option.value;
        const optionLabel = option.label ? option.label : option.value;
        return <option key={optionValue} value={optionValue}>{optionLabel}</option>
    });
    return (<div className="pure-select">
        {label && <TextLabelControl text={label}/>}
        <select value={value} onChange={(e) => handleChange(e.target.value)}>{renderOptions()}</select>
    </div>);
}

SelectControl.propTypes = {
    label: PropTypes.string,
    options: PropTypes.array,
    handleChange: PropTypes.func,
    value: PropTypes.string,
}
