import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import {LandingViewModule} from "./landing-view/landing-view.module";

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    LandingViewModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
