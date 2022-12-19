import React, {useCallback} from 'react';
import PropTypes from 'prop-types';
import {TableControl} from './controls/TableControl';
import {TextLabelControl} from './controls/TextLabelControl';
import {ImagePreviewControl} from './controls/ImagePreviewControl';
import {ButtonControl} from './controls/ButtonControl';
import {useTranslation} from 'react-i18next';

export const CitiesTableComponent = ({columns, rows, editCity}) => {
  const {t} = useTranslation();

  const columnHeaderRenderer = useCallback((column) => {
    return (<TextLabelControl text={t(`column.${column.name}`)}/>);
  }, [t]);

  const cellRenderer = useCallback((row, rowIndex, column) => {
    if (column.name === 'photo') {
      return (<ImagePreviewControl imageUrl={row[column.name]}/>);
    }
    if (column.name === 'action') {
      return (<ButtonControl key={`edit-button-${rowIndex}`} label={t('action.edit')} onClick={() => editCity(row)}/>);
    }
    return (<TextLabelControl text={row[column.name]}/>);
  }, [t, editCity]);

  return (<div className="cities-table">
    <TableControl
      columns={columns}
      rows={rows}
      headerCellRenderer={columnHeaderRenderer}
      rowCellRenderer={cellRenderer}
    />
  </div>);
};

CitiesTableComponent.propTypes = {
  rows: PropTypes.array,
  columns: PropTypes.array,
  editCity: PropTypes.func,
};
