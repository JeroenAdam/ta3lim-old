import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import locale from './locale';
import authentication from './authentication';
import applicationProfile from './application-profile';

import administration from 'app/modules/administration/administration.reducer';
import userManagement from './user-management';
// prettier-ignore
import file from 'app/entities/file/file.reducer';
// prettier-ignore
import subject from 'app/entities/subject/subject.reducer';
// prettier-ignore
import topic from 'app/entities/topic/topic.reducer';
// prettier-ignore
import resource from 'app/entities/resource/resource.reducer';
// prettier-ignore
import userExtended from 'app/entities/user-extended/user-extended.reducer';
// prettier-ignore
import notification from 'app/entities/notification/notification.reducer';
// prettier-ignore
import favorite from 'app/entities/favorite/favorite.reducer';
// prettier-ignore
import skill from 'app/entities/skill/skill.reducer';
// prettier-ignore
import message from 'app/entities/message/message.reducer';
// prettier-ignore
import votes from 'app/entities/votes/votes.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const rootReducer = {
  authentication,
  locale,
  applicationProfile,
  administration,
  userManagement,
  file,
  subject,
  topic,
  resource,
  userExtended,
  notification,
  favorite,
  skill,
  message,
  votes,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar,
};

export default rootReducer;
