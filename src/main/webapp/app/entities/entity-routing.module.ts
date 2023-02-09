import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'audio',
        data: { pageTitle: 'ziaPattyApp.audio.home.title' },
        loadChildren: () => import('./audio/audio.module').then(m => m.AudioModule),
      },
      {
        path: 'categoria',
        data: { pageTitle: 'ziaPattyApp.categoria.home.title' },
        loadChildren: () => import('./categoria/categoria.module').then(m => m.CategoriaModule),
      },
      {
        path: 'frase',
        data: { pageTitle: 'ziaPattyApp.frase.home.title' },
        loadChildren: () => import('./frase/frase.module').then(m => m.FraseModule),
      },
      {
        path: 'lingua',
        data: { pageTitle: 'ziaPattyApp.lingua.home.title' },
        loadChildren: () => import('./lingua/lingua.module').then(m => m.LinguaModule),
      },
      {
        path: 'frase-audio',
        data: { pageTitle: 'ziaPattyApp.fraseAudio.home.title' },
        loadChildren: () => import('./frase-audio/frase-audio.module').then(m => m.FraseAudioModule),
      },
      {
        path: 'categoria-frase',
        data: { pageTitle: 'ziaPattyApp.categoriaFrase.home.title' },
        loadChildren: () => import('./categoria-frase/categoria-frase.module').then(m => m.CategoriaFraseModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
