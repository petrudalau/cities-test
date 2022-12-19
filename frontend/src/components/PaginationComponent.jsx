import React from 'react';
import PropTypes from 'prop-types';
import {TextLabelControl} from './controls/TextLabelControl';
import {ButtonControl} from './controls/ButtonControl';
import {AjaxLoaderControl} from './controls/AjaxLoaderControl';
import {SelectControl} from './controls/SelectControl';
import {useTranslation} from 'react-i18next';

const PAGINATION_RANGE = 5;
export const PaginationComponent = ({totalCount, rowsPerPage, currentPageIndex, pageChanged, pageSizeChanged}) => {
  const {t} = useTranslation();
  if (!totalCount) {
    if (totalCount === 0) {
      return (<TextLabelControl text={t('cities.not.found')}/>);
    }
    return (<AjaxLoaderControl/>);
  }
  const maxPage = Math.ceil(totalCount / rowsPerPage) - 1;
  const startPage = Math.max(0, currentPageIndex - PAGINATION_RANGE);
  const endPage = Math.min(maxPage, startPage + PAGINATION_RANGE * 2);
  const paginationButtons = [];
  for (let i = startPage; i <= endPage; i++) {
    paginationButtons.push((<ButtonControl
      key={`pagination-${i}`} label={(i + 1).toString()}
      onClick={() => pageChanged(i)} disabled={i === currentPageIndex}
    />));
  }
  return (<div className="pagination">
    <div>
      <TextLabelControl text={`${t('pagination.page')} ${currentPageIndex + 1} ${t('pagination.of')} ${maxPage + 1}`}/>
      <ButtonControl label="1" onClick={() => pageChanged(0)} disabled={currentPageIndex === 0}/>
      <TextLabelControl text=".."/>
      {paginationButtons}
      <TextLabelControl text=".."/>
      <ButtonControl label={(maxPage + 1).toString()}
        onClick={() => pageChanged(maxPage)} disabled={currentPageIndex === maxPage}/>
    </div>
    <div>
      <SelectControl label={t('pagination.pageSize')}
        options={[{value: 10}, {value: 20}, {value: 50}]}
        handleChange={pageSizeChanged} value={rowsPerPage.toString()}
      />
    </div>
  </div>);
};

PaginationComponent.propTypes = {
  pageSizeChanged: PropTypes.func,
  rowsPerPage: PropTypes.number,
  pageChanged: PropTypes.func,
  currentPageIndex: PropTypes.number,
  totalCount: PropTypes.number,
};
