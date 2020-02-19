# Java Postgres Explain Visualizer (pev)

Postgres Explain Visualizer (dev) is designed to make [EXPLAIN](http://www.postgresql.org/docs/current/static/sql-explain.html) output easier to grok. It creates a graphical representation of the plan.

The frontend is written in [angular 2](https://angular.io/) with [TypeScript](http://www.typescriptlang.org/). The backend is written with Java spring boot.

## frontend

```
npm install
npm start
```

You may also need to install tsd and compass:

```
npm install tsd -g
gem install compass
```

To build, run the build command for a specific environment. For example, the following will create a production distribution:

```
npm start build.prod
```

## backend

You can use eclipse to run this project.

## Example

You can create a table firstly in your postgresql:

```
CREATE TABLE t_test AS SELECT x, 'a'::char(100) AS y, 'b'::char(100) AS z FROM  generate_series(1, 5000) AS x ORDER BY random();
```

And then run this sql query in jpev:

```
SELECT count(*) FROM t_test, test WHERE t_test.x = test.x and sqrt(t_test.x) > 0 GROUP BY t_test.y;
```

## Initialize

```
create table ext_type (extname name PRIMARY KEY, etype INT NOT NULL);
```