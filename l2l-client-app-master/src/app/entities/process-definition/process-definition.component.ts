import { AfterViewInit, Component, OnInit } from '@angular/core';
import { ProcessDefinitionService } from './process-definition.service';
import { ProcessDefinitionModel, ProcessDefinitionsResponse } from './process-definition.model';
import { ActivatedRoute, Router } from '@angular/router';
import { ITEMS_PER_PAGE } from '../../shared';
import { HttpResponse } from '@angular/common/http';
import { JhiAlertService, JhiParseLinks } from 'ng-jhipster';

@Component({
  selector: 'l2l-process-definition',
  templateUrl: './process-definition.component.html',
  styleUrls: ['./process-definition.component.scss']
})
export class ProcessDefinitionComponent implements OnInit, AfterViewInit {
  private definitions: ProcessDefinitionModel[];
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

  constructor(private processDefinitionService: ProcessDefinitionService ,
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
  transition() {
    this.router.navigate(['/admin/user-management'], {
      queryParams: {
        page: this.page,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    });
    this.loadAll(this.runtimeBundle);
  }
  private onSuccess(data, headers) {
    this.totalItems = headers.get('X-Total-Count');
    this.queryCount = this.totalItems;
    this.definitions = [];
    data.list.entries.forEach((val , idx , array) => {
      this.definitions.push(val.entry);
    });
    console.log('definitions', this.definitions);
  }

  private onError(error) {
    this.alertService.error(error.error, error.message, null);
  }
  loadAll(runtimeBundle: string) {
    this.processDefinitionService
      .queryAll(runtimeBundle , {
        page: this.page - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe(
        (res: HttpResponse<ProcessDefinitionsResponse[]>) => this.onSuccess(res.body, res.headers),
        (res: HttpResponse<any>) => this.onError(res.body)
      );
  }


  trackIdentity(index, item: ProcessDefinitionModel) {
    return item.id;
  }


}
