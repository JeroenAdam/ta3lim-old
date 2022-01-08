import React from 'react';
import { render } from '@testing-library/react';
import { Provider } from 'react-redux';
import { Router } from 'react-router-dom';
import { createMemoryHistory } from 'history';

import initStore from 'app/config/store';
import Header from './header';

describe('Header', () => {
  let mountedWrapper;
  const devProps = {
    isAuthenticated: true,
    isAdmin: true,
    currentLocale: 'en',
    isOpenAPIEnabled: true,
  };
  const prodProps = {
    ...devProps,
    isOpenAPIEnabled: false,
  };
  const userProps = {
    ...prodProps,
    isAdmin: false,
  };
  const guestProps = {
    ...prodProps,
    isAdmin: false,
    isAuthenticated: false,
  };

  const wrapper = (props = devProps) => {
    if (!mountedWrapper) {
      const store = initStore();
      const history = createMemoryHistory();
      const { container } = render(
        <Provider store={store}>
          <Router history={history}>
            <Header {...props} />
          </Router>
        </Provider>
      );
      mountedWrapper = container.innerHTML;
    }
    return mountedWrapper;
  };

  beforeEach(() => {
    mountedWrapper = undefined;
  });

  // All tests will go here
  it('Renders a Header component in dev profile with LoadingBar, Navbar, Nav.', () => {
    const html = wrapper();
    // Find AccountMenu component
    expect(html).toContain('account-menu');
  });

  it('Renders a Header component in prod profile with LoadingBar, Navbar, Nav.', () => {
    const html = wrapper(prodProps);
    // Find AccountMenu component
    expect(html).toContain('account-menu');
  });

  it('Renders a Header component in prod profile with logged in User', () => {
    const html = wrapper(userProps);

    // Find AccountMenu component
    expect(html).toContain('account-menu');
  });

});
