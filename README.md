# REST-API with Spring - 백기선님 인프런 강의 


스프링 기반 REST API 개발
=========================

강좌 소개
---------

-	REST API라고 부르려면 Self-Describtive Message 와 HATEOAS 를 만족해야함.

-	두 가지를 만족해야만 진화할 수 있음(서버와 클라이언트가 독립적으로 진화할 수 있는) -> 이것이 본질

-	이벤트라는 API (모임 생성, 목록 조회 등) 개발 예정

-	인텔리제이 단축키, junit, TDD

-	*무엇을 하려는가?* 에 집중할 것

---

REST API 및 프로젝트 소개 - REST API
------------------------------------

-	API : Application Programming Interface

-	REST

	-	REpresentional State Transfer
	-	인터넷 상의 시스템 간의 상호운용성(Interoperability)을 제공하는 방법 중 하나
	-	시스템 제각각의 **독립적인** 진화를 보장하기 위한 방법
	-	REST API : REST 아키텍쳐 스타일을 따르는 API

-	REST 아키텍쳐 스타일 (유투브 : 그런 RSET API로 괜찮은가 -> 볼 것!!)

	-	Client-Server
	-	Cache
	-	Uniform Interface
	-	Layerd System
	-	Code-On-Demand (optional)

-	Uniform Interface

	-	Indentification of resources
	-	manipulation of resource through represenations
	-	self-descriptive message
	-	hypermisa as the engine of application state (HATEOAS)

-	두 문제를 좀 더 자세히 살펴보자

	-	Self-descriptive meesge

		-	메시지 스스로 메시지에 대한 설명이 가능해야한다.
		-	서버가 변해서 메시지가 변해도 클라이언트는 그 메시지를 보고 해석이 가능하다.
		-	확장 가능한 커뮤니케이션

	-	HATEOAS

		-	하이퍼미디어(링크)를 통해 애플리케이션 상태 변화가 가능해야한다.

		-	링크 정보를 동적으로 바꿀 수 있다.(Versioning 할 필요 없이!)

		-	(응답을 받은 다음에 다음애플리케이션 상태로 전이를 하려면 서버가 보내준 응답에 들어있는 링크정보를 사용해서 이동을 해야한다.)

-	Self-descriptive message 해결방법

	-	방법 1: 미디어 타입을 정의하고 IANA에 등록하고 그 미디어 타입을 리소스 리턴할 때 Content-Type으로 사용한다.

	-	방법 2: profile 링크 헤더를 추가한다. (응답에 대한 스펙정보 링크)

		-	브라우저들이 아직 스팩지원을 잘안해
		-	대안으로 HAL의 링크 데이터에 profile 링크 추가(강의에서 사용예정)

-	HATEOAS 해결방법

	-	방법1: 데이터에 링크제공

		-	링크를 어떻게 정의할 것인가? HAL(강의에서 사용예정)

	-	방법2: 링크 헤더나 Location을 제공

---

### REST API 및 프로젝트 소개 - Event REST API

-	이벤트 등록, 조회 및 수정 API

-	GET /api/events

-	이벤트 목록 조회 REST API (로그인 안 한 상태)

	-	응답에 보여줘야할 데이터

		-	이벤트 목록
		-	링크

			-	self
			-	profile : 이벤트 목록 조회 API 문서로 링크
			-	get-an-event : 이벤트 하나 조회하는 API 링크
			-	next : 다음 페이지 (optional)
			-	prev : 이전 페이지 (optional)

	-	문서 ?

		-	스프링 REST Docs로 만들 예정

-	이벤트 목록 조회 REST API (로그인 한 상태)

	-	응답에 보여줘야할 데이터

	-	이벤트 목록

	-	링크

		-	self
		-	profile : 이벤트 목록 조회 API 문서로 링크
		-	get-an-event : 이벤트 하나 조회하는 API 링크
		-	create-new-event : 이벤트를 생성할 수 있는 API 링크
		-	next : 다음 페이지 (optional)
		-	prev : 이전 페이지 (optional)

	-	로그인 한 상태 ??? (stateless 라며...)

		-	아니, 사실은 Bearer 헤더에 유효한 AccessToken이 들어있는 경우! (세션정보 사용X)

