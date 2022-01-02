import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Votes from './votes';
import VotesDetail from './votes-detail';
import VotesUpdate from './votes-update';
import VotesDeleteDialog from './votes-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={VotesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={VotesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={VotesDetail} />
      <ErrorBoundaryRoute path={match.url} component={Votes} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={VotesDeleteDialog} />
  </>
);

export default Routes;
