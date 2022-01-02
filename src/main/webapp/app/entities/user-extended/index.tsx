import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import UserExtended from './user-extended';
import UserExtendedDetail from './user-extended-detail';
import UserExtendedUpdate from './user-extended-update';
import UserExtendedDeleteDialog from './user-extended-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={UserExtendedUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={UserExtendedUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={UserExtendedDetail} />
      <ErrorBoundaryRoute path={match.url} component={UserExtended} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={UserExtendedDeleteDialog} />
  </>
);

export default Routes;