-	POST /api/events

	-	이벤트 생성

-	GET /api/events/{id}

	-	이벤트 하나 조회

-	PUT /api/events/{id}

	-	이벤트 수정

---

### REST API 및 프로젝트 소개 - Evens API 사용 예제

1.	(토큰 없이) 이벤트 목록 조회

	-	a. create 안보임

2.	access token 발급 받기 (A 사용자 로그인)

3.	(유효한 A토큰 가지고) 이벤트 목록 조회

	-	a. create event 보임

4.	(유효한 A토큰 가지고) 이벤트 만들기

5.	(토큰없이) 이벤트조회

	-	a. update 링크 안보임

6.	(유효한 A토큰 가지고) 이벤트 조회

	-	a. update 링크 보임

7.	access token 발급 받기(B 사용자 로그인)

8.	(유효한 B토큰 가지고) 이벤트 조회

	-	a. update 안보임

9.	REST API 테스트 클라이언트 애플리케이션

	-	크롬 플러그인
		-	Restlet
	-	애플리케이션
		-	Postman

---

### REST API 및 프로젝트 소개 - Project 만들기

-	추가할 의존성

	-	Web
	-	JPA
	-	REST Docs
	-	H2
	-	PostgreSQL
	-	Lombok

-	자바 버전 11로 시작

-	스프링 부트 핵심 원리

	-	의존성 설정(pom.xml)
	-	자동설정(@EnableAutoConfiguration)
	-	내장 웹 서버(의존성과 자동 설정의 일부)
	-	독립적으로 실행 가능한 JAR (pom.xml의 플러그인)

```
  인텔리제이 - new project

  얼티메이트 버전은 spring initalizr 가 있음   

  얼티메이트 버전이 아니면 spring.io 에서 프로젝트를 생성해서 다운받을 수 있음(war)

  자바11 (집에서 11설치하고 다시 만들어볼 것)
  의존성 추가 (기본의존성이 다른데 이유는??)

  h2, postgresql 스코프변경함

  <optional> 이 프로젝트를 참조하고 있는 다르프로젝트에 자동으로 추가되지 않음

```

---

### REST API 및 프로젝트 소개 - 이벤트 비즈니스 로직

-	Event 생성 API

	-	다음의 입력 값을 받는다.

		-	name

		-	description

		-	beginEnrollmentDateTime

		-	closeEnrollmentDateTime

		-	beginEventDateTime

		-	endEventDateTime

		-	location(optional) 이게 없으면 온라인 모임

		-	basePrice(optional)

		-	maxPrice(optional)

		-	limitOfEnrollment

	-	basePrice와 maxPrice 경우의 수와 각각의 로직

		```
		    0, 100 - 선착순 등록
		    0, 0 - 무료
		    100, 0 - 무제한 경매(높은 금액 낸 사람이 등록)
		    100, 200 - 제한가 선착순 등록 ( 처음부터 200을 낸사람은 선등록, 100을 내고 등록할 수 있으나 더 많이 낸 사람에 의해 밀려날 수 있음.)
		```

	-	결과 값

		-	id

		-	name

		-	...

		-	eventStatus : DRAFT, PUBLISHED, ENROLLMENT_STARTED, ..

		-	offline

		-	links

			-	profile (for the self_descriptive message)
			-	self
			-	publish
			-	...

---

### REST API 및 프로젝트 소개 - 이벤트 도메인 구현

