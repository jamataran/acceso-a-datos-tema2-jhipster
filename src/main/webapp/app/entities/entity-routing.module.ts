import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'categoria',
        data: { pageTitle: 'Categorias' },
        loadChildren: () => import('./categoria/categoria.module').then(m => m.CategoriaModule),
      },
      {
        path: 'pelicula',
        data: { pageTitle: 'Peliculas' },
        loadChildren: () => import('./pelicula/pelicula.module').then(m => m.PeliculaModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
