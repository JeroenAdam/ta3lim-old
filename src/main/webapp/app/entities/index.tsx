import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import File from './file';
import Subject from './subject';
import Topic from './topic';
import Resource from './resource';
import UserExtended from './user-extended';
import Notification from './notification';
import Favorite from './favorite';
import Skill from './skill';
import Message from './message';
import Votes from './votes';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}file`} component={File} />
      <ErrorBoundaryRoute path={`${match.url}subject`} component={Subject} />
      <ErrorBoundaryRoute path={`${match.url}topic`} component={Topic} />
      <ErrorBoundaryRoute path={`${match.url}resource`} component={Resource} />
      <ErrorBoundaryRoute path={`${match.url}user-extended`} component={UserExtended} />
      <ErrorBoundaryRoute path={`${match.url}notification`} component={Notification} />
      <ErrorBoundaryRoute path={`${match.url}favorite`} component={Favorite} />
      <ErrorBoundaryRoute path={`${match.url}skill`} component={Skill} />
      <ErrorBoundaryRoute path={`${match.url}message`} component={Message} />
      <ErrorBoundaryRoute path={`${match.url}votes`} component={Votes} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