-	이벤트 도메인
-	EventStatus 이늄
-	롬북 애노테이션

	-	왜 @EqualsAndHasCode 에서 of를 사용하는가
		-	EualsAndHasCode를 구현할 때 모든 필드를 사용하게됨. 앤티티간 연관관계가 있을 때 서로 상호참조할 경우 스택오브플로우 발생할 수있음.
	-	왜 @Builder를 사용할 때 @AllArgConstructor가 필요한가
		-	builder만 추가하면 기본생성자가 생성이 안됨.(NoArg, AllArg 기본생성자가 모든 아규먼트가 있는 생성자를 만들기위해) 그렇기 때문에 추가
	-	@Data를 쓰지 않는 이유
		-	앤티티에는 안씀. 상호참조 때문에 스택오버플로우가 발생할 수있음
	-	애노테이션 줄일 수 없나

		-	롬북은 안됨. 메타애노테이션으로 동작하지 않기 때문에 구조상 현재로서는 안됨.

	-	참조

		-	https://www.popit.kr/%EC%8B%A4%EB%AC%B4%EC%97%90%EC%84%9C-lombok-%EC%82%AC%EC%9A%A9%EB%B2%95/

```
클래스를 확인해보면 빌더가 생성해준 코드를 직접 볼 수 있음.

단축키

command shift T : Create Test

option control O : 옵티마이즈 임포트 (사용하지 import 삭제하는 듯)

control shift R : 메서드 실행

alt command V : 리펙토링 (중복코드 리펙토링해줌 )

```

---

### 이벤트 생성 API 개발 - 이벤트 API 테스트 클래스 생성

-	스프링 부트 슬라이스 ㅔㅌ스트

	-	@WebMvcTest
		-	MockMvc 빈을 자동 설정해준다. 따라서 그냥 가져와서 쓰면 됨.
		-	웹 관련 빈만 등록해준다.(슬라이스)

-	MockMvc

	-	스프링 MVC 테스트 핵심 클래스
	-	웹서버를 띄우지 않고도 스프링 MVC(DispatcherServlet)가 요청을 처리하는 과정을 확인할 수 있기 때문에 컨트롤러 테스트용으로 자주 쓰임.

-	테스트 할 것

	-	입력값들을 전달하면 JSON응답으로 201이 나오는 지 확인.

		-	Location 헤더에 생성된 이벤트를 조회할 수 있는 URI 담겨있는 지 확인
		-	id는 DB에 들어갈 때 자동생성된 값으로 나오는 지 확인

	-	입력값으로 누가 id나 eventStatus, offline, free 이런 데이터까지 같이주면?

		-	Bad_Request로 응답 vs 받기로 한 값 이외는 무시

	-	입력 데이터가 이상한 경우 Bad_Request로 응답

		-	입력값이 이상한 경우 에러
		-	비지니스 로직으로 검사할 수 있는 에러
		-	에러 응답 메시지에 에러에 대한 정보가 있어야한다.

	-	비즈니스 로직 적용 됐는지 응답메시지 확인

		-	offline과 free 값 확인

	-	응답에 HATEOA 와 profile 관련 링크가 있는 지 확인

		-	self(view)
		-	update(만든 사람은 수정할 수 있으니까)
		-	events(목록으로 가는 링크)

	-	API 문서만들기

		-	요청 문서화
		-	응답 문서화
		-	링크 문서화
		-	profile 링크 추가

---

### 이벤트 생성 API 개발 - 201 응답받기

-	@RestController

	-	@ResponseBody를 모든 메소드에 적용한 것과 동일하다.

-	ResponseEntity를 사용하는 이유

	-	응답코드, 헤더, 본문 모두 다루기 편한 API

-	Location URI 만들기

	-	HATEOS가 제공하는 lineTo(), methodOn() 사용

-	객체를 JSON으로 변환

	-	ObjectMapper 사용

-	테스트할 것

	-	입력값들을 전달하면 JSON응답으로 201이 나오는 지 확인.

		-	Location 헤더에 생성된 이벤트를 조회할 수 있는 URI 담겨있는 지 확인

---

### 이벤트 생성 API 개발 - EventRepository 구현

-	스프링 데이터 JPA

	-	JpaRepository 상속받아 만들기

-	Enum을 JPA 맵핑 시 주의할 것

	-	@Enumerated(EnumType.STRING)

-	@MockBean

	-	Mockito을 사용해서 mock 객체를 민들고 빈으로 등록해 줌.
	-	(주의) 기존 빈을 테스트용 빈이 대체한다.

