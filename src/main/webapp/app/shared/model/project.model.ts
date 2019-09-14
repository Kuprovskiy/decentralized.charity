import { Moment } from 'moment';
import { ProjectType } from 'app/shared/model/enumerations/project-type.model';

export interface IProject {
  id?: number;
  name?: string;
  amount?: number;
  projectType?: ProjectType;
  expiredDate?: Moment;
  description?: string;
  userLogin?: string;
  userId?: number;
}

export class Project implements IProject {
  constructor(
    public id?: number,
    public name?: string,
    public amount?: number,
    public projectType?: ProjectType,
    public expiredDate?: Moment,
    public description?: string,
    public userLogin?: string,
    public userId?: number
  ) {}
}
