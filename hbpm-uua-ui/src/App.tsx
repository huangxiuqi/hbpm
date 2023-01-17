import './App.less';
import PasswordLogin from './components/PasswordLogin';
import { createContext } from 'react';

declare global {
  interface Window {
    readonly __LOGIN_PAGE_VARS: PageVars.LoginPageVars;
  }
}

export const loginPageVars = createContext(window.__LOGIN_PAGE_VARS);

function App() {
  return (
    <loginPageVars.Provider value={window.__LOGIN_PAGE_VARS}>
      <div className="app-area">
        <div className="app-box">
          <div className="app-box-content">
            <div className="app-page">
              <PasswordLogin />
            </div>
          </div>
        </div>
      </div>
    </loginPageVars.Provider>
  );
}

export default App;
