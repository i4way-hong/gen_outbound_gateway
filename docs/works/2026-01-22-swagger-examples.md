# 2026-01-22 Swagger 응답 예시 및 DTO Payload 정리

## 공통 응답 예시 (ApiResponse)

### 성공
```json
{"success":true,"message":"요청 성공","data":{},"timestamp":"2026-01-22T10:00:00+09:00"}
```

### 생성 성공
```json
{"success":true,"message":"생성 성공","data":{},"timestamp":"2026-01-22T10:00:00+09:00"}
```

### 오류 (400)
```json
{"success":false,"message":"요청 값이 올바르지 않습니다.","data":null,"timestamp":"2026-01-22T10:00:00+09:00"}
```

### 오류 (409)
```json
{"success":false,"message":"이미 존재합니다.","data":null,"timestamp":"2026-01-22T10:00:00+09:00"}
```

### 오류 (500)
```json
{"success":false,"message":"서버 오류가 발생했습니다.","data":null,"timestamp":"2026-01-22T10:00:00+09:00"}
```

### 오류 (503)
```json
{"success":false,"message":"연동 실패","data":null,"timestamp":"2026-01-22T10:00:00+09:00"}
```

## DTO 예시 Payload

### PersonRequest
```json
{
  "tenantDbid": 101,
  "employeeId": "E1001",
  "userName": "e1001",
  "firstName": "길동",
  "lastName": "홍",
  "agentLoginId": "1001",
  "agent": true,
  "enabled": true,
  "agentGroupNames": ["GROUP_A", "GROUP_B"]
}
```

### AgentGroupRequest
```json
{
  "tenantDbid": 101,
  "name": "GROUP_A",
  "enabled": true
}
```

### AgentLoginRequest
```json
{
  "tenantDbid": 101,
  "switchDbid": 101,
  "loginCode": "1001",
  "enabled": true
}
```

### FilterRequest
```json
{
  "tenantDbid": 101,
  "name": "FILTER_A",
  "description": "설명",
  "formatDbid": 2001,
  "userProperties": {
    "default": {
      "criteria": "KCC_CAMPAIGN_NO = \"12345\"",
      "order_by": ""
    }
  },
  "enabled": true
}
```

### FilterSummary
```json
{
  "dbid": 4001,
  "name": "FILTER_A",
  "description": "설명",
  "enabled": true,
  "formatDbid": 2001,
  "formatName": "FORMAT_B",
  "userProperties": {
    "default": {
      "criteria": "KCC_CAMPAIGN_NO = \"12345\"",
      "order_by": ""
    }
  }
}
```

### FormatSummary
```json
{
  "dbid": 12001,
  "name": "FORMAT_A",
  "description": "Format 설명",
  "enabled": true,
  "tenantDbid": 101,
  "fields": [
    {
      "dbid": 21001,
      "name": "FIELD_A",
      "fieldType": "CFGFieldTypeStandard",
      "description": "Field 설명",
      "description": "Field 설명"
    }
  ],
  "userProperties": {
    "Section": {
      "Key": "Value"
    }
  }
}
```

### CallingListDetailRequest
```json
{
  "tenantDbid": 101,
  "name": "LIST_A",
  "description": "콜링리스트 설명",
  "filterDbid": 4001,
  "logTableAccessDbid": 3001,
  "maxAttempts": 3,
  "scriptDbid": 2001,
  "tableAccessDbid": 1001,
  "timeFrom": 800,
  "timeTo": 1800,
  "enabled": true,
  "treatmentDbids": [100, 200],
  "userProperties": {
    "OCServer": {
      "CPNDigits": "0311234567"
    }
  }
}
```

### CampaignRequest
```json
{
  "tenantDbid": 101,
  "name": "CMP_A",
  "description": "캠페인 설명",
  "scriptDbid": 2001,
  "callingListNames": ["LIST_A", "LIST_B"],
  "userProperties": {
    "OCServer": {
      "CPNDigits": "0311234567"
    },
    "NewProperty": {
      "ExampleKey": "ExampleValue"
    }
  },
  "enabled": true
}
```

### CampaignGroupRequest
```json
{
  "tenantDbid": 101,
  "campaignDbid": 7001,
  "groupDbid": 8001,
  "groupType": "CFGGroup",
  "name": "GROUP_A",
  "description": "그룹 설명",
  "dialMode": "Predictive",
  "operationMode": "Basic",
  "numOfChannels": 30,
  "optMethod": "Availability",
  "optMethodValue": 0,
  "minRecBuffSize": 0,
  "optRecBuffSize": 0,
  "origDnDbid": 9001,
  "trunkGroupDnDbid": 9002,
  "scriptDbid": 2001,
  "interactionQueueDbid": 2101,
  "ivrProfileDbid": 2201,
  "serverDbids": [3001, 3002],
  "userProperties": {
    "OCServer": {
      "CampaignType": "VOICE"
    },
    "NewProperty": {
      "ExampleKey": "ExampleValue"
    }
  },
  "enabled": true
}
```

