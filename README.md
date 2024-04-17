Open API 활용 Spring project
---
- 특정 지역의 단기예보를 확인할 수 있는 API 개발 프로젝트입니다. 

개발 기간
---
- 1인 개발 (BackEnd)
- 2024.04.12 - 2024.04.18

기술 스택
---
- Java 17
- Spring boot 3.2.4
- Spring Data JPA
- MySQL
- Gradle - Groovy
- Github
- Postman

구현 대상 API
---
- POST 요청시, 공공데이터 포털의 Open API 를 호출한 단기예보의 데이터를 DB에 적재합니다.
- GET 요청시, DB에 저장된 데이터를 조회합니다.
- 데이터가 없을 경우, Http status 204 오류를 응답합니다.

요구사항
---
- 공공데이터 포털의 `기상청_단기예보 ((구)_동네예보) 조회서비스` 를 사용
https://www.data.go.kr/data/15084084/openapi.do
- API 와의 연동은 RestTemplate 사용
- UrlConnection 직접 여는 코드는 사용 X
- 멀티모듈로 구성, 모듈은 최대한 작고 높은 응집도를 보여야 함

Develop
---
- Open API 호출시 응답하는 JSON 데이터를 가공하여(deserialize) Java Object 에 매핑하여 값을 반환하였습니다.
- JSON 데이터의 역직렬화 작업은 JSON 라이브러리를 활용하였습니다.
- 오픈 API 활용가이드를 참고하여 응답 데이터의 Category 명을 해석하고 필드를 생성하여 값을 저장하도록 구현하였습니다.
- csv 파일을 활용하여 도시의 정보가 담긴 샘플데이터를 적재하도록 하였습니다.
- API 테스트는 Postman 을 활용하였습니다.
- apiUrl, SecretKey, DB 등 민감한 정보는 application-secret.properties 파일에 작성했습니다. (gitignore 적용)

프로젝트 실행 주의사항
---
- 실행 시 필요한 데이터는 secret 파일에 작성하였으며, 공공데이터 포털에서 발급받을 수 있습니다.
- 스프링 실행 시 MySQL 의 UTF8 설정이 되지 않은 사용자는 `ddl-auto=create` 설정 시 에러가 발생할 수 있습니다. 샘플 데이터의 한글 삽입이 원인이기 때문에 테이블 생성 후 아래 SQL 문을 실행해주세요.

    `ALTER TABLE town convert to charset UTF8;`
    
    `ALTER TABLE weather convert to charset UTF8;`

- SQL 문 실행 후, `create -> update` 로 수정하고 재실행 해주세요. town 테이블을 조회하면 샘플 데이터가 삽입되어 있습니다.
- 간혹 POST 요청시, 응답하는 API URL 이 올바름에도 불구하고 `SERVICE_KEY_IS_NOT_REGISTERED_ERROR` (등록되지 않은 서비스 키) 에러가 발생할 수 있습니다. 정확한 원인은 알 수 없으나, 이는 네트워크 혹은 개발 환경에 원인이 있을 것으로 추측되며 시스템 혹은 프로그램 재시작을 권장합니다.