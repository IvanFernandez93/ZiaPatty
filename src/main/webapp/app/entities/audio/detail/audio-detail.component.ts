import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAudio } from '../audio.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-audio-detail',
  templateUrl: './audio-detail.component.html',
})
export class AudioDetailComponent implements OnInit {
  audio: IAudio | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ audio }) => {
      this.audio = audio;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
