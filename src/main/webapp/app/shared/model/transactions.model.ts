import { Moment } from 'moment';
import { TransactionType } from 'app/shared/model/enumerations/transaction-type.model';

export interface ITransactions {
  id?: number;
  txid?: string;
  amount?: number;
  transactionType?: TransactionType;
  createdDate?: Moment;
  transactionStatus?: boolean;
  blockHeight?: number;
  key?: string;
  note?: string;
  longitude?: number;
  latitude?: number;
  humidity?: number;
  temperature?: number;
  projectId?: number;
  userLogin?: string;
  userId?: number;
}

export class Transactions implements ITransactions {
  constructor(
    public id?: number,
    public txid?: string,
    public amount?: number,
    public transactionType?: TransactionType,
    public createdDate?: Moment,
    public transactionStatus?: boolean,
    public blockHeight?: number,
    public key?: string,
    public note?: string,
    public longitude?: number,
    public latitude?: number,
    public humidity?: number,
    public temperature?: number,
    public projectId?: number,
    public userLogin?: string,
    public userId?: number
  ) {
    this.transactionStatus = this.transactionStatus || false;
  }
}