-	테스트할 것

	-	입력값들을 전달하면 JSON응답으로 201이 나오는 지 확인.

		-	id는 DB에 들어갈 때 자동생성된 값으로 나오는 지 확인

---

### 이벤트 생성 API 개발 - 입력값 제한하기

-	입력 값 제한

	-	id 또는 입력 받은 데이터로 계산해야 하는 값들은 입력을 받지 않아야 한다.
	-	강의에서는 EventDto을 사용하여 해결 (dto에 명시된 파라미터만 받음 ) (애노테이션도 동일한 효과 가능함)

-	DTO -> 도메인 객체로 값 복사

	-	ModelMapper

-	통합 테스트로 전환

	-	@WebMvcTest 빼고 다음 애노테이션 추가 - @SpringBootTest - @AutoConfigureMockMvc

	-	Repositody @MockBean 코드 제거

-	테스트할 것

	-	입력값으로 누가 id나 eventStatus, offine, free 이런 데이터까지 같이 주면?
		-	Bad_Request로 응답 vs **받기로 한 값 이외는 무시**

---

### 이벤트 생성 API 개발 - 입력값 이외에 에러 발생

-	ObjectMapper 커스터마이징

	-	spring.jackson.deserialization.fail-on-unknown-properties=true

	(unknown 프로퍼티가 있으면 실패하라)(deserialization json문자열을 객체로 반대는 serialization)

-	테스트할 것

	-	입력값으로 누가 id나 eventStatus, offine, free 이런 데이터까지 같이 주면?
		-	**Bad_Request로 응답** vs 받기로 한 값 이외는 무시

---

### 이벤트 생성 API 개발 - Bad Request 처리하기

```
control shift r : 실행 (특정 메소드안이면 해당 메소드만 실행)
control command p : 어떤 파라미터를 입력할 차례인지 보여줌  
```

-	@Valid와 BindingResult(또는 Errors)

	-	BindingResult는 항상 @Valid 바로 다음 인자로 사용해야함(스프링 MVC)
	-	@NotNull, @NotEmpty, @Min, @Max, ... 사용해서 입력 값 바인딩할 때 에러 확인할 수 있음.

-	도메인 Validator 만들기

	-	Validator 인터페이스 없이 만들어도 상관없음

-	테스트 설명용 애노테이션 만들기 (주석도 좋은방법임. junit5로 작성하면 생성한 어노테이션이름으로 나오게 할수 있음)

	-	@Target, @Retention (애노테이션 유지시간)

-	테스트할 것

	-	입력 데이터가 이상한 경우 Bad_Request로 응답

		-	**입력값이 이상한 경우 에러**
		-	**비즈니스 로직으로 검사 할 수 있는 에러**
		-	에러 응답 메시지에 에러에 대한 정보가 있어야한다.

---

### 이벤트 생성 API 개발 - Bad Request 응답 본문 만들기

-	커스텀 JSON Serializer 만들기

	-	extends JsonSerializer<T> (Jackson JSON 제공)
	-	@JsonComponent (스프링 부트 제공)

-	BindingError

	-	FieldError 와 GlobalError (ObjectError)가 있음
	-	objectName
	-	defaultMessage
	-	code
	-	field
	-	rejectedValue

-	테스트할 것

	-	입력 데이터가 이상한 경우 Bad_Request로 응답

		-	입력값이 이상한 경우 에러
		-	비즈니스 로직으로 검사 할 수 있는 에러
		-	**에러 응답 메시지에 에러에 대한 정보가 있어야한다.**

---

### 이벤트 생성 API 개발 - 비즈니스 로직 적용

-	테스트할 것
	-	비즈니스 로직 적용 됐는지 응답 메시지 확인
		-	offine 과 free 값 확인

---

### 이벤트 생성 API 개발 - 매개변수를 이용한 테스트

-	테스트코드 리팩토링

	-	테스트에서 중복 코드 제거
	-	매개변수만 바꿀 수 있으면 좋겠는데?
	-	JunitParams

