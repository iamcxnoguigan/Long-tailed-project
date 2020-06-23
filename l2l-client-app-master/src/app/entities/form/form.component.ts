import { AfterViewInit, Component, Input, OnInit } from '@angular/core';
import { FormService } from './form.service';
import { FormDefinitionModel } from './form.model';
import { ActivatedRoute, Router } from '@angular/router';
import { ITEMS_PER_PAGE } from '../../shared';
import { HttpResponse } from '@angular/common/http';
import { JhiAlertService, JhiParseLinks } from 'ng-jhipster';

@Component({
  selector: 'l2l-activiti-form',
  templateUrl: './form.component.html',
  styleUrls: ['./form.component.scss']
})
export class FormComponent implements OnInit, AfterViewInit {
  @Input() private formDefinition: FormDefinitionModel;
  runtimeBundle: string;

  constructor(private formService: FormService ,
              private alertService: JhiAlertService,
              private activatedRoute: ActivatedRoute,
              private router: Router) {
  this.activatedRoute.data.subscribe(data => {
      console.log(data);
    });
  }

  ngOnInit() {
  }

  ngAfterViewInit() {

  }

}
