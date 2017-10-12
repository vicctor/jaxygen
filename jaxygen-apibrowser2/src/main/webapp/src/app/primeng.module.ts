/* This module is an example how you can package all services and components
 from PrimeNG into one Angular2 module, which you can import in other modules
 */
import { NgModule, ModuleWithProviders } from '@angular/core';

import { ProgressBarModule, DialogModule, ButtonModule, AutoCompleteModule, CheckboxModule, EditorModule, InputTextModule, ListboxModule, MultiSelectModule, RadioButtonModule, SliderModule, SelectButtonModule, CalendarModule, DropdownModule, InputSwitchModule, InputTextareaModule, InputMaskModule, PasswordModule, RatingModule, SpinnerModule, ToggleButtonModule, SplitButtonModule, CarouselModule, DataListModule, DataTableModule, SharedModule, GMapModule, PickListModule, TreeModule, DataGridModule, DataScrollerModule, OrderListModule, PaginatorModule, ScheduleModule, AccordionModule, PanelModule, TabViewModule, FieldsetModule, ToolbarModule, OverlayPanelModule, LightboxModule, TooltipModule, MenuModule, ContextMenuModule, PanelMenuModule, TabMenuModule, BreadcrumbModule, MegaMenuModule, MenubarModule, SlideMenuModule, TieredMenuModule, ChartModule, GrowlModule, GalleriaModule, DragDropModule, TerminalModule, ConfirmDialogModule, FileUploadModule } from 'primeng/primeng';

@NgModule({

    exports: [
        ProgressBarModule, DialogModule, ButtonModule, AutoCompleteModule, CheckboxModule, EditorModule, InputTextModule, ListboxModule, MultiSelectModule, RadioButtonModule, SliderModule, SelectButtonModule, CalendarModule, DropdownModule, InputSwitchModule, InputTextareaModule, InputMaskModule, PasswordModule, RatingModule, SpinnerModule, ToggleButtonModule, SplitButtonModule, CarouselModule, DataListModule, DataTableModule, SharedModule, GMapModule, PickListModule, TreeModule, DataGridModule, DataScrollerModule, OrderListModule, PaginatorModule, ScheduleModule, AccordionModule, PanelModule, TabViewModule, FieldsetModule, ToolbarModule, OverlayPanelModule, LightboxModule, TooltipModule, MenuModule, ContextMenuModule, PanelMenuModule, TabMenuModule, BreadcrumbModule, MegaMenuModule, MenubarModule, SlideMenuModule, TieredMenuModule, ChartModule, GrowlModule, GalleriaModule, DragDropModule, TerminalModule, ConfirmDialogModule, FileUploadModule
    ]
})

export class PrimeNGModule {

    static forRoot(): ModuleWithProviders {
        return {
            ngModule: PrimeNGModule
        };
    }
}
