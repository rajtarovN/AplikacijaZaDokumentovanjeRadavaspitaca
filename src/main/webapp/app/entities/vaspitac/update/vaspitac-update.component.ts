import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IVaspitac, Vaspitac } from '../vaspitac.model';
import { VaspitacService } from '../service/vaspitac.service';
import { IObjekat } from 'app/entities/objekat/objekat.model';
import { ObjekatService } from 'app/entities/objekat/service/objekat.service';
import { User } from '../../../admin/user-management/user-management.model';
import { LANGUAGES } from '../../../config/language.constants';
import { UserManagementService } from '../../../admin/user-management/service/user-management.service';

@Component({
  selector: 'jhi-vaspitac-update',
  templateUrl: './vaspitac-update.component.html',
})
export class VaspitacUpdateComponent implements OnInit {
  isSaving = false;

  languages = LANGUAGES;
  authorities: string[] = [];
  user!: User;
  doNotMatch = false;
  url: any;
  isImageSaved = false;

  objekatsSharedCollection: IObjekat[] = [];

  editForm = this.fb.group({
    datumZaposlenja: [],
    godineIskustva: [],
    opis: [],
    id: [],
    objekat: [],
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
    protected vaspitacService: VaspitacService,
    protected objekatService: ObjekatService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.user = new User();
    this.activatedRoute.data.subscribe(({ vaspitac }) => {
      this.updateForm(vaspitac);

      this.loadRelationshipsOptions();
    });
    this.userService.authorities().subscribe(authorities => (this.authorities = authorities));
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vaspitac = this.createFromForm();
    vaspitac.user = this.user;
    if (vaspitac.id !== undefined) {
      // eslint-disable-next-line no-console
      console.log('upd');
      this.subscribeToSaveResponse(this.vaspitacService.update(vaspitac));
    } else {
      // eslint-disable-next-line no-console
      console.log('gr');
      this.subscribeToSaveResponse(this.vaspitacService.create(vaspitac));
    }
  }
  saveUser(): void {
    this.doNotMatch = false;
    const vaspitac = this.createFromForm();
    if (vaspitac.id === undefined) {
      this.doNotMatch = false;
      this.isSaving = true;
      this.updateUser(this.user);
      if (this.editForm.get(['password'])!.value !== this.editForm.get(['confirmPassword'])!.value) {
        this.doNotMatch = true;
      } else {
        this.vaspitacService.createZaposlen(this.user).subscribe({
          next: res => {
            this.user.id = res.id;
            this.save();
          },
          error: () => this.onSaveError(),
        });
      }
    } else {
      this.save();
    }
  }

  trackObjekatById(_index: number, item: IObjekat): number {
    return item.id!;
  }

  onFileSelected(event: any): void {
    const reader = new FileReader();
    if (event.target.files && event.target.files.length > 0) {
      const file = event.target.files[0];
      reader.readAsDataURL(file);
      reader.onload = () => {
        this.url = reader.result;
        this.isImageSaved = true;
      };
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVaspitac>>): void {
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

  protected updateForm(vaspitac: IVaspitac): void {
    this.editForm.patchValue({
      datumZaposlenja: vaspitac.datumZaposlenja,
      godineIskustva: vaspitac.godineIskustva,
      opis: vaspitac.opis,
      id: vaspitac.id,
      objekat: vaspitac.objekat,
      login: 'pogresno',
      email: 'pogresno@gmail.com',
      password: 'Aa1!aaaaaaaa',
      confirmPassword: 'Aa1!aaaaaaaa',
      firstName: 'aaaaaaaaa',
      lastName: 'aaaaaaaa',
      activated: true,
      langKey: 'ddd',
      authorities: ['ROLE_VASPITAC'],
    });
    this.objekatsSharedCollection = this.objekatService.addObjekatToCollectionIfMissing(this.objekatsSharedCollection, vaspitac.objekat);
  }

  protected loadRelationshipsOptions(): void {
    this.objekatService
      .query()
      .pipe(map((res: HttpResponse<IObjekat[]>) => res.body ?? []))
      .pipe(
        map((objekats: IObjekat[]) => this.objekatService.addObjekatToCollectionIfMissing(objekats, this.editForm.get('objekat')!.value))
      )
      .subscribe((objekats: IObjekat[]) => (this.objekatsSharedCollection = objekats));
  }

  protected createFromForm(): IVaspitac {
    return {
      ...new Vaspitac(),
      datumZaposlenja: this.editForm.get(['datumZaposlenja'])!.value,
      godineIskustva: this.editForm.get(['godineIskustva'])!.value,
      opis: this.editForm.get(['opis'])!.value,
      id: this.editForm.get(['id'])!.value,
      objekat: this.editForm.get(['objekat'])!.value,
      slika: this.url,
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
