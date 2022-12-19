import React from 'react';
import PropTypes from 'prop-types';

export const ImagePreviewControl = ({imageUrl}) => {
  return (<img src={imageUrl} className="pure-image"></img>);
};

ImagePreviewControl.propTypes = {
  imageUrl: PropTypes.string,
};
