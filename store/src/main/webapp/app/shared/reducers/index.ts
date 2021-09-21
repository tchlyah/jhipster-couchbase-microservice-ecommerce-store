import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import locale from './locale';
import authentication from './authentication';
import applicationProfile from './application-profile';

import administration from 'app/modules/administration/administration.reducer';
import userManagement from 'app/modules/administration/user-management/user-management.reducer';
import register from 'app/modules/account/register/register.reducer';
import activate from 'app/modules/account/activate/activate.reducer';
import password from 'app/modules/account/password/password.reducer';
import settings from 'app/modules/account/settings/settings.reducer';
import passwordReset from 'app/modules/account/password-reset/password-reset.reducer';
// prettier-ignore
import productCategory from 'app/entities/product/product-category/product-category.reducer';
// prettier-ignore
import notification from 'app/entities/notification/notification/notification.reducer';
// prettier-ignore
import productOrder from 'app/entities/product/product-order/product-order.reducer';
// prettier-ignore
import invoice from 'app/entities/invoice/invoice/invoice.reducer';
// prettier-ignore
import customer from 'app/entities/customer/customer.reducer';
// prettier-ignore
import product from 'app/entities/product/product/product.reducer';
// prettier-ignore
import shipment from 'app/entities/invoice/shipment/shipment.reducer';
// prettier-ignore
import orderItem from 'app/entities/product/order-item/order-item.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const rootReducer = {
  authentication,
  locale,
  applicationProfile,
  administration,
  userManagement,
  register,
  activate,
  passwordReset,
  password,
  settings,
  productCategory,
  notification,
  productOrder,
  invoice,
  customer,
  product,
  shipment,
  orderItem,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar,
};

export default rootReducer;
