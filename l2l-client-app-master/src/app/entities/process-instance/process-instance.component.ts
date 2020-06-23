import { AfterViewInit, Component, OnInit } from '@angular/core';
import { ProcessInstanceService } from './process-instance.service';
import {ProcessInstance, ProcessInstancesResponse} from './process-instance.model';
import { ActivatedRoute, Router } from '@angular/router';
import { ITEMS_PER_PAGE } from '../../shared';
import { HttpResponse } from '@angular/common/http';
import { JhiAlertService, JhiParseLinks } from 'ng-jhipster';

@Component({
  selector: 'l2l-process-instance',
  templateUrl: './process-instance.component.html',
  styleUrls: ['./process-instance.component.scss']
})
export class ProcessInstanceComponent implements OnInit, AfterViewInit {
  private instances: ProcessInstance[];
  displayedColumns = ['id', 'applicationName', 'status', 'processDefinitionId', 'lastModified', 'actions'];
  total: number;

  actions: Array<any> = [];
  success: any;
  routeData: any;
  links: any;
  totalItems: any;
  queryCount: any;
  itemsPerPage: any;
  page: any;
  predicate: any;
  previousPage: any;
  reverse: any;
  runtimeBundle: string;

  constructor(private processInstanceService: ProcessInstanceService ,
              private alertService: JhiAlertService,
              private activatedRoute: ActivatedRoute,
              private router: Router) {
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.routeData = this.activatedRoute.data.subscribe(data => {
      console.log(data);
      this.page = data['pagingParams'].page;
      this.previousPage = data['pagingParams'].page;
      this.reverse = data['pagingParams'].ascending;
      this.predicate = data['pagingParams'].predicate;
      this.runtimeBundle = data['runtimeBundle'];
    });
  }

  ngOnInit() {
      this.loadAll(this.runtimeBundle);
  }

  ngAfterViewInit() {

  }
  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  loadAll(runtimeBundle: string) {
    this.processInstanceService
      .queryAll(runtimeBundle , {
        page: this.page - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe(
        (res: HttpResponse<ProcessInstancesResponse>) => {
          this.queryCount = this.totalItems;
          this.instances = [];
          res.body.list.entries.forEach((val) => {
            this.instances.push(val.entry) ;
          });
        },
        (res: HttpResponse<any>) => {
          console.log('queryall error');
        }
      );
  }

  trackIdentity(index, item: ProcessInstance) {
    return item.id;
  }



}
