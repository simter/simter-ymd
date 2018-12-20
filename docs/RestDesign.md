## Rest Interface

| Sn | Url__________________________________| Description
|----|--------------------------------------|-------------
| 1  | /{type}/year                         | Find all years of the specific type
| 2  | /{type}/month?y=2018                 | Find all months of the specific type and year
| 3  | /{type}/day?ym=201801                | Find all days of the specific type, year and month
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
GET {context-path}/{type}/month?y=2018
```

| Name | Require | Description
|------|---------|-------------
| type | true    | 
| y    | true    | 4 digits year, such as 2018

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
GET {context-path}/{type}/day?ym=201801
```

| Name | Require | Description
|------|---------|-------------
| type | true    | 
| ym   | true    | 6 digits yearMonth, such as 201801

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