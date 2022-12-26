import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { PresetsFormService, PresetsFormGroup } from './presets-form.service';
import { IPresets } from '../presets.model';
import { PresetsService } from '../service/presets.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { Waveform } from 'app/entities/enumerations/waveform.model';
import { Instrument } from 'app/entities/enumerations/instrument.model';

@Component({
  selector: 'jhi-presets-update',
  templateUrl: './presets-update.component.html',
})
export class PresetsUpdateComponent implements OnInit {
  isSaving = false;
  presets: IPresets | null = null;
  waveformValues = Object.keys(Waveform);
  instrumentValues = Object.keys(Instrument);

  usersSharedCollection: IUser[] = [];

  editForm: PresetsFormGroup = this.presetsFormService.createPresetsFormGroup();

  constructor(
    protected presetsService: PresetsService,
    protected presetsFormService: PresetsFormService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ presets }) => {
      this.presets = presets;
      if (presets) {
        this.updateForm(presets);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const presets = this.presetsFormService.getPresets(this.editForm);
    if (presets.id !== null) {
      this.subscribeToSaveResponse(this.presetsService.update(presets));
    } else {
      this.subscribeToSaveResponse(this.presetsService.create(presets));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPresets>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(presets: IPresets): void {
    this.presets = presets;
    this.presetsFormService.resetForm(this.editForm, presets);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, presets.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.presets?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
