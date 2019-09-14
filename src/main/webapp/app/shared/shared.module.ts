import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { DecentralizedcharitySharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';

@NgModule({
  imports: [DecentralizedcharitySharedCommonModule],
  declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective],
  entryComponents: [JhiLoginModalComponent],
  exports: [DecentralizedcharitySharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DecentralizedcharitySharedModule {
  static forRoot() {
    return {
      ngModule: DecentralizedcharitySharedModule
    };
  }
}
