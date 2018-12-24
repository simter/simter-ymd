## Rest Interface

| Sn | Url__________________________________| Description
|----|--------------------------------------|-------------
| 1  | /{type}/year                         | Find all years of the specific type
| 2  | /{type}/{year}/month                 | Find all months of the specific type and year
| 3  | /{type}/{year}/{month}/day           | Find all days of the specific type, year and month
| 4  | /{type}/year?with-latest-year-months | Find all years of the specific type, also load all months of the latest year
| 5  | /{type}/year?with-latest-month-days  | Find all years of the specific type, also load all months of the latest year and all days of the latest month

### 1. Find all years of the specific type

**Request：**

```
GET {context-path}/{type}/year
```

| Name | Require | Description
|------|---------|-------------
| type | true    | 

**Response：**

```
200 OK
Content-Type : application/json;charset=utf-8

[yN, ..., y2, y1]
```

> Order by year desc, such as `[2018, ..., 2001, 2000]`. If no years with this type, return `204 No Content`.

### 2. Find all months of the specific type and year

**Request：**

```
GET {context-path}/{type}/{year}/month
```

| Name | Require | Description
|------|---------|-------------
| type | true    | 
| year | true    | 4 digits year, such as 2018

**Response：**

```
200 OK
Content-Type : application/json;charset=utf-8

[mN, ..., m2, m1]
```

> Order by month desc, such as `[12, ..., 2, 1]`. If no months with this type and year, return `204 No Content`.

### 3. Find all days of the specific type, year and month

**Request：**

```
GET {context-path}/{type}/{year}/{month}/day
```

| Name  | Require | Description
|-------|---------|-------------
| type  | true    | 
| year  | true    | 6 digits year, such as 2018
| month | true    | 1 or 2 digits month, such as 1 or 12

**Response：**

```
200 OK
Content-Type : application/json;charset=utf-8

[dN, ..., d2, d1]
```

> Order by day desc, such as `[31, ..., 2, 1]`. If no days with this type and yearMonth, return `204 No Content`.

### 4. Find all years of the specific type, also load all months of the latest year

**Request：**

```
GET {context-path}/{type}/year?with-latest-year-months
```

| Name                    | Require | Description
|-------------------------|---------|-------------
| type                    | true    | 
| with-latest-year-months | false   | given or set to true means also load the latest year's months

**Response：**

```
200 OK
Content-Type : application/json;charset=utf-8

[...]
```

> Order by year desc, month desc. If no years with this type, return `204 No Content`.

Response body example:

```
[
  {
    year: 2018, 
    months: [12, ..., 2, 1]
  },
  ...
]
```

### 5. Find all years of the specific type, also load all months of the latest year and all days of the latest month

**Request：**

```
GET {context-path}/{type}/year?with-latest-month-days
```

| Name                   | Require | Description
|------------------------|---------|-------------
| type                   | true    | 
| with-latest-month-days | false   | given or set to true means load to latest month's days

**Response：**

```
200 OK
Content-Type : application/json;charset=utf-8

[...]
```

> Order by year desc, month desc, day desc. If no years with this type, return `204 No Content`.

Response body example:

```
[
  {
    year: 2018, 
    months: [
      {
        month: 12, 
        days: [31, ..., 2, 1]
      }
    ]
  },
  ...
]
```