-	JunitParams

	-	https://github.com/Pragmatists/JUnitParams

---

### HATEOAS 와 Self-Describtive Message 적용 - 스프링 HATEOAS 소개

-	스프링 HATEOAS

	-	https://docs.spring.io/spring-hateoas/docs/current/reference/html/
	-	링크 만드는 기능

		-	문자열 가지고 놀기
		-	컨트롤러와 메소드로 만들기

	-	리소스 만드는 기능

		-	리소스: 데이터 + 링크

	-	링크 찾아주는 기능 (이번 강좌에서는 생략)

		-	Traverson
		-	LinkDiscoverers

	-	링크

		-	HREF
		-	REL (현재 릴레이션과의 관계)
			-	self
			-	profile
			-	update-event
			-	query-events

---

### HATEOAS 와 Self-Describtive Message 적용 - 스프링 HATEOAS 적용

-	EventResource 만들기

	-	extends ResourceSupport의 문제
		-	@JsonUnwrapped 로 해결
		-	extends Resource<T>로 해결

-	테스트할 것

	-	응답에 HATEOAS와 profile 관련 링크가 있는지 확인
		-	self(view)
		-	update(만든사람은 수정할 수 있으니까)
		-	events (목록으로 가는 링크)

---

### HATEOAS 와 Self-Describtive Message 적용 - 스프링 REST Docs 소개

-	(스웨거? 라는것도 있다고함. 애노테이션을 사용. 이번 강좌는 rest docs만 소개)

-	REST Docs 코딩

	-	andDo(document("doc-name", snippets))
	-	snippets

		-	link()
		-	requestParameters() + parameterWithName()
		-	pathParameters() + parametersWithName()
		-	requestParts() + partWithName()
		-	requestPartBody()
		-	requestPartField()
		-	requestHeaders() + headerWithName()
		-	requestField() + headerWithName()
		-	responseFields() + filedWithPath()
		-	...

	-	Relaxed\*

	-	Processor

		-	preprocessRequest(prettyPrint())
		-	preprocessResponse(prettyPrint())
		-	...

-	Constraint

	-	https://github.com/spring-projects/spring-restdocs/blob/v2.0.2.RELEASE/samples/res t-notes-spring-hateoas/src/test/java/com/example/notes/ApiDocumentation.java

---

### HATEOAS 와 Self-Describtive Message 적용 - 스프링 REST Docs 적용

-	REST Docs 자동설정

	-	@AutoConfigureRestDocs
		-	자동으로 생성됨 (target/generated-snippets/)

-	RestDocsMockMvc 커스터마이징

	-	RestDocsMockMvcConfigurationCustomizer 구현한 빈 등록
	-	@TestConfiguration

-	테스트할 것

	-	API 문서만들기
		-	**요청 본문 문서화**
		-	**응답 본문 문서화**
		-	링크 문서화
			-	profile 링크 추가
		-	응답헤더 문서화

---

### HATEOAS 와 Self-Describtive Message 적용 - 스프링 REST Docs 각종 문서 조각 생성하기 (링크, Req, Res 필드와 헤더)

-	요청 필드 문서화

	-	requestFields() + fieldWithPath()
	-	responseFields() + fieldWithPath()
	-	requestHeaders() + headerWithName()
	-	responseHeaders() + headerWithName()
	-	links() + linkWithRel()

-	테스트할 것

	-	API 문서만들기
		-	요청 본문 문서화
		-	응답 본문 문서화
		-	**링크 문서화**
			-	self
			-	query-events
			-	update-event
			-	profile 링크 추가
		-	**요청헤더 문서화**
		-	**요청필드 문서화**
		-	**응답헤더 문서화**
		-	**응답필드 문서화**

-	Relaxed 접두어 (relaxedResponseFields )

	-	장점 : 문서 일부분만 테스트 할 수 있다.
	-	단점 : 정확한 문서를 생성하지 못한다.

---

### HATEOAS 와 Self-Describtive Message 적용 - 스프링 REST Docs 문서 빌드

