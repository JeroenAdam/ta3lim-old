import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Input, InputGroup, FormGroup, Form, Row, Col, Table } from 'reactstrap';
import { Translate, translate, TextFormat, getSortState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { searchEntities, getEntities } from './user-extended.reducer';
import { IUserExtended } from 'app/shared/model/user-extended.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const UserExtended = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const [search, setSearch] = useState('');
  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(props.location, ITEMS_PER_PAGE, 'id'), props.location.search)
  );

  const userExtendedList = useAppSelector(state => state.userExtended.entities);
  const loading = useAppSelector(state => state.userExtended.loading);
  const totalItems = useAppSelector(state => state.userExtended.totalItems);

  const getAllEntities = () => {
    if (search) {
      dispatch(
        searchEntities({
          query: search,
          page: paginationState.activePage - 1,
          size: paginationState.itemsPerPage,
          sort: `${paginationState.sort},${paginationState.order}`,
        })
      );
    } else {
      dispatch(
        getEntities({
          page: paginationState.activePage - 1,
          size: paginationState.itemsPerPage,
          sort: `${paginationState.sort},${paginationState.order}`,
        })
      );
    }
  };

  const startSearching = e => {
    if (search) {
      setPaginationState({
        ...paginationState,
        activePage: 1,
      });
      dispatch(
        searchEntities({
          query: search,
          page: paginationState.activePage - 1,
          size: paginationState.itemsPerPage,
          sort: `${paginationState.sort},${paginationState.order}`,
        })
      );
    }
    e.preventDefault();
  };

  const clear = () => {
    setSearch('');
    setPaginationState({
      ...paginationState,
      activePage: 1,
    });
    dispatch(getEntities({}));
  };

  const handleSearch = event => setSearch(event.target.value);

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (props.location.search !== endURL) {
      props.history.push(`${props.location.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort, search]);

  useEffect(() => {
    const params = new URLSearchParams(props.location.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [props.location.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const { match } = props;

  return (
    <div>
      <h2 id="user-extended-heading" data-cy="UserExtendedHeading">
        <Translate contentKey="ta3LimApp.userExtended.home.title">User Extendeds</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="ta3LimApp.userExtended.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="ta3LimApp.userExtended.home.createLabel">Create new User Extended</Translate>
          </Link>
        </div>
      </h2>
      <Row>
        <Col sm="12">
          <Form onSubmit={startSearching}>
            <FormGroup>
              <InputGroup>
                <Input
                  type="text"
                  name="search"
                  defaultValue={search}
                  onChange={handleSearch}
                  placeholder={translate('ta3LimApp.userExtended.home.search')}
                />
                <Button className="input-group-addon">
                  <FontAwesomeIcon icon="search" />
                </Button>
                <Button type="reset" className="input-group-addon" onClick={clear}>
                  <FontAwesomeIcon icon="trash" />
                </Button>
              </InputGroup>
            </FormGroup>
          </Form>
        </Col>
      </Row>
      <div className="table-responsive">
        {userExtendedList && userExtendedList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="ta3LimApp.userExtended.id">ID</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('lastLogin')}>
                  <Translate contentKey="ta3LimApp.userExtended.lastLogin">Last Login</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('aboutMe')}>
                  <Translate contentKey="ta3LimApp.userExtended.aboutMe">About Me</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('occupation')}>
                  <Translate contentKey="ta3LimApp.userExtended.occupation">Occupation</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('socialMedia')}>
                  <Translate contentKey="ta3LimApp.userExtended.socialMedia">Social Media</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('civilStatus')}>
                  <Translate contentKey="ta3LimApp.userExtended.civilStatus">Civil Status</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('firstchild')}>
                  <Translate contentKey="ta3LimApp.userExtended.firstchild">Firstchild</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('secondchild')}>
                  <Translate contentKey="ta3LimApp.userExtended.secondchild">Secondchild</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('thirdchild')}>
                  <Translate contentKey="ta3LimApp.userExtended.thirdchild">Thirdchild</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('fourthchild')}>
                  <Translate contentKey="ta3LimApp.userExtended.fourthchild">Fourthchild</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('filesquota')}>
                  <Translate contentKey="ta3LimApp.userExtended.filesquota">Filesquota</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('approverSince')}>
                  <Translate contentKey="ta3LimApp.userExtended.approverSince">Approver Since</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('lastApproval')}>
                  <Translate contentKey="ta3LimApp.userExtended.lastApproval">Last Approval</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="ta3LimApp.userExtended.user">User</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {userExtendedList.map((userExtended, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${userExtended.id}`} color="link" size="sm">
                      {userExtended.id}
                    </Button>
                  </td>
                  <td>
                    {userExtended.lastLogin ? (
                      <TextFormat type="date" value={userExtended.lastLogin} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{userExtended.aboutMe}</td>
                  <td>{userExtended.occupation}</td>
                  <td>{userExtended.socialMedia}</td>
                  <td>
                    <Translate contentKey={`ta3LimApp.CivilStatus.${userExtended.civilStatus}`} />
                  </td>
                  <td>
                    <Translate contentKey={`ta3LimApp.Children.${userExtended.firstchild}`} />
                  </td>
                  <td>
                    <Translate contentKey={`ta3LimApp.Children.${userExtended.secondchild}`} />
                  </td>
                  <td>
                    <Translate contentKey={`ta3LimApp.Children.${userExtended.thirdchild}`} />
                  </td>
                  <td>
                    <Translate contentKey={`ta3LimApp.Children.${userExtended.fourthchild}`} />
                  </td>
                  <td>{userExtended.filesquota}</td>
                  <td>
                    {userExtended.approverSince ? (
                      <TextFormat type="date" value={userExtended.approverSince} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {userExtended.lastApproval ? (
                      <TextFormat type="date" value={userExtended.lastApproval} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{userExtended.user ? userExtended.user.login : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${userExtended.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${userExtended.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${userExtended.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="ta3LimApp.userExtended.home.notFound">No User Extendeds found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={userExtendedList && userExtendedList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default UserExtended;
