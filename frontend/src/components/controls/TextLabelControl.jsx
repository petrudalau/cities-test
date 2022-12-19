import React from 'react';
import PropTypes from 'prop-types';

export const TextLabelControl = ({ text }) => {
    return (<span className="pure-text">{text}</span>);
}

TextLabelControl.propTypes = {
    text: PropTypes.string,
}
