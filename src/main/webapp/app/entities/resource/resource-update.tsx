import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm, ValidatedBlobField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/shared/reducers/user-management';
import { ISubject } from 'app/shared/model/subject.model';
import { getEntities as getSubjects } from 'app/entities/subject/subject.reducer';
import { ITopic } from 'app/shared/model/topic.model';
import { getEntities as getTopics } from 'app/entities/topic/topic.reducer';
import { ISkill } from 'app/shared/model/skill.model';
import { getEntities as getSkills } from 'app/entities/skill/skill.reducer';
import { getEntity, updateEntity, createEntity, reset } from './resource.reducer';
import { IResource } from 'app/shared/model/resource.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { ResourceType } from 'app/shared/model/enumerations/resource-type.model';
import { AgeRange } from 'app/shared/model/enumerations/age-range.model';

export const ResourceUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const users = useAppSelector(state => state.userManagement.users);
  const subjects = useAppSelector(state => state.subject.entities);
  const topics = useAppSelector(state => state.topic.entities);
  const skills = useAppSelector(state => state.skill.entities);
  const resourceEntity = useAppSelector(state => state.resource.entity);
  const loading = useAppSelector(state => state.resource.loading);
  const updating = useAppSelector(state => state.resource.updating);
  const updateSuccess = useAppSelector(state => state.resource.updateSuccess);
  const resourceTypeValues = Object.keys(ResourceType);
  const ageRangeValues = Object.keys(AgeRange);
  const handleClose = () => {
    props.history.push('/resource' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getUsers({}));
    dispatch(getSubjects({}));
    dispatch(getTopics({}));
    dispatch(getSkills({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...resourceEntity,
      ...values,
      topics: mapIdList(values.topics),
      skills: mapIdList(values.skills),
      user: users.find(it => it.id.toString() === values.user.toString()),
      subject: subjects.find(it => it.id.toString() === values.subject.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          resourceType: 'ARTICLES',
          angeRage: 'AGE_ALL',
          ...resourceEntity,
          user: resourceEntity?.user?.id,
          subject: resourceEntity?.subject?.id,
          topics: resourceEntity?.topics?.map(e => e.id.toString()),
          skills: resourceEntity?.skills?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="ta3LimApp.resource.home.createOrEditLabel" data-cy="ResourceCreateUpdateHeading">
            <Translate contentKey="ta3LimApp.resource.home.createOrEditLabel">Create or edit a Resource</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col sm="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              <ValidatedField
                label={translate('ta3LimApp.resource.title')}
                id="resource-title"
                name="title"
                data-cy="title"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('ta3LimApp.resource.description')}
                id="resource-description"
                name="description"
                data-cy="description"
                type="textarea" style={{ height: 255 }} 
              />

              <ValidatedField
                label={translate('ta3LimApp.resource.angeRage')}
                id="resource-angeRage"
                name="angeRage"
                data-cy="angeRage"
                type="select"
              >
                {ageRangeValues.map(ageRange => (
                  <option value={ageRange} key={ageRange}>
                    {translate('ta3LimApp.AgeRange.' + ageRange)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedBlobField
                label={translate('ta3LimApp.resource.file')}
                id="resource-file"
                name="file"
                data-cy="file"
                openActionLabel={translate('entity.action.open')}
              />
              <ValidatedField label={translate('ta3LimApp.resource.url')} id="resource-url" name="url" data-cy="url" type="text" />
              
              
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/resource" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
        <Col md="4">
        <ValidatedField
                id="resource-subject"
                name="subject"
                data-cy="subject"
                label={translate('ta3LimApp.resource.subject')}
                type="select"
              >
                <option value="" key="0" />
                {subjects
                  ? subjects.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.label}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('ta3LimApp.resource.resourceType')}
                id="resource-resourceType"
                name="resourceType"
                data-cy="resourceType"
                type="select"
              >
                {resourceTypeValues.map(resourceType => (
                  <option value={resourceType} key={resourceType}>
                    {translate('ta3LimApp.ResourceType.' + resourceType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('ta3LimApp.resource.topics')}
                id="resource-topics"
                data-cy="topics"
                type="select"
                multiple
                name="topics"
              >
                {topics
                  ? topics.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.label}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('ta3LimApp.resource.skills')}
                id="resource-skills"
                data-cy="skills"
                type="select"
                multiple
                name="skills"
              >
                {skills
                  ? skills.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.label}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('ta3LimApp.resource.author')}
                id="resource-author"
                name="author"
                data-cy="author"
                type="text"
              />
              {!isNew ? (                   
              <ValidatedField
                
                id="resource-user"
                name="user"
                data-cy="user"
                label={translate('ta3LimApp.resource.user')}
                type="select">
                  {users
                  ? users.map(otherEntity => (
                      otherEntity.id === "7ddb9efa-1112-4975-9ce1-009da61cf9b2" ? <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.login} 
                      </option> : null
                    ))
                  : null}
              </ValidatedField>
              ) : null} 
              {isNew ? (                    
              <ValidatedField id="resource-user" name="user" data-cy="user" label={translate('ta3LimApp.resource.user')} type="select">
                {users
                  ? users.map(otherEntity => (
                    otherEntity.id === "7ddb9efa-1112-4975-9ce1-009da61cf9b2" ? <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.login}
                      </option> : null
                    ))
                  : null}
              </ValidatedField>
              ) : null} 
        </Col>
      </Row>
    </div>
  );
};

export default ResourceUpdate;