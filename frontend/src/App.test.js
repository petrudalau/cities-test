import {fireEvent, render} from '@testing-library/react';
import App from './App';
import {Provider as CityListProvider} from './store/CityListStore';
import {Provider as CityUpdateProvider} from './store/CityUpdateStore';
import {Map} from 'immutable';
import React from 'react';
import {EDIT_CITY, LOAD_CITIES, UPDATE_CITY} from './store/CitiesActionTypes';
import {pendingFor} from './util/actionPromiseUtil';

// TODO component tests
describe('whole APP test', () => {
  const renderWithContexts = (cityListState, cityListDispatch, cityUpdateState, cityUpdateDispatch) => {
    const rendered = render(
        <CityListProvider value={{state: cityListState, dispatch: cityListDispatch}}>
          <CityUpdateProvider value={{state: cityUpdateState, dispatch: cityUpdateDispatch}}>
            <App/>
          </CityUpdateProvider>
        </CityListProvider>
    );
    jest.clearAllMocks();
    return rendered;
  };

  test('render cities table and pagination', () => {
    const cities = Array.from({length: 10}, (_, id) => ({id, name: `city${id}`, photo: `http://test${id}.url/`}));
    const {container} = renderWithContexts(Map({cities, totalCount: 1000}), jest.fn(), Map(), jest.fn());
    expect(container.querySelector('.cities-table-container')).toBeInTheDocument();
    expect(container.querySelector('.cities-table-container .name-filter')).toBeInTheDocument();
    expect(container.querySelector('.cities-table-container .pagination')).toBeInTheDocument();
    expect(container.querySelector('.cities-table-container .cities-table')).toBeInTheDocument();
    expect(container.querySelectorAll('.cities-table-container .cities-table tbody tr').length).toBe(cities.length);
    expect(container.querySelector('.city-editor')).toBeNull();
    expect(container.querySelector('.cities-table-container .loader')).toBeNull();
  });

  test('handle edit button click', () => {
    const cityUpdateDispatch = jest.fn();
    const cities = Array.from({length: 10}, (_, id) => ({id, name: `city${id}`, photo: `http://test${id}.url/`}));
    const {container} = renderWithContexts(Map({cities, totalCount: 1000}), jest.fn(), Map(), cityUpdateDispatch);
    expect(container.querySelector('.cities-table-container')).toBeInTheDocument();
    fireEvent.click(container.querySelectorAll('.cities-table tbody tr button')[7]);
    expect(cityUpdateDispatch).toHaveBeenCalledWith({type: EDIT_CITY, city: cities[7]});
  });

  test('handle page change', () => {
    const cityListDispatch = jest.fn();
    const cities = Array.from({length: 10}, (_, id) => ({id, name: `city${id}`, photo: `http://test${id}.url/`}));
    const {container} = renderWithContexts(Map({cities, totalCount: 1000}), cityListDispatch, Map(), jest.fn());
    expect(container.querySelector('.cities-table-container')).toBeInTheDocument();
    fireEvent.click(container.querySelectorAll('.pagination button')[5]);
    expect(cityListDispatch).toHaveBeenCalledWith({type: pendingFor(LOAD_CITIES)});
  });

  test('handle page size change', () => {
    const cityListDispatch = jest.fn();
    const cities = Array.from({length: 10}, (_, id) => ({id, name: `city${id}`, photo: `http://test${id}.url/`}));
    const {container} = renderWithContexts(Map({cities, totalCount: 1000}), cityListDispatch, Map(), jest.fn());
    expect(container.querySelector('.cities-table-container')).toBeInTheDocument();
    fireEvent.change(container.querySelector('.pagination select'), {target: {value: 20}});
    expect(cityListDispatch).toHaveBeenCalledWith({type: pendingFor(LOAD_CITIES)});
  });

  test('handle search by name', () => {
    const cityListDispatch = jest.fn();
    const cities = Array.from({length: 10}, (_, id) => ({id, name: `city${id}`, photo: `http://test${id}.url/`}));
    const {container} = renderWithContexts(Map({cities, totalCount: 1000}), cityListDispatch, Map(), jest.fn());
    expect(container.querySelector('.cities-table-container')).toBeInTheDocument();
    fireEvent.change(container.querySelector('.name-filter input'), {target: {value: 'test'}});
    expect(cityListDispatch).not.toHaveBeenCalled();
    fireEvent.click(container.querySelector('.name-filter .filter-button button'));
    expect(cityListDispatch).toHaveBeenCalledWith({type: pendingFor(LOAD_CITIES)});
  });

  test('render loaders when cities are not yet loaded', () => {
    const {container} = renderWithContexts(Map({loading: true}), jest.fn(), Map(), jest.fn());
    expect(container.querySelector('.cities-table-container')).toBeInTheDocument();
    expect(container.querySelector('.cities-table-container .pagination')).toBeNull();
    expect(container.querySelector('.cities-table-container .cities-table')).toBeNull();
    expect(container.querySelector('.city-editor')).toBeNull();
    expect(container.querySelectorAll('.cities-table-container .loader').length).toBe(2);
  });

  test('render message when results are empty', () => {
    const {container} = renderWithContexts(Map({cities: [], totalCount: 0}), jest.fn(), Map(), jest.fn());
    expect(container.querySelector('.cities-table-container')).toBeInTheDocument();
    expect(container.querySelector('.cities-table-container .pagination')).toBeNull();
    expect(container.querySelector('.cities-table-container .loader')).toBeNull();
    expect(container.querySelector('.city-editor')).toBeNull();
    expect(container.querySelector('.pagination-row span').textContent).toBe('No cities found.');
  });

  test('render message in case of error', () => {
    const {container} = renderWithContexts(Map({loading: false, error: 'error'}), jest.fn(), Map(), jest.fn());
    expect(container.querySelector('.cities-table-container')).toBeNull();
    expect(container.querySelector('.city-editor')).toBeNull();
    expect(container.querySelector('.load-error').textContent).toBe('Failed to load cities, please try again.');
  });

  test('render city editor and photo update', () => {
    const {container} = renderWithContexts(Map(), jest.fn(), Map({
      cityToEdit: {
        id: 1,
        name: 'city',
        photo: 'http://test.url/',
      },
    }), jest.fn());
    expect(container.querySelector('.cities-table-container')).toBeNull();
    expect(container.querySelector('.city-editor')).toBeInTheDocument();
    expect(container.querySelector('.city-photo')).toBeInTheDocument();
    expect(container.querySelector('.city-photo input').value).toBe('http://test.url/');
    expect(container.querySelector('.city-name')).toBeInTheDocument();
    expect(container.querySelector('.city-name input').value).toBe('city');
    expect(container.querySelector('.city-preview')).toBeInTheDocument();
    expect(container.querySelector('.city-preview img').src).toBe('http://test.url/');
    expect(container.querySelector('.action-close')).toBeInTheDocument();
    expect(container.querySelector('.action-save')).toBeInTheDocument();
    fireEvent.change(container.querySelector('.city-photo input'), {target: {value: 'http://new.url/'}});
    expect(container.querySelector('.city-preview img').src).toBe('http://new.url/');
  });

  test('handle city editor close', () => {
    const cityUpdateDispatch = jest.fn();
    const cityListDispatch = jest.fn();

    const {container} = renderWithContexts(Map(), cityListDispatch, Map({
      cityToEdit: {
        id: 1,
        name: 'city',
        photo: 'test-url',
      },
    }), cityUpdateDispatch);
    expect(container.querySelector('.cities-table-container')).toBeNull();
    expect(container.querySelector('.city-editor')).toBeInTheDocument();

    fireEvent.click(container.querySelector('.action-close button'));
    expect(cityUpdateDispatch).toHaveBeenCalledWith({city: null, type: EDIT_CITY});
    expect(cityListDispatch).toHaveBeenCalledWith({type: pendingFor(LOAD_CITIES)});
  });

  test('handle city editor save', () => {
    const cityUpdateDispatch = jest.fn();

    const {container} = renderWithContexts(Map(), jest.fn(), Map({
      cityToEdit: {
        id: 1,
        name: 'city',
        photo: 'test-url',
      },
    }), cityUpdateDispatch);
    expect(container.querySelector('.cities-table-container')).toBeNull();
    expect(container.querySelector('.city-editor')).toBeInTheDocument();

    fireEvent.click(container.querySelector('.action-save button'));
    expect(cityUpdateDispatch).toHaveBeenCalledWith({type: pendingFor(UPDATE_CITY)});
  });

  test('render city save error', () => {
    const {container} = renderWithContexts(Map(), jest.fn(), Map({
      cityToEdit: {id: 1, name: 'city', photo: 'test-url'},
      saveStatus: 'error',
    }), jest.fn());
    expect(container.querySelector('.cities-table-container')).toBeNull();
    expect(container.querySelector('.city-editor')).toBeInTheDocument();
    expect(container.querySelector('#saveResult span').textContent).toBe('Failed to save the city.');
  });

  test('render city save success', () => {
    const {container} = renderWithContexts(Map(), jest.fn(), Map({
      cityToEdit: {id: 1, name: 'city', photo: 'test-url'},
      saveStatus: 'stored',
    }), jest.fn());
    expect(container.querySelector('.cities-table-container')).toBeNull();
    expect(container.querySelector('.city-editor')).toBeInTheDocument();
    expect(container.querySelector('#saveResult span').textContent).toBe('Successfully saved.');
  });

  test('render city save ajax loader', () => {
    const {container} = renderWithContexts(Map(), jest.fn(), Map({
      cityToEdit: {id: 1, name: 'city', photo: 'test-url'},
      loading: true,
    }), jest.fn());
    expect(container.querySelector('.cities-table-container')).toBeNull();
    expect(container.querySelector('.city-editor')).toBeInTheDocument();
    expect(container.querySelector('#saveResult span')).toBeNull();
    expect(container.querySelector('#saveResult .loader')).toBeInTheDocument();
  });
});
