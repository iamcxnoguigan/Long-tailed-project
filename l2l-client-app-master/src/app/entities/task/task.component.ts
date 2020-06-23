import { AfterViewInit, Component, OnInit } from '@angular/core';
import { TaskService } from './task.service';
import {TaskModel, TasksResponse} from './task.model';
import { ActivatedRoute, Router } from '@angular/router';
import { ITEMS_PER_PAGE } from '../../shared';
import { HttpResponse } from '@angular/common/http';
import { JhiAlertService, JhiParseLinks } from 'ng-jhipster';

@Component({
  selector: 'l2l-task',
  templateUrl: './task.component.html',
  styleUrls: ['./task.component.scss']
})
export class TaskComponent implements OnInit, AfterViewInit {
  private tasks: TaskModel[];
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

  constructor(private taskService: TaskService ,
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
    this.taskService
      .queryAll(runtimeBundle , {
        page: this.page - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe(
        (res: HttpResponse<TasksResponse>) => {
          this.queryCount = this.totalItems;
          this.tasks = [];
          res.body.list.entries.forEach((val , idx , array) => {
            this.tasks.push(val.entry);
            console.log('all tasks : ' , this.tasks);
          });
        },
        (res: HttpResponse<any>) => {
          console.log('query all tasks error');
        }
      );
  }


  trackIdentity(index, item: TaskModel) {
    return item.id;
  }
  transition() {
    this.router.navigate(['/task'], {
      queryParams: {
        page: this.page,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    });
  }


}
