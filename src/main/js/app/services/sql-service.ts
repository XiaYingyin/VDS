import { Http } from 'angular2/http';
import { Injectable } from 'angular2/core';

@Injectable()
export class SqlService {
    private queryURL: string = 'http://localhost:8080/query/';
    constructor(private _http: Http) { }
    getQueryPlan(query: string) {
        return this._http.get(this.queryURL + query).map(response => response.text());
    }
}
