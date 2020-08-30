# BOOK Fiinder
사용자가 검색어 (Title, Author 등)를 입력하여 책을 검색하고 결과를 리스트로 표시하는 앱을 만드세요.

## 유저 스토리
- 사용자가 `입력` 필드에 검색어를 입력합니다. (구현 완료)
- 사용자가 검색어를 검색하면 특정 데이터(**Title, Author, Published Date, Picture** 등)가 포함된 책 목록을 반환하는 [API를 호출](https://www.notion.so/chanju/Book-Finder-App-a6a2dee0e057410292583e7a111652fa#89bbf68ae69d4fb3aa13c0821b479514) 합니다. (구현 완료)
- 사용자는 검색된 도서 수와 전체 결과를 모두 볼 수 있습니다. (**목록 페이징**이 가능해야 합니다.)

## 필수요건
- Kotlin (kotlin 1.4 적용)
- minSDK 21, compileSDK 29 (적용)
- AAC ViewModel 사용 (적용)
- 기타 라이브러리는 자유롭게 사용하셔도 됩니다. 단, 차후 면접에서 사용하신 라이브러리에 대한 질의가 있을 수 있습니다.   
[ViewModel Overview | Android Developers](https://developer.android.com/topic/libraries/architecture/viewmodel)

## 선택 사항
- 목록에서 각 항목의 자세한 정보가 있는 화면으로 이동
    - 외부 사이트로 이동하는 링크 추가하거나,
    - 책 상세 화면을 직접 구현해도 됩니다.
    > expandable list로 상세 화면 구현
- 반응형(적응형) 디자인 구현
- 로딩 애니메이션 추가
> 재사용 가능한 네트워크 상태 표시 모듈 사용 
- 유닛 테스트 작성
> API 테스트를 위한 유닛 테스트 작성

### 빌드 요구사항
1. Android Studio 4.0 이상
2. SDK 29 이상
3. Kotlin 1.4 필요

### 요구사항 분석
#### 1. 서버 분석
1. [Google Book API](https://developers.google.com/books/docs/overview) 서버는 페이징 기능이 있고, 요구사항에도 **페이징기능**을 요구
2. 네트워크 케싱을 위해 **DB** 필요
#### 2. UI 분석
1. 도서 목록 검색을 위해 serch bar 필요
2. 도서 표지 표시를 위해 이미지 케싱 라이브러리 필요
#### 3. 앱 분석
1. 네트워크 쿼리를 통해 원하는 도서 목록을 불러 올것 
2. Min API 21를 위해 SDK API 레벨에 신경 쓸것
#### 4. 추가 구현
1. 구매 기능을 통해 내가 구매한 도서 목록을 보여주는 **책장** 기능 구현
2. 검색기능에 이전에 검색한 목록을 보여주는 기능 구현
3. 책 상세 화면을 구현할 예정이었으나, 생각보다 상세화면에 들어갈 항목이 많지 않아 Expandable list로 구현
4. 최초 사용자 가이드 추가
5. collapsingToolbar, searchBar Transition, expandable animation등 에니메이션을 중점 구현

### 앱 구조
전체적으로 MVVM 페턴을 따르고 있습니다  
네트워크로 부터 들어온 도서 정보는 DB에 저장이 되고 이 정보는 [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel?hl=ko) 로 들어 옵니다
이렇게 들어온 데이터는
[Android Databinding](https://developer.android.com/topic/libraries/data-binding?hl=ko) 과
[LiveData](https://developer.android.com/topic/libraries/architecture/livedata?hl=ko) 를
사용해 layout과 연결 됩니다  
이때 사용하는 데이터베이스 모듈과 네트워크 모듈은 [Koin](https://insert-koin.io/)
DI 라이브러리로 관리 합니다

#### MVVM에 관하여
요구사항으로 AAC ViewModel을 요구하고 있으므로 제가 이해하고 있는 사항들을 잠깐 언급하고 가겠습니다

**MVVM**은 Model-View-ViewModel의 약자로서 안드로이드 앱의 구조를 정의 하는
일련의 아키텍처 페턴을 말합니다.
이 아키텍처 페턴은 앱을 어떻게 설계할 것인가의 질문에 관한 답변으로,
딱히 정답이 있는 것도 아니고 사람마다 각 페턴에 대한 이해도도 다릅니다
MVVM 을 이해하기 전에 MVVM 페턴이 나오기 까지의 과정을 살펴 보면서, 기존
페턴이 해결하려고 한 문제점은 무엇이고, 그 과정중에 새로 발생한 문제점은
무엇인지 살펴보면서 MVVM은 어떤 식으로 해결하고 있는지 살펴보겠습니다

#### MVC
**MVC**는 *Model-View-Controller* 의 약자로 웹 환경에서 많이 쓰는 방식
입니다
컨트롤러에서 입력을 받으면, Model에서 데이터를 처리하여, View로
보내는 방식입니다
이러한 방식은 데이터 처리 요청에 맞춰 데이터를 대량으로 가공하고 표현 하는데는 적합 하지만,
다양한 사용자의 조작에 반응해 효과적으로 빠르게 반응하는데는 한계가 있습니다
특히 View와 controller의 특성을 동시에 가지고 있는 Activity의 특성으로
인해 View와 controller를 나눌수 없어 페기 하게 되었습니다

#### MVP
**MVP**는 Model View Presenter 의 약자로 MVC 페턴을 안드로이드에
적용할려는 노력이 실패로 돌아가고, Activity가 가지는 View와 Controller의
기능을 분리하는데 좀더 집중하기 위해 나왔습니다  
뷰의 이벤트를 처리를 최대한 프리젠터에서 처리하고 엑티비티는 뷰에 관한
컨트롤을 끊어 엑티비티에서 뷰를 분리시키는 것을 목표로 하고 있습니다  
이 페턴의 단점은 엑티비티에서 처리가능한 로직들이 프리젠터로 옮겨 가면서
필요 없는 보일러 플레이트가 발생하고 의미없는 인터페이스와 함수 호출이
증가 하면서, 구조가 복잡해지고, 유지보수를 어렵게 한다는 것입니다

#### MVVM
**MVVM**은 1990년대 Microsoft C# 진영에서 나온 게념 입니다.  
핵심 기능은 **data binding**으로 ViewModel은 View가 관찰할 수 있는
옵저버 클래스를 두고 뷰는 이 옵저버 클래스의 데이터를 관찰 합니다
(이과정을 데이터 바인딩이라고 합니다)  
View가 ViewModel의 옵저버를 연결시키면 ViewModel은 Model에서 갖고 온
데이터를 이 옵저버에 넣어 둡니다  
이벤트가 옵저버의 데이터를 변경시키고 데이터가 변경되면 이를 관찰 하고
있던 뷰가 케치하여 뷰 스스로 화면을 갱신하게 됩니다  
이 과정에서 뷰모델은 뷰의 화면 갱신에 전혀 관여 하지 않으며, 데이터의
조작에 좀더 집중할 수 있습니다

이 페턴의 장점은 View 스스로 화면을 갱신 함으로 화면 관련 로직은 View에,
데이터 관련 로직은 ViewModel로 구분이 가능 합니다  
이때 엑티비티는 뷰 및 뷰모델을 알고 있으며, 뷰는 뷰모델을 알고 있습니다  
뷰 모델은 모델만 알고 있으며, 데이터의 조작에만 집중 할 수 있습니다

기존의 구조는 **이벤트발생->데이터변경->화면갱신**이 한사이클에서
발생한다면, MVVM 구조에서는 **이벤트발생->데이터변경**,
**데이터관찰->화면갱신** 두사이클로 나눠 지게 됩니다  
즉, 기존에는 데이터를 변경한 쪽에서 화면 갱신의 책임이 있다면, MVVM
구조에서는 화면갱신의 책임은 데이터를 관찰하는 뷰가 가지게 됩니다  
이런식으로 하면 예전에는 텍스트가 바뀌면 바뀐 정보로 textView에 변경시켜주는
작업을 적절한 시점에 해야 하는데, MVVM에서는 화면 갱신의 책임이 뷰에 있으므로 그럴필요가 없어지고
코드는 더 간단해 지고 클레스의 기능이 더욱 명확해 지게 됩니다  
이 구조에서 엑티비티의 역할은 context와 관련된 다른 activity, fragment
관리, systemService 호출, 라이프 사이클 제어등과 같은 엑티비티 본연의
기능으로 축소 됩니다    

이 MVVM 페턴을 지원 하기 위해서 구글에서는
[AAC(Android Architecture Components)](https://developer.android.com/topic/libraries/architecture)
만들었습니다  
이 라이브러리 페키지는 데이터 바인딩을 위한
[Android DataBinding](https://developer.android.com/topic/libraries/data-binding?hl=ko),  
데이터를 옵저빙 하기 위한
[LiveData](https://developer.android.com/topic/libraries/architecture/livedata),  
ViewModel을 지원하기위한
[ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel?gclid=CjwKCAjwscDpBRBnEiwAnQ0HQPqEwqcwqWIIiS4vvyyb8yOwD-TnU_JdB40PasppO49PaAWgGpBVkhoC6cMQAvD_BwE)
및  
[AndroidViewModel](https://developer.android.com/reference/android/arch/lifecycle/AndroidViewModel)
을 지원하고 안드로이드 라이프 사이클을 추적하기 위한
[LifeCycleObserver](https://developer.android.com/reference/android/arch/lifecycle/LifecycleObserver)
를 제공 하고 있습니다

#### 화면  페키지 구성
MainActivity, SearchActivity 두개의 화면으로 구성 되어 있습니다
1. MainActivity: 도서 검색 화면과 책장 화면을 하위 항목으로 가지고 있으며 Tablayout로 표시하고 있습니다  
   1. BooksFragment: 도서 검색 화면을 표시 합니다
   2. BookShelfFragment: 구매한 도서가 있는 책장 화면을 표시 합니다
2. SearchActivity: 도서 검색어를 입력 받습니다. 입력받은 검색어는 BooksFragment에 전달 됩니다
   
#### 모델  페키지 구성 
1. network
   1. api: Google Book API와 통신하는 서비스를 제공합니다 
   2. dto: 서버로 부터 받은 데이터를 담습니다 
2. database
   1. entity: 디비 스키마를 구성합니다 
   2. dao: entity를 쿼리 합니다 
3. di: 위 두개의 페키지 및 뷰모델 펙로리를 모듈화 하여 ViewModel에 전달 하기 위한 di 페키지입니다 

### 오픈소스 라이브러리
1. [Android Architecture components](https://developer.android.com/jetpack/androidx/releases/lifecycle)  
안드로이드 ViewModel을 지원하고 라이프사이클에 맞게 제어 하는 역할을 합니다
2. [Koin]((https://insert-koin.io/))  
의존성주입(Dependency Injection) 라이브러리입니다   
클레스에서 사용하는 인스턴스를 외부에서 주입 받아
클레스간 의존성을 줄이고 테스트를 용의하게 해줍니다 
3. [Retrofit](https://square.github.io/retrofit/)   
httpClient rapper library입니다   
http 프로토콜을 통해서 WAS에 접근하는 라이브러리입니다 Rest API 메소드를 제공할 뿐만 아니라
Gson과 연계해서 파싱지원을 하는 등 강력하고 유연한 API 콜을 제공합니다
4. [Room](https://developer.android.com/topic/libraries/architecture/room)   
SQLite 기반의 데이터 영속화 라이브러리입니다   
네트워크 데이터를 DB에 저장하고 케쉬 처럼 읽고 쓸 수 있습니다
네트워크 통신전에 DB에 데이터가 있으면, 이 데이터로 화면을 구성하고 네트워크 데이터로 수정합니다
이렇게 하면 화면 반응성이 빨라집니다
5. [Paging](https://developer.android.com/topic/libraries/architecture/paging?hl=ko)   
페이징 라이브러리입니다   
리스트를 분할하여 페이지 단위로 조금식 가저오기 위한 기능 입니다
이렇게 하면 사용자가 필요한 만큼만 보여 주기 때문에 성능이나 효율성 측면에서 많은 도움이 됩니다
6. [Timber](https://github.com/JakeWharton/timber)   
로그 라이브러리입니다
7. [Glide](https://bumptech.github.io/glide/)   
이미지 로드 라이브러리입니다
네트워크 이미지 다운로드, 이미지 케싱, 이미지 변환, 이미지 사용 메모리 관리, 화면에 이미지 출력을 위해 사용합니다