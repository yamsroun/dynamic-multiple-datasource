### Spring Boot에서 설정 기반 동적 데이터소스 구성

* `application.yml`의 `spring.datasource.groups` 프로퍼티 항목에 따라 동적으로 데이터소스를 구성함
* 동적으로 데이터소스 그룹 이름 지정 가능
* 데이터소스 그룹을 2가지 타입으로 설정 가능
    * `CLUSTER` : Writer/Reader 데이터소스로 구성되며, `AbstractRoutingDataSource`를 통해 라우팅되도록 구성
    * `SINGLE` : 단일 데이터소스로 구성
* 여러 데이터소스 그룹 중, 우선순위가 가장 놓은 데이터소스 그룹을 프로퍼티에서 지정 가능
* 아래 HikariCP 옵션을 공통 또는 각 데이터소스 그룹에서 지정 가능
    * `minimumIdle`
    * `maximumPoolSize`
* 위 HikariCP 옵션은 각 데이터소스 그룹에 지정된 옵션을 우선 사용하며, 개별 지정되지 않으면 공통 옵션을 사용함

### 동적 프러퍼티 구성 예시

```yaml
spring:
  datasource:
    groups:
      integration:
        type: CLUSTER
        primary: true
        writer:
          jdbc-url: jdbc:h2:mem:integration
          username: sa
          password:
          minimum-idle: 5
          maximum-pool-size: 10
        reader:
          jdbc-url: jdbc:h2:mem:integration
          username: sa
          password:
      etc:
        type: SINGLE
        single:
          jdbc-url: jdbc:h2:mem:etc
          username: sa
          password:
          minimum-idle: 1
    hikari:
      minimum-idle: 25
      maximum-pool-size: 25
```

* `integration` 데이터소스 그룹
    * `CLUSTER` 타입으로 구성
    * `writer`, `reader` 데이터소스가 `readOnly` 속성에 따라 라우팅되어 사용됨
    * `writer` 데이터 소스는 `minimumIdle=5`, `maximumPoolSize=10`으로 구성 (개별 옵션 적용)
    * `reader` 데이터 소스는 `minimumIdle=25`, `maximumPoolSize=25`로 구성 (공통 옵션 적용)
* `etc` 데이터소스 그룹
    * `SINGLE` 타입으로 구성
    * 단일 데이터소스가 사용됨
    * `single` 데이터 소스는 `minimumIdle=1`, `maximumPoolSize=25`으로 구성 (개별/공통 옵션 각각 적용)

### 커넥션 풀 생성 로그 예시

```
## `integration` Reader Connection Pool
integrationReaderPool - Pool stats (total=1, active=0, idle=1, waiting=0)
integrationReaderPool - Added connection conn5: url=jdbc:h2:mem:integration user=SA
integrationReaderPool - After adding stats (total=2, active=0, idle=2, waiting=0)

## `integration` Writer Connection Pool
integrationWriterPool - Pool stats (total=1, active=0, idle=1, waiting=0)
integrationWriterPool - Added connection conn6: url=jdbc:h2:mem:integration user=SA
integrationWriterPool - After adding stats (total=2, active=0, idle=2, waiting=0)

## `etc` Connection Pool
etcPool - Pool stats (total=1, active=0, idle=1, waiting=0)
etcPool - Added connection conn9: url=jdbc:h2:mem:etc user=SA
etcPool - After adding stats (total=2, active=0, idle=2, waiting=0)
```