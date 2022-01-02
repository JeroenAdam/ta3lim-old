import { IResource } from 'app/shared/model/resource.model';

export interface IFile {
  id?: number;
  label?: string;
  fileContentType?: string | null;
  file?: string | null;
  resource?: IResource | null;
}

export const defaultValue: Readonly<IFile> = {};
