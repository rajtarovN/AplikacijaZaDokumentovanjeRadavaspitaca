import { Component, OnInit, ViewChild } from '@angular/core';
import { RichTextEditorComponent } from '@syncfusion/ej2-angular-richtexteditor';
import { FormControl, FormGroup } from '@angular/forms';
import { Observable } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { IKonacnaPrica, KonacnaPrica } from '../konacna-prica.model';
import { finalize } from 'rxjs/operators';
import { KonacnaPricaService } from '../service/konacna-prica.service';

@Component({
  selector: 'jhi-editor',
  templateUrl: './editor.component.html',
  styleUrls: ['./editor.component.scss'],
})
export class EditorComponent implements OnInit {
  isSaving = false;
  title = 'angular-richtexteditor';
  public mode = 'Markdown';
  @ViewChild('exampleRTE')
  public componentObject!: RichTextEditorComponent;

  public customToolbar: any = {
    items: ['Bold', 'Italic', 'Undo', 'Redo', 'CreateTable', 'Image', 'CreateLink'],
  };
  private buttonElement!: HTMLElement | null;
  private htmlContent!: string;

  constructor(protected konacnaPricaService: KonacnaPricaService) {}

  ngOnInit(): void {
    // eslint-disable-next-line no-console
    console.log('aa');
  }

  getFormattedContent(): void {
    this.buttonElement = document.getElementById('button');
    this.htmlContent = this.componentObject.getHtml();
    // eslint-disable-next-line no-console
    console.log(this.htmlContent);
    this.save();
  }
  previousState(): void {
    window.history.back();
  }
  save(): void {
    this.isSaving = true;
    const konacnaPrica = this.createFromForm();
    this.subscribeToSaveResponse(this.konacnaPricaService.create(konacnaPrica));
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IKonacnaPrica>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }
  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }
  protected createFromForm(): IKonacnaPrica {
    return {
      ...new KonacnaPrica(),
      id: undefined,
      tekst: this.htmlContent,
    };
  }
}
