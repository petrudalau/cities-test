import React from 'react';
import PropTypes from 'prop-types';

export const TableControl = ({columns, rows, headerCellRenderer, rowCellRenderer}) => {
  const renderHeaders = () => {
    const headers = columns
        .map((column, index) => {
          return (<th key={`th-${index}`} className="pure-table-th">{headerCellRenderer(column, index)}</th>);
        });
    return <tr key="theader" className="pure-table-header">{headers}</tr>;
  };

  const renderRows = () => rows
      .map((row, rowIndex) => {
        const cells = columns
            .map((column, columnIndex) => {
              return (<td key={`td-${rowIndex}-${columnIndex}`} className="pure-table-td">
                {rowCellRenderer(row, rowIndex, column, columnIndex)}
              </td>);
            });
        return <tr key={`tr-${rowIndex}`} className="pure-table-row">{cells}</tr>;
      });

  return (<table className="table-control">
    <thead>{renderHeaders()}</thead>
    <tbody>{renderRows()}</tbody>
  </table>);
};

TableControl.propTypes = {
  columns: PropTypes.arrayOf(PropTypes.object),
  rows: PropTypes.arrayOf(PropTypes.object),
  headerCellRenderer: PropTypes.func,
  rowCellRenderer: PropTypes.func,
};