-	스프링 REST Docs

	-	스프링 REST Docs

		-	https://docs.spring.io/spring-restdocs/docs/2.0.2.RELEASE/reference/html5/
		-	pom.xml에 메이븐 플러그인 설정

		-	템플릿 파일 추가

			-	src/main/asciidoc/index.adoc

	-	문서 생성하기

		-	mvn package

			-	test
			-	prepare-package :: process-asciidoc
			-	prepare-package :: copy-resources

		-	문서 확인

			-	/docs/index.html

---

### HATEOAS 와 Self-Describtive Message 적용 - PostgrSQL 적용

-	테스트 할 때는 계속 H2를 사용해도 좋지만 애플리케이션 서버를 실행할 때 PostgreSQL을 사용하도록 변경하자.

-	/script.md 참고

-	PostgreSQL 드라이버 의존성 추가

-	도커로 PostgreSQL 컨테이너 실행

-	도커 컨테이너에 들어가보기

-	데이터소스 설정

-	하이버네이트 설정

-	애플리케이션 설정과 테스트 설정 중복 어떻게 줄일 것인가?

	-	프로파일과 @ActiveProfiles 활용

---

### HATEOAS 와 Self-Describtive Message 적용 - API 인덱스 만들기

-	인덱스 핸들러

	-	다른 리소스에 대한 링크 제공
	-	문서화

-	테스트 컨트롤러 리팩토링

	-	중복 코드 제거

-	에러 리소스

	-	인덱스로 가는 링크 제공

---

### 이벤트 조회 및 수정 REST 개발 - 이벤트 목록 조회 API 구현

```
option command V : Extract Variable
```

-	페이징, 정렬 어떻게 하지?

	-	스프링 데이터 JPA가 제공하는 Pageable

-	Page<Event>에 안에 들어있는 Event들은 리소스로 어떻게 변경할까?

	-	하나씩 순회하면서 직접 EventResource로 맵핑시킬까...
	-	PagedResourceAssembler<T> 사용하기

-	테스트할 때 Pageable 파라미터 제공하는 방법

	-	page: 0부터 시작
	-	size: 기본 값 20
	-	sort: property, property(ASC | DESC)

-	테스트할 것

	-	Event 목록 Page 정보와 함께 받기

		-	content[0].id 확인
		-	pagealbe 경로 확인

	-	Sort와 Paging 확인

		-	30개를 만들고, 10개 사이즈로 두번째 페이지 조회하면 이전, 다음페이지로 가는 링크가 있어야한다.
		-	이벤트 이름순으로 정렬하기
		-	page 관련 링크

	-	Event를 EventResource로 변환해서 받기

		-	각 이벤트마다 self

	-	링크 확인

		-	self
		-	profile
		-	(create)

	-	문서화

---

### 이벤트 조회 및 수정 REST 개발 - Event 조회 API

-	테스트 할 것

	-	조회하는 이벤트가 있는 경우 이벤트 리소스 확인

		-	링크
			-	self
			-	profile
			-	(update)
		-	이벤트 데이터

	-	조회하는 이벤트가 없는 경우 404 응답 확인

---

### 이벤트 조회 및 수정 REST 개발 - Event 수정 API

-	테스트 할 것
	-	수정하려는 이벤트가 업슨ㄴ 경우 404 NOT_FOUND
	-	입력 데이터 (데이터 바인딩)가 이상한 경우 400 BAD_REQUEST
	-	도메인 로직으로 데이터 검증 실패하면 400 BAD_REQUEST
	-	권한이 충분하지 않은 경우에 403 FORBIDDEN
	-	정상적으로 수정한 경우에 이벤트리소스 응답
		-	200OK
		-	링크
		-	수정한 이벤트 데이터

---

### 이벤트 조회 및 수정 REST 개발 - 테스트 코드 리펙토링

-	- 클래스 상속을 사용하는 방법
	-	@Ignore 애노테이션으로 테스트로 간주되지 않도록 설정

```
control + option + O : import 정리

```

---

### REST API 보안 적용 - Account 도메인 추가

