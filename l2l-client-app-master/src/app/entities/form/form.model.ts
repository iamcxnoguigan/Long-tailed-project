export interface FormFieldModel {
    id: string;
    name: string;
    type: string;
    value: string;
    required?: string;
    readOnly?: string;
}

export interface SubmitFormModel {
  name: string;
  value: string;
}

export interface FormDefinitionModel {
  fields: FormFieldModel[];
}

export interface  SubmitForm {
  fields: SubmitForm[];
}
