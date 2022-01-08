import 'react-toastify/dist/ReactToastify.css';
import './app.scss';
import 'app/config/dayjs.ts';

import { Storage } from 'react-jhipster';
import { isRTL } from 'app/config/translation';

import React, { useEffect } from 'react';
import { Card } from 'reactstrap';
import { BrowserRouter as Router, Redirect } from 'react-router-dom';
import { ToastContainer, toast } from 'react-toastify';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getSession } from 'app/shared/reducers/authentication';
import { getProfile } from 'app/shared/reducers/application-profile';
import { setLocale } from 'app/shared/reducers/locale';
import Header from 'app/shared/layout/header/header';
import Footer from 'app/shared/layout/footer/footer';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import ErrorBoundary from 'app/shared/error/error-boundary';
import { AUTHORITIES } from 'app/config/constants';
import AppRoutes from 'app/routes';
import { LocaleMenu } from 'app/shared/layout/menus/locale';

const baseHref = document.querySelector('base').getAttribute('href').replace(/\/$/, '');

export const App = () => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getSession());
    dispatch(getProfile());
  }, []);

  const currentLocale = useAppSelector(state => state.locale.currentLocale);
  const isAuthenticated = useAppSelector(state => state.authentication.isAuthenticated);
  const isAdmin = useAppSelector(state => hasAnyAuthority(state.authentication.account.authorities, [AUTHORITIES.ADMIN]));
  const isOpenAPIEnabled = useAppSelector(state => state.applicationProfile.isOpenAPIEnabled);

  useEffect(() => document.querySelector('html').setAttribute('dir', isRTL(Storage.session.get('locale')) ? 'rtl' : 'ltr'));

  const handleLocaleChange = event => {
    const langKey = event.target.value;
    Storage.session.set('locale', langKey);
    dispatch(setLocale(langKey));
    document.querySelector('html').setAttribute('dir', isRTL(langKey) ? 'rtl' : 'ltr');
  };

  const paddingTop = '0px';
  return (
    <Router basename={baseHref+"/"}>
      <div className="app-container" style={{ paddingTop }}>
      
        <ToastContainer position={toast.POSITION.TOP_LEFT} className="toastify-container" toastClassName="toastify-toast" />
      { /* <ErrorBoundary>
          <Header
            isAuthenticated={isAuthenticated}
            isAdmin={isAdmin}
            currentLocale={currentLocale}
            isOpenAPIEnabled={isOpenAPIEnabled}
          />
        </ErrorBoundary> */
      }
        <div className="container-fluid view-container" id="app-view-container">
          <Card className="jh-card">
            <ErrorBoundary>
              <AppRoutes />
              
            </ErrorBoundary>
          </Card>
          <LocaleMenu currentLocale={currentLocale} onClick={handleLocaleChange} />
          {// <Footer />
          }
        </div>
      </div>
    
      <Redirect to="/resource" />
    </Router>
  );
};

export default App;
