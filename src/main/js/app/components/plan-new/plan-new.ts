import { Component, OnInit } from 'angular2/core';
import { Router, ROUTER_DIRECTIVES } from 'angular2/router';
import { Http, Response } from 'angular2/http';
import { IPlan } from '../../interfaces/iplan';

import { PlanService } from '../../services/plan-service';
import { Observable } from 'rxjs/Observable';
import { resolve } from 'url';
import { SqlService } from '../../services/sql-service';

@Component({
  selector: 'plan-new',
  templateUrl: './components/plan-new/plan-new.html',
  providers: [PlanService, SqlService],
  directives: [ROUTER_DIRECTIVES]
})

export class PlanNew {
  planIds: string[];
  newPlanName: string;
  newPlanContent: string;
  newPlanQuery: string;
  newPlan: IPlan;
  validationMessage: string;

  constructor(private _router: Router, private _planService: PlanService, private _sqlService: SqlService) { }

  submitPlan() {
    this._sqlService.getQueryPlan(this.newPlanQuery).subscribe(res => {
      this.newPlanContent = res;
      if (this.newPlanContent != null) {
        this.newPlanContent = this.newPlanContent.replace('QUERY PLAN=', '');
        this.newPlanContent = this.newPlanContent.slice(2, this.newPlanContent.length - 2);

        if (!this._planService.isJsonString(this.newPlanContent)) {
          this.validationMessage = 'The string you submitted is not valid JSON'
          return;
        }

        this.newPlan = this._planService.createPlan(this.newPlanName, this.newPlanContent, this.newPlanQuery);
        this._router.navigate(['PlanView', { id: this.newPlan.id }]);
      }
    });
  }
}
