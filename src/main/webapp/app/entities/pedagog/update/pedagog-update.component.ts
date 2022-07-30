import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IPedagog, Pedagog } from '../pedagog.model';
import { PedagogService } from '../service/pedagog.service';
import { LANGUAGES } from '../../../config/language.constants';
import { UserManagementService } from '../../../admin/user-management/service/user-management.service';
import { User } from '../../../admin/user-management/user-management.model';

@Component({
  selector: 'jhi-pedagog-update',
  templateUrl: './pedagog-update.component.html',
})
export class PedagogUpdateComponent implements OnInit {
  isSaving = false;
  languages = LANGUAGES;
  authorities: string[] = [];
  user!: User;
  doNotMatch = false;
  editForm = this.fb.group({
    id: [],
    login: [
      '',
      [
        Validators.required,
        Validators.minLength(1),
        Validators.maxLength(50),
        Validators.pattern('^[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$|^[_.@A-Za-z0-9-]+$'),
      ],
    ],
    email: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(254), Validators.email]],
    password: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(50)]],
    confirmPassword: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(50)]],
    firstName: ['', [Validators.maxLength(50)]],
    lastName: ['', [Validators.maxLength(50)]],
    activated: [],
    langKey: [],
    authorities: [],
  });

  constructor(
    private userService: UserManagementService,
    protected pedagogService: PedagogService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.user = new User();
    this.activatedRoute.data.subscribe(({ pedagog }) => {
      this.updateForm(pedagog);
    });
    this.userService.authorities().subscribe(authorities => (this.authorities = authorities));
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pedagog = this.createFromForm();
    pedagog.user = this.user;
    if (pedagog.id !== undefined) {
      this.subscribeToSaveResponse(this.pedagogService.update(pedagog));
    } else {
      this.subscribeToSaveResponse(this.pedagogService.create(pedagog));
    }
  }
  saveUser(): void {
    this.doNotMatch = false;
    this.isSaving = true;
    this.updateUser(this.user);

    this.pedagogService.createZaposlen(this.user).subscribe({
      next: res => {
        // eslint-disable-next-line no-console
        console.log(res);
        this.user.id = res.id;
        this.save();
      },
      error: () => this.onSaveError(),
    });
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPedagog>>): void {
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

  protected updateForm(pedagog: IPedagog): void {
    this.editForm.patchValue({
      id: pedagog.id,
    });
  }

  protected createFromForm(): IPedagog {
    return {
      ...new Pedagog(),
      id: this.editForm.get(['id'])!.value,
    };
  }
  private updateUser(user: User): void {
    user.login = this.editForm.get(['login'])!.value;
    user.firstName = this.editForm.get(['firstName'])!.value;
    user.lastName = this.editForm.get(['lastName'])!.value;
    user.email = this.editForm.get(['email'])!.value;
    user.activated = this.editForm.get(['activated'])!.value;
    user.langKey = this.editForm.get(['langKey'])!.value;
    user.authorities = this.editForm.get(['authorities'])!.value;
    const password = this.editForm.get(['password'])!.value;
    if (password !== this.editForm.get(['confirmPassword'])!.value) {
      this.doNotMatch = true;
    } else {
      user.password = this.editForm.get(['password'])!.value;
    }
  }
}
