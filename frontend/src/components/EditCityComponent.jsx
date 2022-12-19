import React, {useState} from 'react';
import PropTypes from 'prop-types';
import {TextLabelControl} from './controls/TextLabelControl';
import {ImagePreviewControl} from './controls/ImagePreviewControl';
import {ButtonControl} from './controls/ButtonControl';
import {TextInputControl} from './controls/TextInputControl';
import {useTranslation} from 'react-i18next';
import {AjaxLoaderControl} from './controls/AjaxLoaderControl';

export const EditCityComponent = ({city, updateCityHandler, closeEditor, saveStatus}) => {
  const {t} = useTranslation();
  const [cityName, setCityName] = useState(city.name);
  const [photoUrl, setPhotoUrl] = useState(city.photo);
  // TODO add debouncing for photo url input
  return (<div className="city-editor">
    <div id="saveResult" className={saveStatus}>
      {saveStatus === 'loading' && <AjaxLoaderControl/>}
      {(saveStatus && saveStatus !== 'loading') && <TextLabelControl text={t(`save.result.${saveStatus}`)}/>}
    </div>
    <div className="city-name">
      <TextLabelControl text={t('column.name')}/>
      <TextInputControl value={cityName} onChange={setCityName}/>
    </div>
    <div className="city-photo">
      <TextLabelControl text={t('column.photo')}/>
      <TextInputControl value={photoUrl} onChange={setPhotoUrl}/>
    </div>
    <div className="city-preview">
      <ImagePreviewControl imageUrl={photoUrl}/>
    </div>
    <div className="button-wrapper action-save">
      <ButtonControl label={t('action.save')}
        onClick={() => updateCityHandler({id: city.id, name: cityName, photo: photoUrl})}/>
    </div>
    <div className="button-wrapper action-close">
      <ButtonControl label={t('action.close')} onClick={closeEditor}/>
    </div>

  </div>);
};

EditCityComponent.propTypes = {
  city: PropTypes.object,
  updateCityHandler: PropTypes.func,
  closeEditor: PropTypes.func,
  saveStatus: PropTypes.string,
};
