import React from 'react';
import { Switch } from 'react-router-dom';
import { ReducersMapObject, combineReducers } from '@reduxjs/toolkit';

import getStore from 'app/config/store';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import entitiesReducers from './reducers';

import ProductCategory from './product/product-category';
import Notification from './notification/notification';
import ProductOrder from './product/product-order';
import Invoice from './invoice/invoice';
import Customer from './customer';
import Product from './product/product';
import Shipment from './invoice/shipment';
import OrderItem from './product/order-item';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default ({ match }) => {
  const store = getStore();
  store.injectReducer('store', combineReducers(entitiesReducers as ReducersMapObject));
  return (
    <div>
      <Switch>
        {/* prettier-ignore */}
        <ErrorBoundaryRoute path={`${match.url}product-category`} component={ProductCategory} />
        <ErrorBoundaryRoute path={`${match.url}notification`} component={Notification} />
        <ErrorBoundaryRoute path={`${match.url}product-order`} component={ProductOrder} />
        <ErrorBoundaryRoute path={`${match.url}invoice`} component={Invoice} />
        <ErrorBoundaryRoute path={`${match.url}customer`} component={Customer} />
        <ErrorBoundaryRoute path={`${match.url}product`} component={Product} />
        <ErrorBoundaryRoute path={`${match.url}shipment`} component={Shipment} />
        <ErrorBoundaryRoute path={`${match.url}order-item`} component={OrderItem} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </Switch>
    </div>
  );
};