-	OAuth2로 인증응 하려면 일단 Account 부터

	-	id  
	-	email
	-	password
	-	roles

-	AccountRoles

	-	ADMIN, USER

-	JPA 맵핑

	-	@Table("Users")

---

### REST API 보안 적용 - 스프링 시큐리티

-	스프링 시큐리티

	-	웹 시큐리티(Filter 기반 시큐리티)
	-	메소드 시큐리티 (aop)
	-	이 둘다 Security Interceptor를 사용
		-	리소스에 접근을 허용할 것이냐 말것이냐를 결정하는 로직이 들어있음.

-	스프링 시큐리티가 정의해놓은 인터페이스로 변환하는 작업함.

---

### REST API 보안 적용 - 예외 테스트

-	@Test(expected)

	-	예외 타입만 확인 가능

-	try-catch

	-	예외타입과 메시지 확인 가능
	-	하지만 코드가 다시 복잡

-	@Rule ExpectedException

	-	코드는 간결하면서 예외타입과 메시지 모두 확인 가능

---

### REST API 보안 적용 - 스프링 시큐리티 기본 설정

-	시큐리티 필터를 적용하지 않음

	-	/docs/index.html

-	로그인 없이 접근 가능

	-	GET /api/events
	-	GET /api/events/{id}

-	로그인 해야 접근 가능

	-	나머지 모두!
	-	POST /api/events
	-	POST /api/events/{id}
	-	...

-	스프링 시큐리티 OAuth 2.0

	-	AuthorizationServer : OAuth2 로 토큰 발행(/oauth/token) 및 토큰 인증(/oauth/authorize)

		-	Oder 0 (리소스 서버보다 우선 순위가 높다.)

	-	ResourceServer : 리소스 요청 인증 처리 (OAuth2 토큰 검사)

		-	Oder 3 (이 값은 현재 고칠 수 없음)

-	스프링 시큐리티 설정

	-	@EnableWebSercurity
	-	@EnableGlobalMethodSecurity
	-	extends WebSecurityConfigureAdapter
	-	PasswordEncoder : PasswordEncoderFactories.createDelegatingPasswrodEncoder()
	-	TokenStore : InMemoryTokenStore
	-	AuthenticationManagerBean
	-	configure(AuthenticationManagerBuilder auth)
		-	userDetailsService
		-	passwordEncoder
	-	configure(HttpSecurity http)
		-	/docs/\** : permitAll
	-	configure(WebSecurity web)

		-	ignore

			-	/docs/\*\*
			-	/favicon.ico

		-	PathRequest.toStaticResources() 사용하기

---

### REST API 보안 적용 - 스프링 시큐리티 폼 인증 설정

-	익명사용자 사용 활성화
-	폼 인증 방식 활성화

	-	스프링 시큐리티가 기본 로그인 페이지 제공

-	요청에 인증 적용

	-	/api 이하 모든GET 요청에 인증이 필요함. (permitALl()을 사용하여 인증이 필요없이 익명으로 접근이 가능하게 할 수 있음)

	-	그밖에 모든 요청도 인증이 필요함.

---

### REST API 보안 적용 - 인증 서버 설정

-	토큰 발행 테스트

	-	User
	-	Client
	-	POST /oauth/token
		-	HTTP Basic 인증 헤더(클라이언트 아이디 + 클라이언트 시크릿)
		-	요청 매개변수 ( MultiValueMap<String, String>\)
			-	grant_type : password
			-	username
			-	password
		-	응답에 access_token 나오는지 확인

-	Grant Type : Password

	-	Granty Type : 토큰 받아오는 방법
	-	서비스 오너가 만든 클라이언트에서 사용하는 Grant Type
	-	https://developer.okta.com/blog/2018/06/29/what-is-the-oauth2-password-grant

