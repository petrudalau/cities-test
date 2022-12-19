import React from 'react';
import PropTypes from 'prop-types';

export const ButtonControl = ({label, onClick, disabled}) => {
  return (<button onClick={onClick} disabled={disabled}>{label}</button>);
};

ButtonControl.propTypes = {
  label: PropTypes.string,
  onClick: PropTypes.func,
  disabled: PropTypes.bool,
};