### CampaignSummary
```json
{
  "dbid": 7001,
  "name": "CMP_A",
  "description": "캠페인 설명",
  "enabled": true,
  "tenantDbid": 101,
  "scriptDbid": 2001,
  "state": "CFGEnabled",
  "callingLists": [
    {
      "dbid": 3001,
      "name": "LIST_A",
      "description": "콜링리스트 설명",
      "filterDbid": 4001,
      "logTableAccessDbid": 3001,
      "maxAttempts": 3,
      "scriptDbid": 2001,
      "tableAccessDbid": 1001,
      "timeFrom": 800,
      "timeTo": 1800,
      "enabled": true,
      "treatmentDbids": [100, 200],
      "userProperties": {
        "OCServer": {
          "CPNDigits": "0311234567"
        }
      }
    }
  ],
  "userProperties": {
    "OCServer": {
      "Description": "default"
    }
  },
  "campaignGroups": [
    {
      "dbid": 6001,
      "name": "GROUP_A",
      "enabled": true,
      "tenantDbid": 101,
  "campaignDbid": 7001,
  "groupDbid": 8001,
  "groupType": "CFGGroup",
      "description": "그룹 설명",
      "state": "CFGEnabled",
      "dialMode": "Predictive",
      "operationMode": "Basic",
      "numOfChannels": 30,
      "optMethod": "Availability",
      "optMethodValue": 0,
  "minRecBuffSize": 0,
  "optRecBuffSize": 0,
      "origDnDbid": 9001,
      "trunkGroupDnDbid": 9002,
      "scriptDbid": 2001,
      "interactionQueueDbid": 2101,
      "ivrProfileDbid": 2201,
      "servers": [
        {"dbid": 3001, "name": "OCS_A"},
        {"dbid": 3002, "name": "OCS_B"}
      ],
      "origDnNumber": "1001",
      "trunkGroupDnNumber": "9000",
      "userProperties": {
        "OCServer": {
          "CampaignType": "VOICE"
        }
      }
    }
  ]
}
```

### CampaignGroupSummary
```json
{
  "dbid": 6001,
  "name": "GROUP_A",
  "enabled": true,
  "tenantDbid": 101,
  "campaignDbid": 7001,
  "groupDbid": 8001,
  "groupType": "CFGGroup",
  "description": "그룹 설명",
  "state": "CFGEnabled",
  "dialMode": "Predictive",
  "operationMode": "Basic",
  "numOfChannels": 30,
  "optMethod": "Availability",
  "optMethodValue": 0,
  "minRecBuffSize": 0,
  "optRecBuffSize": 0,
  "origDnDbid": 9001,
  "trunkGroupDnDbid": 9002,
  "scriptDbid": 2001,
  "interactionQueueDbid": 2101,
  "ivrProfileDbid": 2201,
  "servers": [
    {"dbid": 3001, "name": "OCS_A"},
    {"dbid": 3002, "name": "OCS_B"}
  ],
  "origDnNumber": "1001",
  "trunkGroupDnNumber": "9000",
  "userProperties": {
    "OCServer": {
      "CampaignType": "VOICE"
    }
  }
}
```

### TableAccessSummary
```json
{
  "dbid": 13001,
  "name": "TABLE_A",
  "tenantDbid": 101,
  "description": "TableAccess 설명",
  "type": "CFGTableAccess",
  "dbAccessDbid": 11001,
  "dbAccessName": "DB_ACCESS_A",
  "formatDbid": 12001,
  "formatName": "FORMAT_A",
  "dbTableName": "TABLE_NAME",
  "isCachable": "CFGTrue",
  "updateTimeout": 30,
  "state": "CFGEnabled",
  "userProperties": {
    "OCServer": {
      "ExampleKey": "ExampleValue"
    }
  }
}
```

### TableAccessSummary (By DBID)
```json
{
  "dbid": 13001,
  "name": "TABLE_A",
  "tenantDbid": 101,
  "description": "TableAccess 설명",
  "type": "CFGTableAccess",
  "dbAccessDbid": 11001,
  "dbAccessName": "DB_ACCESS_A",
  "formatDbid": 12001,
  "formatName": "FORMAT_A",
  "dbTableName": "TABLE_NAME",
  "isCachable": "CFGTrue",
  "updateTimeout": 30,
  "state": "CFGEnabled",
  "userProperties": {
    "OCServer": {
      "ExampleKey": "ExampleValue"
    }
  }
}
```

### DnRequest
```json
{
  "tenantDbid": 101,
  "switchDbid": 101,
  "number": "1001",
  "name": "DN_A",
  "type": "Agent",
  "registerFlag": "CFGRegisterAll",
  "routeType": "Default",
  "switchSpecificType": 0,
  "trunks": 0,
  "enabled": true
}
```

### TransactionRequest
```json
{
  "tenantDbid": 101,
  "name": "TRX_A",
  "alias": "TRX_ALIAS",
  "type": "Transaction",
  "enabled": true
}
```

### PlaceRequest
```json
{
  "tenantDbid": 101,
  "switchDbid": 101,
  "name": "PLACE_A",
  "dnNumbers": ["1001", "1002"]
}
```

### OutboundCommandRequest
```json
{
  "campaignDbid": 7001,
  "groupDbid": 6001
}
```

### OutboundDialRequest
```json
{
  "campaignDbid": 7001,
  "groupDbid": 6001,
  "dialMode": "Predictive",
  "optimizeMethod": "Availability",
  "optimizeGoal": 0
}
```
