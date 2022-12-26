import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPresets } from '../presets.model';

@Component({
  selector: 'jhi-presets-detail',
  templateUrl: './presets-detail.component.html',
})
export class PresetsDetailComponent implements OnInit {
  presets: IPresets | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ presets }) => {
      this.presets = presets;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