-	AuthorizationServer 설정

	-	@EnableAuthorizationServer
	-	extends AuthorizationServerConfigureAdapter
	-	configure(AuthorizationServerSecurityConfigurer security)
		-	PasswordEncode 설정
	-	configure(ClientDetailsServiceConfigurer clients)

		-	클라이언트 설정
		-	grantTypes

			-	password
			-	refresh_token

		-	scopes

		-	secret / name

		-	accessTokenValiditySeconds

		-	refreshTokenValiditySeconds

	-	AuthorizationServerEndpointsConfigurer

		-	tokenStore
		-	authenticationManager
		-	userDetailsService

---

### REST API 보안 적용 - 리소스 서버 설정

-	테스트 수정

	-	GET을 제외하고 모드 엑세스 토큰을 가지고 요청하도록 테스트수정

-	ResourceServer 설정

	-	@EnableResourceServer
	-	extends ReosourceServerConfigurerAdapter
	-	configure(ResourceServerSecurityConfigurer resoruces)

		-	리소스 ID

	-	configure(HttpSecurity http)

		-	anonymous
		-	GET /api/\** : permit all
		-	POST /api/\** : authenticated
		-	PUT /api/\** : authenticated
		-	에러처리
			-	accesDeniedHandler(OAuth2AccessDeniedHandler())

---

### REST API 보안 적용 - 문자열을 외부설정으로 빼내기

-	기본 유저 만들기

	-	ApplicationRunner
		-	Admin
		-	User

-	외부설정으로 기본 유저와 클라이언트 정보 빼내기

	-	@ConfigurationProperties

---

### REST API 보안 적용 - 이벤트 API 점검

-	토근 발급받기

	-	POST /oauth/token
	-	BASIC authentication 헤더

		-	client Id(myApp) + client secret(pass)

	-	요청 본문 폼

		-	username : admin@email.com
		-	password : admin
		-	grant_type : password

-	토큰 갱신하기

	-	POST /oauth/token
	-	BASIC authentication 헤더
		-	client Id(myApp) + client secret(pass)
	-	요청본문 폼
		-	token : 처음에 발급받았던 refresh 토큰
		-	grant_type : refresh_token

-	이벤트 목록 조회 API

	-	로그인 했을 떄
		-	이벤트 생성 링크 제공

-	이벤트 조회

	-	로그인 했을 때
		-	이벤트 Manager 인 경우에는 이번트 수정 링크 제공

---

### REST API 보안 적용 - 현재 사용자 조회

-	SecurityContext

	-	자바 ThreadLocal 기반 구현으로 인증정보를 담고있다.
	-	인증 정보 꺼내는 방법
		-	Authentication authentication = SecurityContextHolder.getContet().getAuthentication();

-	@AuthenticationPrincipal spring.security.User user

	-	인증 안한 경우에는 null
	-	인증 한 경우에는 username과 authorities 참조 가능

-	spring.security.User를 상속받는 클래스를 구현하면

	-	도메인 User를 받을 수 있다.
	-	@AuthenticationPrincipa ~~~user.UserAdapter
	-	Adapter.getUser().getId()

-	SpEL을 사용하면

	-	@AuthenticationPrincipa(expression="account") ~~~user.Account

-	커스텀 애노테이션을 만들면

	-	@CurrentUser Account account
	-	인증 안하고 접근하면?
		-	expression ="#this == 'annonymousUser' ? null : account"
	-	현재 인증정보가 annonymousUser인 경우에는 null을 보내고 아니면 "account"를 꺼내 준다.

-	조회 API 개선

	-	현재 조회하는 사용자가 owner 인 경우에는 update 링크 추가 (HATEOAS)

-	수정 API 개선

	-	현재 사용자가 이벤트 owner가 아닌 경우에는 403 에러 발생

---

### REST API 보안 적용 - 출력 값 제한하기

-	생성 API 개선

	-	Event owner 설정
	-	응답에서 owner의 id 만 보내줄 것

	-	JsonSerializer<User> 구현

	-	@JsonSerialize(using) 설정

---

### 깨진 테스트 살펴보기

-	EventControllerTests.updateEvent()

	-	깨지는 이유
	-	해결방법

-	EventControllerTests.getEvent()

	-	꺠지는 이유
	-	해결 방법

-	DemoApplicationTests

	-	깨지는 이유
	-	해결방법
