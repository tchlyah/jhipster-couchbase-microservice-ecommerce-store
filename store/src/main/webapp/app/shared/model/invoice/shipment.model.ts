import dayjs from 'dayjs';
import { IInvoice } from 'app/shared/model/invoice/invoice.model';

export interface IShipment {
  id?: string;
  trackingCode?: string | null;
  date?: string;
  details?: string | null;
  invoice?: IInvoice;
}

export const defaultValue: Readonly<IShipment> = {};
