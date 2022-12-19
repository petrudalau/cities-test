import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';
import { CityListStateProvider } from './store/CityListStore';
import { CityUpdateStateProvider } from './store/CityUpdateStore';

const root = ReactDOM.createRoot(document.getElementById('root'));
//FIXME strict mode causes double rendering, but only in development mode
root.render(<React.StrictMode>
      <CityListStateProvider>
          <CityUpdateStateProvider>
            <App />
          </CityUpdateStateProvider>
      </CityListStateProvider>
  </React.StrictMode>);
