import React from 'react';
import PropTypes from 'prop-types';

export const TextInputControl = ({ value, onChange }) => {
    return (<input type="text" onChange={(e) => onChange(e.target.value)} value={value}/>);
}

TextInputControl.propTypes = {
    value: PropTypes.string,
    onChange: PropTypes.func,
}
