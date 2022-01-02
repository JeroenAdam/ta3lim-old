import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import File from './file';
import FileDetail from './file-detail';
import FileUpdate from './file-update';
import FileDeleteDialog from './file-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={FileUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={FileUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={FileDetail} />
      <ErrorBoundaryRoute path={match.url} component={File} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={FileDeleteDialog} />
  </>
);

export default Routes;
