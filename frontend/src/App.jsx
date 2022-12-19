import React from 'react';
import './App.scss';
import { Consumer as CityListConsumer} from './store/CityListStore';
import { Consumer as CityUpdateConsumer} from './store/CityUpdateStore';
import {CitiesTableContainer} from "./components/CitiesTableContainer";
import './i18n';

const App = () => (<div className="App">
        <CityListConsumer>
            { cityListStore =>
                <CityUpdateConsumer>
                    {cityUpdateStore => <CitiesTableContainer
                        cityListDispatch={cityListStore.dispatch}
                        updateDispatch={cityUpdateStore.dispatch}
                        cityToEdit={cityUpdateStore.state.get('cityToEdit')}
                        saveStatus={cityUpdateStore.state.get('loading') ? 'loading' : cityUpdateStore.state.get('saveStatus')}
                        loading={cityListStore.state.get('loading')}
                        error={cityListStore.state.get('error')}
                        cities={cityListStore.state.get('cities')}
                        totalCount={cityListStore.state.get('totalCount')}
                    />}
                </CityUpdateConsumer>
            }
        </CityListConsumer>
    </div>);

export default App;
